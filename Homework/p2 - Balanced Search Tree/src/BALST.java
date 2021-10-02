import java.util.List;

/**
 * @author Wilson Tjoeng
 * tjoeng@wisc.edu
 * CS400 010
 * Due: 10/15/21 
 *
 * Implementation of a balanced search tree using a red-black implementation.
 */

// DO IMPLEMENT A RED BLACK TREE IN THIS CLASS

/**
 * Defines the operations required of student's RBT class.
 *
 * NOTE: There are many methods in this interface 
 * that are required solely to support gray-box testing 
 * of the internal tree structure.  They must be implemented
 * as described to pass all grading tests.
 * 
 * @author Deb Deppeler (deppeler@cs.wisc.edu)
 * @param <K> A Comparable type to be used as a key to an associated value.  
 * @param <V> A value associated with the given key.
 */
public class BALST<K extends Comparable<K>, V> implements BALSTADT<K, V> {
    
    // Private fields of BST
    private BSTNode<K, V> root;
    private int heightTree; 
    private int numKeys;
	
	/**
	 * Inner private node class modified from BSTNode.java.
	 *  
	 * @author Wilson Tjoeng
	 *
	 * @param <K> A unique key
	 * @param <V> The key's associated value
	 */
	private class BSTNode<K,V> {
	    
	    K key;
	    V value;
	    BSTNode<K,V> left;
	    BSTNode<K,V> right;
	    BSTNode<K, V> parent;
	    String color;
	    int balanceFactor;
	    int height;

	    /**
	     * Constructor given a node's children 
	     * 
	     * @param key
	     * @param value
	     * @param leftChild
	     * @param rightChild
	     */
	    BSTNode(K key, V value, BSTNode<K,V>  leftChild, BSTNode<K,V> rightChild, BSTNode<K,V> parent) {
	        this.key = key; 
	        this.value = value;
	        this.left = leftChild;
	        this.right = rightChild;
	        this.parent = parent;
	        this.height = 0;
	        this.balanceFactor = 0;
	        this.color = null; // Color during insert
	    }
	    
	    // Constructs a node with no children
	    BSTNode(K key, V value) { this(key,value,null,null,null); }
	}
	
	/**
	 * Construct an empty BST with no root
	 */
	public BALST() {
		root = null;
		heightTree = 0;
		numKeys = 0;
	}
	
    /**
     * Returns the key that is in the root node of this ST.
     * If root is null, returns null.
     * @return key found at root node, or null
     */
    public K getKeyAtRoot() {
        if (this.root == null) {
        	return null;
        }
        
        return this.root.key;
    }
    
