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
    private BookHashTable[] table;
    private int tableSize;
    private double loadFactor;
    private double loadFactorThreshold;
    private int numKeys;
    
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
        this.table = new BookHashTable[tableSize]; // Initialize empty list
    }

	@Override
	public void insert(String key, Book value) throws IllegalNullKeyException, DuplicateKeyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean remove(String key) throws IllegalNullKeyException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Book get(String key) throws IllegalNullKeyException, KeyNotFoundException {
		// TODO Auto-generated method stub
		return null;
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
	private BookHashTable[] resizeTable() {
		
		// Don't do anything if below load factor threshold
		if (getLoadFactor() <= loadFactorThreshold) {
			return table;
		}
		
		// TODO implement resize
		return table;
	}
	
    // TODO: add all unimplemented methods so that the class can compile

}
