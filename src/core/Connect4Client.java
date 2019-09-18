package core;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

/** Connect4Client allows user to connect to a server and play against anther player in real time
 * @author Chris Trefts
 * @version 2.0, April 9th, 2019
 */
public class Connect4Client extends Application implements Runnable, Connect4Constants {
	  /** global object which is used to have a set height for when we make the buttons and shapes
	   */
	  final static int HEIGHT = 6;
	  /** global object which is used to have a set height for when we make the buttons and shapes
	   */
	  final static int WIDTH = 7; 
	  /** global object which is used to tell each player whether its their turn or not
	   */
	  private boolean playerTurn = false;
	  /** global object which makes the player wait when the opponent is moving
	   */
	  private boolean waiting = false;
	  /** global object which sends data from the client to the server
	   */
	  private DataOutputStream toServer; 
	  /**global object which receives data from the server and is processed by the client
	   */
	  private DataInputStream fromServer;
	  /**global object which uses a string and allows the client to connect to a local server
	   */
	  private String host = "localHost";
	  /**global object which tells the client to continue playing until server enters an end state
	   */
	  private boolean continueGame = true;
	  /** Initialization of ButtonHandler object and stems from the embedded class ButtonHandler, it handles the buttons
	   */
	  ButtonHandler handler = new ButtonHandler();
	  /** Initialization of Connect4 object and stems from the core class Connect4 and manages logic of the game on the client and server
	   */
	  Connect4 baseGame;
	  /** allows the player to be assigned a color to their pieces
	   */
	  Color playerColor;
	  /** allows the opponent to be assigned a color to their pieces
	   */
	  Color opponentColor;
	  /** sets a base string to the player
	   */
	  String player = "X";
	  /** sets a base string to the opponent
	   */
	  String opponent;
	  /** A GridPane which will hold our buttons and visual player area for each client
	   */
	  GridPane pane;
	  
	  
	 @Override
	 	/**start is an Override method which starts the Client/Server Connect4 process, by placing buttons and shapes on to the GridPane, 
	 	 * creating a scene with a title and connect client to Server 
	 	 * 
	 	 */
	 	public void start(Stage primaryStage) throws Exception {
			pane = new GridPane();
			baseGame = new Connect4();
			for (int i = 0; i < WIDTH; i++) {
				Button button = new Button(String.valueOf(i + 1));
				button.setMinSize(50, 50);
				button.setOnAction(handler);
				pane.add(button, i, 0);
				GridPane.setHalignment(button, HPos.CENTER);
				for (int j = 0; j <= HEIGHT; j++) {
					Circle circle = new Circle();
					circle.setRadius(50 / 2);
					circle.setStroke(Color.BLUE);
					circle.setFill(null);
					pane.add(circle, i, j);
				}
			}
			Scene scene = new Scene(pane, 360, 360);
			primaryStage.setTitle("Connect4:");
			primaryStage.setScene(scene);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();
			connectToServer();
		} 
	 
	 @Override
	 	/**run is an Override method which notifies each client whether they are player1 or player2
	 	 * and assigns colors to each player. It also manages wait times and taking in server info
	 	 * @exception looks for and handles an exception if it occurs
	 	 */
		public void run() {
		 try {
			int player = fromServer.readInt();
			if (player == PLAYER1) {
				fromServer.readInt();
				playerTurn = true;
				playerColor = Color.RED;
				opponentColor = Color.YELLOW;
			}
			else if (player == PLAYER2) {
				playerTurn = false;
				playerColor = Color.YELLOW;
				opponentColor = Color.RED;
			}
			while (continueGame) {
				if (player == PLAYER1) {
					waitForPlay();
					receiveServerInfo();
				}
				else if (player == PLAYER2){
					receiveServerInfo();
					waitForPlay();
				}
			}
		 }
		 catch (Exception ex) {
		 }
	}
	 /**connectToServer help to assign the client to a socket and takes date and give data to the server
	  * @exception looks for and handles an exception which occur 
	  */
	 private void connectToServer() {
		 try {
			 // creating sockets which players to connect to in the server
			 Socket socket;
			 socket = new Socket(host, 8000);
			 
			 fromServer = new DataInputStream(socket.getInputStream());
			 toServer = new DataOutputStream(socket.getOutputStream());
		 }
		 catch (Exception ex) {
			 System.err.println(ex);
		 }
		 
		 Thread thread = new Thread(this);
		 thread.start();
	 }
	 
