package project;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;
import java.lang.String;

/*
 * @author Miguel Ferreira 61879
 */
public class VideoClub {
	private int numberOfMovies;
	private double revenue;
	private double profit;
	private Movie[] movies;
	
	
	public VideoClub(String fileName, int numberOfMovies) throws FileNotFoundException {
		
		// Initialize variables
		this.revenue = 0;
		this.profit = 0;
		
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		int counter = 0;
		sc.nextLine(); //skips the first line
		//Counts how much movies there is in fileName
		while(sc.hasNextLine()) {
			counter++;
			sc.nextLine(); 
		}
		
		this.movies = new Movie[counter];
		if(counter < numberOfMovies) {
			this.numberOfMovies = counter;
		}
		else {
			this.numberOfMovies = numberOfMovies;
		}
		sc.close();
		
		//fill the movies array with all of movies from fileName
		int i = 0;
		sc = new Scanner(file);
		sc.nextLine(); //skips first line
		while(sc.hasNextLine()) {
			//saves all movie information in an array
			String line = sc.nextLine();
			String[] movieInfo = line.split(",");
			this.movies[i] = processMovieInfo(movieInfo);
			i++;
		}
		
		sc.close();
	}
	
	/*
	 * @return number of movies
	 */
	public int getNumberOfMovies() {
		return this.numberOfMovies;
	}
	
	/*
	 * @return number of available moveis to rent
	 */
	public int numberAvailableMovies() {
		
		int numberAvailableMovies = 0;
		for(int i = 0; i < this.numberOfMovies; i++) {
			
			Movie movie = this.movies[i];
			int quantity = movie.getQuantity();
			int[][] rentals = movie.getRentals();
			
			if(quantity - rentals[0].length > 0) {
				numberAvailableMovies++;
			}
		}
		
		return numberAvailableMovies;
	}
	
	/*
	 * @return total revenue of rentals
	 */
	public double getTotalRevenue() {
		return this.revenue;
	}
	
	/*
	 * @return total profit from movies rentals
	 */
	public double getTotalProfit() {
		return this.profit;
	}
	
	/* Filtered every movie by certain year
	 * @return String with title and price information filtered by year
	 * @param year		year of movie
	 * @requires	{@code year > 0}
	 */
	public String filterByYear(int year) {
		
		String moviesList = "";
		for(int i = 0; i < this.numberOfMovies; i++) {
			
			Movie movie = this.movies[i];
			
			if(movie.getYear() == year) {
				moviesList = moviesList.concat("Title:" + movie.getTitle() + ",");
				moviesList = moviesList.concat("Price:$" + movie.getPrice() + "\n");
			}
		}
		
		return moviesList;
	}
	
	/*
	 * Filtered every movie by certain price
	 * @return String with title and price information
	 * @param price		price of movie
	 * @requires	{@code price > 0}
	 */
	public String filterByPrice(double price) {
		String moviesList = "";
		for(int i = 0; i < this.numberOfMovies; i++) {
			Movie movie = this.movies[i];
			if(movie.getPrice() < price) {
				moviesList = moviesList.concat("Title:" + movie.getTitle() + ",");
				moviesList = moviesList.concat("Price:$" + movie.getPrice() + "\n");
			}
		}
		return moviesList;
	}
	
