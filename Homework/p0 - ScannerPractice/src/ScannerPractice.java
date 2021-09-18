import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Wilson Tjoeng
 * tjoeng@wisc.edu
 * CS400 010
 * Due: 9/23/21
 * 
 * This program uses the Scanner class to read an input file containing a 
 * list of movies and rates them, writing a randomly generated opinion onto 
 * an output file via the PrintWriter confclass. Afterwards the user has the option
 * to get the machine's thoughts on other movies he/she thinks of via the console
 * (also read by Scanner),
 */

// learn how Scanner instances that are connected to the keyboard work.
public class ScannerPractice {
  // A single instance of a Scanner connected to default input (keyboard)
  private static final Scanner STDIN = new Scanner(System.in);
  private static final List<String> ratings = Arrays.asList("This is awful", 
		  "This is god-awful", "Not the best", "Could be better", "What a classic",
		  "It's aight", "I like it", "Was this made by a kindergartener?",
		  "Even Alfred Hitchcock can't save this one", "Deserves an Academy Award",
		  "OK","Brilliant. Just brilliant.", "Marvelous", "Culture defining", 
		  "I wish I had gouged my eyes out", "Meh", 
		  "Would be good if Danny DeVito were in it", "Hilarious", 
		  "Hilariously bad", "I question your humanity if you didn't like it",
		  "10/10", "Work of art", "The director should never have been born",
		  "Not even Jesus can save this sin of a movie", "Atrocious", "Trash",
		  "It'll do in a pinch", "Would take this to a deserted island", 
		  "I had a new view on life after watching", "Cringe");
    
  
  /**
   * Informs the user of the program and prompts user to enter into the console
   * additional movies.
   *
   * @param args Not used
   * @throws FileNotFoundException Thrown if input/output files are invalid
   */
  public static void main(String [] args) throws FileNotFoundException {
    String inputPath = "./movies.txt";
    String outputPath = "./fileOutput.txt";
           
    System.out.println("I rate movies. Feed me a file to hear the truth.");
    
    rateMovieFromFile(inputPath, outputPath);
    
    System.out.println("Any other movies you want to hear my opinion on? "
    		+ "Type \"Quit\" to exit. ");
    
    rateMovieFromConsole();
  }
  
  /** 
   * Reads line by line of a text file containing a 
   * list of movies and generates a random review of 
   * each in an output file.
   *
   * @param inputPath The relative path of the input file, passed in as a string
   * @param outputPath The relative path of where the output file should 
   * be written to, passed in as a string
   */
  private static void rateMovieFromFile(String inputPath, String outputPath) {
	  try {
		  File input = new File(inputPath);
		  File output = new File(outputPath);
		  Scanner fileReader = new Scanner(input); // New scanner to read input
		  PrintWriter writer = new PrintWriter(output);
		  
		  // Read file line by line and write to output file
		  while (fileReader.hasNextLine()) {
			  String movie = fileReader.nextLine();
			  writer.write(movie + " - " + getRating() + "\n");
			  writer.flush();
		  } 
		  System.out.println("I left you a file with my wise insights.");
		  writer.close();
		  fileReader.close();
	  } catch (FileNotFoundException e) {
			  System.err.println("Error: file not found");
			  return;
		  }
  }
  
  /**
   * Reads a movie entered by user into the console and prints a random 
   * review of it. 
   * Stops when the review types the word "Quit".
   */
  private static void rateMovieFromConsole() {
	  String endCommand = "Quit";

	  while (STDIN.hasNextLine() ) {
			String text = STDIN.nextLine();
			if (text.equals(endCommand)) {
				System.out.println("I hope you learned something "
						+ "new today.");
				return;
			}
			System.out.println(text + " - " + getRating());
		}
  }
  
  /**
   * Randomly grabs a rating/review from the statics ratings list.
   * @return A string representing a rating.
   */
  private static String getRating() {
	  Random rand = new Random();
	  int size = ratings.size(); // upper bound
	  int index = rand.nextInt(size);
	  
	  return ratings.get(index); // Return a random rating from list
  }
}