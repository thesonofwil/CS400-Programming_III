import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

// TODO: comment and complete your HashTableADT implementation
//
// TODO: implement all required methods
// DO ADD REQUIRED PUBLIC METHODS TO IMPLEMENT interfaces
//
// DO NOT ADD ADDITIONAL PUBLIC MEMBERS TO YOUR CLASS 
// (no public or package methods that are not in implemented interfaces)
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// if open addressing: describe probe sequence 
// if buckets: describe data structure for each bucket
//
// TODO: explain your hashing algorithm here 

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
    private Bucket[] table;
    private int tableSize;
    private double loadFactor;
    private double loadFactorThreshold;
    private int numKeys;
    
    /**
     * Bucket inner class. Each index of the hash table will contain a bucket
     * that has nodes organized in a sorted linked list
     * 
     * @author Wilson Tjoeng
     *
     */
    private class Bucket {
    	
    	// Private fields of Bucket
    	Node head;
    	
    	/**
    	 * When called, constructs an empty bucket
    	 */
    	Bucket() {
    		this.head = null;
    	}
    }
    
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
     * @param initialCapacity number of elements table should hold at start.
     * @param loadFactorThreshold the ratio of items/capacity that causes table to resize and rehash
     */
    public BookHashTable(int initialCapacity, double loadFactorThreshold) {
        this.tableSize = initialCapacity;
        this.loadFactorThreshold = loadFactorThreshold;
        this.numKeys = 0;
        this.table = new Bucket[tableSize]; // Initialize list of null buckets
    }

	@Override
	public void insert(String key, Book value) throws IllegalNullKeyException, DuplicateKeyException {
		// TODO Auto-generated method stub
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
		
		// If bucket is empty, insert at head
		if (this.table[hashIndex] == null) {
			this.table[hashIndex].head = newNode;
		} else { // Insert at sorted position
			Node curr = this.table[hashIndex].head;
			int cmp = key.compareTo(curr.key); // Compare key to insert with head
			
			// Loop through Bucket's linked list and insert in sorted position
			while (curr.next != null && (key.compareTo(curr.next.key) > 0)) {
				curr = curr.next;
			}
			newNode.next = curr.next;
			curr.next = newNode;
		}
	}

	@Override
	public boolean remove(String key) throws IllegalNullKeyException {
		// TODO Auto-generated method stub
		return false;
	}

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

	@Override
	public int numKeys() {
		return this.numKeys;
	}

	@Override
	public double getLoadFactorThreshold() {
		return this.loadFactorThreshold;
	}

	@Override
	public int getCapacity() {
		return this.tableSize;
	}

	@Override
	public int getCollisionResolutionScheme() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Hash function to calculate index. Calculates using Java's hashcode % 
	 * table size
	 * 
	 * @param key the key to calculate the hash index for 
	 * @return hash index
	 */
	private int getHashIndex(String key) {
		int hash = Math.abs(hashCode());
		return hash % tableSize;
	}
	
	/**
	 * Returns the node with a key of interest in the hash table by looping through the 
	 * bucket at the key's hash index i.e. where it would be even if not there. 
	 * 
	 * @param key key to search for 
	 * @return the node if key is present; null if not
	 */
	private Node find(String key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		
		int hashIndex = getHashIndex(key);
		
		Node curr = this.table[hashIndex].head; // Get bucket's head at index
		
		// Check if head is the key of interest
		if (curr.key.compareTo(key) == 0) {
			return curr;
		}
		
		// Else loop through linked list
		while (curr.next != null && (key.compareTo(curr.next.key) >= 0)) {
			if (key.compareTo(curr.next.key) == 0) {
				return curr.next;
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
		return numKeys / tableSize;
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
		
		int newSize = getNextPrime(tableSize * 2);
		Bucket[] newTable = new Bucket[newSize]; 
		
		for (int i = 0; i < this.tableSize; i++) {
			
			// TODO: Insert from original table to new table
		}
		
		// Update fields
		this.tableSize = newSize;
		this.table = newTable;
	}
	
	private int getNextPrime(int num) {
		
		boolean primeFound = false;
		
		// Check if num is divisible by any integer from 2 to num - 1.
		// If it is, increment num
		while (!primeFound) {
			num++;
			for (int i = 2; i < num; i++) {
				if (num % i == 0) {
					break;
				}
				if (i == num - 1) { // if we've checked every divisor
					primeFound = true;
				}
			}
		}
		
		return num;
	}
	
    // TODO: add all unimplemented methods so that the class can compile

}
