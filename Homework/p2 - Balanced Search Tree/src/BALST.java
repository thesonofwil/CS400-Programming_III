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
	    char color;
	    int balanceFactor; // may not need this
	    int height; // may not need this

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
	        // Color during insert
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
        	root.color = 'b';
        }
        
        // If not empty, then
        // 1. Use BST insert algorithm to add key
        // 2. Color the new node red
        // 3. Restore RB tree properties if necessary
        
        else {
        	root.color = 'r';
        }
        
        numKeys++;
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
     * Returns the sibling of a parent node
     * 
     * @param P the parent of the node of interest
     * @param G the grandparent of the node of interest
     * @return
     */
    private BSTNode<K, V> getParentSibling(BSTNode<K, V> P, BSTNode<K, V> G) {
    	if (G.left.equals(P)) {
    		return G.right;
    	} else {
    		return G.left;
    	}
    }
    
    /**
     * Checks if the sibling of a child's parent is null. Used for TNR
     * @param n the child node
     * @return true if the parent of the child node has no sibling
     */
    private boolean parentSiblingIsNull(BSTNode<K, V> n) {
    	BSTNode<K, V> parent = n.parent;
    	
    	// n is already the root
    	if (parent == null) {
    		return false;
    	}
    	
    	BSTNode<K, V> grandparent = parent.parent;
    	
    	// Parent is the root, so no sibling
    	if (grandparent == null) {
    		return false;
    	}
    	
    	return getParentSibling(parent, grandparent) == null;
    }
    
    /**
     * Checks if the sibling of a child's parent is black. Used for TNR
     * @param n the child node
     * @return true if the parent of the child node has a black sibling
     */
    private boolean parentSiblingIsBlack(BSTNode<K, V> n) {
    	BSTNode<K, V> parent = n.parent;
    	
    	// n is already the root
    	if (parent == null) {
    		return false;
    	}
    	
    	BSTNode<K, V> grandparent = parent.parent;
    	
    	// Parent is the root, so no sibling
    	if (grandparent == null) {
    		return false;
    	}
    	
    	return getParentSibling(parent, grandparent).color == 'b';
    }
    
    /**
     * Checks if the sibling of a child's parent is red. Used for recoloring
     * @param n the child node
     * @return true if the parent of the child has a red sibling
     */
    private boolean parentSiblingIsRed(BSTNode<K, V> n) {
    	BSTNode<K, V> parent = n.parent;
    	
    	// n is already the root
    	if (parent == null) {
    		return false;
    	}
    	
    	BSTNode<K, V> grandparent = parent.parent;
    	
    	// Parent is the root, so no sibling
    	if (grandparent == null) {
    		return false;
    	}
    	
    	return getParentSibling(parent, grandparent).color == 'r';
    }
    
    // There's 4 cases to consider when doing TNR
    
    /**
     *  Case 1: P is the left-child of G and K is the left child of P. Do a
     *  right rotate on the root similar to AVL trees. 
     *  
     *  	G              P
     * 	   / \            / \
     * 	  P   S    ->    K   G
     *   /   / \        	/ \
     *  K	         	       S 					 
     *  
     * @param G the grandparent node to rotate on
     * @return the new root which is P
     */
    private BSTNode<K, V> rotateRight(Node<K, V> G) {
    	BSTNode<K, V> P = G.left;
    	BSTNode<K, V> K = P.left;
    	
    	// Rotate and update parent
    	G.left = P.right;
    	P.right.parent = G;
    	P.right = G;
    	G.parent = P;
    	    	
    	// Recolor 
    	P.color = 'b';
    	K.color = 'r';
    	G.color = 'r';
    	
    	return P;
    }
    
    /**
     *  Case 2: P is the right-child of G and K is the right child of P. Do a
     *  left rotate on the root similar to AVL trees. 
     *  
     *  	G              P
     * 	   / \            / \
     * 	  S   P    ->    G   K
     *   	 / \        / \
     *  	    K      S 					 
     *  
     * @param G the grandparent node to rotate on
     * @return the new root which is P
     */
    private BSTNode<K, V> rotateLeft(BSTNode<K, V> G) {
    	BSTNode<K, V> P = G.right;
    	BSTNode<K, V> K = P.right;
    	
    	// Rotate and update parent
    	G.right = P.left;
    	P.left.parent = G;
    	P.left = G;
    	G.parent = P;
    	    	
    	// Recolor 
    	P.color = 'b';
    	K.color = 'r';
    	G.color = 'r';
    	
    	return P;
    }
    
    /**
     *  Case 3: P is the left-child of G and K is the right child of P. Do a
     *  left-right rotate on the root. 
     *  
     *  	G              K
     * 	   / \            / \
     * 	  P   S    ->    P   G
     *   / \                / \
     *      K                  S 					 
     *  
     * @param G the grandparent node to rotate on
     * @return the new root which is P
     */
    private BSTNode<K, V> rotateLeftRight(BSTNode<K, V> G) {
    	BSTNode<K, V> P = G.left;
    	BSTNode<K, V> K = P.right;
    	
    	// Rotate and update parent
    	P.right = K.left;
    	K.left.parent = P;
    	G.left = K.right;
    	K.right.parent = G;
    	K.left = P;
    	P.parent = K;
    	K.right = G;
    	G.parent = K;
    	
    	// Recolor
    	K.color = 'b';
    	P.color = 'r';
    	G.color = 'r';
    	
    	return K;
    }
    
    /**
     *  Case 4: P is the right-child of G and K is the left child of P. Do a
     *  right-left rotate on the root. 
     *  
     *  	G              K
     * 	   / \            / \
     * 	  S   P    ->    G   P
     *       / \        / \
     *      K          S 					 
     *  
     * @param G the grandparent node to rotate on
     * @return the new root which is P
     */
    private BSTNode<K, V> rotateRightLeft(BSTNode<K, V> G) {
    	BSTNode<K, V> P = G.left;
    	BSTNode<K, V> K = P.right;
    	
    	// Rotate and update parent
    	P.right = K.left;
    	K.left.parent = P;
    	G.left = K.right;
    	K.right.parent = G;
    	K.left = P;
    	P.parent = K;
    	K.right = G;
    	G.parent = K;
    	
    	return K;
    }
    
    /**
     * Recolor nodes which occurs after inserting if P's sibling is red.
     * Structure remains the same and generally will look like below. 
     *  
     * 1. Set G to red, unless G is already the root which is black
     * 2. Set P and S to black
     * 3. Set K as red
     * 
     * [] - red
     *  O - black 
     * 
     * 		[]
     * 	   /  \
     *    O    O
     *        /
     *      []
     * 
     * 
     * @param K the leaf node that was inserted
     */
    private BSTNode<K,V> recolor(BSTNode<K, V> K) {
    	BSTNode<K, V> P = K.parent;
    	BSTNode<K, V> G = P.parent;
    	BSTNode<K, V> S = getParentSibling(P, G);
    	
    	if (!this.root.equals(G)) {
    		G.color = 'r';
    	}
    	P.color = 'b';
    	S.color = 'b';
    	K.color = 'r';
    	
    	return G; // If G happened to be the root
    }
    
    private void maintainRootProperty() {
    	this.root.color = 'b';
    }
    
    /**
     * Red property - the children of a red node are black
     * @return
     */
    private boolean redPropertyMaintained() {
    	
    }
    
} // copyrighted material, students do not have permission to post on public sites




//  deppeler@cs.wisc.edu
