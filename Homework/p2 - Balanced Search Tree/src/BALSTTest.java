import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//@SuppressWarnings("rawtypes")
public class BALSTTest {

    protected BALSTADT<Integer,String> bst;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
         bst = new BALST<Integer,String>();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }

    /** 
     * CASE 123 Insert three values in sorted order and then check 
     * the root, left, and right keys to see if insert worked 
     * correctly.
     */
    @Test
    void testBST_001_insert_sorted_order_simple() {
        try {
            bst.insert(10, "10");
            if (!bst.getKeyAtRoot().equals(10))
                fail("insert at root does not work");
            
            bst.insert(20, "20");
            if (!bst.getKeyOfRightChildOf(10).equals(20)) 
                fail("insert to right child of root does not work");
            
            bst.insert(30, "30");
            if (!bst.getKeyAtRoot().equals(10)) 
                fail("inserting 30 changed root");
                        
            if (!bst.getKeyOfRightChildOf(20).equals(30)) 
                fail("inserting 30 as right child of 20");

            // IF rebalancing is working,
            // the tree should have 20 at the root
            // and 10 as its left child and 30 as its right child

            Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(10));
            Assert.assertEquals(bst.getKeyOfRightChildOf(10), Integer.valueOf(20));
            Assert.assertEquals(bst.getKeyOfRightChildOf(20), Integer.valueOf(30));

            bst.print();
            
        } catch (Exception e) {
            e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
        }
    }

    /** 
     * CASE 321 Insert three values in reverse sorted order and then check 
     * the root, left, and right keys to see if insert 
     * worked in the other direction.
     */
    @Test
    void testBST_002_insert_reversed_sorted_order_simple() {
        try {
            bst.insert(30, "30");
            if (!bst.getKeyAtRoot().equals(30))
                fail("insert at root does not work");
            
            bst.insert(20, "20");
            if (!bst.getKeyOfLeftChildOf(30).equals(20)) 
                fail("insert to left child of root does not work");
            
            bst.insert(10, "10");
            if (!bst.getKeyAtRoot().equals(30)) 
                fail("inserting 10 changed root");

            if (!bst.getKeyOfLeftChildOf(20).equals(10)) 
                fail("inserting 10 as left child of 20");

            // IF rebalancing is working,
            // the tree should have 20 at the root
            // and 10 as its left child and 30 as its right child

            Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(30));
            Assert.assertEquals(bst.getKeyOfLeftChildOf(30), Integer.valueOf(20));
            Assert.assertEquals(bst.getKeyOfLeftChildOf(20), Integer.valueOf(10));

            bst.print();
            
        } catch (Exception e) {
            e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
        }
    }

    /** 
     * CASE 132 Insert three values so that rebalancing requires new key 
     * to be the "new" root of the rebalanced tree.
     * 
     * Then check the root, left, and right keys to see if insert 
     * occurred correctly.
     */
    @Test
    void testBST_003_insert_smallest_largest_middle_order_simple() {
        try {
            bst.insert(10, "10");
            if (!bst.getKeyAtRoot().equals(10))
                fail("insert at root does not work");
            
            bst.insert(30, "30");
            if (!bst.getKeyOfRightChildOf(10).equals(30)) 
                fail("insert to right child of root does not work");
            Assert.assertEquals(bst.getKeyOfRightChildOf(10),Integer.valueOf(30));
            
            bst.insert(20, "20");
            if (!bst.getKeyAtRoot().equals(10)) 
                fail("inserting 20 changed root");

            if (!bst.getKeyOfLeftChildOf(30).equals(20)) 
                fail("inserting 20 as left child of 30");

            // IF rebalancing is working,
            // the tree should have 20 at the root
            // and 10 as its left child and 30 as its right child

            Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(10));
            Assert.assertEquals(bst.getKeyOfRightChildOf(10), Integer.valueOf(30));
            Assert.assertEquals(bst.getKeyOfLeftChildOf(30), Integer.valueOf(20));

            bst.print();
            
        } catch (Exception e) {
            e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
        }
    }

    /** 
     * CASE 312 Insert three values so that rebalancing requires new key 
     * to be the "new" root of the rebalanced tree.
     * 
     * Then check the root, left, and right keys to see if insert 
     * occurred correctly.
     */
    @Test
    void testBST_004_insert_largest_smallest_middle_order_simple() {
        try {
            bst.insert(30, "30");
            if (!bst.getKeyAtRoot().equals(30))
                fail("insert at root does not work");
            
            bst.insert(10, "10");
            if (!bst.getKeyOfLeftChildOf(30).equals(10)) 
                fail("insert to left child of root does not work");
            
            bst.insert(20, "20");
            if (!bst.getKeyAtRoot().equals(30)) 
                fail("inserting 10 changed root");

            if (!bst.getKeyOfRightChildOf(10).equals(20)) 
                fail("inserting 20 as right child of 10");

            // the tree should have 30 at the root
            // and 10 as its left child and 20 as 10's right child

            Assert.assertEquals(bst.getKeyAtRoot(), Integer.valueOf(30));
            Assert.assertEquals(bst.getKeyOfLeftChildOf(30), Integer.valueOf(10));
            Assert.assertEquals(bst.getKeyOfRightChildOf(10), Integer.valueOf(20));

            bst.print();
            
        } catch (Exception e) {
            e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
        }
    }
    
    
    // TODO: Add your own tests
    
    // Add tests to make sure that bst grows as expected.
    // Does it maintain it's balance?
    // Does the height of the tree reflect it's actual height
    // Use the traversal orders to check.
    
    // Can you insert many and still "get" them back out?
    
    // Does delete work? 
    // If delete is not implemented, does calling it throw an UnsupportedOperationException
    
    @Test
    void testBST_005_insert_simple_sorted_delete_root() {
    	try {
			bst.insert(10, "10");
			bst.insert(20, "20");
		    bst.insert(30, "30");
		    assert bst.numKeys() == 3;
		    assert bst.remove(20);
		    assert bst.getKeyAtRoot() == 10;
		    assert bst.numKeys() == 2;
		} catch (Exception e) {
			e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
		}
    }
    
    @Test
    void testBST_006_insert_simple_rotate_left_right() {
    	try {
			bst.insert(20, "20");
			bst.insert(10, "10");
		    bst.insert(15, "15");
		    assert bst.numKeys() == 3;
		    assert bst.getKeyAtRoot() == 15;
		    assert bst.getKeyOfLeftChildOf(15) == 10;
		    assert bst.getKeyOfRightChildOf(15) == 20; 
		} catch (Exception e) {
			e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
		}
    	bst.print();
    }
    
    @Test
    void testBST_007_insert_simple_rotate_right_left() {
    	try {
			bst.insert(20, "20");
			bst.insert(30, "30");
		    bst.insert(25, "25");
		    assert bst.numKeys() == 3;
		    assert bst.getKeyAtRoot() == 25;
		    assert bst.getKeyOfLeftChildOf(25) == 20;
		    assert bst.getKeyOfRightChildOf(25) == 30; 
		} catch (Exception e) {
			e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
		}
    	bst.print();
    }
    
    // Modeling RBT Insert Practice from lecture notes
    
    // After recoloring, insert again which should not trigger an RPV
    @Test
    void testBST_008_insert_recolor_insert_no_violation() {
    	try {
    		bst.insert(7, "7");
    		bst.insert(14, "14");
    		bst.insert(18, "18"); // root becomes 14 here but is red instead of black
    		bst.insert(23, "23"); // Recolor 
    		assert(bst.getKeyAtRoot() == 14);
    		bst.insert(1, "1"); // This is causing a rotation when it shouldn't. Root is red for some reason
    		assert(bst.getKeyOfLeftChildOf(7) == 1);
    		bst.insert(11, "11");
    		assert(bst.getKeyOfRightChildOf(7) == 11);
    	} catch (Exception e) {
			e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
    	}
    	bst.print();
    }
    
    @Test
    void testBST_009_RPV_after_Recolor() {
    	try {
    		bst.insert(20, "20");
    		bst.insert(15, "15");
    		bst.insert(25, "25");
    		bst.insert(30, "30");
    		bst.insert(12, "12");
    		bst.insert(17, "17");
    		bst.insert(28, "28"); // rotate 25-30-28
    		assert(bst.getKeyAtRoot() == 20);
    		assert(bst.getKeyOfRightChildOf(20) == 28);
    		assert(bst.getKeyOfLeftChildOf(28) == 25);
    		assert(bst.getKeyOfRightChildOf(28) == 30);
    	} catch (Exception e) {
			e.printStackTrace();
            fail( "Unexpected exception: "+e.getMessage() );
    	}
    	bst.print();
    }
    
    @Test
    void testBST_010_insert_cascade_rotates() {
    	try {
    		bst.insert(20, "20");
    		bst.insert(15, "15");
    		bst.insert(25, "25");
    		bst.insert(30, "30");
    		bst.insert(12, "12");
    		bst.insert(17, "17");
    		bst.insert(28, "28"); // rotate 25-30-28
    		bst.insert(33, "33"); // recolor here
    		assert(bst.getKeyOfRightChildOf(31) == 28);
    		assert(bst.getKeyOfLeftChildOf(28) == 25);
    		assert(bst.getKeyOfRightChildOf(28) == 30);
    		bst.insert(31, "31"); // one TNR here
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail( "Unexpected exception: "+e.getMessage() );
    	}	
    	bst.print();
    }
}