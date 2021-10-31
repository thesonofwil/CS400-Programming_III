import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Wilson Tjoeng
 * tjoeng@wisc.edu
 * CS400.010
 * Due: 11/5/21
 * 
 * Hash table map implementation utilizing separate chaining as the collision resolution scheme.
 * Each index of the table is its own sorted linked list. A node is used to hold each key-value 
 * pair. The hash index is calculated as key.hashcode() % Table_Size. Before inserting, the program
 * determines if the current table needs to be rehashed. If there is a collision, the program inserts
 * the node into the linked list in sorted order. 
 */

/** HashTable implementation that uses:
 * @param <K> unique comparable identifier for each <K,V> pair, may not be null
 * @param <V> associated value with a key, value may be null
 */
public class BookHashTable implements HashTableADT<String, Book> {

    /** The initial capacity that is used if none is specified user */
    static final int DEFAULT_CAPACITY = 101;
    
    /** The load factor that is used if none is specified by user */
    static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;
    
    // Private fields of hash table
    private Node[] table;
    private int tableSize;
    private double loadFactorThreshold;
    private int numKeys;
    
    /**
     * Node inner-inner class to hold key-value pairs.
     * 
     * @author Wilson Tjoeng
     *
     */
    private class Node {
    	String key;
    	Book book;
    	Node next;
    		
    	/**
    	 * Constructs a new node
    	 * 
    	 * @param K unique key to hold
    	 * @param book associated Book value
    	 */
    	Node (String K, Book book) {
    		this.key = K;
    		this.book = book;
    		this.next = null;
    		}
    	}
    
    /**
     * REQUIRED default no-arg constructor
     * Uses default capacity and sets load factor threshold 
     * for the newly created hash table.
     */
    public BookHashTable() {
        this(DEFAULT_CAPACITY,DEFAULT_LOAD_FACTOR_THRESHOLD);
    }
    
    /**
     * Creates an empty hash table with the specified capacity 
     * and load factor.
     * 
     * @param initialCapacity number of elements table should hold at start.
     * @param loadFactorThreshold the ratio of items/capacity that causes table to resize and rehash
     * @exception IllegalArgumentException thrown if either parameter is negative
     */
    public BookHashTable(int initialCapacity, double loadFactorThreshold) 
    		throws IllegalArgumentException {
    	if (initialCapacity < 0 || loadFactorThreshold < 0) {
    		throw new IllegalArgumentException("Capacity and/or threshold cannot be negative");
    	}
        this.tableSize = initialCapacity;
        this.loadFactorThreshold = loadFactorThreshold;
        this.numKeys = 0;
        this.table = new Node[tableSize]; // Initialize list of null buckets
    }
    
    /**
     * Add the key,value pair to the hash table and increase the number of keys.
     * 
     * @param key the key to insert
     * @param value the Book object paired to the key
     * @exception IllegalNullKeyExcpetion thrown if key is null
     * @exception DuplicateKeyException thrown if key is already in hash table 
     */
	@Override
	public void insert(String key, Book value) throws IllegalNullKeyException, DuplicateKeyException {
		// Check table first if key is already present
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		if (find(key) != null) {
			throw new DuplicateKeyException();
		}
		
		rehash(); // if needed, expand and rehash table
		insertIntoBucket(key, value);
				
		this.numKeys++;
		// Check for case when n is already in table
	
	}
	
	/**
	 * Insert a node into a bucket's linked list. The linked list is in sorted order.
	 * 
	 * @param key unique key to insert
	 * @param value associated value of key
	 */
	private void insertIntoBucket(String key, Book value) {
		int hashIndex = getHashIndex(key);
		Node newNode = new Node(key, value);
		
		// If bucket is empty or should be inserted before head, insert at head
		if (this.table[hashIndex] == null || key.compareTo(this.table[hashIndex].key) > 0) {
			newNode.next = this.table[hashIndex];
			this.table[hashIndex] = newNode;
			return;
		} else { // Insert at sorted position
			Node curr = this.table[hashIndex];
			
			// Loop through Bucket's linked list and insert in sorted position
			while (curr.next != null) {
				if (key.compareTo(curr.next.key) > 0) {
					break;
				}
				curr = curr.next;
			}
			newNode.next = curr.next;
			curr.next = newNode;
		}
		
		// Unsorted Linked List
//		Node curr = this.table[hashIndex];
//		if (curr == null) {
//			this.table[hashIndex] = newNode;
//			return;
//		}
//		while (curr.next != null) {
//			curr = curr.next;
//		}
//		curr.next = newNode;
	}
	
	/**
	 * If key is found, remove the key,value pair from the data structure and 
	 * decrease number of keys. If key is null, throw IllegalNullKeyException.
     *
     * @param key the key to remove from the hash table 
     * @exception IllegalNullKeyException thrown if key is null
     * @return true if the key was found; false otherwise
	 */
	@Override
	public boolean remove(String key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		
		int i = getHashIndex(key);
		Node curr = this.table[i];
		
		// Bucket is empty so key is not there
		if (curr == null) {
			return false;
		}
		
		// Key is located at head, so assign new head
		if (curr.key.equals(key)) {
			this.table[i] = curr.next;
		} else { // Loop through list to find key
			while(curr != null) {
				Node prev = curr;
				curr = curr.next;
				
				// Key not found if we've reached the end of the list or are past sorted order
				if (curr == null || key.compareTo(curr.key) > 0) { 
					return false;
				} else if (curr.key.equals(key)) {
					prev.next = curr.next;
					curr.next = null;
					break;
				}
			}
		}
		this.numKeys--;
		return true;
	}
	
