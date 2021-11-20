import static org.junit.Assert.fail;

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
	
	//	 	 A  
	//	    / \
	//	   B - C
	//	        \
	//	         D
	private final String sharedDependencies2 = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/shared_dependencies_2.json";
	
	// [A, E] -> B -> [C, D]
	private final String valid = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/valid.json";
	
	// A -> B
	// C -> D
	private final String separate = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/separate.json";
	
	// A <-> B
	private final String cyclic = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/cyclic.json";
	
	// // A -> B <-> D
	//  \-> C
	private final String graphHasACycle = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/graphHasACycle.json";
	
	// A -> [B, C]
	// B -> C
	// D -> [E, F]
	private final String challenge1 = "C://Users/Wilson Tjoeng/Documents/School/UW CS400/"
			+ "CS400-Programming_III/CS400-Programming_III/Homework/"
			+ "p4 - Package Manager/challenge1.json";
	
	
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
    	assert(A_dependencies.get(3).equals("A"));
    	assert(A_dependencies.get(2).equals("C"));
    	assert(A_dependencies.get(1).equals("B"));
    	assert(A_dependencies.get(0).equals("D"));
    	assert(A_dependencies.size() == 4);
    }
    
    @Test
    void test003_get_installation_order_root_linear() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(linear);
    	List<String> A_dependencies = pkgMgr.getInstallationOrder("A");
    	
    	assert(A_dependencies.get(3).equals("A"));
    	assert(A_dependencies.get(2).equals("B"));
    	assert(A_dependencies.get(1).equals("C"));
    	assert(A_dependencies.get(0).equals("D"));
    	assert(A_dependencies.size() == 4);
    }
    
    @Test
    void test004_get_installation_order_subroot_linear() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(linear);
    	List<String> B_dependencies = pkgMgr.getInstallationOrder("B");
    	
    	assert(B_dependencies.get(2).equals("B"));
    	assert(B_dependencies.get(1).equals("C"));
    	assert(B_dependencies.get(0).equals("D"));
    	assert(B_dependencies.size() == 3);
    }
    
    @Test
    void test005_get_installation_order_B_valid() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(valid);
    	List<String> B_dependencies = pkgMgr.getInstallationOrder("B");
    	
    	assert(B_dependencies.get(2).equals("B"));
    	assert(B_dependencies.get(1).equals("D"));
    	assert(B_dependencies.get(0).equals("C"));
    	assert(B_dependencies.size() == 3);
    }
    
    @Test
    void test006_to_install_B_installed_linear() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(linear);
    	List<String> A_order = pkgMgr.toInstall("A", "B");
    	
    	// If B is installed, then that means D and C must have been installed before
    	
    	assert(A_order.get(0).equals("A"));
    	assert(A_order.size() == 1);
    }
    
    @Test
    void test007_to_install_D_installed_sharedDependencies() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies);
    	List<String> A_order = pkgMgr.toInstall("A", "D");
    	    	
    	assert(A_order.get(2).equals("A"));
    	assert(A_order.get(1).equals("C"));
    	assert(A_order.get(0).equals("B"));
    	assert(A_order.size() == 3);
    }
    
    @Test
    void test008_to_install_B_installed_sharedDependencies() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies);
    	List<String> A_order = pkgMgr.toInstall("A", "B");
    	    	
    	assert(A_order.get(1).equals("A"));
    	assert(A_order.get(0).equals("C"));
    	assert(A_order.size() == 2);
    }
    
    @Test
    void test009_get_installation_order_root_valid() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(valid);
    	List<String> A_dependencies = pkgMgr.getInstallationOrder("A");
    	
    	assert(A_dependencies.get(3).equals("A"));
    	assert(A_dependencies.get(2).equals("B"));
    	assert(A_dependencies.get(1).equals("D"));
    	assert(A_dependencies.get(0).equals("C"));
    	assert(A_dependencies.size() == 4);
    }
    
    @Test
    void test010_get_installation_order_B_sharedDependencies2() throws FileNotFoundException,
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies2);
    	List<String> A_dependencies = pkgMgr.getInstallationOrder("B");
    	
    	assert(A_dependencies.get(2).equals("B"));
    	assert(A_dependencies.get(1).equals("C"));
    	assert(A_dependencies.get(0).equals("D"));
    	assert(A_dependencies.size() == 3);
    }
    
    @Test
    void test011_to_install_C_installed_valid_sharedDependencies2() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies2);
    	List<String> A_order = pkgMgr.toInstall("A", "C");
    	    	
    	assert(A_order.get(1).equals("A"));
    	assert(A_order.get(0).equals("B"));
    	assert(A_order.size() == 2);
    }
    
    @Test
    void test012_get_installation_order_all_linear() throws FileNotFoundException, IOException,
    ParseException, CycleException {
    	pkgMgr.constructGraph(linear);
    	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
    	
    	assert(order.get(3).equals("A"));
    	assert(order.get(2).equals("B"));
    	assert(order.get(1).equals("C"));
    	assert(order.get(0).equals("D"));
    	assert(order.size() == 4);
    }
    
    @Test
    void test013_get_installation_order_all_simple() throws FileNotFoundException, IOException,
    ParseException, CycleException {
    	pkgMgr.constructGraph(simple);
    	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
    	
    	assert(order.get(3).equals("A"));
    	assert(order.get(2).equals("C"));
    	assert(order.get(1).equals("B"));
    	assert(order.get(0).equals("D"));
    	assert(order.size() == 4);	
    }
    
    @Test
    void test014_get_installation_order_all_sharedDependencies() throws FileNotFoundException, 
    IOException, ParseException, CycleException {
    	pkgMgr.constructGraph(sharedDependencies);
    	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
    	
    	assert(order.get(3).equals("A"));
    	assert(order.get(2).equals("C"));
    	assert(order.get(1).equals("B"));
    	assert(order.get(0).equals("D"));
    	assert(order.size() == 4);	
    }
    
    @Test
    void test015_get_installation_order_all_sharedDependencies2() throws FileNotFoundException,
    IOException, ParseException, CycleException {
    	pkgMgr.constructGraph(sharedDependencies2);
    	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
    	
    	assert(order.get(3).equals("A"));
    	assert(order.get(2).equals("B"));
    	assert(order.get(1).equals("C"));
    	assert(order.get(0).equals("D"));
    	assert(order.size() == 4);	
    }
    
    @Test
    void test016_get_installation_order_all_valid() throws FileNotFoundException, IOException, 
    ParseException, CycleException {
    	pkgMgr.constructGraph(valid);
    	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
    	// A and E have no predecessors
    	
    	assert(order.get(4).equals("E"));
    	assert(order.get(3).equals("A"));
    	assert(order.get(2).equals("B"));
    	assert(order.get(1).equals("D"));
    	assert(order.get(0).equals("C"));
    	assert(order.size() == 5);	
    }
    
    @Test
    void test017_max_dependencies_linear() throws FileNotFoundException, IOException, 
    ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(linear);
    	String maxPkg = pkgMgr.getPackageWithMaxDependencies();
    	
    	assert (maxPkg.equals("A"));
    }
    
    @Test
    void test018_max_dependencies_simple() throws FileNotFoundException, IOException, 
    ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(simple);
    	String maxPkg = pkgMgr.getPackageWithMaxDependencies();
    	
    	assert (maxPkg.equals("A"));
    }
    
    @Test
    void test019_max_dependencies_sharedDependencies() throws FileNotFoundException, IOException, 
    ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies);
    	String maxPkg = pkgMgr.getPackageWithMaxDependencies();
    	
    	assert (maxPkg.equals("A"));
    }
    
    @Test
    void test020_max_dependencies_sharedDependencies2() throws FileNotFoundException, IOException, 
    ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(sharedDependencies2);
    	String maxPkg = pkgMgr.getPackageWithMaxDependencies();
    	
    	assert (maxPkg.equals("A"));
    }
    
    @Test
    void test021_max_dependencies_valid() throws FileNotFoundException, IOException, 
    ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(valid);
    	String maxPkg = pkgMgr.getPackageWithMaxDependencies();
    	
    	assert (maxPkg.equals("A"));
    	// E also is valid as it has the same number of dependencies. But the function is written
    	// so that the unless the # dependencies of the next package > the current, the current
    	// won't be overwritten
    }
    
    @Test
    void test022_get_installation_order_A_separate() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(separate);
    	List<String> A_dependencies = pkgMgr.getInstallationOrder("A");
    	
    	assert(A_dependencies.get(1).equals("A"));
    	assert(A_dependencies.get(0).equals("B"));
    	assert(A_dependencies.size() == 2);
    }
    
    @Test
    void test023_get_installation_order_C_separate() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(separate);
    	List<String> C_dependencies = pkgMgr.getInstallationOrder("C");
    	
    	assert(C_dependencies.get(1).equals("C"));
    	assert(C_dependencies.get(0).equals("D"));
    	assert(C_dependencies.size() == 2);
    }
    
    @Test
    void test024_to_install_A_C_separate() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(separate);
    	List<String> A_order = pkgMgr.toInstall("A", "C");
    	
    	assert(A_order.get(1).equals("A"));
    	assert(A_order.get(0).equals("B"));
    	assert(A_order.size() == 2);
    }
    
    @Test
    void test025_to_install_A_B_separate() throws FileNotFoundException, 
    IOException, ParseException, CycleException, PackageNotFoundException {
    	pkgMgr.constructGraph(separate);
    	List<String> A_order = pkgMgr.toInstall("A", "B");
    	
    	assert(A_order.get(0).equals("A"));
    	assert(A_order.size() == 1);
    }
    
    @Test
    void test026_getInstallationOrder_throws_CycleException_simpleCycle() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(cyclic);
    	
    	try {
        	List<String> A_dependencies = pkgMgr.getInstallationOrder("A");
        	fail("CycleException not thrown");
    	} catch (CycleException e) {
    	} catch (Exception e) {
    		fail("CycleException not thrown");
    	}
    }
    
    @Test
    void test027_getInstallationOrder_throws_CycleException_graphHasACycle() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(graphHasACycle);
    	
    	try {
        	List<String> A_dependencies = pkgMgr.getInstallationOrder("A");
        	fail("CycleException not thrown");
    	} catch (CycleException e) {
    	} catch (Exception e) {
    		fail("CycleException not thrown");
    	}
    }
    
    @Test
    void test028_getInstallationOrder_does_not_throw_CycleException_graphHasACycle() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(graphHasACycle);
    	
    	// C is independent of everything else 
    	try {
        	List<String> C_dependencies = pkgMgr.getInstallationOrder("C");
    	} catch (CycleException e) {
    		fail("CycleException was thrown");
    	} catch (Exception e) {
    		fail("Exception was thrown");
    	}
    }
    
    @Test
    void test029_toInstall_throws_CycleException_cyclic() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(cyclic);
    	
    	try {
        	List<String> A_dependencies = pkgMgr.toInstall("A","B");
        	fail("CycleException not thrown");
    	} catch (CycleException e) {
    	} catch (Exception e) {
    		fail("CycleException not thrown");
    	}
    }
    
    @Test
    void test030_toInstall_throws_CycleException_graphHasACycle() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(graphHasACycle);
    	
    	try {
        	List<String> A_dependencies = pkgMgr.toInstall("A","D");
        	fail("CycleException not thrown");
    	} catch (CycleException e) {
    	} catch (Exception e) {
    		fail("CycleException not thrown");
    	}
    }
    
    @Test
    void test031_install_all_throws_CycleException_cyclic() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(cyclic);
    	
    	try {
        	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
        	fail("CycleException not thrown");
    	} catch (CycleException e) {
    	} catch (Exception e) {
    		fail("CycleException not thrown");
    	}
    }
    
    @Test
    void test032_install_all_throws_CycleException_graphHasACycle() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(graphHasACycle);
    	
    	try {
        	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
        	fail("CycleException not thrown");
    	} catch (CycleException e) {
    	} catch (Exception e) {
    		fail("CycleException not thrown");
    	}
    }
    
    @Test
    void test033_get_max_dependencies_CycleException_cyclic() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(cyclic);
    	
    	try {
        	String pkg = pkgMgr.getPackageWithMaxDependencies();
        	fail("CycleException not thrown");
    	} catch (CycleException e) {
    	} catch (Exception e) {
    		fail("CycleException not thrown");
    	}
    }
    
    @Test
    void test034_get_max_dependencies_CycleException_graphHasACycle() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(graphHasACycle);
    	
    	try {
        	String pkg = pkgMgr.getPackageWithMaxDependencies();
        	fail("CycleException not thrown");
    	} catch (CycleException e) {
    	} catch (Exception e) {
    		fail("CycleException not thrown");
    	}
    }
    
    @Test
    void test035_getInstallationOrder_throws_PackageNotFoundException() throws 
    FileNotFoundException, IOException, ParseException {
    	pkgMgr.constructGraph(simple);
    	
    	try {
        	List<String> order = pkgMgr.getInstallationOrder("Z");
        	fail("PackageNotFoundException not thrown");
    	} catch (PackageNotFoundException e) {
    	} catch (Exception e) {
    		fail("PackageNotFoundException not thrown");
    	}
    }
    
    @Test
    void test036_toInstall_throws_PackageNotFoundException_first_param() throws 
    FileNotFoundException, IOException, ParseException {
    	pkgMgr.constructGraph(simple);
    	
    	try {
        	List<String> order = pkgMgr.toInstall("Z","A");
        	fail("PackageNotFoundException not thrown");
    	} catch (PackageNotFoundException e) {
    	} catch (Exception e) {
    		fail("PackageNotFoundException not thrown");
    	}
    }
    
    @Test
    void test037_toInstall_throws_PackageNotFoundException_second_param() throws 
    FileNotFoundException, IOException, ParseException {
    	pkgMgr.constructGraph(simple);
    	
    	try {
        	List<String> order = pkgMgr.toInstall("A","Z");
        	fail("PackageNotFoundException not thrown");
    	} catch (PackageNotFoundException e) {
    	} catch (Exception e) {
    		fail("PackageNotFoundException not thrown");
    	}
    }
    
    @Test
    void test038_install_all_does_not_throw_CycleException_challenge1() throws 
    FileNotFoundException, IOException, ParseException, PackageNotFoundException {
    	pkgMgr.constructGraph(challenge1);
    	
    	try {
        	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
    	} catch (CycleException e) {
    		fail("CycleException was thrown");
    	} catch (Exception e) {
    		fail("Exception was thrown");
    	}
    }
    
    @Test
    void test039_get_installation_order_all_challenge1() throws FileNotFoundException, IOException, 
    ParseException, CycleException {
    	pkgMgr.constructGraph(challenge1);
    	List<String> order = pkgMgr.getInstallationOrderForAllPackages();
    	
    	assert(order.get(5).equals("D"));
    	assert(order.get(4).equals("F"));
    	assert(order.get(3).equals("E"));
    	assert(order.get(2).equals("A"));
    	assert(order.get(1).equals("B"));
    	assert(order.get(0).equals("C"));
    	assert(order.size() == 6);	
    }
}