	/*
	 * Filters every available movie
	 * @return String with title and price information
	 */
	public String filterAvailableMovies() {
		String moviesList = "";
		for(int i = 0; i < this.numberOfMovies; i++) {
			Movie movie = this.movies[i];
			int quantity = movie.getQuantity();
			int[][] rentals = movie.getRentals();
			if(quantity - rentals[0].length > 0) {
				moviesList = moviesList.concat("Title:" + movie.getTitle() + ",");
				moviesList = moviesList.concat("Price:$" + movie.getPrice() + "\n");
			}
		}
		return moviesList;
	}
	
	
	/*
	 * Make, if possible, all transactions and makes a list with all of them
	 * @return String with type of transaction, movie title and profit information
	 * @param rentalsFileName	file with rentals information
	 * @requires		{@code rentalsFileName != null}
	 */
	public String activityLog(String rentalsFileName) throws FileNotFoundException {
		File file = new File(rentalsFileName);
		Scanner sc = new Scanner(file);
		//String[] rentInfo = new String[3];
		String rentalsAndReturns = "";
		sc.nextLine(); // skips first line
		while(sc.hasNextLine()) {
			
			// Read next line
			String line = sc.nextLine();
			String[] rentDescription = line.split(",");
			
			// process rentals information
			String transaction = rentDescription[0];
			int clientID = Integer.parseInt(rentDescription[1]);
			String movieNameOrCode = rentDescription[2];
			boolean movieExists = false;
			
			String rentalStatus = "";
			for(int i = 0; i < this.numberOfMovies; i++) {
							
				Movie movie = this.movies[i];
				
				// Check if movie exists
				if(movieNameOrCode.equals(movie.getTitle()) || 
						movieNameOrCode.equals(movie.getCode())) {
					
					// Rent transaction
					if(transaction.equals("rent")) {
						int quantity = movie.getQuantity();
						int[][] rentals = movie.getRentals();
						
						// If movie is available, rental is successful
						if(quantity - rentals[0].length > 0) {
							
							// String construction
							rentalStatus = rentalStatus.concat("Rental successful: ");
							rentalStatus = rentalStatus.concat("client " + clientID);
							rentalStatus = rentalStatus.concat(" rented " + movie.getTitle());
							rentalStatus = rentalStatus.concat(" for $" + movie.getPrice()+"\n");
							
							// Update rental information
							int[] rentalInfo = {clientID, 7};
							movie.addRental(rentalInfo);
							
							// Total and profit calculation
							double price = movie.getPrice();
							double tax = movie.getTax();
							double profit = price * (1 - tax);
							rentalStatus = rentalStatus.concat("Total: $" + String.format(Locale.ROOT, "%.2f", price));
							rentalStatus = rentalStatus.concat(" [$" + String.format(Locale.ROOT, "%.2f", profit) + "]\n");
							
							// Update revenue and profit
							this.revenue = this.revenue + price;
							this.profit = this.profit + profit;
							
						}
						// If movie not available, inform that client asked for
						else {
							// String construction
							rentalStatus = rentalStatus.concat("Movie currently not available: ");
							rentalStatus = rentalStatus.concat("client " + clientID);
							rentalStatus = rentalStatus.concat(" asked for " + movie.getTitle() + "\n");
						}
						
					}
					// Return transaction
					else if(transaction.equals("return")) {
						
						// Return movie to videoclub
						int daysOfDelay = movie.removeRental(clientID);
						
						// Movie returned: client number returned movieTitle
						if(daysOfDelay == 1) {
							rentalStatus = rentalStatus.concat("Movie returned "
									+ "with " + daysOfDelay + " day of delay: ");
						}
						else if(daysOfDelay > 1) {
							rentalStatus = rentalStatus.concat("Movie returned "
									+ "with " + daysOfDelay + " days of delay: ");
						}
						else {
							rentalStatus = rentalStatus.concat("Movie returned: ");
						}
						
						rentalStatus = rentalStatus.concat("client " + clientID);
						rentalStatus = rentalStatus.concat(" returned " + movie.getTitle() + "\n");
						
						// Total and profit calculation
						if(daysOfDelay > 0) {
							double penalty = 2.0 * daysOfDelay;
							double tax = movie.getTax();
							double profit = penalty * (1 - tax);
							rentalStatus = rentalStatus.concat("Total: $" + String.format(Locale.ROOT, "%.2f", penalty));
							rentalStatus = rentalStatus.concat(" [$" + String.format(Locale.ROOT, "%.2f", profit) + "]\n");
							
							// Update revenue and profit
							this.revenue = this.revenue + penalty;
							this.profit = this.profit + profit;
							
						}
						else {
							rentalStatus = rentalStatus.concat("Total: $0.00 [$0.00]\n");
						}
					}
					
					movieExists = true;
					break;
				}
			}
			
			
			//(Movie not found: client number asked formovieTitle
			if(!movieExists) {
				//(Movie not found: client number asked formovieTitle
				rentalStatus = rentalStatus.concat("Movie not found: ");
				rentalStatus = rentalStatus.concat("client " + clientID);
				rentalStatus = rentalStatus.concat(" asked for " + movieNameOrCode + "\n");
			}
			
			// Save transactions information
			rentalsAndReturns = rentalsAndReturns.concat(rentalStatus);
		}
		sc.close();
		return rentalsAndReturns;
	}
	
