import org.junit.After;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GraphTest {

	protected Graph graph;
	
	/**
     * @throws java.lang.Exception
     */
	@BeforeEach
	void setUp() throws Exception {
		graph = new Graph();
	}
	
	/**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }
	
	@Test
	void test001_simple_insert_vertex() {
		graph.addVertex("1");
		Set<String> vertices = graph.getAllVertices();
		assert(vertices.contains("1"));
		assert(graph.order() == 1); // Num Vertices
		assert(graph.size() == 0);
	}
	
	@Test
	void test002_simple_insert_vertices() {
		graph.addVertex("1");
		graph.addVertex("2");
		graph.addVertex("3");
		Set<String> vertices = graph.getAllVertices();
		assert(vertices.contains("1"));
		assert(vertices.contains("2"));
		assert(vertices.contains("3"));
		assert(graph.order() == 3); // Num Vertices
		assert(graph.size() == 0);
	}
	
	@Test
	void test003_insert_null_vertex() {
		graph.addVertex("1");
		graph.addVertex(null);
		Set<String> vertices = graph.getAllVertices();
		assert(vertices.contains("1"));
		assert(graph.order() == 1); // Num Vertices
		assert(graph.size() == 0);
	}
	
	@Test
	void test004_insert_duplicate_vertex() {
		graph.addVertex("1");
		graph.addVertex("1");
		Set<String> vertices = graph.getAllVertices();
		assert(vertices.contains("1"));
		assert(graph.order() == 1); // Num Vertices
		assert(graph.size() == 0);
	}
	
	@Test
	void test005_add_simple_edge() {
		graph.addVertex("1");
		graph.addVertex("2");
		graph.addEdge("1", "2"); // Edge points from 1 to 2
		Set<String> vertices = graph.getAllVertices();
		assert(vertices.contains("1"));
		assert(graph.order() == 2); // Num Vertices
		assert(graph.size() == 1); // Num Edges
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		assert(AdjVertices_1.contains("2"));
		assert(AdjVertices_1.size() == 1);
		
		List<String> AdjVertices_2 = graph.getAdjacentVerticesOf("2");
		assert(AdjVertices_2.size() == 0);
	}
	
	@Test
	void test006_add_edge_vertices_do_not_exist() {
		graph.addEdge("1", "2");
		Set<String> vertices = graph.getAllVertices();
		assert(vertices.contains("1"));
		assert(vertices.contains("2"));
		assert(graph.order() == 2); // Num Vertices
		assert(graph.size() == 1); // Num Edges
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		assert(AdjVertices_1.contains("2"));
		assert(AdjVertices_1.size() == 1);
		
		List<String> AdjVertices_2 = graph.getAdjacentVerticesOf("2");
		assert(AdjVertices_2.size() == 0);
	}
}
