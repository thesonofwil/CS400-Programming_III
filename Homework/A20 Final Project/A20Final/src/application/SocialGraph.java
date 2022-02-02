package application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filename:   Graph.java
 * Project:    p4
 * Authors:    Wilson Tjoeng
 * Course:	   CS400.010
 * Due:		   11/19/21
 * 
 * Directed and unweighted graph implementation
 */

public class SocialGraph implements SocialGraphADT {
	
	private static Set<Person> users;
	private int numUsers;
	private int numFriendships;
	
	/*
	 * Default no-argument constructor
	 */ 
	public SocialGraph() {
		users = new HashSet<Person>();
		numUsers = 0;
		numFriendships = 0;
	}

	/**
     * Add new user to the graph.
     *
     * If user is null or already exists,
     * method ends without adding a user or 
     * throwing an exception.
     * 
     * Valid argument conditions:
     * 1. user is non-null
     * 2. user is not already in the graph 
     * 
     * @param user the user to be added
	 * @throws IllegalNullUserException 
	 * @throws UserExistsException 
	 * @throws UserNotFoundException 
     */
	public void addUser(String user) throws IllegalNullUserException, UserExistsException  {
		if (user == null || user == "") {
			throw new IllegalNullUserException();
		}
		if (hasUser(user)) {
			throw new UserExistsException();
		}		
		Person p = new Person(user); 
		users.add(p);
		numUsers++;
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
	 * @throws UserNotFoundException 
	 * @throws IllegalNullUserException 
     */
	public void removeUser(String user) throws UserNotFoundException, IllegalNullUserException {
		if (user == null) {
			throw new IllegalNullUserException();
		}
		if (!hasUser(user)) {
			throw new UserNotFoundException();
		}		
		Person p = getUser(user);
		removeDirectFriends(p);
		users.remove(p);
		numUsers--;
	}

	/**
     * Add friendship from user1 to user2
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
     * @param user1 the first user
     * @param user2 the second user
	 * @throws IllegalNullUserException 
	 * @throws UserNotFoundException 
     */
    public void addFriendship(String user1, String user2) throws IllegalNullUserException, FriendshipExistsException {
		
		if (user1 == null || user1 == "" || user2 == null || user2 == "") {
			throw new IllegalNullUserException();
		}
		try {
			addUser(user1);
		} catch (UserExistsException e) {
			// DO NOTHING
		}
		try {
			addUser(user2);
		} catch (UserExistsException e) {
			// DO NOTHING
		}
		
		// Get Person objects
		Person p1 = null;
		Person p2 = null;
		
		try {
			p1 = getUser(user1);
			p2 = getUser(user2);
		} catch (UserNotFoundException e) {
			
		}
		
		if (areFriends(p1, p2)) { // i.e there is already an edge
			throw new FriendshipExistsException();
		}
		
		p1.friends.add(p2);
		p2.friends.add(p1);
		this.numFriendships++;
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
     * @throws IllegalNullUserException 
     * @throws UserNotFoundException 
     */
    public void removeFriendship(String user1, String user2) throws IllegalNullUserException, UserNotFoundException, FriendshipDoesNotExistException {
    	
		if (user1 == null || user1 == "" || user2 == null || user2 == "") {
			throw new IllegalNullUserException();
		}
		
		if (!hasUser(user1) || !hasUser(user2)) {
			throw new UserNotFoundException();
		}
		
		Person p1 = getUser(user1);
		Person p2 = getUser(user2);
				
		// Remove entry. Nothing happens if object is not in list
		p1.friends.remove(p2);
		p2.friends.remove(p1);
		numFriendships--;	
	}

    /**
     * Returns a Set that contains all the users
     * 
     * @return a Set<String> which contains all the users in the graph
     */
    public Set<Person> getAllUsers() {
		Set<Person> set = new HashSet<Person>();
		
		for (Person p : users) {
			set.add(p);
		}
		
		return set;
	}

    /**
     * Get all the direct (adjacent) friends of a user
     * 
     * @param user the specified user
     * @return an List<String> of all the direct friends for specified user
     * @throws UserNotFoundException 
     * @throws IllegalNullUserException 
     */
    public List<Person> getFriendsOf(String user) throws UserNotFoundException, IllegalNullUserException {
		if (user == null || user == "") {
			throw new IllegalNullUserException();
		}
		// User not in graph
		if (!hasUser(user)) {
			throw new UserNotFoundException();
		}
		
		Person p = getUser(user);
		
		return p.getFriends();
		
	}

    /**
     * Returns the number of friendships in this graph.
     * @return number of friendships in the graph.
     */
    public int numFriendships() {
        return numFriendships;
    }
    
    /**
     * Returns the number of users in this graph.
     * @return number of users in graph.
     */
    public int numUsers() {
		return numUsers;
	}
	
	/////---------------- Private Helper Methods ----------------\\\\\
	
	/**
	 * Removes all friends connected to a user by removing user from adjacency lists
	 * 
	 * @param user to clear direct edges from
	 */
	private void removeDirectFriends(Person user) {
		
		// We need to remove all references to the user from other user's friends list
		for (Person p : users) {
			if (p.friends.contains(user)) {
				p.friends.remove(user);
			}
		}
	}
	
	/**
	 * Checks if a user exists in graph given username
	 * 
	 * @param username to search for in set of users
	 * @return true if there is a person with a matching username, or false otherwise
	 */
	public boolean hasUser(String username) {
		
		for (Person p : users) {
			if (p.username.equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the user from graph given username
	 * 
	 * @param username to search for in set of users
	 * @return Person object that has a matching username, or null if not found
	 * @throws UserNotFoundException 
	 */
	private Person getUser(String username) throws UserNotFoundException {
		for (Person p : users) {
			if (p.username.equals(username)) {
				return p;
			}
		}
		throw new UserNotFoundException();
	}
	
	/**
	 * Checks if a user is friends with another user
	 * 
	 * @param user1 the first user to check
	 * @param user2 the second user to check
	 * @return true if user1's friends list contains user2, or vice-versa
	 */
	private boolean areFriends(Person user1, Person user2) {
		if (user1.friends.contains(user2) || user2.friends.contains(user1)) {
			return true;
		}
		
		return false;
	}	
}