	/*
	 * updates all the stock and writes in output file
	 * @param fileName		file where all new stock will be writen
	 */
	public void updateStock(String fileName) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(fileName);
		writer.println("Title,Year,Quantity,Rentals,Price,Tax");
		for(int i = 0; i < this.numberOfMovies; i++) {
			
			String movieInfo = "";
			Movie movie = this.movies[i];
			
			// Add title, year and quantity
			movieInfo = movieInfo.concat(movie.getTitle() + ",");
			movieInfo = movieInfo.concat(movie.getYear() + ",");
			movieInfo = movieInfo.concat(movie.getQuantity() + ",");
			
			// Add rentals
			int[][] rentals = movie.getRentals();
			String rentalInfo = "";
			if(rentals[0].length > 0) {
				String[] rentalsArray = new String[rentals[0].length];
				for(int j = 0; j < rentals[0].length; j++) {
					int clientID = rentals[0][j];
					int daysLeft = rentals[1][j];
					rentalsArray[j] = "(" + clientID + ";" + daysLeft + ")";
				}
				rentalInfo = rentalInfo.concat(String.join(" ", rentalsArray));
			}
			movieInfo = movieInfo.concat(rentalInfo + ",");
			
			// Add price and tax
			String priceStr = String.format(Locale.ROOT, "%.2f", movie.getPrice());
			String taxStr = String.format(Locale.ROOT, "%.1f", movie.getTax());
			movieInfo = movieInfo.concat(priceStr + ",");
			movieInfo = movieInfo.concat(taxStr + "%");
			
			writer.println(movieInfo);
		}
		writer.close();
	}
	
	
	/*
	 * Processes all movie information
	 * @return movie with all his processed information
	 * @param		list with movie information
	 * @requires		{@code movieInfo != null && movieInfo.length == 6}
	 */
	private Movie processMovieInfo(String[] movieInfo) {
		
		// Process movie information
		String movieTitle = movieInfo[0];
		int movieYear = Integer.parseInt(movieInfo[1]);
		int movieQuantity = Integer.parseInt(movieInfo[2]);
		double moviePrice = Double.parseDouble(movieInfo[4]);
		
		// Transform rentals in a matrix
		int[][] rentalsMatrix = new int[2][0];
		if(movieInfo[3] != "") {
			String[] rentalsArray = movieInfo[3].split(" ");
			rentalsMatrix = new int[2][rentalsArray.length];
	
			for(int k = 0; k < rentalsArray.length; k++) {
				rentalsArray[k] = rentalsArray[k].replace("(", "");
				rentalsArray[k] = rentalsArray[k].replace(")", "");
				
				String[] rentalInfo = rentalsArray[k].split(";");
				// Save client identifier
				rentalsMatrix[0][k] = Integer.parseInt(rentalInfo[0]);
				// Save days left to deliver movie
				rentalsMatrix[1][k] = Integer.parseInt(rentalInfo[1]);
			}
		}
		
		// Remove percentage from tax value and convert to double
		String taxValue = movieInfo[5].replaceAll("%", "");
		double tax = Double.parseDouble(taxValue) / 100;
		
		Movie movie = new Movie(movieTitle, 
				movieYear, 
				movieQuantity, 
				moviePrice, 
				tax, rentalsMatrix);
		
		return movie;
		
	}
			
			
}

