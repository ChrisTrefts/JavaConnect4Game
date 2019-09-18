package test;

import static org.junit.Assert.*;
import org.junit.Test;
import core.Connect4;
import core.Connect4ComputerPlayer;

/**Connect4ComputerPlayerTest uses Unit Testing to test both the desired functionality of Connect4ComputerPlayerss methods and to determine the class coverage of the code
 * 
 * @author Chris Trefts
 * @version 1.0, April 18th, 2019
 */
public class Connect4ComputerPlayerTest {
	Connect4 game = new Connect4();
	Connect4ComputerPlayer monty = new Connect4ComputerPlayer(game);

	
	@Test
	/**
	 * winCheck_emptyBoardTest makes sure winCheck can detect empty boards and place a random piece
	 */
	public void winCheck_emptyBoardTest() {
		String player = "O";
		game.piecePlay(monty.winCheck() - 1, player);
		boolean result =  false;
		for (int h = 0; h < game.board.length; h++) {
			for (int w = 0; w < game.board[h].length; w++) {
				if (!game.board[h][w].equals(" ")) {
					result = true;
				}
			}
		}
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_rightHorizontalWin makes sure winCheck can detect wins which will occur from Horizontal right
	 */
	public void winCheck_rightHorizontalWin() {
		String player = "O";
		boolean result = false;
		game.board[5][1] = "X";
		game.board[5][2] = "X";
		game.board[5][3] = "X";
		game.piecePlay(monty.winCheck() - 1, player);
		if (game.board[5][4].equals("O")) { 
			result = true;
		}
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_leftHorizontalWin makes sure winCheck can detect wins which will occur from Horizontal left
	 */
	public void winCheck_leftHorizontalWin() {
		String player = "O";
		boolean result = false;
		game.board[5][4] = "X";
		game.board[5][5] = "X";
		game.board[5][6] = "X";
		game.piecePlay(monty.winCheck() - 1, player);
		if (game.board[5][3].equals("O")) {
			result = true;
		}
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_vertalWin makes sure winCheck can detect wins which will occur from a vertical position
	 */
	public void winCheck_vertalWin() {
		String player = "O";
		boolean result = false;
		game.board[5][3] = "X";
		game.board[4][3] = "X";
		game.board[3][3] = "X";
		game.piecePlay(monty.winCheck() - 1, player);
		if(game.board[2][3].equals("O")) {
			result = true;
		}
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_rightDiagWin makes sure winCheck can detect wins which will occur from Diagonally Right
	 */
	public void winCheck_rightDiagWin() {
		String player = "O";
		boolean result = false;
		game.board[5][0] = "X";
		game.board[5][1] = "X";
		game.board[5][2] = "X";
		game.board[5][3] = "X";
		game.board[4][1] = "X";
		game.board[4][2] = "X";
		game.board[4][3] = "X";
		game.board[3][2] = "X";
		game.board[2][2] = "O";
		game.board[5][4] = "O";
		game.board[4][4] = "O";
		game.board[4][0] = "O";
		game.piecePlay(monty.winCheck() - 1, player);
		if(game.board[3][3].equals("O")) {
			result = true;
		}
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_leftDiagWin makes sure winCheck can detect wins which will occur from Diagonally left
	 */
	public void winCheck_leftDiagWin() {
		String player = "O";
		boolean result = false;
		game.board[5][6] = "X";
		game.board[5][5] = "X";
		game.board[5][4] = "X";
		game.board[5][3] = "X";
		game.board[4][5] = "X";
		game.board[4][4] = "X";
		game.board[4][3] = "X";
		game.board[3][4] = "X";
		game.board[2][4] = "O";
		game.board[4][6] = "O";
		game.board[5][2] = "O";
		game.board[4][2] = "O";
		game.piecePlay(monty.winCheck() - 1, player);
		if(game.board[3][3].equals("O")) {
			result = true;
		}
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * checkforSpace_test makes sure Monty can place a random piece in a spot in addition to detecting full rows
	 */
	public void checkforSpace_test() {
		String player = "O";
		boolean result = false;
		for (int h = 0; h < game.board.length; h++) {
			for (int w = 0; w < game.board[h].length; w++) {
				game.board[h][w] = "X";
			}
		}
		game.board[1][6] = "O";
		game.board[1][5] = "O";
		game.board[0][5] = "O";
		game.board[1][3] = "O";
		game.board[0][6] = " ";
		game.piecePlay(monty.winCheck() - 1, player);
		System.out.println(game.displayBoard());
		if(game.board[0][6].equals("O")) {
			result = true;
		}
		assertEquals(true, result);
		
	}

}
