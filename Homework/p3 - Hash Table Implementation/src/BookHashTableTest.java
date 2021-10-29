/**
 * Filename:   TestHashTableDeb.java
 * Project:    p3
 * Authors:    Debra Deppeler (deppeler@cs.wisc.edu)
 * 
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   before 10pm on 10/29
 * Version:    1.0
 * 
 * Credits:    None so far
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

import org.junit.After;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/** 
 * Test HashTable class implementation to ensure that required 
 * functionality works for all cases.
 */
public class BookHashTableTest {

    // Default name of books data file or try books_clean.csv
    public static final String BOOKS = "books_clean.csv"; 

    // Empty hash tables that can be used by tests
    static BookHashTable bookObject;
    static ArrayList<Book> bookTable;

    static final int INIT_CAPACITY = 2;
    static final double LOAD_FACTOR_THRESHOLD = 0.49;
       
    static Random RNG = new Random(0);  // seeded to make results repeatable (deterministic)

    /** Create a large array of keys and matching values for use in any test */
    @BeforeAll
    public static void beforeClass() throws Exception{
        bookTable = BookParser.parse(BOOKS);
    }
    
    /** Initialize empty hash table to be used in each test */
    @BeforeEach
    public void setUp() throws Exception {
         bookObject = new BookHashTable(INIT_CAPACITY,LOAD_FACTOR_THRESHOLD);
    }

    /** Not much to do, just make sure that variables are reset     */
    @AfterEach
    public void tearDown() throws Exception {
        bookObject = null;
    }

    private void insertMany(ArrayList<Book> bookTable) 
        throws IllegalNullKeyException, DuplicateKeyException {
        for (int i=0; i < bookTable.size(); i++ ) {
            bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
        }
    }

    /**
     * File: books_clean.csv
     * get returns book from list that the rows in the file are added to
     * getKey returns ISBN 13
     */
    
    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is empty upon initialization
     */
    @Test
    public void test000_collision_scheme() {
        if (bookObject == null)
        	fail("Gg");
    	int scheme = bookObject.getCollisionResolutionScheme();
        if (scheme < 1 || scheme > 9) 
            fail("collision resolution must be indicated with 1-9");
    }


    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is empty upon initialization
     */
    @Test
    public void test000_IsEmpty() {
        //"size with 0 entries:"
        assertEquals(0, bookObject.numKeys());
    }

    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is not empty after adding one (key,book) pair
     * @throws DuplicateKeyException 
     * @throws IllegalNullKeyException 
     */
    @Test
    public void test001_IsNotEmpty() throws IllegalNullKeyException, DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
        String expected = ""+1;
        //"size with one entry:"
        assertEquals(expected, ""+bookObject.numKeys());
    }
    
    /** IMPLEMENTED AS EXAMPLE FOR YOU 
    * Test if the hash table  will be resized after adding two (key,book) pairs
    * given the load factor is 0.49 and initial capacity to be 2.
    */
    
    @Test 
    public void test002_Resize() throws IllegalNullKeyException, DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	int cap1 = bookObject.getCapacity(); 
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	int cap2 = bookObject.getCapacity();
    	
        //"size with one entry:"
        assertTrue(cap2 > cap1 & cap1 ==2);
    }
    
    @Test
    void test003_Insert_Simple() throws IllegalNullKeyException, DuplicateKeyException,
    KeyNotFoundException {
    	String key = bookTable.get(0).getKey();
    	Book book = bookTable.get(0);
    	
    	bookObject.insert(key, book);
    	assert(bookObject.numKeys() == 1);
    	assert(bookObject.get(key).equals(book));
    }
    
    @Test
    void test004_Insert_Throws_Duplicate_Key_Exception() {
    	try {
    		bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    		bookObject.insert(bookTable.get(0).getKey(), bookTable.get(1));
    		fail("Duplicate key exception not thrown");
    	} catch (DuplicateKeyException e) {
    		assert(bookObject.numKeys() == 1);
    		assert(bookObject.getCapacity() == INIT_CAPACITY);
    	} catch (Exception e) {
    		fail("Duplicate key exception not thrown");
    	}
    }
    
    @Test
    void test005_Insert_Throws_Null_Key_Exception() {
    	try {
    		bookObject.insert(null, bookTable.get(0));
    		fail("Illegal null key exception not thrown");
    	} catch (IllegalNullKeyException e) {
    		assert(bookObject.numKeys() == 0);
    		assert(bookObject.getCapacity() == INIT_CAPACITY);
    	} catch (Exception e) {
    		fail("Illegal null key exception not thrown");
    	}
    }
    
    @Test
    void test006_Insert_Multiple_Keys() throws IllegalNullKeyException, DuplicateKeyException,
    KeyNotFoundException {
    	String key1 = bookTable.get(0).getKey();
    	String key2 = bookTable.get(1).getKey();
    	String key3 = bookTable.get(2).getKey();
    	
    	Book book1 = bookTable.get(0);
    	Book book2 = bookTable.get(1);
    	Book book3 = bookTable.get(3);
    	
    	assert(bookObject.numKeys() == 0);
    	assert(bookObject.getCapacity() == INIT_CAPACITY);
    	
    	bookObject.insert(key1, book1);
    	assert(bookObject.numKeys() == 1);
    	assert(bookObject.getCapacity() == INIT_CAPACITY);
    	assert(bookObject.get(key1).equals(book1));
    	
    	bookObject.insert(key2, book2);
    	assert(bookObject.numKeys() == 2);
    	assert(bookObject.getCapacity() == 5); // next prime of 2 * Initial Capacity
    	assert(bookObject.get(key2).equals(book2));
    	
    	bookObject.insert(key3, book3);
    	assert(bookObject.numKeys() == 3);
    	assert(bookObject.getCapacity() == 5);
    	assert(bookObject.get(key1).equals(book3));  	
    }
}