	 /** receiveServerInfo allow the server to communicate with the client communicate whether the game is in play or if it 
	  *  has reached an end state
	  * @throws IOExceptions that occur
	  */
	 public void receiveServerInfo() throws IOException {
		 int gameState = fromServer.readInt();
		 System.out.println("Status: " + gameState);
		 if (gameState == DRAW) {
			 continueGame = false;
			 JOptionPane.showMessageDialog(null, "TIE GAME", "Game over", JOptionPane.PLAIN_MESSAGE);
			 
			 if(player == "O") receiveMove();
		 }
		 
		 else if (gameState == PLAYER1_WON) {
			 continueGame = false;
			 JOptionPane.showMessageDialog(null, "PLAYER 1 IS THE WINNER!", "Game over", JOptionPane.PLAIN_MESSAGE);
		 }
		 
		 else if (gameState == PLAYER2_WON) {
			 continueGame = false;
			 JOptionPane.showMessageDialog(null, "PLAYER 2 IS THE WINNER!", "Game over", JOptionPane.PLAIN_MESSAGE);
		 }
		 else {
			 receiveMove();
			 playerTurn = true;
		 }
		 
		 
	 }
	 /** sendMove sends the player move to the server so it can then update its board and sends that data to the opposing player
	  * 
	  * @param column is the column which the player placed their piece
	  * @throws IOExceptions that occur
	  */
	 public void sendMove(int column) throws IOException {
		 toServer.writeInt(column);
		// Was used for testing purposes System.out.println("Sent " + column + " to server");
	 }
	 
	 /** receiveMove receives move data form the server and update the clients perspective based on
	  *  the opponents move
	  * 
	  * @throws IOExceptions that might occur
	  */
	 public void receiveMove() throws IOException {
		 int column = fromServer.readInt();
		// Was used for testing purposes System.out.println("Received " + column + " from server");
		 playerColor(column, baseGame.getCoordinates(column - 1), opponentColor);
		 baseGame.piecePlay(column - 1, "O");
		 if (player.equals("X")) {
			 player = "O";
		 }
		 else {
			 player = "X";
		 }
	 }
	 
	 /** waitForPlay makes the player wait while the other player makes their move
	  * @throws InterruptedExceptions that might occur
	  */
	 public void waitForPlay() throws InterruptedException {
		 waiting = true;
		 while (waiting) {
			 Thread.sleep(100);
		 }
	 }

	 /**playerColor helps the client and server update the gui implement colored circles to denote player moves
	  * 
	  * @param row is the row in which color to denote player choices on the gui will be updated
	  * @param column is the column in which color to denote player choices on the gui will be updated
	  * @param color is the specific players character
	  */
	 public void playerColor(int row, int column, Color color) {
		 for(Node node : pane.getChildren()) {
			 if (pane.getRowIndex(node) == column + 1 && pane.getColumnIndex(node).equals(row - 1) && column != -1) {
				 ((Circle) node).setFill(color);
			 }
		 }
	 }
	 
	 /**Button Handler is a embedded class which uses ActionEvent
	  * 
	  * @author Chris Trefts
	  * @version 1.0 April 9th, 2019
	  */
	 class ButtonHandler implements EventHandler<ActionEvent> {
		 
		 /**handle is used to handle events and player moves when a button on the gui is pressed
		  */
		 public void handle(ActionEvent event) {
			 if (baseGame.winCheck() == false && baseGame.checkForSpace() == true) {
				 System.out.println("TIE GAME. the Board is full and this game is a tie, wanna duke it out again");
				 return;
			 }
			 System.out.println("Player " + player + " placed their piece in column " + ((Button) event.getSource()).getText() + ".");
			 if (playerTurn) {
				 
				 int column = Integer.valueOf(((Button) event.getSource()).getText());
				 playerColor(column , baseGame.getCoordinates(column - 1), playerColor);
				 baseGame.piecePlay(column - 1, player);
				 try {
					 sendMove(column);
				 }
				 catch (IOException e) {
					 e.printStackTrace();
				 }
				 playerTurn = false;
				 waiting = false;
			 }
		 }
	 }
	
	/**main runs the code 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}