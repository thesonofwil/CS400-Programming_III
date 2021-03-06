import java.util.ArrayList;
import java.util.List;

/**
 * @author Wilson Tjoeng
 * tjoeng@wisc.edu
 * CS400 010
 * Due: 10/15/21 
 *
 * Implementation of a balanced search tree using a red-black implementation. Note 
 * that remove() follows regular BST implementation. 
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
    private RBTNode<K, V> root;
    private int numKeys;
	
	/**
	 * Inner private node class modified from BSTNode.java.
	 *  
	 * @author Wilson Tjoeng
	 *
	 * @param <K> A unique key
	 * @param <V> The key's associated value
	 */
	private class RBTNode<K,V> {
	    
	    K key;
	    V value;
	    RBTNode<K,V> left;
	    RBTNode<K,V> right;
	    RBTNode<K, V> parent;
	    char color;

	    /**
	     * Constructor given a node's children 
	     * 
	     * @param key
	     * @param value
	     * @param leftChild
	     * @param rightChild
	     */
	    RBTNode(K key, V value, RBTNode<K,V>  leftChild, RBTNode<K,V> rightChild, RBTNode<K,V> parent, char color) {
	        this.key = key; 
	        this.value = value;
	        this.left = leftChild;
	        this.right = rightChild;
	        this.parent = parent;
	        this.color = color;
	    }
	    
	    // Constructs a node with no children
	    RBTNode(K key, V value) { this(key,value,null,null,null,'b'); }
	}
	
	/**
	 * Construct an empty BST with no root
	 */
	public BALST() {
		root = null;
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
    	if (key == null) {
    		throw new IllegalNullKeyException("Cannot handle null key");
    	}
    	
    	RBTNode<K, V> parent = find(key);
    	
    	if (parent == null) {
    		throw new KeyNotFoundException("Key not found");
    	}
    	
    	if (parent.left == null) {
    		return null;
    	} 
    	
        return parent.left.key;
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
    	if (key == null) {
    		throw new IllegalNullKeyException("Cannot handle null key");
    	}
    	
    	RBTNode<K, V> parent = find(key);
    	
    	if (parent == null) {
    		throw new KeyNotFoundException("Key not found");
    	}
    	
    	if (parent.right == null) {
    		return null;
    	} 
    	
        return parent.right.key;
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
         if (root == null) {
        	 return 0;
         } else if (numKeys() == 1) {
        	 return 1;
         } 
         
         return getHeight(root);
    }
    
    /**
     * Recursive helper function that returns the larger of heights between subtrees.
     * We define height of the tree as the number of nodes on the path from the root 
     * to the deepest leaf. 
     * 
     * @param n the node to start from
     * @return height of the tree
     */
    public int getHeight(RBTNode<K, V> n) {
    	if (n == null) {
    		return 0;
    	}
    	return Math.max(getHeight(n.left), getHeight(n.right)) + 1;
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
       List<K> list = new ArrayList<K>();
       
       inOrderTraverse(list, this.root);
       
       return list;
    }
    
    /**
     * In-order traversal recursive helper function.
     * 
     * @param list list to store keys in 
     * @param root the node to traverse from
     */
    private void inOrderTraverse(List<K> list, RBTNode<K, V> root) {
    	// Base case
    	if (root == null) {
    		return;
    	}
    	
    	inOrderTraverse(list, root.left); // Traverse left subtree
    	list.add(root.key);
    	inOrderTraverse(list, root.right); // Traverse right subtree
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
    	List<K> list = new ArrayList<K>();
        
        preOrderTraverse(list, this.root);
        
        return list;
    }
    
    /**
     * Pre-order traversal recursive helper function.
     * 
     * @param list list to store keys in 
     * @param root the node to traverse from
     */
    private void preOrderTraverse(List<K> list, RBTNode<K, V> root) {
    	// Base case
    	if (root == null) {
    		return;
    	}
    	
    	list.add(root.key);
    	preOrderTraverse(list, root.left); // Traverse left subtree
    	preOrderTraverse(list, root.right); // Traverse right subtree
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
    	List<K> list = new ArrayList<K>();
        
        postOrderTraverse(list, this.root);
        
        return list;    
    }
    
    /**
     * Post-order traversal recursive helper function.
     * 
     * @param list list to store keys in 
     * @param root the node to traverse from
     */
    private void postOrderTraverse(List<K> list, RBTNode<K, V> root) {
    	// Base case
    	if (root == null) {
    		return;
    	}
    	
    	postOrderTraverse(list, root.left); // Traverse left subtree
    	postOrderTraverse(list, root.right); // Traverse right subtree
    	list.add(root.key);
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
    	List<K> list = new ArrayList<K>();
    	
    	int heightTree = getHeight(root);
    	
    	for (int level = 1; level <= heightTree; level++) {
    		levelOrderTraverse(list, root, level);
    	}
    	
    	return list;
    }
    
    /**
     * Level-order traversal recursive helper function. Adds each node of tree level 
     * by level until level = height of tree. 
     * 
     * @param list list to store keys in
     * @param root the node to traverse from
     * @param level the level at which to get nodes from tree
     */
    private void levelOrderTraverse(List<K> list, RBTNode<K, V> root, int level) {
    	if (root == null) {
    		return;
    	}
    	
    	if (level == 1) {
    		list.add(root.key);
    	} else if (level > 1) {
    		levelOrderTraverse(list, root.left, level - 1);
    		levelOrderTraverse(list, root.right, level - 1);
    	}
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
        
        insert(root, key, value, null, 0);         
        numKeys++;
    }

    /**
     * Insert node following BST recursion and fix any violations
     * 
     * @param node node to start traversal from
     * @param key key to insert
     * @param value value of key
     * @param parent the parent node of node
     * @param cmp used to update parent/left/right pointers
     * @throws DuplicateKeyException if key already exists in tree
     */
    private void insert(RBTNode<K, V> node, K key, V value, RBTNode<K, V> parent, int cmp) throws DuplicateKeyException {
    	
    	// Insert at root if there are no nodes
    	if (root == null) {
    		root = new RBTNode<K, V>(key, value, null, null, null, 'b');
    		return;
    	}
    	
    	// Base case - create new red node and update pointers
    	if (node == null) {
        	RBTNode<K,V> leaf = new RBTNode<K, V>(key, value, null, null, parent,'r');
        	if (cmp < 0) {
        		parent.left = leaf;
        	} else if (cmp > 0) {
        		parent.right = leaf;
        	} 
        	return;
        }
    	
    	// Recursively travel through tree and insert at empty spot 
    	cmp = key.compareTo(node.key);    
    	if (cmp == 0) {
    		throw new DuplicateKeyException("Cannot insert duplicate key");
    	} else if (cmp < 0) { // Add key to left subtree
    		insert(node.left, key, value, node, cmp);
    		maintainRedProperty(node.left);
    	} else { // Add key to right subtree 
    		insert(node.right, key, value, node,cmp);
    		maintainRedProperty(node.right);
    	} 
    }
    
    /** 
     * If key is found, remove the key,value pair from the data structure 
     * and decrease num keys, and return true. Note this remove method only 
     * maintains BST property, NOT RB properties. 
     * If key is not found, do not decrease the number of keys in the data structure, return false.
     * If key is null, throw IllegalNullKeyException
     */
    public boolean remove(K key) throws IllegalNullKeyException {
    	if (key == null) {
    		throw new IllegalNullKeyException("Cannot handle null key");
    	}
    	
    	if (!contains(key)) {
    		return false;
    	}
    	
    	root = remove(root, key); // Recursively go through BST and delete key
    	numKeys--;
    	
    	return true;
    }
    
    private RBTNode<K, V> remove(RBTNode<K, V> n, K key) throws IllegalNullKeyException {
    	
    	if (key == null) {
    		throw new IllegalNullKeyException("Cannot handle null key");
    	}
    	
    	// Base case and Case 1 - n has no children
    	if (n == null) {
    		return n;
    	} 
    	
    	// Another base case - key found
    	if (n.key.equals(key)) {
    		if (n.left == null && n.right == null) {
    			return null;
    		}
    		
    		// Case 2: n has one child
        	if (n.left == null) {
        		return n.right;
        	} else if (n.right == null) {
        		return n.left;
        	}
        	
        	// Case 3: n has two children
        	// Get In-order predecessor key by finding largest key in the left subtree of node
        	RBTNode<K, V> predecessor = find(max(n.left).key); 
        	n.key = predecessor.key;
        	n.value = predecessor.value;
        	n.left = remove(n.left, predecessor.key);
    	}
        	
        	else if (key.compareTo(n.key) < 0) {
        		n.left = remove(n.left, key);
        	} else {
        		n.right = remove(n.right, key);
        	}
    	return n;
    }
    	
    /**
     * Returns the value associated with the specified key.
     *
     * Does not remove key or decrease number of keys
     * If key is null, throw IllegalNullKeyException 
     * If key is not found, throw KeyNotFoundException().
     */
    public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
    	if (key == null) {
        	throw new IllegalNullKeyException("Cannot handle null key");
        }
        
        if (!contains(key)) {
        	throw new KeyNotFoundException("Key not found");
        }
        return find(key).value;
    }

    /** 
     * Returns true if the key is in the data structure
     * If key is null, throw IllegalNullKeyException 
     * Returns false if key is not null and is not present 
     */
    public boolean contains(K key) throws IllegalNullKeyException { 
    	if (key == null) {
        	throw new IllegalNullKeyException("Cannot handle null key");
        }	
    	return find(key) != null;
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
        int space = 0;
        int height = 5;
    	print(root, space, height);    	
    }
    
    public void print(RBTNode<K, V> n, int space, int height)
    {
        // Base case
        if (n == null) {
            return;
        }
 
        // increase distance between levels
        space += height;
 
        // print right child first
        print(n.right, space, height);
        System.out.println();
 
        // print the current node after padding with spaces
        for (int i = height; i < space; i++) {
            System.out.print(' ');
        }
 
        System.out.print(n.key);
 
        // print left child
        System.out.println();
        print(n.left, space, height);
    }
    
    /**
     * Finds the RBTNode in the BST given the key.
     * 
     * @param k they key to search
     * @return RBTNode that has the key. Null if the key isn't there
     * @throws IllegalNullKeyException if key is null
     */
    private RBTNode<K, V> find(K k) throws IllegalNullKeyException {
    	if (k == null) {
    		throw new IllegalNullKeyException("Cannot handle null key");
    	} 
    	
    	RBTNode<K, V> n = root;
    	while (n != null) {
	    	int cmp = k.compareTo(n.key);
	    	if (cmp < 0) {
	    		n = n.left;
	    	} else if (cmp > 0) {
	    		n = n.right;
	    	} else {
	    		return n;
	    	}
    	}
    	return n;
    }
    
    // This function is not in use, but could be used instead to get
    // the in-order successor when deleting
    
    /**
     * Finds the node with the smallest key in the BST
     * 
     * @param n node to start from
     * @return the node with the smallest key
     */
    private RBTNode<K, V> min(RBTNode<K, V> n) {
    	if (n.left != null) {
    		return max(n.left);
    	}
    	return n;
    }
    
    /**
     * Finds the node with the largest key in the BST
     * 
     * @param n node to start from
     * @return the node with the largest key
     */
    private RBTNode<K, V> max(RBTNode<K, V> n) {
    	if (n.right != null) {
    		return max(n.right);
    	}
    	return n;
    }
    
    /**
     * After insert or delete, check if red property is violated and restore if needed
     * Red property - red nodes must have black children. New leaves are added as black
     * 
     * @return
     */
    private void maintainRedProperty(RBTNode<K, V> K) {
    	
    	if (K == null || K.equals(root)) {
    		return;
    	}
    	
    	// Check if K violates red property
    	// 1. Red nodes must have black children
    	// 2. That also means K can be red with a black parent if K is a leaf
    	if ((K.color == 'b' && K.parent.color == 'r') || 
    			(isOnlyChild(K)) && K.color == 'r' && K.parent.color == 'b') {
    		return;
    	}
    	    	
    	// Recolor if K's parent has a red sibling
    	if (parentSiblingIsRed(K)) {
    		recolor(K);
    	} else if (parentSiblingIsNull(K) || parentSiblingIsBlack(K)) {
    		RBTNode<K, V> P = K.parent;
    		RBTNode<K, V> G = P.parent;
    		boolean doubleRotated = false; // This determines recoloring after rotating
    		
    		// TNR depending on structures of K, P, and G
    		if ((G.left != null && P.left != null) && (G.left.equals(P) && P.left.equals(K))) {
    			rotateRight(G);
    		} else if ((G.right != null && P.right != null) && (G.right.equals(P) && P.right.equals(K))) {
    			rotateLeft(G);
    		} else if ((G.left != null && P.right != null) && (G.left.equals(P) && P.right.equals(K))) {
    			rotateLeftRight(G);
    			doubleRotated = true;
    		} else if ((G.right != null && P.left != null) && (G.right.equals(P) && P.left.equals(K))) {
    			rotateRightLeft(G);
    			doubleRotated = true;
    		}
    		
    		// Recolor
    		if (doubleRotated) {
	        	K.color = 'b';
	        	P.color = 'r';
	        	G.color = 'r';
	        } else {
	        	P.color = 'b';
	        	K.color = 'r';
	        	G.color = 'r';
	        }
    	}
    }
    
    /**
     * Returns the sibling of a parent node
     * 
     * @param P the parent of the node of interest
     * @param G the grandparent of the node of interest
     * @return the sibling node of P, or null if there is none
     */
    private RBTNode<K, V> getParentSibling(RBTNode<K, V> P, RBTNode<K, V> G) {
    	
    	if (G.left != null && G.left.equals(P)) {
    		return G.right;
    	} else {
    		return G.left;
    	}
    }
    
    /**
     * Checks if the sibling of a child's parent is null. Used for TNR
     * @param K the child node
     * @return true if the parent of the child node has no sibling
     */
    private boolean parentSiblingIsNull(RBTNode<K, V> K) {
    	RBTNode<K, V> P = K.parent;
    	
    	// n is already the root
    	if (P == null) {
    		return false;
    	}
    	
    	RBTNode<K, V> G = P.parent;
    	
    	// Parent is the root, so no sibling
    	if (G == null) {
    		return false;
    	}
    	
    	return getParentSibling(P, G) == null;
    }
    
    /**
     * Checks if the sibling of a child's parent is black. Used for TNR
     * @param K the child node
     * @return true if the parent of the child node has a black sibling
     */
    private boolean parentSiblingIsBlack(RBTNode<K, V> K) {
    	RBTNode<K, V> P = K.parent;
    	
    	// n is already the root
    	if (P == null) {
    		return false;
    	}
    	
    	RBTNode<K, V> G = P.parent;
    	
    	// Parent is the root, so no sibling
    	if (G == null) {
    		return false;
    	}
    	
    	return getParentSibling(P, G).color == 'b';
    }
    
    /**
     * Checks if the sibling of a child's parent is red. Used for recoloring
     * @param n the child node
     * @return true if the parent of the child has a red sibling
     */
    private boolean parentSiblingIsRed(RBTNode<K, V> n) {
    	RBTNode<K, V> parent = n.parent;
    	
    	// n is already the root
    	if (parent == null) {
    		return false;
    	}
    	
    	RBTNode<K, V> grandparent = parent.parent;
    	
    	// Parent is the root, so no sibling
    	if (grandparent == null) {
    		return false;
    	}
    	
    	RBTNode<K, V> sibling = getParentSibling(parent, grandparent);
    	
    	if (sibling == null) {
    		return false;
    	}
    	
    	return sibling.color == 'r';
    }
    
    private boolean isOnlyChild(RBTNode<K, V> n) {
    	return (n.left == null && n.right == null);
    }
    
    // There's 4 cases to consider when doing tri-node restructure
    
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
    private RBTNode<K, V> rotateRight(RBTNode<K, V> G) {
    	RBTNode<K, V> P = G.left;
    	
    	// Rotate and update parent
    	G.left = P.right;
    	if (P.right != null) {
    		P.right.parent = G;
    	}
    	
    	P.parent = G.parent;
    	if (G.parent == null) {
    		this.root = P;
    	} else if (G.equals(G.parent.right)) {
    		G.parent.right = P;
    	} else {
    		G.parent.left = P;
    	}
    	P.right = G;
    	G.parent = P;
    	
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
    private RBTNode<K, V> rotateLeft(RBTNode<K, V> G) {
    	RBTNode<K, V> P = G.right;
    	
    	G.right = P.left;
    	if (P.left != null) {
    		P.left.parent = G;
    	}
    	
    	P.parent = G.parent;
    	if (G.parent == null) {
    		this.root = P;
    	} else if (G.equals(G.parent.left)) {
    		G.parent.left = P;
    	} else {
    		G.parent.right = P;
    	}
    	P.left = G;
    	G.parent = P;
    	
    	return P;
    }
    
    /**
     *  Case 3: P is the left-child of G and K is the right child of P. Do a
     *  left rotate on P and then a right rotate on G. 
     *  
     *  	G          		G		        K
     * 	   / \             / \		       / \
     * 	  P   S    ->     K	  S	   ->	  P   G
     *   / \             / \		         / \
     *      K           P   			        S 					 
     *  
     * @param G the grandparent node to rotate on
     * @return the new root which is K
     */
    private RBTNode<K, V> rotateLeftRight(RBTNode<K, V> G) {
    	RBTNode<K, V> P = G.left;
    	RBTNode<K, V> K = P.right;
    	
    	rotateLeft(P);
    	rotateRight(G);	   	
    	return K;
    }
    
    /**
     *  Case 4: P is the right-child of G and K is the left child of P. Do a
     *  right rotate on P and then a left rotate on G. 
     *  
     *  	G          		G		        K
     * 	   / \             / \		       / \
     * 	  S   P    ->     S	  K	   ->	  G   P
     *       / \             / \		 / \
     *      K                	P	    S 	
     *  
     * @param G the grandparent node to rotate on
     * @return the new root which is K
     */
    private RBTNode<K, V> rotateRightLeft(RBTNode<K, V> G) {
    	RBTNode<K, V> P = G.right;
    	RBTNode<K, V> K = P.left;
    	
    	rotateRight(P);
    	rotateLeft(G);
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
    private void recolor(RBTNode<K, V> K) {
    	RBTNode<K, V> P = K.parent;
    	RBTNode<K, V> G = P.parent;
    	RBTNode<K, V> S = getParentSibling(P, G);
    	
    	if (!this.root.equals(G)) {
    		G.color = 'r';
    	}
    	
    	P.color = 'b';
    	S.color = 'b';
    	K.color = 'r';    	
    }  
} // copyrighted material, students do not have permission to post on public sites




//  deppeler@cs.wisc.edu
