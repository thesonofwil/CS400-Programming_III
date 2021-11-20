import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            	JSONArray dependencies = (JSONArray) pkg.get("dependencies");
            	
            	// Get each dependency and add an edge. addEdge will automatically create a
            	// new vertex if it doesn't exist.
            	// If package A depends on B, then create a directed edge from A to B.
            	for (Object o : dependencies) {
            		String dependency = o.toString();
            		graph.addEdge(name, dependency);
            	}
            }
        } catch (FileNotFoundException e) {
        	//System.err.println("Could not find file: " + e.getMessage());
        	throw new FileNotFoundException();
        } catch (IOException e) {
        	//System.err.println("IO Exception: " + e.getMessage());
        	throw new IOException();
        } catch (ParseException e) {
        	//System.err.println("Parse Exception: " + e.getMessage());
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
    	
    	List<String> pkgOrder = new ArrayList<String>(); // Order to return
    	List<String> visited = new ArrayList<String>(); // Keep track of what's been called on
    	List<String> inProgress = new ArrayList<String>(); // Vertex currently examining
    	
    	getInstallationOrder(pkgOrder, visited, inProgress, pkg);
    	    	
    	return pkgOrder;
    }
    
    /**
     * Recursive depth-first search helper function
     * 
     * @param pkgOrder output list to hold package dependencies 
     * @param visited list to keep track of which vertices have been visited 
     * @param pkg package to visit
     * @throws CycleException if a cycle is detected while traversing
     */
    private void getInstallationOrder(List<String> pkgOrder, List<String> visited, 
    		List<String> inProgress, String pkg) throws CycleException {
    	
    	// This conditional helps for topological ordering
    	if (visited.contains(pkg)) {
    		return;
    	}
    	
    	// Recursive call iterates on successors. If a successor was already being examined,
    	// then we've encountered a cycle
    	if (inProgress.contains(pkg)) {
    		throw new CycleException();
    	}
    	
    	inProgress.add(pkg);
    	
    	List<String> successors = graph.getAdjacentVerticesOf(pkg);
    	
    	// Recursive call on successor
    	for (String s : successors) {
    		if (!visited.contains(s)) {
    			getInstallationOrder(pkgOrder, visited, inProgress, s);
    		}
    	}
    	
    	pkgOrder.add(pkg);
    	visited.add(pkg);
    	inProgress.remove(pkg);
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
    	
    	// First we need to get a list of successors of installedPkg
    	List<String> installedPkgs = getInstallationOrder(installedPkg);
    	
    	// Next we'll get the list of dependencies for newPkg
    	List<String> newPkgInstallOrder = getInstallationOrder(newPkg);
    	
    	// Then remove packages from the second list that are in the first list and therefore have
    	// have been installed. The remaining will be packages that have not been installed yet 
    	// that need to be. 
    	for (String s : installedPkgs) {
    		if (newPkgInstallOrder.contains(s)) {
    			newPkgInstallOrder.remove(s);
    		}
    	}
    	
    	return newPkgInstallOrder;
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
        
    	// Topological ordering idea: do the same DFS as before, but for all vertices
    	List<String> pkgOrder = new ArrayList<String>();
    	List<String> visited = new ArrayList<String>();
    	List<String> inProgress = new ArrayList<String>();
    	
    	Set<String> packages = graph.getAllVertices();
    	
    	for (String pkg : packages) {
    		getInstallationOrder(pkgOrder, visited, inProgress, pkg);
    	}
    	
    	return pkgOrder;
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
    	
    	// This may not be the most efficient and elegant solution
    	// Idea: get the installation order list for each package and count the number of
    	// vertices. Keep track of the one with the largest size.
    	Set<String> packages = graph.getAllVertices();
    	int size = 0;
    	String pkgWithMaxDependencies = "";
    	
    	for (String pkg : packages) {
    		try {
				List<String> pkgDependencies = getInstallationOrder(pkg);
				if (pkgDependencies.size() > size) {
					size = pkgDependencies.size();
					pkgWithMaxDependencies = pkg;
				}
			} catch (CycleException e) {
				throw new CycleException();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
    	}
    	
    	return pkgWithMaxDependencies;
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