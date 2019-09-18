package test;
import static org.junit.Assert.*;
import org.junit.Test;
import core.Connect4;
/**Connect4Test uses Unit Testing to test both the desired functionality of Connect4s methods and to determine the class coverage of the code
 * 
 * @author Chris Trefts
 * @version 1.0, April 18th, 2019
 */
public class Connect4Test {
	Connect4 game = new Connect4();
	
	@Test
	/**
	 * checkForSpaceTest_empty makes sure checkForSpace can detect empty spaces on a empty board
	 */
	public void checkForSpaceTest_empty() {
		boolean result = game.checkForSpace();
		assertEquals(false, result);
	}
	
	@Test
	/**
	 * checkForSpaceTest_halfFull makes sure checkForSpace can detect empty spaces on a semi empty board
	 */
	public void checkForSpaceTest_halfFull() {
		game.board[0][5] = "X";
		game.board[4][2] = "X";
		game.board[5][3] = "X";
		boolean result = game.checkForSpace();
		assertEquals(false, result);
	}

	@Test
	/**
	 * checkForSpaceTest_full makes sure checkForSpace can detect no empty spaces on a full board
	 */
	public void checkForSpaceTest_full() {
		for (int h = 0; h < game.board.length; h++) {
			for (int w = 0; w < game.board[h].length; w++) {
				game.board[h][w] = "X";
			}
		}
		boolean result = game.checkForSpace();
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * checkForlimitTest_true makes sure checkForLimit can check the limit of a column when the column is full
	 */
	public void checkForlimitTest_true() {
		game.board[0][3] = "X";
		boolean result = game.checkForLimit(3);
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * checkForlimitTest_false makes sure checkForLimit can check the limit of a column when the column is not full
	 */
	public void checkForlimitTest_false() {
		boolean result = game.checkForLimit(3);
		assertEquals(false, result);
		
	}
	
	@Test
	/**
	 * checkForlimitTest_wrounginput makes sure checkForLimit can throw improper int if the occasion may occur
	 */
	public void checkForlimitTest_wronginput() {
		boolean result = game.checkForLimit(-5);
		assertEquals(false, result);
	}
	
	@Test
	/**
	 * piecePlay_validInput check to make sure piecePlay will take a input from the user and place it in the correct spot
	 */
	public void piecePlay_validInput() {
		String player = "X";
		game.piecePlay(4, player);
		boolean result;
		if (game.board[5][4] == "X") result = true;
		else result = false;
		assertEquals(true, result);	
	}
	
	@Test
	/**
	 * piecePlay_invaildInput check to make sure piecePlay will take a input from the user and does not place it in the wrong spot
	 */
	public void piecePlay_invaildInput() {
		String player = "X";
		game.piecePlay(4, player);
		boolean result;
		if (game.board[5][2] == "X") result = true;
		else result = false;
		assertEquals(false, result);	
	}
	
	@Test
	/**
	 * piecePlay_nonIntInput makes sure piecePlay can throw improper int if the occasion may occur
	 */
	public void piecePlay_nonIntInput() {
		String player = "X";
		game.piecePlay(-1, player);
		boolean result;
		if (game.board[5][2] == "X") result = true;
		else result = false;
		assertEquals(false, result);	
	}
	
	@Test
	/**
	 * winCheck_horizontalWin makes sure winCheck can detect wins which will occur from Horizontal right
	 */
	public void winCheck_horizontalWin() {
		game.board[0][1] = "X";
		game.board[0][2] = "X";
		game.board[0][3] = "X";
		game.board[0][4] = "X";
		boolean result = game.winCheck();
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_noWin make sure Win check does not detect a win when one has not occurred
	 */
	public void winCheck_noWin() {
		game.board[0][3] = "X";
		game.board[1][3] = "X";
		game.board[2][3] = "X";
		boolean result = game.winCheck();
		assertEquals(false, result);
	}
	
	@Test
	/**
	 * winCheck_vertalWin makes sure winCheck can detect wins which will occur from a vertical position
	 */
	public void winCheck_vertalWin() {
		game.board[0][3] = "X";
		game.board[1][3] = "X";
		game.board[2][3] = "X";
		game.board[3][3] = "X";
		boolean result = game.winCheck();
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_rightDiagWin makes sure winCheck can detect wins which will occur from Diagonally Right
	 */
	public void winCheck_rightDiagWin() {
		game.board[5][0] = "X";
		game.board[4][1] = "X";
		game.board[3][2] = "X";
		game.board[2][3] = "X";
		boolean result = game.winCheck();
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_leftDiagWin makes sure winCheck can detect wins which will occur from Diagonally left
	 */
	public void winCheck_leftDiagWin() {
		game.board[4][6] = "X";
		game.board[3][5] = "X";
		game.board[2][4] = "X";
		game.board[1][3] = "X";
		boolean result = game.winCheck();
		assertEquals(true, result);
	}
	
	@Test
	/**
	 * winCheck_displayBoardPrints makes sure that the displayBoard method actually produces a string and not something that is null
	 */
	public void winCheck_displayBoardPrints() {
		boolean result = false;
		String check = game.displayBoard();
		if (!check.equals(null)) {
			result = true;
		}
		assertEquals(true, result);
	}	
}