// TODO: Add file header here




// TODO: Add class header here
/**
 * 
 * @author Wilson Tjoeng
 *
 */
public class DS_My implements DataStructureADT< String, String > {

    // TODO may wish to define an inner class 
    // for storing key and value as a pair
    // such a class and its members should be "private"
	
	// Inner class - doubly linked list defining key-value pair relationship
	private static class Node {
		private String key;
		private String value;
		private Node next;
		private Node prev;
		
		// Inner constructor 
		private Node(String K, String V) {
			this.key = K;
			this.value = V;
			this.next = null; 
			this.prev = null;
		}
	}
    // Private Fields of the class
    // TODO create field(s) here to store data pairs
	
	//private DS_My.Pair[] list; // Array representation of pairs
	private int numElements; // number of elements in list
	//private int size; // size of list 
	private Node head; // start of linked list
	/**
	 * Construct a new DS_My list of pairs of an arbitrary size
	 */
    public DS_My() {
    	//size = 5;
    	numElements = 0;
    	//list = new Pair[size];
    	head = null;
    }

	@Override
	// Insert a new Pair at end of list 
	// Add the key,value pair to the data structure and increases size.
    // If key is null, throws IllegalArgumentException("null key");
    // If key is already in data structure, throws RuntimeException("duplicate key");
    // can accept and insert null values
	public void insert(String key, String value) {
		// TODO Auto-generated method stub
		if (this.contains(key)) {
			throw new RuntimeException("duplicate key");
		}
		
		if (key == null) {
			throw new IllegalArgumentException("null key");
		}
		
//		if (numElements == size) {
//			grow();
//		}
		
		//Pair pair = new Pair(key, value);
		Node newNode = new Node(key,value);
		
		// Insert new node at head if empty or end of list if not
		if (this.size() == 0) {
			this.head = newNode;
		} else {
			Node last = find();
			last.next = newNode;
			newNode.prev = last;
			
		}
		//this.list[numElements] = pair;
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
		
		if (!this.contains(key)) {
			return false;
		}
		
		Node curr = find(key);
		
		// If deleting head, set head to next node
		if (this.head.key.equals(curr.key)) {
			this.head = this.head.next;
			this.head.prev = null;
		} else {
			curr.prev = curr.next;
		}
		
		this.numElements--;
//		int index = find(key);
//		if (index >= 0) {
//			this.list[index] = null;
//			this.numElements--;
			
			// After removing, need to re-organize list
//		}
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
    
	// Return the last node in list 
	private Node find() { 
		
		// Loop through nodes starting at head
		Node curr = this.head;
		while (curr.next != null) {
			curr = curr.next;
		}
		
		return curr;
	}
	
	// Return node given a key, or null if not found 
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
//	// Create a larger list if at capacity
//	private void grow() {
//		size *= 2;
//		
//		DS_My.Pair[] tmp = new Pair[size];
//		for (int i = 0; i < size; i++) {
//			tmp[i] = this.list[i];
//		}
//		this.list = tmp;
//	}
//	
//	/**
//	 * Find the index of a key in DS_My list
//	 * @param K The key to search for
//	 * @return index of key. -1 if not found
//	 */
//	private int find(String K) {
//		int index = -1;
//		
//		for (int i = 0; i < numElements; i++) {
//			if (this.list[i].key.equals(K)) {
//				index = i;
//				break;
//			}
//		}
//		return index;
//	}
//
//}                            
    