	/**
	 * Returns the value associated with the specified key
     * Does not remove key or decrease number of keys
     * If key is null, throw IllegalNullKeyException 
     * If key is not found, throw KeyNotFoundException
     * 
     * @param key the key to retrieve the value for 
     * @exception IllegalNullKeyException thrown if key is null
     * @exception KeyNotFoundException thrown if the key to search for isn't in the hash table
     * @return the book object associated with the key
	 */
	@Override
	public Book get(String key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		
		Node node = find(key);
		
		if (node == null) {
			throw new KeyNotFoundException();
		}
		
		return node.book;
	}
		
	/**
	 * Returns the number of key,value pairs in the hash table
	 *   
	 * @return the number of keys
	 */
	@Override
	public int numKeys() {
		return this.numKeys;
	}
	
	/**
	 * Returns the load factor for this hash table that determines when to increase the 
	 * capacity of the hash table
	 * 
	 * @return the load factor threshold
	 */
	@Override
	public double getLoadFactorThreshold() {
		return this.loadFactorThreshold;
	}
	
	/**
	 * Capacity is the size of the hash table array. This method returns the current capacity.
	 * 
	 * The initial capacity must be a positive integer, 1 or greater
     * and is specified in the constructor.
     * 
     * REQUIRED: When the load factor is reached, 
     * the capacity must increase to: 2 * capacity + 1
     *
     * Once increased, the capacity never decreases
     * 
     * @return the capacity or size of the table
	 */
	@Override
	public int getCapacity() {
		return this.tableSize;
	}
	
	/**
	 * Returns the collision resolution scheme used for this hash table.
     * implement this ADT with one of the following collision resolution strategies
     * and implement this method to return an integer to indicate which strategy.
     *
     * 1 OPEN ADDRESSING: linear probe
     * 2 OPEN ADDRESSING: quadratic probe
     * 3 OPEN ADDRESSING: double hashing
     * 4 CHAINED BUCKET: array list of array lists
     * 5 CHAINED BUCKET: array list of linked lists
     * 6 CHAINED BUCKET: array list of binary search trees
     * 7 CHAINED BUCKET: linked list of array lists
     * 8 CHAINED BUCKET: linked list of linked lists
     * 9 CHAINED BUCKET: linked list of of binary search trees
	 */
	@Override
	public int getCollisionResolutionScheme() {
		return 5;
	}
	
	/**
	 * Hash function to calculate index. Calculates using Java's hashcode % 
	 * table size
	 * 
	 * @param key the key to calculate the hash index for 
	 * @return hash index
	 */
	private int getHashIndex(String key) {
		int hash = Math.abs(key.hashCode());
		return hash % tableSize;
	}
	
	/**
	 * Returns the node with a key of interest in the hash table by looping through the 
	 * bucket at the key's hash index i.e. where it would be even if not there. 
	 * 
	 * @param key key to search for
	 * @exception IllegalNullKeyException if key is null 
	 * @return the node if key is present; null if not
	 */
	private Node find(String key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
				
		Node curr = this.table[getHashIndex(key)]; // Get the head of list at index
		
		// If bucket is empty, then the key isn't there
		if (curr == null) {
			return null;
		}
		
		// Else loop through linked list
		while (curr != null) {
			if (curr.key.equals(key)) {
				return curr;
			}
			
			// Went past where key would be located
			if (key.compareTo(curr.key) > 0) {
				break;
			}
			curr = curr.next;
		}
		
		return null; // If node is not found
	}

	
	/**
	 * Calculates the load factor of the hash table
	 * 
	 * @return the load factor in decimal format
	 */
	private double getLoadFactor() {
		return (double) numKeys / (double) tableSize;
	}
	
	/**
	 * Creates a new hash table and copies over content from original table if 
	 * the load factor is greater than the threshold
	 * 
	 * @return a bigger hash table, or the original table if load factor is below threshold
	 */
	private void rehash() {
		
		// Don't do anything if below load factor threshold
		if (getLoadFactor() <= loadFactorThreshold) {
			return;
		}
		
		int newSize = 2 * getCapacity() + 1;
		int keys = this.numKeys; 
		Node[] oldTable = this.table; // Save a temp copy of old table
		Node[] newTable = new Node[newSize];
		
		this.table = newTable; // Assign reference to new table
		this.tableSize = newSize;
		
		// Go through old table and rehash all keys into new table
		int count = 0; // Makes this more efficient
		this.numKeys = 0; // Reset numKeys because we'll be inserting
		for (int i = 0; i < oldTable.length; i++) {
			if (oldTable[i] == null) {
				continue;
			}
			// No need to rehash anymore if we got all the keys
			if (count == keys) {
				break;
			}
			
			Node curr = oldTable[i]; // Assign to head in each index 
			while (curr != null) {
				try {
					insert(curr.key, curr.book); // Insert into new table
					count++;
					curr = curr.next;
				} catch (IllegalNullKeyException | DuplicateKeyException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// Alternative way to expand capacity
	/**
	 * Given an integer, finds the next prime number after it
	 * 
	 * @param num the integer to find the next prime number for
	 * @return the next prime number of num
	 */
//	private int getNextPrime(int num) {
//		
//		boolean primeFound = false;
//		
//		// Check if num is divisible by any integer from 2 to num - 1.
//		// If it is, increment num
//		while (!primeFound) {
//			num++;
//			for (int i = 2; i < num; i++) {
//				if (num % i == 0) {
//					break;
//				}
//				if (i == num - 1) { // if we've checked every divisor
//					primeFound = true;
//				}
//			}
//		}
//		
//		return num;
//	}
}