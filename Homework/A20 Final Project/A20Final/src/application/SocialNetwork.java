/**
 * 
 */
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stores the data structure for a SocialNetwork, used in the Main class
 * parses an input file and adds and removes friends/friendships
 * Stores a central user to explore the network from the perspective of
 * 
 * @author Wilson Tjoeng, Tommy Hefferan, Dannielle Hendon
 */
public class SocialNetwork {
	
	private SocialGraph graph;
	private Person centralUser;
	private final Queue<String> log;
	private final Queue<String> errorLog;
	private final String logFilePath; // Output file
	
	/**
	 * default no-arg constructor
	*/
	public SocialNetwork() {
		graph = new SocialGraph();
		log = new LinkedList<String>();
		errorLog = new LinkedList<String>();
		this.logFilePath = "log.txt";
	}
	
	/**
	 * Builds the network from an import file
	 * Also has the ability to append actions from file to existing network
	 * @param fileName the file to build the graph from
	 * @return true if the file was successfully parsed, false is the file was not found
	 */
	public boolean buildGraph(String fileName) {	
		File inputFile = new File(fileName);
		return parseFile(inputFile);
	} // buildGraph
	
	/**
	 * Parses the input file and checks to see if the username and the 
	 * action are valid. If valid, do the corresponding action to the valid
	 * username
	 * @param inputFile the file to be parsed
	 */
	private boolean parseFile(File inputFile) {
		try {
			int lineCount = 0;
			Scanner inputScanner = new Scanner(inputFile);
			while (inputScanner.hasNextLine()) {
				String inputLine = inputScanner.nextLine();
				lineCount++;
				String[] inputLineArray = inputLine.split(" ");
				
				if (inputLineArray.length == 2) {
					String username = inputLineArray[1];
					if (inputLineArray[0].equals("a")) {
						addFriend(username);
					} else if (inputLineArray[0].equals("r")) {
						removeFriend(username);
					} else if (inputLineArray[0].equals("s")) {
						setCentralUser(username);
					} else {
						logError("Invalid action on line " + lineCount + " of file: " + inputFile);
					}
				}
				else if (inputLineArray.length == 3) {
					String username1 = inputLineArray[1];
					String username2 = inputLineArray[2];
					if (inputLineArray[0].equals("a")) {
						addFriendship(username1, username2);
					}
					else if (inputLineArray[0].equals("r")) {
						removeFriendship(username1, username2);
					} 
					else {
						logError("Invalid action on line " + lineCount + " of file: " + inputFile);
					}
				} 
				else {
					logError("Invalid number of arguments on line " + lineCount + " of file: " + inputFile);
				}
			}
			inputScanner.close();
			return true;
		}
		catch (FileNotFoundException e) {
			logError("Invalid file path. Please enter a valid file path.");
			return false;
		}
	} // parseFile
	
	/**
	 * Used to check if the username for a given input is valid or not
	 * Used in addFriendship/removeFriendship to specify which username is invalid
	 * @param username string to check if valid
	 * @return true if invalid username, false if valid
	 */
	private boolean invalidUsername(String username) {
		Pattern usernamePattern = Pattern.compile("[^_a-zA-Z0-9']");
		Matcher usernameMatcher = usernamePattern.matcher(username);
		return usernameMatcher.matches();
	} // invalidUsername
	
	/**
	 * Gets the graph's central user 
	 * 
	 * @return central user of the graph
	 */
	public Person getCentralUser() {
		return this.centralUser;
	} // getCentralUser
	
	/**
	 * Sets the graph's central user
	 * Logs an error if username is blank
	 * 
	 * @param username user to search for and set
	 */
	public void setCentralUser(String username) {
		if (username == null || username == "") {
			errorLog.add("Invalid action: cannot set a blank central user");
			return;
		}
		Person user = getUser(username);
		this.centralUser = user;
		logAction('s', username);
	}
	
	/**
	 * Gets a list of all usernames currently registerd in the SocialNetwork Graph
	 * 
	 * @return list of usernames
	 */
	public List<String> getAllPeople() {
		Set<Person> users = graph.getAllUsers();
		List<String> people = new ArrayList<String>();
		
		for (Person p : users) {
			people.add(p.getUsername());
		}
		
		return people;
		
	}
	
	/**
	 * Clears entire social graph network by creating new SocialGraph and setting centralUser to null
	 */
	public void clearNetwork() {
		this.graph = new SocialGraph();
		this.centralUser = null;
	}
	
	/**
	 * Given a user, returns the list of direct friends of that user
	 * 
	 * @param username user to search for 
	 * @return Person list of the user's friends
	 * @throws Exception 
	 */
	public List<Person> getClosestConnection(String username) throws Exception {
		Person user = getUser(username);
		
		if (username == null) {
			throw new Exception(); // TODO We need to change this to a more specific exception
		}
		
		if (user == null) {
			throw new Exception(); // TODO We need to change this to a more specific exception
		}
		
		return user.getFriends();
	}
	
