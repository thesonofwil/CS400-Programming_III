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

    
    // TODO: add tests 05 - 07 as described in assignment

    // TODO: add more tests of your own design to ensure that you can detect implementation that fail
    
    // Tip: consider different numbers of inserts and removes and how different combinations of insert and removes

}
