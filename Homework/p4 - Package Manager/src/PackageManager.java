import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Filename:   PackageManager.java
 * Project:    p4
 * Authors:    
 * 
 * PackageManager is used to process json package dependency files
 * and provide function that make that information available to other users.
 * 
 * Each package that depends upon other packages has its own
 * entry in the json file.  
 * 
 * Package dependencies are important when building software, 
 * as you must install packages in an order such that each package 
 * is installed after all of the packages that it depends on 
 * have been installed.
 * 
 * For example: package A depends upon package B,
 * then package B must be installed before package A.
 * 
 * This program will read package information and 
 * provide information about the packages that must be 
 * installed before any given package can be installed.
 * all of the packages in
 * 
 * You may add a main method, but we will test all methods with
 * our own Test classes.
 */

public class PackageManager {
    
    private Graph graph;
    
    /*
     * Package Manager default no-argument constructor.
     */
    public PackageManager() {
        
    }
    
    /**
     * Takes in a file path for a json file and builds the package dependency graph from it. 
     * 
     * @param jsonFilepath the name of json data file with package dependency information
     * @throws FileNotFoundException if file path is incorrect
     * @throws IOException if the give file cannot be read
     * @throws ParseException if the given json cannot be parsed 
     */
    public void constructGraph(String jsonFilepath) throws FileNotFoundException, IOException, ParseException {
        try {
        	Object obj = new JSONParser().parse(new FileReader(jsonFilepath));
            JSONObject jo = (JSONObject) obj;
            JSONArray packages = (JSONArray) jo.get("packages");
            
            for (int i = 0; i < packages.size(); i++) {
            	JSONObject pkg = (JSONObject) packages.get(i);
            	String name = (String) pkg.get("name");
            	graph.addVertex(name);
            	JSONArray dependencies = (JSONArray) pkg.get("dependencies");
            	
            	// Get each dependency and add an edge. addEdge will automatically create a
            	// new vertex if it doesn't exist.
            	// If package A depends on B, then create a directed edge from B to A.
            	for (Object o : dependencies) {
            		String dependency = o.toString();
            		graph.addEdge(dependency, name);
            	}
            }
        } catch (FileNotFoundException e) {
        	System.err.println("Could not find file: " + e.getMessage());
        } catch (IOException e) {
        	System.err.println(e.getMessage());
        } catch (ParseException e) {
        	System.err.println(e.getMessage());
    }
                    
        // TODO add vertices and edges to graph
    }
    
    /**
     * Helper method to get all packages in the graph.
     * 
     * @return Set<String> of all the packages
     */
    public Set<String> getAllPackages() {
        return graph.getAllVertices();
    }
    
    /**
     * Given a package name, returns a list of packages in a
     * valid installation order.  
     * 
     * Valid installation order means that each package is listed 
     * before any packages that depend upon that package.
     * 
     * @return List<String>, order in which the packages have to be installed
     * 
     * @throws CycleException if you encounter a cycle in the graph while finding
     * the installation order for a particular package. Tip: Cycles in some other
     * part of the graph that do not affect the installation order for the 
     * specified package, should not throw this exception.
     * 
     * @throws PackageNotFoundException if the package passed does not exist in the 
     * dependency graph.
     */
    public List<String> getInstallationOrder(String pkg) throws CycleException, PackageNotFoundException {
    	// TODO CycleException
    	
    	if (!hasPackage(pkg)) {
    		throw new PackageNotFoundException();
    	}
    	
    	List<String> pkgOrder = new ArrayList<String>();
    	List<String> visited = new ArrayList<String>();
    	
    	getInstallationOrder(pkgOrder, visited, pkg);
    	
    	// DFS returns list in sequential order. So we need to get the reverse
    	for (int i = visited.size() - 1; i > 0; i--) {
    		pkgOrder.add(visited.get(i));
    	}
    	
    	return pkgOrder;
    }
    
    private void getInstallationOrder(List<String> pkgOrder, List<String> visited, String pkg) {
    	visited.add(pkg);
    	List<String> successors = graph.getAdjacentVerticesOf(pkg);
    	
    	for (String s : successors) {
    		if (!visited.contains(s)) {
    			getInstallationOrder(pkgOrder, visited, s);
    			//pkgOrder.add(s); // Adds pkg in reverse order
    		}
    	}
    }
    
    /**
     * Given two packages - one to be installed and the other installed, 
     * return a List of the packages that need to be newly installed. 
     * 
     * For example, refer to shared_dependecies.json - toInstall("A","B") 
     * If package A needs to be installed and packageB is already installed, 
     * return the list ["A", "C"] since D will have been installed when 
     * B was previously installed.
     * 
     * @return List<String>, packages that need to be newly installed.
     * 
     * @throws CycleException if you encounter a cycle in the graph while finding
     * the dependencies of the given packages. If there is a cycle in some other
     * part of the graph that doesn't affect the parsing of these dependencies, 
     * cycle exception should not be thrown.
     * 
     * @throws PackageNotFoundException if any of the packages passed 
     * do not exist in the dependency graph.
     */
    public List<String> toInstall(String newPkg, String installedPkg) throws CycleException, PackageNotFoundException {
        return null;
    }
    
