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
	private static Set<Edge> edges;
	//private static ArrayList<ArrayList<Boolean>> adjMatrix;
	private int numVertices;
	private int numEdges;
	
	/**
	 * Vertex class that holds data and keeps track of directed edges
	 * 
	 * @author Wilson Tjoeng
	 *
	 */
	private class Vertex {
		String data;
		List<Edge> outgoing; // edges going out of vertex
		List<Edge> incoming; // edges going to vertex
		
		Vertex (String s) {
			this.data = s;
		}
	}
	
	/**
	 * Edge class used to connect a source vertex to a target vertex
	 * 
	 * @author Wilson Tjoeng
	 *
	 */
	private class Edge {
		Vertex source;
		Vertex target;
		
		Edge (Vertex source, Vertex target) {
			this.source = source;
			this.target = target;
		}
	}
	
	/*
	 * Default no-argument constructor
	 */ 
	public Graph() {
		numVertices = 0;
		numEdges = 0;
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
	 * Removes all edges connected to a vertex 
	 * 
	 * @param v vertex to clear edges from 
	 */
	private void removeAdjacentEdges(Vertex v) {
		for (Edge e : v.outgoing) {
			removeEdge(e.source.data, e.target.data);
		}
		
		for (Edge e : v.incoming) {
			removeEdge(e.source.data, e.target.data);
		}
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
		
		if (hasOutgoingEdge(v1, v2)) {
			return;
		}
		
		Edge e = new Edge(v1, v2);
		
		v1.outgoing.add(e);
		v2.incoming.add(e);
		edges.add(e);
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
		
		Edge e = getOutgoingEdge(v1, v2);
		
		// Remove entry. Nothing happens if object is not in list
		v1.outgoing.remove(e);
		v2.incoming.remove(e);
		edges.remove(e);
		numEdges--;
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
     * Get all the neighbor (adjacent) vertices of a vertex
     *
	 */
	public List<String> getAdjacentVerticesOf(String vertex) {
		
		// Vertex not in graph
		if (!hasVertex(vertex)) {
			return null;
		}
		
		List<String> neighbors = new ArrayList<String>();
		
		Vertex v = getVertex(vertex);
		
		// Loop through incoming and outgoing vertex lists
		for (Edge e : v.incoming) {
			neighbors.add(e.source.data);
		}
		
		for (Edge e : v.outgoing) {
			neighbors.add(e.target.data);
		}
		
		return neighbors;
	}
	
	/**
     * Returns the number of edges in this graph.
     */
    public int size() {
        return numEdges;
    }

	/**
     * Returns the number of vertices in this graph.
     */
	public int order() {
        return numVertices;
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
	 * Checks if there is an edge that connects v1 and v2 regardless of direction
	 * 
	 * @param v1
	 * @param v2
	 * @return true if v1 and v2 are connected; false otherwise
	 */
	private boolean hasEdge(Vertex v1, Vertex v2) {
		
		// We just need to check if one vertex exists in either the 
		// incoming or outgoing lists of the other vertex
		
		for (Edge e : edges) {
			if ((e.source.equals(v1) && e.target.equals(v2)) || 
					(e.source.equals(v2)) && (e.target.equals(v1))) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if there is an edge from v1 to v2
	 * 
	 * @param v1 the source vertex 
	 * @param v2 the target vertex
	 * @return true if v1 points to v2; false otherwise
	 */
	private boolean hasOutgoingEdge(Vertex v1, Vertex v2) {
		for (Edge e : v1.outgoing) {
			if (e.target.equals(v2)) {
				return true;
			}
		}
		
		return false;
	}
	
	private Edge getOutgoingEdge(Vertex v1, Vertex v2) {
		for (Edge e : v1.outgoing) {
			if (e.target.equals(v2)) {
				return e;
			}
		}
		return null;
	}
}
