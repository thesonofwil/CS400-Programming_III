/**
 * 
 */
package application;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tommy, Dannielle, Wilson
 *
 */
class Person {
	String username;
	List<Person> friends;
	
	/*
	 * Constructor to create a new Person for Social Network. Throws an exception if username
	 * is invalid. 
	 */
	Person(String username) {
		if (!validateUsername(username)) {
			throw new IllegalArgumentException("Username must consist of letters, numbers, "
					+ "underscores, and/or apostrophes.");
		}
		
		this.username = username;
		friends = new ArrayList<Person>();
	}
	
	/**
	 * Get the person's username 
	 * 
	 * @return the username
	 */
	String getUsername() {
		return this.username;
	}
	
	/**
	 * Get the list of direct friends a Person has
	 * 
	 * @return list of person's friends
	 */
	List<Person> getFriends() {
		return this.friends;
	}
	
	/**
	 * Checks if a person's username is of a valid format. The name can only consist of 
	 * alphanumeric characters (case insensitive) and/or an underscore and/or an apostrophe
	 * 
	 * @return true if the username is valid
	 */
	private boolean validateUsername(String username) {
		
		// Regex - this basically says check if any characters do not match a - z, A - Z, 0 -9,
		// or _ or '
		// ^ and $ indicate beginning and end of line, respectively
		// + indicates find one or more occurrences of the [ ] expression
		return username.matches("^[a-zA-Z0-9_]+$"); 
	}
}