    /**
     * Return a valid global installation order of all the packages in the 
     * dependency graph.
     * 
     * assumes: no package has been installed and you are required to install 
     * all the packages
     * 
     * returns a valid installation order that will not violate any dependencies
     * 
     * @return List<String>, order in which all the packages have to be installed
     * @throws CycleException if you encounter a cycle in the graph
     */
    public List<String> getInstallationOrderForAllPackages() throws CycleException {
        
    	// Topological ordering
    	
    	int num = graph.order(); // Number of vertices
        Stack<String> st = new Stack<String>();
        
        // Keep track of vertices that have or have not been visited
        Set<String> unvisited = graph.getAllVertices();
        List<String> visited = new ArrayList<String>();
        
        List<String> noPredPackages = getPackagesWithNoPredecessors();
        
        List<String> installOrder = new ArrayList<String>(); // Return value
        
        // For each vertex with no predecessor, push to stack
        for (String v : noPredPackages) {
        	st.push(v);
        	unvisited.remove(v);
        }
        
        while (!st.empty()) {
        	String curr = st.peek();
        	
        	// If all successors of curr are visited
        	if (isSubsetOfList(graph.getAdjacentVerticesOf(curr), visited)) { 
        		st.pop();
        		installOrder.add(num - 1, curr); // Assign num to vertex
        		num--;
        	} else { // Add an unvisited successor of curr to stack
        		List<String> currSuccessors = graph.getAdjacentVerticesOf(curr);
        		for (String s : currSuccessors) {
        			if (!visited.contains(s)) {
        				visited.add(s);
        				unvisited.remove(s);
        				st.push(s);
        				break;
        			}
        		}
        	}
        }
        
    	return installOrder;
    }
    
    /**
     * Finds and creates a list of all vertices without any predecessors. In a DAG, these 
     * will essentially be root nodes. 
     * 
     * @return a list of packages with no predecessors
     */
    private List<String> getPackagesWithNoPredecessors() {
    	Set<String> packages = graph.getAllVertices();
    	List<String> noPredPackages = new ArrayList<String>();
    	//List<String> successors = new ArrayList<String>();; 
    	
    	// If a vertex is a successor then by definition it has a predecessor. Hashtable used for
    	// faster lookup
    	Hashtable<String, Integer> successors = new Hashtable<String, Integer>();
    	
    	// Idea: loop through all packages. For each package, loop through the other packages
    	// and check if their adjacent neighbors list contains the current package. If none do,
    	// add to list. Challenge is making this as efficient as possible
    	for (String main : packages) {
    		boolean hasNoPred = true;
    		for (String pkg : packages) {
    			if (pkg.equals(main) || successors.contains(pkg)) continue; //graph.getAdjacentVerticesOf(main).contains(pkg)) continue;
    			
    			// Add adjacent neighbors to successors list. These will be skipped next iteration
    			List<String> pkgNeighbors = graph.getAdjacentVerticesOf(pkg);
    			for (String pkgNeighbor : pkgNeighbors) {
    				successors.put(pkgNeighbor, 1);
    			}
    			
    			if (pkgNeighbors.contains(main)) {
    				hasNoPred = false;
    				successors.put(main, 1);
    				break; // There is a vertex that points to main
    			}
    		}
    		
    		if (hasNoPred) noPredPackages.add(main);
    	}
    	
    	return noPredPackages;
    }
    
    /**
     * Find and return the name of the package with the maximum number of dependencies.
     * 
     * Tip: it's not just the number of dependencies given in the json file.  
     * The number of dependencies includes the dependencies of its dependencies.  
     * But, if a package is listed in multiple places, it is only counted once.
     * 
     * Example: if A depends on B and C, and B depends on C, and C depends on D.  
     * Then,  A has 3 dependencies - B,C and D.
     * 
     * @return String, name of the package with most dependencies.
     * @throws CycleException if you encounter a cycle in the graph
     */
    public String getPackageWithMaxDependencies() throws CycleException {
        return "";
    }

    public static void main (String [] args) {
        System.out.println("PackageManager.main()");
    }
    
    private boolean isSubsetOfList(List<String> child, List<String> parent) {
    	boolean isSubset = true;
    	
    	for (String s : child) {
    		if (!parent.contains(s)) {
    			isSubset = false;
    			break;
    		}
    	}
    	
    	return isSubset;
    }
    
    /**
     * Checks if dependency graph has a specified package 
     * 
     * @param pkg Package to look for in graph
     * @return true if graph contains the package; false otherwise
     */
    private boolean hasPackage(String pkg) {
    	Set<String> packages = graph.getAllVertices();
    	return packages.contains(pkg);
    }
    
}