	/**
	 * 
	 * @return the file path and name of the log file
	 */
	public String printLogFile() {
		try {
			FileWriter outWriter = new FileWriter(logFilePath); // Will overwrite file each time

			Iterator<String> logIterator = log.iterator();
			// Loop through linked list and write to file
			while (logIterator.hasNext()) {
				outWriter.write(logIterator.next() + "\n");
				outWriter.flush();
			}

			outWriter.close();
		} catch (IOException e) {
			// DO NOTHING
		}
		return logFilePath;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getLog() {
		List<String> logFile = new LinkedList<String>();
		Iterator<String> logIterator = log.iterator();
		// Loop through linked list and write to file
		while (logIterator.hasNext()) {
			logFile.add(0, logIterator.next());
		}
		return logFile;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getErrorLog() {
		List<String> errorLogFile = new LinkedList<String>();
		Iterator<String> errorLogIterator = errorLog.iterator();
		// Loop through linked list and write to file
		while (errorLogIterator.hasNext()) {
			errorLogFile.add(0, errorLogIterator.next());
		}
		return errorLogFile;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLatestError() {
		List<String> errorLogList = getErrorLog();
		return errorLogList.get(0);
	}
	
	/**
	 * Return the number of users in the social network
	 * 
	 * @return total number of users
	 */
	public int getNumPeople() {
		return graph.numUsers();
		
	}
	
	/**
	 * Return the number of friendships (edges) in the social network 
	 * 
	 * @return total number of edges
	 */
	public int getNumConnections() {
		return graph.numFriendships();
	}
	
	/**
	 * Gets usernames of the direct friends of the central user
	 * 
	 * @return list of friends of central user
	 */
	public List<String> getCentralUserFriends() {
		List<Person> friends = centralUser.getFriends();
		List<String> friendNames = new ArrayList<String>();
		
		for (Person p : friends) {
			friendNames.add(p.getUsername());
		}
		
		return friendNames;
	}
	
	/**
	 * Writes to log file an action executed by the user or from an input file. Used when handling
	 * vertices.
	 * 
	 * @param action 'a' for adding, 'r' for removing, 's' for setting
	 * @param username the username the action was acted upon
	 * @return true if action was successfully logged
	 */
	private Boolean logAction(char action, String username) {
		if (action != 'a' && action != 'r' && action != 's') {
			return false;
		}
		
		log.add(action + " " + username); // Add input to linked list
		return true;
	}
	
	/**
	 * Writes to log file an action executed by the user or from an input file. Used when handling
	 * edges.
	 * 
	 * @param action 'a' for adding, 'r' for removing, 's' for setting
	 * @param username1 the username the action was acted upon
	 * @param username2 the second user if adding or removing a friendship
	 * @return true if action was successfully logged
	 */
	private Boolean logAction(char action, String username1, String username2) {
		if (action != 'a' && action != 'r' && action != 's') {
			return false;
		}
		
		log.add(action + " " + username1 + " " + username2); // Add input to linked list
		return true;
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	private Boolean logError(String message) {
		if (message != null) {
			errorLog.add(message);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Given a username, find and return the associated Person object
	 * 
	 * @param username username of the person to search for
	 * @return Person with the username
	 */
	private Person getUser(String username) {
		Set<Person> users = graph.getAllUsers();
		
		for (Person p : users) {
			if (p.getUsername().equals(username)) {
				return p;
			}
		}
		
		return null;
	}
	
	/**
	 * Given a username, add user to the socialNetwork
	 * 
	 * @param username username of the person to add
	 * @throws UserExistsException 
	 */
	public boolean addFriend(String username) {
		try {
			graph.addUser(username);
			if (graph.numUsers() == 1) {
				this.centralUser = getUser(username);
			}
			logAction('a', username);
			return true;
		} catch (IllegalNullUserException e) {
			logError("Invalid username: failed to add user because username was blank");
		} catch (IllegalArgumentException e) {
			logError("Invalid username: failed to add " + username + " due to invalid username");
		} catch (UserExistsException e) {
			logError("Invalid action: failed to add existing user " + username + " to the network");
		}
		return false;
	}
	
	/**
     * Add friendship user1 and user2
     * to this graph.
     * 
     * If either user does not exist,
     * USER IS ADDED and then friendship is created.
     * No exception is thrown.
     *
     * If the friendship exists in the graph,
     * no friendship is added and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither user is null
     * 2. both users are in the graph 
     * 3. the friendship is not in the graph
     *  
     * @param username1 the first user
     * @param username2 the second user
	 * @throws UserExistsException 
     */
	public boolean addFriendship(String username1, String username2) {
		try {
			int numUsers = graph.numUsers();
			boolean setCentUser = false;
			if (numUsers == 0) {
				setCentUser = true;
			}
			graph.addFriendship(username1, username2);
			if (setCentUser) {
				this.centralUser = getUser(username1);
			}
			logAction('a', username1, username2);
			return true;
		} catch (IllegalNullUserException e) {
			String whichUsername = "second";
			if (username1 == null || username1 == "") {
				whichUsername = "first";
			} 
			logError("Invalid username: failed to add friendship because " + whichUsername + " username was blank");
		}
		catch (IllegalArgumentException e) {
			
			String whichUsername = username2;
			
			if (invalidUsername(username1)) {
				whichUsername = username1;
			}
			
			logError("Invalid username: failed to add friendship because " + whichUsername + " is an invalid username");
		} 
		catch (FriendshipExistsException e) {
			logError("Invalid action: failed to add friendship because friendship already exists");
		}
		return false;
	}
	
	/**
     * Remove a user and all associated 
     * friendships from the graph.
     * 
     * If user is null or does not exist,
     * method ends without removing a user, friendships, 
     * or throwing an exception.
     * 
     * Valid argument conditions:
     * 1. user is non-null
     * 2. user is not already in the graph 
     *  
     * @param user the user to be removed
     */
	public boolean removeFriend(String username) {
		try {
			if (graph.numUsers() == 0) {
				logError("Invalid action: cannot remove user from an empty network");
				return false;
			}
			Person personToRemove = getUser(username);
			if (personToRemove == this.centralUser) {
				logError("Invalid action: cannot delete the central user");
				return false;
			}
			graph.removeUser(username);
			if (graph.numUsers() == 0) {
				this.centralUser = null;
			}
			logAction('r',username);
			return true;
		}
		catch (IllegalNullUserException e) {
			logError("Invalid username: failed to remove user because username was null");
		}
		catch (IllegalArgumentException e) {
			logError("Invalid username: failed to remove user because " + username + " is an invalid username");
		}
		catch (UserNotFoundException e) {
			logError("Invalid username: failed to remove " + username + "because user was not in Social Network");
		}
		return false;
	}
	
    /**
     * Remove the friendship from user1 to user2
     * from this graph.  (friendship is directed and unweighted)
     * If either user does not exist,
     * or if a friendship from user1 to user2 does not exist,
     * no friendship is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither user is null
     * 2. both users are in the graph 
     * 3. the friendship from user1 to user2 is in the graph
     *  
     * @param user1 the first user
     * @param user2 the second user
     */
    public boolean removeFriendship(String username1, String username2) {
    	try {
    		if (graph.numUsers() == 0) {
				logError("Invalid action: cannot remove friendship from an empty network");
				return false;
			}
			graph.removeFriendship(username1, username2);
			logAction('r', username1, username2);
			return true;
		} catch (IllegalNullUserException e) {
			String whichUsername = "second";
			if (username1 == null || username1 == "") {
				whichUsername = "first";
			} 
			logError("Invalid username: failed to add friendship because " + whichUsername + " username was null");
		}
		catch (IllegalArgumentException e) {
			String whichUsername = username2;
			if (invalidUsername(username1)) {
				whichUsername = username1;
			}
			logError("Invalid username: failed to add friendship because " + whichUsername + " is an invalid username");
		} 
    	catch (UserNotFoundException e) {
    		String whichUsername = username1;
    		if (graph.hasUser(username1)) {
    			whichUsername = username2;
    		}
    		logError("Invalid username: " + whichUsername + " does not exist in network");
    	}
    	catch(FriendshipDoesNotExistException e) {
			logError("Invalid action: failed to add friendship because friendship already exists");
		}
    	return false;
	}

    /**
     * 
     * @param username1
     * @param username2
     * @return
     */
	public List<String> mutualFriends(String username1, String username2) {
		try{
			List<String> mutualFriends = new LinkedList<String>();
			List<Person> userFriends1 = graph.getFriendsOf(username1);
			List<Person> userFriends2 = getUser(username2).getFriends();
			for (int i = 0; i < userFriends1.size(); i++) {
				if (userFriends2.contains(userFriends1.get(i))) {
					mutualFriends.add(userFriends1.get(i).getUsername());
				}
			}
			if (mutualFriends.size() > 0) {
				return mutualFriends;
			}
			else {
				errorLog.add("Invalid action: these users share no mutual friends");
				return null;
			}
		}
		catch(IllegalNullUserException e) {
			String whichUsername = "second";
			if (username1 == null || username1 == "") {
				whichUsername = "first";
			}
			errorLog.add("Invalid username: could not find mutual friends because " + whichUsername + " username was blank");
		}
		catch(UserNotFoundException e) {
			String whichUsername = username1;
			if (graph.hasUser(username1)) {
				whichUsername = username2;
			}
			errorLog.add("Invalid username: could not find mutual friends because " + whichUsername + " is not in network");
		}
		return null;
	}
}
