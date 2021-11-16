import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PackageManagerTest {

	protected PackageManager pkgMgr;
	
	// JSON Files
	
	// A -> B -> D
	//  \-> C     
	private final String simple = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/simple.json";
	
	// A -> B -> C -> D
	private final String linear = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/linear.json";
	
	// A -> [B, C] -> D
	private final String sharedDependencies = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/shared_dependencies.json";
	
	private final String sharedDependencies2 = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/shared_dependencies_2.json";
	
	//  	 A  
	//	    / \
	//	   B - C
	//	        \
	//	         D
	private final String valid = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/valid.json";
	
	/**
     * @throws java.lang.Exception
     */
	@BeforeEach
	void setUp() throws Exception {
		pkgMgr = new PackageManager();
	}
	
	/**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }
    
    @Test
    void test001_construct_graph_get_vertices_simple() throws FileNotFoundException, IOException, 
    ParseException {
    	pkgMgr.constructGraph(simple);
    	Set<String> vertices = pkgMgr.getAllPackages();
    	assert(vertices.contains("A"));
    	assert(vertices.contains("B"));
    	assert(vertices.contains("C"));
    	assert(vertices.contains("D"));
    }
    
    // Installation Orders return vertices via DFS (pre-order) traversal, so packages theoretically
    // should be installed in reverse order of list
    
    @Test
    void test002_get_installation_order_root_simple() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(simple);
    	List<String> A_dependencies = pkgMgr.getInstallationOrder("A");
    	
    	// D has to be installed first before B. C can be installed anytime as long as its before A
    	assert(A_dependencies.get(0).equals("A"));
    	assert(A_dependencies.get(1).equals("B"));
    	assert(A_dependencies.get(2).equals("D"));
    	assert(A_dependencies.get(3).equals("C"));
    	assert(A_dependencies.size() == 4);
    }
    
    @Test
    void test003_get_installation_order_root_linear() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(linear);
    	List<String> A_dependencies = pkgMgr.getInstallationOrder("A");
    	
    	assert(A_dependencies.get(0).equals("A"));
    	assert(A_dependencies.get(1).equals("B"));
    	assert(A_dependencies.get(2).equals("C"));
    	assert(A_dependencies.get(3).equals("D"));
    	assert(A_dependencies.size() == 4);
    }
    
    @Test
    void test004_get_installation_order_subroot_linear() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(linear);
    	List<String> B_dependencies = pkgMgr.getInstallationOrder("B");
    	
    	assert(B_dependencies.get(0).equals("B"));
    	assert(B_dependencies.get(1).equals("C"));
    	assert(B_dependencies.get(2).equals("D"));
    	assert(B_dependencies.size() == 3);
    }
    
    @Test
    void test005_to_install_B_installed_linear() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(linear);
    	List<String> A_order = pkgMgr.toInstall("A", "B");
    	
    	// If B is installed, then that means D and C must have been installed before
    	
    	assert(A_order.get(0).equals("A"));
    	assert(A_order.size() == 1);
    }
    
    @Test
    void test006_to_install_D_installed_sharedDependencies() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies);
    	List<String> A_order = pkgMgr.toInstall("A", "D");
    	    	
    	assert(A_order.get(0).equals("A"));
    	assert(A_order.get(1).equals("B"));
    	assert(A_order.get(2).equals("C"));
    	assert(A_order.size() == 3);
    }
    
    @Test
    void test007_to_install_B_installed_sharedDependencies() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies);
    	List<String> A_order = pkgMgr.toInstall("A", "B");
    	    	
    	assert(A_order.get(0).equals("A"));
    	assert(A_order.get(1).equals("C"));
    	assert(A_order.size() == 2);
    }
    
    @Test
    void test008_get_installation_order_root_valid() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(valid);
    	List<String> A_dependencies = pkgMgr.getInstallationOrder("A");
    	
    	assert(A_dependencies.get(0).equals("A"));
    	assert(A_dependencies.get(1).equals("B"));
    	assert(A_dependencies.get(2).equals("C"));
    	assert(A_dependencies.get(3).equals("D"));
    	assert(A_dependencies.size() == 4);
    }
    
    @Test
    void test009_get_installation_order_B_sharedDependencies2() throws FileNotFoundException,
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies2);
    	List<String> A_dependencies = pkgMgr.getInstallationOrder("B");
    	
    	assert(A_dependencies.get(0).equals("B"));
    	assert(A_dependencies.get(1).equals("C"));
    	assert(A_dependencies.get(2).equals("D"));
    	assert(A_dependencies.size() == 3);
    }
    
    @Test
    void test010_to_install_C_installed_valid_sharedDependencies2() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies2);
    	List<String> A_order = pkgMgr.toInstall("A", "C");
    	    	
    	assert(A_order.get(0).equals("A"));
    	assert(A_order.get(1).equals("B"));
    	assert(A_order.size() == 2);
    }
}
