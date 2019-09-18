package core;

/**Connect4Constants will be implemented by Connect4Server and Connect4Client to maintain the state of the game
 * @author Chris Trefts
 * @version 1.0 April 9th, 2019
 */
public interface Connect4Constants {
	  /** PLAYER1 is an int which tells a client they are PLAYER1
	   */
	  public static int PLAYER1 = 1; // Indicate player 1
	  /** PLAYER2 is an int which tells a client they are PLAYER1
	   */
	  public static int PLAYER2 = 2; // Indicate player 2
	  /** PLAYER1_WON is an int which tells the server that player 1 has won and the server should enter an end state
	   */
	  public static int PLAYER1_WON = 1; // Indicate player 1 won
	  /** PLAYER2_WON is an int which tells the server that player 1 has won and the server should enter an end state
	   */
	  public static int PLAYER2_WON = 2; // Indicate player 2 won
	  /** DRAW is an int which tells the server that the game has resulted in a draw and that it should enter an end state
	   */
	  public static int DRAW = 3; // Indicate a draw
	  /** CONTINUE is an int which tells the server the game is not over and that it should keep going until an end state has been entered
	   */
	  public static int CONTINUE = 4; // Indicate to continue
}
