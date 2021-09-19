import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class DataStructureADTTest<T extends DataStructureADT<String,String>> {
    
    private T ds;
    
    protected abstract T createInstance();

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        ds = createInstance();
    }

    @AfterEach
    void tearDown() throws Exception {
        ds = null;
    }

    
    @Test
    void test00_empty_ds_size() {
        if (ds.size() != 0)
        fail("data structure should be empty, with size=0, but size="+ds.size());
    }
    
    // TODO: review tests 01 - 04

    @Test
    void test01_insert_one() {
        String key = "1";
        String value = "one";
        assert (ds.size()==0);
        ds.insert(key, value);
        assert (ds.size()==1);
    }
    
    @Test
    void test02_insert_remove_one_size_0() {
        String key = "1";
        String value = "one";
        ds.insert(key, value);
        assert (ds.remove(key)); // remove the key
        if (ds.size() != 0)
            fail("data structure should be empty, with size=0, but size="+ds.size());
    }
    
    @Test
    void test03_duplicate_exception_thrown() {
        String key = "1";
        String value = "one";
        ds.insert("1", "one");
        ds.insert("2", "two");
        try { 
            ds.insert(key, value); 
            fail("duplicate exception not thrown");
        }
        catch (RuntimeException re) { }
        assert (ds.size()==2);
    }
            
    
    @Test
    void test04_remove_returns_false_when_key_not_present() {
        String key = "1";
        String value = "one";
        ds.insert(key, value);
        assert (!ds.remove("2")); // remove non-existent key is false
        assert (ds.remove(key));  // remove existing key is true
        if (ds.get(key)!=null)
            fail("get("+key+ ") returned " + ds.get(key) + " which should have been removed");
    }

    
    // TODO: add tests 05 - 07 as described in assignment
    
    @Test
    void test05_get_after_inserts() {
    	assert (ds.get("a") == null); // return null if key isn't in DS
    	String key = "1";
        String value = "one";
        ds.insert("1", "one");
        ds.insert("2", "two");
        assert (ds.get("1") == "one");
        assert (ds.get("2") == "two");
    }
    
    @Test
    void test06_get_does_not_remove_and_change_size() {
    	String key = "1";
        String value = "one";
        ds.insert(key, value);
        assert(ds.size() == 1);
        String t = ds.get("1");
        assert(ds.contains("1"));
        assert(ds.size() == 1);
    }
    
    @Test
    void test07_get_throws_exception_if_key_is_null() { 
    	try {
    		ds.get(null);
    		fail("illegal argument exception not thrown");
    	} catch (IllegalArgumentException e) { }
    }
    
    @Test
    void test08_ds_contains_keys() { 
        ds.insert("1", "one");
        ds.insert("2", "two");
        assert (ds.contains("1"));
        assert (ds.contains("2"));
    }
    
    @Test
    void test09_ds_does_not_contain_keys() {
    	assert (!ds.contains(null));
    	assert (!ds.contains("1"));
    	ds.insert("1", "one");
    	assert (!ds.contains("one")); // contains should only work on keys
    }
    
    @Test
    void test10_remove_null_key_throws_exception_and_size_stays_the_same() {
    	ds.insert("1", "one");
    	try {
    		ds.remove("null");
    		fail("illegal argument exception not thrown");
    	} catch (IllegalArgumentException e) { }
    	assert (ds.size() == 1);
    }
    
    // Empty exception should be thrown if removing when there are no keys
    @Test
    void test11_remove_empty_ds_throws_exception() {
    	assert (ds.size() == 0);
    	try {
    		ds.remove("One");
    		fail("empty exception not thrown");
    	} catch (RuntimeException e) {}
    	assert (ds.size() == 0);
    }
    // TODO: add more tests of your own design to ensure that you can detect implementation that fail
    
    // Tip: consider different numbers of inserts and removes and how different combinations of insert and removes

}
