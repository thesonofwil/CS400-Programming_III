import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
 * Authors:    Wilson Tjoeng
 * Course:	   CS400.010
 * Due:		   11/19/21
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
        graph = new Graph();
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
            	//System.out.print("Package: " + name + " ");
            	JSONArray dependencies = (JSONArray) pkg.get("dependencies");
            	
            	// Get each dependency and add an edge. addEdge will automatically create a
            	// new vertex if it doesn't exist.
            	// If package A depends on B, then create a directed edge from A to B.
            	for (Object o : dependencies) {
            		String dependency = o.toString();
            		//System.out.print("Dep: " + dependency + " ");
            		graph.addEdge(name, dependency);
            	}
            }
        } catch (FileNotFoundException e) {
        	System.err.println("Could not find file: " + e.getMessage());
        	throw new FileNotFoundException();
        } catch (IOException e) {
        	System.err.println("IO Exception: " + e.getMessage());
        	throw new IOException();
        } catch (ParseException e) {
        	System.err.println("Parse Exception: " + e.getMessage());
        	throw new ParseException(e.getPosition());
        }            
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
    	
    	if (!hasPackage(pkg)) {
    		throw new PackageNotFoundException();
    	}
    	
    	if (hasCycle(pkg)) {
    		throw new CycleException();
    	}
    	
    	List<String> pkgOrder = new ArrayList<String>();
    	List<String> visited = new ArrayList<String>();
    	
    	pkgOrder.add(pkg);
    	getInstallationOrder(pkgOrder, visited, pkg);
    	
    	Collections.reverse(pkgOrder); // Reverse to show packages with least dependencies first
    	
    	return pkgOrder;
    }
    
    /**
     * Recursive depth-first search helper function
     * 
     * @param pkgOrder output list to hold package dependencies 
     * @param visited list to keep track of which vertices have been visited 
     * @param pkg package to visit
     */
    private void getInstallationOrder(List<String> pkgOrder, List<String> visited, String pkg) {
    	visited.add(pkg);
    	List<String> successors = graph.getAdjacentVerticesOf(pkg);
    	
    	for (String s : successors) {
    		if (!visited.contains(s)) {
    			pkgOrder.add(s); 
    			getInstallationOrder(pkgOrder, visited, s);
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
        
    	if (!hasPackage(newPkg) || !hasPackage(installedPkg)) {
    		throw new PackageNotFoundException();
    	}
    	
    	if (hasCycle(newPkg) || hasCycle(installedPkg)) {
    		throw new CycleException();
    	}
    	
    	// First we need to get a list of successors of installedPkg
    	List<String> installedPkgs = getInstallationOrder(installedPkg);
    	
    	// Next we'll get the list of dependencies for newPkg
    	List<String> newPkgInstallOrder = getInstallationOrder(newPkg);
    	
    	// Then Get packages found in the second list that are not in the first list. These will 
    	// be packages that have not been installed yet that need to be. 
    	
    	return getUniqueElementsInLists(installedPkgs, newPkgInstallOrder);
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
        
    	if (graphHasCycle()) {
    		throw new CycleException();
    	}
    	
    	// Topological ordering
    	
    	int num = graph.order(); // Number of vertices
        Stack<String> st = new Stack<String>();
        
        // Keep track of vertices that have or have not been visited
        Set<String> unvisited = graph.getAllVertices();
        List<String> visited = new ArrayList<String>();
        
        List<String> noPredPackages = getPackagesWithNoPredecessors(); // Pkgs w/o predecessors
        
        List<String> installOrder = getEmptyList(num); // Return value
        
        // For each vertex with no predecessor, push to stack
        for (String v : noPredPackages) {
        	st.push(v);
        	unvisited.remove(v);
        }
        
        while (!st.empty()) {
        	String curr = st.peek();
        	List<String> currSuccessors = graph.getAdjacentVerticesOf(curr);
        	
        	// If all successors of curr are visited
        	if (currSuccessors != null && isSubsetOfList(currSuccessors, visited)) { 
        		st.pop();
        		installOrder.set(num - 1, curr); // Assign num to vertex
        		num--;
        	} else { // Add an unvisited successor of curr to stack
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
        
        Collections.reverse(installOrder); // Reverse contents of list
    	return installOrder;
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
        
    	if (graphHasCycle()) {
    		throw new CycleException();
    	}
    	
    	// This may not be the most efficient and elegant solution
    	// Idea: get the installation order list for each package and count the number of
    	// vertices. Keep track of the one with the largest size.
    	Set<String> packages = graph.getAllVertices();
    	int size = 0;
    	String pkgWithMaxDependcies = "";
    	
    	for (String pkg : packages) {
    		try {
				List<String> pkgDependencies = getInstallationOrder(pkg);
				if (pkgDependencies.size() > size) {
					size = pkgDependencies.size();
					pkgWithMaxDependcies = pkg;
				}
			} catch (CycleException e) {
				e.printStackTrace();
			} catch (PackageNotFoundException e) {
				e.printStackTrace();
			}
    	}
    	
    	return pkgWithMaxDependcies;
    }

//    public static void main (String [] args) throws FileNotFoundException, IOException, ParseException {
//        System.out.println("PackageManager.main()");
//        String fileName = args[0]; // File name passed in as command line argument
//        
//        PackageManager packageManager = new PackageManager();
//        packageManager.constructGraph(fileName);
//    }
    
	/////---------------- Private Helper Methods ----------------\\\\\
    
    /**
     * Checks if all elements of one list are present in another list i.e. if a list is a 
     * subset of another 
     * 
     * @param child list whose elements will be examined
     * @param parent list whose elements will be compared to 
     * @return true if child is a subset of parent
     */
    private boolean isSubsetOfList(List<String> child, List<String> parent) {
//    	
//    	if (child == null || parent == null) {
//    		return false;
//    	}
//    	
//    	if (child.isEmpty() || parent.isEmpty()) {
//    		return false;
//    	}
    	
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
    
    /**
     * Compares the elements between two lists, and returns a new list of just the unique
     * elements. In shared_dependencies.json, if list1 = [B, D] and list 2 = [A, B, C, D], then
     * this will return [A, C]. For this function to work, we'll assume the second list contains
     * more elements.
     * 
     * @param list1 smaller list
     * @param list2 larger list 
     * @return list of unique elements
     */
    private List<String> getUniqueElementsInLists(List<String> list1, List<String> list2) {
    	List<String> list3 = new ArrayList<String>(list2); // Copies list 2
    	list3.removeAll(list1);
    	
    	return list3;
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
    				break;
    				// There is a vertex that points to main
    			}
    		}
    		
    		if (hasNoPred) noPredPackages.add(main);
    	}
    	
    	return noPredPackages;
    }
    
    /**
     * Detects if a cycle exists in the graph which should be acyclic. This function explores each
     * vertex one by one to see if there is a cycle 
     * 
     * @return true if the graph contains at least one cycle
     */
    private boolean graphHasCycle() {
    	List<String> unexplored = new ArrayList<String>();
    	
    	for (String s : graph.getAllVertices()) {
    		unexplored.add(s);
    	}
    	
    	for (String s : graph.getAllVertices()) {
    		if (unexplored.contains(s)) {
    			if (hasCycle(unexplored, s)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * Detects if a cycle exists and can be reached from a given vertex
     * 
     * @param pkg the package vertex to start the traversal from
     * @return true if there is a cycle that can be reached from pkg
     */
    private boolean hasCycle(String pkg) {
    	List<String> inProgress = new ArrayList<String>();
    	List<String> explored = new ArrayList<String>();
    	
    	return hasCycleHelper(inProgress, explored, pkg);
    }
    
    /**
     * Detects if a cycle exists and can be reached from a given vertex. Intended to be used 
     * with graphHasCycle()
     * 
     * @param unexplored ADT holding a list of vertices that haven't been processed yet
     * @param pkg current package vertex to start exploration on
     * @return true if there is a cycle that can be reached from pkg
     */
    private boolean hasCycle(List<String> unexplored, String pkg) {
    	List<String> inProgress = new ArrayList<String>();
    	List<String> explored = new ArrayList<String>();
    	
    	return hasCycleHelper(unexplored, inProgress, explored, pkg);
    }
    
    /**
     * Recursive helper function for hasCycle() to detect if a graph has a cycle that can be
     * reached starting from a given package
     * 
     * @param inProgress ADT holding a list of vertices that are currently being explored
     * @param explored ADT holding a list of vertices that have been explored and processed
     * @param pkg current package vertex to start exploration on
     * @return true if there is a cycle that can be reached from pkg
     */
    private boolean hasCycleHelper(List<String> inProgress, List<String> explored, String pkg) {
    	inProgress.add(pkg);
    	List<String> successors = graph.getAdjacentVerticesOf(pkg);
    	
    	for (String s : successors) {
    		if (inProgress.contains(s)) {
    			return true;
    		}
    		
    		if (!explored.contains(s)) {
    			if (hasCycleHelper(inProgress, explored, s)) {
    				return true;
    			}
    		}
    	}
    	
    	explored.add(pkg);
    	inProgress.remove(pkg);
    	return false;
    }
    
    /**
     * Recursive helper function for hasCycle() to detect if a graph has a cycle that can be
     * reached starting from a given package. Intended to be used with graphHasCycle()
     * 
     * @param unexplored ADT holding a list of vertices that haven't been processed yet
     * @param inProgress ADT holding a list of vertices that are currently being explored
     * @param explored ADT holding a list of vertices that have been explored and processed
     * @param pkg current package vertex to start exploration on
     * @return true if there is a cycle that can be reached from pkg
     */
    private boolean hasCycleHelper(List<String> unexplored, List<String> inProgress, 
    		List<String> explored, String pkg) {
    	inProgress.add(pkg);
    	unexplored.remove(pkg);
    	List<String> successors = graph.getAdjacentVerticesOf(pkg);
    	
    	for (String s : successors) {
    		if (inProgress.contains(s)) {
    			return true;
    		}
    		
    		if (!explored.contains(s)) {
    			if (hasCycleHelper(unexplored, inProgress, explored, s)) {
    				return true;
    			}
    		}
    	}
    	
    	explored.add(pkg);
    	inProgress.remove(pkg);
    	return false;
    }
    
    /**
     * Creates and initializes list with an empty string. ArrayList(capacity) does also create a
     * new list with the given size, but all values are set to null. 
     * 
     * @param capacity the number of elements the list should have
     * @return an initialized list with the given capacity
     */
    private List<String> getEmptyList(int capacity) {
    	List<String> list = new ArrayList<String>();
    	
    	for (int i = 0; i < capacity; i++) {
    		list.add("");
    	}
    	
    	return list;
    }
}