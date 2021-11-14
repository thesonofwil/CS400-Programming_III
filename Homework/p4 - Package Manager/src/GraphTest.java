import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	
	@Test
	void test007_add_edge_vertex_is_null() {
		graph.addEdge(null, "2");
		assert(graph.order() == 0); // Num Vertices
		assert(graph.size() == 0); // Num Edges
	}
	
	@Test
	void test008_add_duplicate_edge() {
		graph.addEdge("1", "2");
		graph.addEdge("1", "2");
		assert(graph.order() == 2); // Num Vertices
		assert(graph.size() == 1); // Num Edges
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		assert(AdjVertices_1.contains("2"));
		assert(AdjVertices_1.size() == 1);
		
		List<String> AdjVertices_2 = graph.getAdjacentVerticesOf("2");
		assert(AdjVertices_2.size() == 0);
	}
	
	@Test
	void test009_add_multiple_edges() {
		graph.addEdge("1", "2");
		graph.addEdge("2", "1");
		graph.addEdge("2", "3");
		graph.addEdge("4", "5");
		
		assert(graph.order() == 5); // Num Vertices
		assert(graph.size() == 4); // Num Edges
		
		List<String> AdjVertices_2 = graph.getAdjacentVerticesOf("2");
		assert(AdjVertices_2.contains("1"));
		assert(AdjVertices_2.contains("3"));
	}
	
	@Test
	void test010_remove_vertex_simple() {
		graph.addVertex("1");
		graph.removeVertex("1");
		
		assert(graph.order() == 0); // Num Vertices
		
		Set<String> vertices = graph.getAllVertices();
		assert(!vertices.contains("1"));
	}
	
	@Test
	void test011_remove_vertices() {
		graph.addVertex("1");
		graph.addVertex("2");
		graph.addVertex("3");
		
		graph.removeVertex("1");
		graph.removeVertex("2");
		graph.removeVertex("3");
		
		assert(graph.order() == 0); // Num Vertices
		Set<String> vertices = graph.getAllVertices();
		assert(!vertices.contains("1"));
		assert(!vertices.contains("2"));
		assert(!vertices.contains("3"));
	}
	
	@Test
	void test012_remove_null_vertex() {
		graph.removeVertex(null);
		assert(graph.order() == 0); // Num Vertices
		graph.addVertex("1");
		graph.removeVertex(null);
		assert(graph.order() == 1);
	}
	
	@Test
	void test013_remove_vertex_not_found() {
		graph.removeVertex("1");
		assert(graph.order() == 0);
		
		graph.addVertex("2");
		graph.removeVertex("1");
		assert(graph.order() == 1);
	}
	
	@Test
	void test014_remove_edge_simple() {
		graph.addEdge("1", "2");
		graph.removeEdge("1", "2");
		
		assert(graph.order() == 2);
		assert(graph.size() == 0);
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		assert(!AdjVertices_1.contains("2"));
	}
	
	@Test
	void test015_remove_edge_other_direction() {
		graph.addEdge("1", "2");
		graph.removeEdge("2", "1");
		
		assert(graph.order() == 2);
		assert(graph.size() == 1);
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		assert(AdjVertices_1.contains("2"));
	}
	
	@Test
	void test016_remove_edge_null_vertex() {
		graph.addEdge("1", "2");
		graph.removeEdge("1", null);
		
		assert(graph.order() == 2);
		assert(graph.size() == 1);
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		assert(AdjVertices_1.contains("2"));
	}
	
	@Test
	void test017_remove_edge_vertex_not_found() {
		graph.addEdge("1", "2");
		graph.removeEdge("1", "3");
		
		assert(graph.order() == 2);
		assert(graph.size() == 1);
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		assert(AdjVertices_1.contains("2"));
	}
	
	@Test
	void test018_remove_edges() {
		graph.addEdge("1", "2");
		graph.addEdge("3", "4");
		graph.addEdge("1", "3");
		
		graph.removeEdge("1", "2");
		graph.removeEdge("3", "4");
		graph.removeEdge("1", "3");
		graph.removeEdge("2", "3");
		
		assert(graph.order() == 4);
		assert(graph.size() == 0);
	}
	
	@Test
	void test019_remove_vertex_removes_edges() {
		graph.addEdge("1", "2");
		graph.addEdge("1", "3");
		graph.addEdge("4", "1");
		graph.removeVertex("1");
		
		assert(graph.order() == 3);
		assert(graph.size() == 0);
		
		List<String> AdjVertices_2 = graph.getAdjacentVerticesOf("2");
		List<String> AdjVertices_3 = graph.getAdjacentVerticesOf("3");
		List<String> AdjVertices_4 = graph.getAdjacentVerticesOf("4");

		assert(AdjVertices_2.isEmpty());
		assert(AdjVertices_3.isEmpty());
		assert(AdjVertices_4.isEmpty());
	}
	
	@Test
	void test020_get_adjacencies_does_not_get_indirect_adjacencies() {
		graph.addEdge("1", "2");
		graph.addEdge("2", "3");
		graph.addEdge("3", "4");
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		
		assert(AdjVertices_1.size() == 1);
		assert(AdjVertices_1.contains("2"));
		assert(!AdjVertices_1.contains("3"));
		assert(!AdjVertices_1.contains("4"));	
	}
	
	@Test
	void test021_add_remove_self_directed_edge() {
		graph.addEdge("1", "1");
		
		assert(graph.order() == 1);
		assert(graph.size() == 1);
		
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		assert(AdjVertices_1.contains("1"));
		
		graph.removeEdge("1", "1");
		
		assert(graph.order() == 1);
		assert(graph.size() == 0);
		
		List<String> AdjVertices_1_updated = graph.getAdjacentVerticesOf("1");
		assert(AdjVertices_1_updated.isEmpty());
	}
	
	void test022_add_many_edges_remove_vertex() {
		for (int i = 1; i <= 100; i++) {
			graph.addEdge("1", String.valueOf(i));
		}
		
		assert(graph.order() == 100);
		assert(graph.size() == 99);
		
		Set<String> vertices = graph.getAllVertices();
		List<String> AdjVertices_1 = graph.getAdjacentVerticesOf("1");
		
		for (int i = 1; i <= 100; i++) {
			assert(vertices.contains(String.valueOf(i)));
			assert(AdjVertices_1.contains(String.valueOf(i)));
		}
		
		graph.removeVertex("1");
		
		assert(graph.order() == 99);
		assert(graph.size() == 0);
	}
}
