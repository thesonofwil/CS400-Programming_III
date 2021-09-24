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
    		ds.remove(null);
    		fail("illegal argument exception not thrown");
    	} catch (IllegalArgumentException e) { }
    	assert (ds.size() == 1);
    }
    
    @Test
    void test11_remove_empty_ds_returns_false() {
    	assert (ds.size() == 0);
    	assert (!ds.remove("One"));
    	assert (!ds.remove("Two"));
    	assert (ds.size() == 0);
    }
    
    @Test
    void test12_contains_after_insert_and_remove() {
    	assert (!ds.contains("1"));
    	ds.insert("1", "One");
    	assert (ds.contains("1"));
    	ds.remove("1");
    	assert (!ds.contains("1"));
    }
    
    @Test
    void test13_contain_on_null_key() {
    	assert(!ds.contains(null));
    }
    
    @Test
    void test14_get_and_remove_only_work_on_keys() {
    	ds.insert("1", "One");
    	assert (ds.get("One") == null);
    	assert (!ds.remove("One"));
    	assert (ds.get("1").equals("One"));
    	assert (ds.size() == 1);
    }
    
    @Test 
    void test15_insert_multiple_remove_beginning() { 
    	ds.insert("1", "One");
    	ds.insert("2", "Two");
    	ds.insert("3", "Three");
    	assert (ds.size() == 3);
    	assert (ds.remove("1"));
    	assert (ds.size() == 2);
    	assert (!ds.contains("1"));
    	assert (ds.contains("2"));
    	assert (ds.contains("3"));
    }
    
    @Test
    void test16_insert_multiple_remove_middle() {
    	ds.insert("1", "One");
    	ds.insert("2", "Two");
    	ds.insert("3", "Three");
    	assert (ds.size() == 3);
    	assert (ds.remove("2"));
    	assert (ds.size() == 2);
    	assert (ds.contains("1"));
    	assert (!ds.contains("2"));
    	assert (ds.contains("3"));
    }
    
    @Test
    void test17_insert_multiple_remove_end() {
    	ds.insert("1", "One");
    	ds.insert("2", "Two");
    	ds.insert("3", "Three");
    	assert (ds.size() == 3);
    	assert (ds.remove("3"));
    	assert (ds.size() == 2);
    	assert (ds.contains("1"));
    	assert (ds.contains("2"));
    	assert (!ds.contains("3"));
    }
    
    @Test
    void test18_reinsert_after_removing() {
    	ds.insert("1", "One");
    	ds.remove("1");
    	assert (ds.size() == 0);
    	assert (!ds.contains("1"));
    	ds.insert("1", "Two"); // different value
    	assert (ds.contains("1"));
    	assert (ds.get("1").equals("Two"));
    	assert (ds.size() == 1);
    	try { 
            ds.insert("1", "Three"); 
            fail("duplicate exception not thrown");
        }
        catch (RuntimeException re) { }
    }
    
    @Test
    void test19_insert_remove_lots() {
    	String num;
    	int upperBound = 1000;
    	
    	for (int i = 0; i < upperBound; i++) {
    		num = String.valueOf(i);
    		ds.insert(num, "value" + i);
    	}
    	
    	assert (ds.size() == upperBound);
    	for (int i = 0; i < upperBound; i++) {
    		num = String.valueOf(i);
    		assert (ds.get(num).equals("value" + i));
    		assert (ds.remove(num));
    		assert (ds.size() == upperBound - (i + 1));
    	}
    }
   
    // Tip: consider different numbers of inserts and removes and how different combinations of insert and removes
}
