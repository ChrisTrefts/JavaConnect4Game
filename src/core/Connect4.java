package core;

import java.util.Scanner;

/** connect4Core is the core of a Java connect4 game, which hold all the methods which the UI will use to simulate the game 
 * @author Chris Trefts
 * @version 4.0 April 18th, 2019
 */
public class Connect4 {
	final static int HEIGHT = 6;
	final static int WIDTH = 7;
	/** board is a 2dArray which will hold our values and will be the "board", which can be played on */
	static public String[][] board = new String[HEIGHT][WIDTH];
    
	//** scanner which take in user input */
	static public Scanner scan = new Scanner(System.in);
	
	/** connect4Core constructs a 2d array, which will be used to simulate a game board using java
	*/
	public Connect4() {
		String string = " ";

		for (int h = 0; h < board.length; h++) {
			for (int w = 0; w < board[h].length; w++) {
				board[h][w] = string;
			}
		}
	}

	/** display board is a method (essentially our toString), which display the board to the players and will also update as pieces are played onto the board
	 * @return displayBoard displays the board and return the board as a string in a readable fashion
	 */
	public String displayBoard() {
		String string = "";
		for (int h = 0; h < board.length; h++) {
			for (int w = 0; w < board[h].length; w++) {
				string += ("|" + board[h][w]);
			}
			string += "|\n";
		}
		return string;
	}

	/** checkForSpace is a method which checks every position on the board, and if it cannot find an a single empty space, it will return true and tell the UI to end and inform the players of a tie game
	 * @return false if checkForSpace finds a blank spot on the board, and true if it finds no blank spots on the board
	 */
	public boolean checkForSpace() {
		for (int h = 0; h < board.length; h++) {
			for (int w = 0; w < board[h].length; w++) {
				if (board[h][w].equals(" "))
					return false;
			}
		}
		return true;
	}
	
	/** checkForLimit is a method just looks at a single in the top row of the board, and is utilized in order to detect a full column and to have the user pick a different spot to play in if true
	 * @param num is going to be the users input - 1 (in order to account for index and realistic number differences), and is use to look at the top of the column which the play selected, if it checks if there is a symbol at the top of the column seeing if the selected column is full or not 
	 * @return false if checkForLimit finds no symbol at the top of the selected column and true if it does
	 */
	public boolean checkForLimit(int num) {
		try {
			
			if (!board[0][num].equals(" ")) {
				return true;
			}
			return false;
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("JUnit testing for a negative number");
			return false;
		}
	}

	/**piecePlay is used to place the piece in the specific spot the user wants, it first uses the method checkForSpace, so the piecePlay dosen't work if the board it full, and after that the method looks a the specified column looking for an empty space starting from the bottom of the board
	 * 
	 * @param column is used to specify the column the player wants to put their piece in
	 * @param player refers to the X or O string variable which denoted which player is currently selected 
	 * @return true if board is full or method finds spot to place piece and false if nothing is found, but due to if statements in the UI the false can never be hit, and it just the so the program compiles
	 */
	public boolean piecePlay(int column, String player) {
		try {
		if (checkForSpace() == true) {
			return true;
		}
		
		
		for (int h = 5; h >= 0; h--) {
			if (board[h][column].equals(" ")) {
				board[h][column] = player;
				return true;
			}
		}
		return false;
	}
		catch(IndexOutOfBoundsException e) {
			System.out.println("JUnit testing for a negative number");
			return false;
		}
	}

	/**winCheck is a method which checks to see a player has connected 4, accounting for both diagonals, vertical, and horizontal victories.
	 * @return returns true if winCheck finds any 4 pieces in a row and false if it finds no consecutive 4 pieces in a row
	 */
	public boolean winCheck() {
		for (int h = 0; h < board.length - 3; h++) { // this loops to check for diagonals and checks by starting from the top left 
			for (int w = 0; w < board[0].length - 3; w++ ) {
				if (!board[h][w].equals(" ") && board[h][w].equals(board[h + 1][w + 1]) && board[h][w].equals(board[h + 2][w + 2]) && board[h][w].equals(board[h + 3][w + 3])) {
					return true;
				}
			}
		}

		for (int h = board.length - 1; h >= board.length - 3; h--) { // this for loop checks for diagonals and starts at the bottom left
			for (int w = 0; w < board[0].length - 3; w++) {
				if (!board[h][w].equals(" ") && board[h][w].equals(board[h - 1][w + 1]) && board[h][w].equals(board[h - 2][w + 2]) && board[h][w].equals(board[h - 3][w + 3])) { 
					return true;
				}
			}
		}

		for (int h = 0; h < board.length - 3; h++) { // this for loop checks for vertical
			for (int w = 0; w < board[0].length; w++) {
				if (!board[h][w].equals(" ") && board[h][w].equals(board[h + 1][w]) && board[h][w].equals(board[h + 2][w]) && board[h][w].equals(board[h + 3][w])) {
					return true;
				}
			}
		}

		for (int h = 0; h < board.length; h++) { // this for loop checks for horizontal
			for (int w = 0; w < board[h].length - 3; w++) {
				if (!board[h][w].equals(" ") && board[h][w].equals(board[h][w + 1]) && board[h][w].equals(board[h][w + 2]) && board[h][w].equals(board[h][w + 3])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * checkInt makes sure the user input is an int and does not break the program
	 * @return CheckInt returns a int to be processed or calls itself if they input something other than an int and uses recursion until the user supplies an int 
	 * @exception Looks for and handles InputMismatchError, uses recursion in order to have the user supply the correct type of input
	 * @throws throws InputMismateErrors
	 */
	public int checkInt() {
		try {
			return scan.nextInt();
		}
		catch (Exception e){
			System.out.println("Please enter a Integer bewteen 1 and 7");
			scan.nextLine();
			return checkInt();
		}
	}
	/** getCoordinates return the exact position of the last piece played and is a helper method for Connect4Cleint
	 * @param column refers to the exact column in which the last piece was played
	 * @return returns the exact the position of the last piece played and a -1 as a base case that will never be touched
	 */
	public int getCoordinates(int column) {
		for (int h = board.length - 1; h >= 0; h--) {
			if (board[h][column].equals(" ")) {
				return h;
			}
		}
		return -1;
	}
}