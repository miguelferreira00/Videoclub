package project;
import java.util.Scanner;

/*
 * @author Miguel Ferreira 61879
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
public class Movie {
	public String title;
	private int year;
	private int quantity;
	private double price;
	private double tax;
	private String code;
	private int[][] rentals;
	
	
	public Movie(String title, int year, int quantity, double price, double tax, int[][] rentals) {
		this.title = title;
		this.year = year;
		this.quantity = quantity;
		this.price  = price;
		this.tax = tax;
		this.rentals = rentals;
		
		String newTitle = title;
		newTitle = newTitle.replaceAll("\\s", "").toUpperCase(); //Remove all white spaces and upper case all characters
		int row = 0;
		boolean checker = false;
		StringBuilder code = new StringBuilder();
		int sumCode = 0;
		char[][] grid  = new char[3][newTitle.length()];
		
		//Sort the movie name in the grid
		for(int i = 0; i < newTitle.length(); i++) {
			char character = newTitle.charAt(i);
			
			if(character >= 'A' && character <= 'Z') {
				grid[row][i] = newTitle.charAt(i);
				if(row == 0) {
					checker = true; 
				}
				else if (row == 2) {
					checker = false;
				}
				if(checker) {
					row++;
				}
				else {
					row--;
				}
			}
		}
		//Get the 3 first characters
		for(int i = 0; i < grid.length;i++) {
			for(int j = 0; j < grid[0].length; j++) {
				if(sumCode == 3) {
					break;
				}
				if(grid[i][j] != '\0') {
					code.append(Character.toString(grid[i][j]));
					sumCode++;
				}
			}
		}
		//Get the 3 last characters
		sumCode = 0;
		StringBuilder lastCharacters = new StringBuilder();
		for(int i = grid.length - 1; i > 0; i--) {
			for(int j = grid[0].length - 1; j > 0; j--) {
				if(sumCode == 3) {
					break;
				}
				if(grid[i][j] != '\0') {
					lastCharacters.append(Character.toString(grid[i][j]));
					sumCode++;
				}
			}
		}
		//Build the code
		lastCharacters.reverse();
		code.append(lastCharacters);
		this.code = code.toString();
	}	
	
	/*
	 * @return 		title of movie
	 */
	public String getTitle() {
		return this.title;
	}
	
	/*
	 * @return		year of movie
	 */
	public int getYear() {
		return this.year;
	}
	
	/*
	 * @return		quantity of movies
	 */
	public int getQuantity() {
		return this.quantity;
	}
	
	/*
	 * @return		price of movie
	 */
	public double getPrice() {
		return this.price;
	}
	
	/*
	 * @return		tax to pay to the studio of movie
	 */
	public double getTax() {
		return this.tax;
	}
	
	/*
	 * @return		movie identifier
	 */
	public String getCode() {
		return this.code;
	}
	
	/*
	 * @return		list of rentals
	 */
	public int[][] getRentals() {
		return this.rentals;
	}
	
	/*
	 * Add new rental to the list
	 * @param rentalInfo	the information of new rental
	 * @requires 		{@code rentalInfo != null && rentalInfo.length == 2}
	 */
	public void addRental(int[] rentalInfo) {
		
		int numberOfRentals = this.rentals[0].length;
		
		// Increase rentals matrix
		int[][] auxRentals = new int[2][numberOfRentals + 1];
		
		// Copy rentals information to auxiliary matrix
		for(int i = 0; i < numberOfRentals; i++) {
			auxRentals[0][i] = this.rentals[0][i];
			auxRentals[1][i] = this.rentals[1][i];
		}
		
		// Add new rental
		auxRentals[0][numberOfRentals] = rentalInfo[0];
		auxRentals[1][numberOfRentals] = rentalInfo[1];
		
		// Update rentals matrix
		this.rentals = auxRentals;
	}
	
	/*
	 * Remove a rental from the list
	 * @param clientID		client identification
	 * @requires		{@code client > 0}
	 */
	public int removeRental(int clientId) {
			
		int daysOfDelay = 0;
		int numberOfRentals = this.rentals[0].length;
		
		// Decrease rentals matrix
		int[][] auxRentals = new int[2][numberOfRentals - 1];
		
		// Copy rentals information to auxiliary matrix
		for(int i = 0; i < numberOfRentals; i++) {
			// Only copy rentals that not correspond 
			// to return information
			if(this.rentals[0][i] != clientId) {
				auxRentals[0][i] = this.rentals[0][i];
				auxRentals[1][i] = this.rentals[1][i];
			}
			else {
				int days = Math.min(daysOfDelay, this.rentals[1][i]);
				daysOfDelay = Math.abs(days);
			}
		}
		
		// Update rentals matrix
		this.rentals = auxRentals;
		
		return daysOfDelay;
	}
	
	
}
		
		
		
	
	
	
	

