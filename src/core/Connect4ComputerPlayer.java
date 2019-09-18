package core;
import core.Connect4;
import java.util.Random;

/**
 * 
 * @author Chris
 * @version 2.0  18th, 2019
 */
public class Connect4ComputerPlayer {
	/** is the Connect 4 class object */
	static Connect4 logic;
	
	/** Random object which allows the AI to pick a random location on the board */
	static Random rand = new Random();
	
	/** will store the randomly generate integer */
	static int rNum = 0;
	
	//* this is a reference which allows the AI to directly look at the board and make check based upon it 
	public Connect4ComputerPlayer (Connect4 logic) {this.logic = logic;}
	
	/**
	 * Wincheck is the single method within Connect4ComputerPlayer that allows the AI to play. The Picks a random location if there are no viable plays and block or wins the game itself it sees 3 of the same symbols in a row. 
	 * 
	 * @returns The space at which the AI will place its piece, it can either place a piece at a random location if nothing it detects no win conditions or it can place a piece in a specified location to either block the player form winning or to win the game itself
	 */
	public int winCheck() {
		
		for (int h = 5; h > 2; h--) { // this for loop checks for vertical
			for (int w = 0; w < logic.board[0].length; w++) {
				if (!logic.board[h][w].equals(" ") && logic.board[h][w].equals(logic.board[h - 1][w]) && logic.board[h][w].equals(logic.board[h - 2][w]) && " ".equals(logic.board[h - 3][w])) {
					System.out.println("I will put my piece in colunm " + (w + 1));
					return w + 1;
				}
			}
		}
		
		for (int h = 0; h < logic.board.length; h++) { // this for loop checks for horizontal starting from the left
			for (int w = 0; w < logic.board[h].length - 3; w++) {
				if (!logic.board[h][w].equals(" ") && logic.board[h][w].equals(logic.board[h][w + 1]) && logic.board[h][w].equals(logic.board[h][w + 2]) && " ".equals(logic.board[h][w + 3])) {
					System.out.println("I will put my piece in colunm " + (w + 4));
					return w + 4;
				}
			}
		}
		
		for (int h = 0; h < logic.board.length; h++) { // this for loop checks for horizontal starting from the right
			for (int w = 6; w > 2; w--) {
				if (!logic.board[h][w].equals(" ") && logic.board[h][w].equals(logic.board[h][w - 1]) && logic.board[h][w].equals(logic.board[h][w - 2]) && " ".equals(logic.board[h][w - 3])) {
					System.out.println("I will put my piece in colunm " + (w - 2));
					return w - 2;
				}
			}
		}
		
		for (int h = 5; h > 2; h--) { // this loops to check for diagonals and checks by starting from the bottom right
			for (int w = 6; w > 2; w--) {
				if (!logic.board[h][w].equals(" ") && logic.board[h][w].equals(logic.board[h - 1][w - 1]) && logic.board[h][w].equals(logic.board[h - 2][w - 2]) && " ".equals(logic.board[h - 3][w - 3])) {
					System.out.println("I will put my piece in colunm " + (w - 2));
					return w - 2;
				}
			}
		}

		for (int h = logic.board.length - 1; h >= logic.board.length - 3; h--) { // this for loop checks for diagonals and starts at the bottom left
			for (int w = 0; w < logic.board[0].length - 3; w++) {
				if (!logic.board[h][w].equals(" ") && logic.board[h][w].equals(logic.board[h - 1][w + 1]) && logic.board[h][w].equals(logic.board[h - 2][w + 2]) && " ".equals(logic.board[h - 3][w + 3])) { 
					System.out.println("I will put my piece in colunm " + (w + 4));
					return w + 4;
				}
			}
		}
		rNum = rand.nextInt(7);
		while (logic.checkForLimit(rNum) == true) {
			return checkForSpace();
		}
		System.out.println("I will put my piece in colunm " + (rNum + 1));
		return rNum + 1;
	}
	/** checkForSpace is used to generate a random place for Monty to place his piece in the situation that the column he wants to select is full 
	 * 
	 * @return a random number which does not interfere with already full columns
	 */
	public int checkForSpace() {
			rNum = rand.nextInt(7);
			while (logic.checkForLimit(rNum) == true) {
				rNum = rand.nextInt(7);
			}
			System.out.println("I will put my piece in colunm " + (rNum + 1));
			return rNum + 1;
		}

}
