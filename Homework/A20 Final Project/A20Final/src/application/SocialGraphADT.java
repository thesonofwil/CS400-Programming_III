package application;

import java.util.List;
import java.util.Set;

/**
 * Filename:   SocialGraphADT.java
 * Project:    p4
 * Authors:    Tommy, Dannielle, Wilson
 * 
 * A graph interface to represent the social connections between users.
 */
public interface SocialGraphADT {

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
     */
    public void addUser(String user) throws IllegalNullUserException, UserExistsException;

    
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
    public void removeUser(String user) throws UserNotFoundException, IllegalNullUserException;

    
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
     * @throws UserNotFoundException 
     * @throws IllegalNullUserException 
     * @throws UserExistsException 
     * @throws FriendshipExistsException 
     */
    public void addFriendship(String user1, String user2) throws IllegalNullUserException, UserNotFoundException, UserExistsException, FriendshipExistsException;

    
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
     * @throws FriendshipDoesNotExistException 
     */
    public void removeFriendship(String user1, String user2) throws IllegalNullUserException, UserNotFoundException, FriendshipDoesNotExistException;
    
        
    /**
     * Returns a Set that contains all the users
     * 
     * @return a Set<String> which contains all the users in the graph
     */
    public Set<Person> getAllUsers();
    
    
    /**
     * Get all the direct (adjacent) friends of a user
     * 
     * @param user the specified user
     * @return an List<String> of all the direct friends for specified user
     * @throws UserNotFoundException 
     * @throws IllegalNullUserException 
     */
    public List<Person> getFriendsOf(String user) throws UserNotFoundException, IllegalNullUserException;
    

    /**
     * Returns the number of friendships in this graph.
     * @return number of friendships in the graph.
     */
    public int numFriendships();
    
    
    /**
     * Returns the number of users in this graph.
     * @return number of users in graph.
     */
    public int numUsers();

}