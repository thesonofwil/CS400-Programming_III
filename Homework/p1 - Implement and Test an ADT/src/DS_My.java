/**
 * @author Wilson Tjoeng
 * tjoeng@wisc.edu
 * CS400 010
 * Due: 9/30/21 
 *
 * Implementation of a DS using a doubly linked list where each node holds a key-value pair.
 */
public class DS_My implements DataStructureADT< String, String > {
	
	// Inner class - doubly linked list defining key-value pair relationship
	private static class Node {
		private String key;
		private String value;
		private Node next;
		private Node prev;
		
		/**
		 * Node constructor
		 * 
		 * @param K key
		 * @param V value
		 */
		private Node(String K, String V) {
			this.key = K;
			this.value = V;
			this.next = null; 
			this.prev = null;
		}
	}
    // Private Fields of the class
	private int numElements; // number of elements in list
	private Node head; // start of linked list
	
	/**
	 * Construct a new DS_My list with a null head node.
	 */
    public DS_My() {
    	numElements = 0;
    	head = null;
    }

	@Override
	// Insert a new Pair at end of list
	// Add the key,value pair to the data structure and increases size.
    // If key is null, throws IllegalArgumentException("null key");
    // If key is already in data structure, throws RuntimeException("duplicate key");
    // can accept and insert null values
	public void insert(String key, String value) {
		if (this.contains(key)) {
			throw new RuntimeException("duplicate key");
		}
		
		if (key == null) {
			throw new IllegalArgumentException("null key");
		}
		
		Node newNode = new Node(key,value);
		
		// Insert new node at head if empty or end of list if not
		if (this.size() == 0) {
			this.head = newNode;
		} else {
			Node last = find();
			last.next = newNode;
			newNode.prev = last;
			
		}
		this.numElements++;
	}

	@Override
	// If key is found, Removes the key from the data structure and decreases size
    // If key is null, throws IllegalArgumentException("null key") without decreasing size
    // If key is not found, returns false.
	public boolean remove(String key) {
		if (key == null) {
			throw new IllegalArgumentException("null key");
		}
		
		// cannot remove if list is empty
		if (this.size() == 0) {
			throw new IllegalStateException("list is empty");
		}
		
		if (!this.contains(key)) {
			return false;
		}
		
		Node curr = find(key);
		
		// Delete head if found in beginning
		if (this.head.key.equals(curr.key)) {
			this.head = this.head.next;
			// Update previous pointer if needed 
			if (this.size() > 1) {
				this.head.prev = null;
			}
			// Node is found after
			// Delete node by linking previous to next and next to previous
		} else {
			curr.prev.next = curr.next;
			// If deleting tail, then stop here
			if (curr.next != null) {
				curr.next.prev = curr.prev;
			}
		}
		
		this.numElements--;
		return true;
	}

	@Override
	// Returns the value associated with the specified key
    // get - does not remove key or decrease size
    // return null if key is not null and is not found in data structure
    // If key is null, throws IllegalArgumentException("null key") 
	public String get(String key) {
		if (key == null) {
			throw new IllegalArgumentException("null key");
		}
		
		// If key is not found
		if (!contains(key)) {
			return null;
		}
		
		// Else find node and return value
		return find(key).value;
	}

	@Override
    // Returns true if the key is in the data structure
    // Returns false if key is null or not present
	public boolean contains(String key) {
		if (key == null) {
			return false;
		}
		
		if (find(key) == null) {
			return false; // key was not found
		}
		return true;
	}

	@Override
	// Despite name of name of method and fields, returns the number of elements in list
	public int size() {
		return this.numElements;
	}
    
	// ------ Private Helper Methods ------
	
	/**
	 * Finds the last node in a list
	 * 
	 * @return the last node, or null if empty
	 */
	private Node find() { 
		
		// Loop through nodes starting at head
		Node curr = this.head;
		while (curr.next != null) {
			curr = curr.next;
		}
		
		return curr;
	}
	
	/**
	 * Finds the node containing the specified key
	 * 
	 * @param K the string key value to search for in the list
	 * @return the node containing K
	 */
	private Node find(String K) {
		if (K == null) {
			throw new IllegalArgumentException("null key");
		}
		
		Node curr = this.head;
		while (curr != null && !curr.key.equals(K)) {
			curr = curr.next;
		}
		
		return curr;
	}
}