    /**
     * Tries to find a node with a key that matches the specified key.
     * If a matching node is found, it returns the returns the key that is in the left child.
     * If the left child of the found node is null, returns null.
     * 
     * @param key A key to search for
     * @return The key that is in the left child of the found key
     * 
     * @throws IllegalNullKeyException if key argument is null
     * @throws KeyNotFoundException if key is not found in this RBT
     */
    public K getKeyOfLeftChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
         return null;
    }
    
    /**
     * Tries to find a node with a key that matches the specified key.
     * If a matching node is found, it returns the returns the key that is in the right child.
     * If the right child of the found node is null, returns null.
     * 
     * @param key A key to search for
     * @return The key that is in the right child of the found key
     * 
     * @throws IllegalNullKeyException if key is null
     * @throws KeyNotFoundException if key is not found in this RBT
     */
    public K getKeyOfRightChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {
         return null;
    }
    

    /**
     * Returns the height of this RBT.
     * H is defined as the number of levels in the tree.
     * 
     * If root is null, return 0
     * If root is a leaf, return 1
     * Else return 1 + max( height(root.left), height(root.right) )
     * 
     * Examples:
     * A RBT with no keys, has a height of zero (0).
     * A RBT with one key, has a height of one (1).
     * A RBT with two keys, has a height of two (2).
     * A RBT with three keys, can be balanced with a height of two(2)
     *                        or it may be linear with a height of three (3)
     * ... and so on for tree with other heights
     * 
     * @return the number of levels that contain keys in this BINARY SEARCH TREE
     */
    public int getHeight() {
         return 0;
    }
    
    
    /**
     * Returns the keys of the data structure in sorted order.
     * In the case of binary search trees, the visit order is: L V R
     * 
     * If the SearchTree is empty, an empty list is returned.
     * 
     * @return List of Keys in-order
     */
    public List<K> getInOrderTraversal() {
        return null;
    }
    
    /**
     * Returns the keys of the data structure in pre-order traversal order.
     * In the case of binary search trees, the order is: V L R
     * 
     * If the SearchTree is empty, an empty list is returned.
     * 
     * @return List of Keys in pre-order
     */
    public List<K> getPreOrderTraversal() {
        return null;
    }

    /**
     * Returns the keys of the data structure in post-order traversal order.
     * In the case of binary search trees, the order is: L R V 
     * 
     * If the SearchTree is empty, an empty list is returned.
     * 
     * @return List of Keys in post-order
     */
    public List<K> getPostOrderTraversal() {
        return null;
    }

    /**
     * Returns the keys of the data structure in level-order traversal order.
     * 
     * The root is first in the list, then the keys found in the next level down,
     * and so on. 
     * 
     * If the SearchTree is empty, an empty list is returned.
     * 
     * @return List of Keys in level-order
     */
    public List<K> getLevelOrderTraversal() {
        return null;
    }
    
    
    /** 
     * Add the key,value pair to the data structure and increase the number of keys.
     * If key is null, throw IllegalNullKeyException;
     * If key is already in data structure, throw DuplicateKeyException(); 
     * Do not increase the num of keys in the structure, if key,value pair is not added.
     */
    public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
        if (this.contains(key)) {
        	throw new DuplicateKeyException("Cannot insert duplicate key");
        }
        
        if (key == null) {
        	throw new IllegalNullKeyException("");
        }
        
        root = insert(root, key, value, null);
        
        // If just inserted root 
        if (numKeys() == 0) {
        	root.color = "black";
        }
        
        // If not empty, then
        // 1. Use BST insert algorithm to add key
        // 2. Color the new node red
        // 3. Restore RB tree properties if necessary
        
        else {
        	root.color = "red";
        }
    }
    
    /**
     
     */
    
    /**
     * Insert node following BST recursion
     * 
     * @param node node to start traversal from
     * @param key key to insert
     * @param value value of key 
     * @param parent the parent node of the current node
     * @return the new node that was inserted
     * @throws DuplicateKeyException if key already exists
     */
    private BSTNode<K, V> insert(BSTNode<K, V> node, K key, V value, BSTNode<K, V> parent) throws DuplicateKeyException {
    	
    	// Base case - create new node and establish pointer to parent
    	if (node == null) {
        	return new BSTNode<K, V>(key, value, null, null, parent);
        }
    	
    	if (node.key.equals(key)) {
    		throw new DuplicateKeyException("Cannot insert duplicate key");
    	}
    	
    	// Add key to left subtree
    	if (key.compareTo(node.key) < 0) {
    		node.left = insert(node.left, key, value, node);
    	} 
    	
    	// Add key to right subtree
    	else {
    		node.right = (insert(node.right, key, value, node));
    	}
    	
		return node;

    }
    

    /** 
     * If key is found, remove the key,value pair from the data structure 
     * and decrease num keys, and return true.
     * If key is not found, do not decrease the number of keys in the data structure, return false.
     * If key is null, throw IllegalNullKeyException
     */
    public boolean remove(K key) throws IllegalNullKeyException {
        return false;
    }

    /**
     * Returns the value associated with the specified key.
     *
     * Does not remove key or decrease number of keys
     * If key is null, throw IllegalNullKeyException 
     * If key is not found, throw KeyNotFoundException().
     */
    public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
        return null;
    }

    /** 
     * Returns true if the key is in the data structure
     * If key is null, throw IllegalNullKeyException 
     * Returns false if key is not null and is not present 
     */
    public boolean contains(K key) throws IllegalNullKeyException { 
        return false;
    }

    /**
     *  Returns the number of key,value pairs in the data structure
     */
    public int numKeys() {
        return this.numKeys;
    }
    
    
    /**
     * Print the tree. 
     *
     * For our testing purposes of your print method: 
     * all keys that we insert in the tree will have 
     * a string length of exactly 2 characters.
     * example: numbers 10-99, or strings aa - zz, or AA to ZZ
     *
     * This makes it easier for you to not worry about spacing issues.
     *
     * You can display a binary search in any of a variety of ways, 
     * but we must see a tree that we can identify left and right children 
     * of each node
     *
     * For example: 
     
           30
           /\
          /  \
         20  40
         /   /\
        /   /  \
       10  35  50 

       Look from bottom to top. Inorder traversal of above tree (10,20,30,35,40,50)
       
       Or, you can display a tree of this kind.

       |       |-------50
       |-------40
       |       |-------35
       30
       |-------20
       |       |-------10
       
       Or, you can come up with your own orientation pattern, like this.

       10                 
               20
                       30
       35                
               40
       50                  

       The connecting lines are not required if we can interpret your tree.

     */
    public void print() {
        System.out.println("not yet implemented");
    }
    
    
    /**
     * Red property - the children of a red node are black
     * @return
     */
    private boolean redPropertyMaintained() {
    	
    }
    
} // copyrighted material, students do not have permission to post on public sites




//  deppeler@cs.wisc.edu
