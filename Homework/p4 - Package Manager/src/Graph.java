import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filename:   Graph.java
 * Project:    p4
 * Authors:    Wilson Tjoeng
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
	
	private static Set<Vertex> vertices;
	//private static ArrayList<ArrayList<Boolean>> adjMatrix;
	private int numVertices;
	private int numEdges;
	
	/*
	 * Default no-argument constructor
	 */ 
	public Graph() {
		vertices = new HashSet<Vertex>();
		numVertices = 0;
		numEdges = 0;
	}
	
	/**
	 * Private inner vertex class
	 * 
	 * @author Wilson Tjoeng
	 *
	 */
	private class Vertex {
		String data;
		List<Vertex> dependencies; // direct dependencies e.g. A -> [B, C] -> D,
								   // then store B and C
		/**
		 * Vertex constructor 
		 * 
		 * @param s String data to hold
		 */
		Vertex (String s) {
			this.data = s;
			dependencies = new ArrayList<Vertex>();
		}
	}

	/**
     * Add new vertex to the graph.
     *
     * If vertex is null or already exists,
     * method ends without adding a vertex or 
     * throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
	public void addVertex(String vertex) {
		if (vertex == null || hasVertex(vertex)) {
			return;
		}
		
		Vertex v = new Vertex(vertex);
		vertices.add(v);
		numVertices++;
	}

	
	/**
     * Remove a vertex and all associated 
     * edges from the graph.
     * 
     * If vertex is null or does not exist,
     * method ends without removing a vertex, edges, 
     * or throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
	public void removeVertex(String vertex) {
		if (vertex == null || !hasVertex(vertex)) {
			return;
		}
		
		Vertex v = getVertex(vertex);
		removeAdjacentEdges(v);
		vertices.remove(v);
		numVertices--;
	}

	/**
     * Add the edge from vertex1 to vertex2
     * to this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * add vertex, and add edge, no exception is thrown.
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge is not in the graph
	 */
	public void addEdge(String vertex1, String vertex2) {
		
		if (vertex1 == null || vertex2 == null) {
			return;
		}
		
		// Add vertices if they don't exist
		// addVertex already checks for existence
		addVertex(vertex1);
		addVertex(vertex2);
		
		// Get vertex nodes
		Vertex v1 = getVertex(vertex1);
		Vertex v2 = getVertex(vertex2);
		
		if (hasDependency(v1, v2)) { // There is already an edge
			return;
		}
		
		v1.dependencies.add(v2);
		numEdges++;
	}

	/**
     * Remove the edge from vertex1 to vertex2
     * from this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * or if an edge from vertex1 to vertex2 does not exist,
     * no edge is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge from vertex1 to vertex2 is in the graph
     */
	public void removeEdge(String vertex1, String vertex2) {
		if (vertex1 == null || vertex2 == null) {
			return;
		}
		
		if (!hasVertex(vertex1) || !hasVertex(vertex2)) {
			return;
		}
		
		Vertex v1 = getVertex(vertex1);
		Vertex v2 = getVertex(vertex2);
		
//		if (hasOutgoingEdge(v1, v2)) {
//			return;
//		}
				
		// Remove entry. Nothing happens if object is not in list
		if (v1.dependencies.remove(v2)) {
			numEdges--;
		}	
	}

	/**
     * Returns a Set that contains all the vertices
     * 
	 */
	public Set<String> getAllVertices() {
		Set<String> set = new HashSet<String>();
		
		for (Vertex v : vertices) {
			set.add(v.data);
		}
		
		return set;
	}

	/**
     * Get all the neighbor (adjacent-dependencies) of a vertex
     * 
     * For the example graph, A->[B, C], D->[A, B] 
     *     getAdjacentVerticesOf(A) should return [B, C]. 
     * 
     * In terms of packages, this list contains the immediate 
     * dependencies of A and depending on your graph structure, 
     * this could be either the predecessors or successors of A.
     * 
     * @param vertex the specified vertex
     * @return an List<String> of all the adjacent vertices for specified vertex
     */
	public List<String> getAdjacentVerticesOf(String vertex) {
		
		// Vertex not in graph
		if (!hasVertex(vertex)) {
			return null;
		}
		
		List<String> neighbors = new ArrayList<String>();
		
		Vertex v = getVertex(vertex);
		
		// Loop through outgoing vertex lists
		for (Vertex neighbor : v.dependencies) {
			neighbors.add(neighbor.data);
		}
		
		return neighbors;
	}

	/**
     * Returns the number of edges in this graph.
     * 
     * @return number of edges in the graph.
     */
    public int size() {
        return numEdges;
    }
    
    /**
     * Returns the number of vertices in this graph.
     * 
     * @return number of vertices in graph.
     */
	public int order() {
		return numVertices;
	}
	
	/////---------------- Private Helper Methods ----------------\\\\\
	
	/**
	 * Removes all edges connected to a vertex by removing vertex from adjacency lists
	 * 
	 * @param v vertex to clear edges from 
	 */
	private void removeAdjacentEdges(Vertex v) {
//		for (Vertex vertex : vertices) {
//			if (vertex.dependencies.remove(v)) {
//				numEdges--;
//			}
//		}
//		
//		int numOutgoingEdges = v.dependencies.size();
//		numEdges = numEdges - numOutgoingEdges;
		
		// Remove edges pointing to v
		for (Vertex vertex : vertices) {
			removeEdge(vertex.data, v.data);
		}
		
		// Remove edges coming from v
		for (Vertex vertex : v.dependencies) {
			removeEdge(v.data, vertex.data);
		}
	}
	
	/**
	 * Checks if graph has vertex with the given string
	 * 
	 * @param data string to search for in vertex set
	 * @return true if graph has a vertex with the given string; false otherwise
	 */
	private boolean hasVertex(String data) {
		
		for (Vertex v : vertices) {
			if (v.data.equals(data)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the vertex node from graph given string
	 * 
	 * @param data string to search for in vertex set
	 * @return vertex object with the given string
	 */
	private Vertex getVertex(String data) {
		for (Vertex v : vertices) {
			if (v.data.equals(data)) {
				return v;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if there is a direct dependency between v1 and v2 i.e v1 -> v2
	 * 
	 * @param v1 the source vertex 
	 * @param v2 the target vertex
	 * @return true if v1 points to v2; false otherwise
	 */
	private boolean hasDependency(Vertex v1, Vertex v2) {
		for (Vertex v : v1.dependencies) {
			if (v.equals(v2)) {
				return true;
			}
		}
		
		return false;
	}	
}