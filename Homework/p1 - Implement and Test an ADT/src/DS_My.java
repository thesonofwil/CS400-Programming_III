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
	
	// Inner class defining key-value pair relationship
	private class Pair {
		private String key;
		private String value;
		
		// Inner constructor
		private Pair(String K, String V) {
			this.key = K;
			this.value = V;
		}
	}
    // Private Fields of the class
    // TODO create field(s) here to store data pairs
	private DS_My.Pair[] list; // Array representation of pairs
	private int numElements; // number of elements in list
	private int size; // size of list 

	/**
	 * Construct a new DS_My list of pairs of an arbitrary size
	 */
    public DS_My() {
    	size = 5;
    	numElements = 0;
    	list = new Pair[size];
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
		
		if (numElements == size) {
			grow();
		}
		
		Pair pair = new Pair(key, value);
		this.list[numElements] = pair;
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
		
		int index = find(key);
		if (index >= 0) {
			this.list[index] = null;
			this.numElements--;
			
			// After removing, need to re-organize list
		}
		return true;
	}

	@Override
	public String get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	// Despite name of name of method and fields, returns the number of elements in list
	public int size() {
		// TODO Auto-generated method stub
		return this.numElements;
	}
    
	// Create a larger list if at capacity
	private void grow() {
		size *= 2;
		
		DS_My.Pair[] tmp = new Pair[size];
		for (int i = 0; i < size; i++) {
			tmp[i] = this.list[i];
		}
		this.list = tmp;
	}
	
	/**
	 * Find the index of a key in DS_My list
	 * @param K The key to search for
	 * @return index of key. -1 if not found
	 */
	private int find(String K) {
		int index = -1;
		
		for (int i = 0; i < numElements; i++) {
			if (this.list[i].key.equals(K)) {
				index = i;
				break;
			}
		}
		return index;
	}

}                            
    
