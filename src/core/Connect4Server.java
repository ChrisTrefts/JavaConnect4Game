package core;
import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import core.Connect4;

/** Connect4Server is the server which interacts with clients and manages the Connect4 game between the 2
 * @author Chris Trefts
 * @version 1.0, April 9th, 2019
 */
public class Connect4Server extends Application implements Connect4Constants {
	/** is used to denote session number
	 */
	private int sessionNo = 1;
	/** is a Connect4 object which will be used to manage all game logic
	 */
	private Connect4 serverState = new Connect4();
	
	@Override
	/** Start creates a Scene to be played on and generates the server log, which also starts a thread 
	 * which allows two clients to connect to it and begin a Connect4 game
	 * 
	 * @param Stage primary stage is used to display a log which will be used by the server to display 
	 * when players connect and what session it is running
	 */
	public void start(Stage primaryStage) throws Exception {
		TextArea messageLog = new TextArea();
		
		//creates a scene to be placed in the sever stage/message log
		Scene scene = new Scene(new ScrollPane(messageLog), 500, 300);
		primaryStage.setTitle("Connect4 PvP server"); //sets title of message log
		primaryStage.setScene(scene);
		primaryStage.show();
		
		new Thread(  () -> {
			try {
				//creating server socket
				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() -> messageLog.appendText(new Date() + ": Server started at socket 8000\n"));
			while (true) {
				Platform.runLater(() -> messageLog.appendText(new Date() + ": Wait for players to join session " + sessionNo + '\n'));
				
				Socket player1 = serverSocket.accept(); //add player1 to socket and connects them to server
				
				Platform.runLater(() -> {
		            messageLog.appendText(new Date() + ": Player 1 joined session " + sessionNo + '\n');
		            messageLog.appendText("Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');
		          });
				
				new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1); //sends out message to player1 telling them they are player 1
				
				Socket player2 = serverSocket.accept(); //add player2 to socket and connects them to server
				
				Platform.runLater(() -> {
		            messageLog.appendText(new Date() + ": Player 2 joined session " + sessionNo + '\n');
		            messageLog.appendText("Player 2's IP address" + player2.getInetAddress().getHostAddress() + '\n');
		          });
				
				new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2); //sends out message to player1 telling which spot they are
				
				//displays session info
				Platform.runLater(() -> messageLog.appendText(new Date() + ": Start a thread for session "+ sessionNo++ + "\n"));
				
				new Thread(new HandleSession(player1, player2)).start();
			}
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}).start();
}
	/**HandleSession puts both clients into sockets so they are fully connected and ready to play. This embedded
	 * class enables and allows the 2 clients to communicate and have fluid and functional game play.
	 * 
	 * @author Chris Trefts
	 * @version 1.0, April 9th, 2019
	 */
	class HandleSession implements Runnable, Connect4Constants {
		private Socket player1;
		private Socket player2;
		
	
		private DataInputStream fromPlayer1;
		private DataInputStream fromPlayer2;
		private DataOutputStream toPlayer1;
		private DataOutputStream toPlayer2;
		
		private boolean continueGame = true;
		
		public HandleSession(Socket player1, Socket player2) {
			this.player1 = player1;
			this.player2 = player2;
			serverState = new Connect4();
		}

		@Override
		/**run takes in both players input and output which enables the server to communicate whether it has
		 * reached an end state, in which it would inform both clients
		 * @exception looks for and handle an IOException exception if it occurs
		 */
		public void run() {
			try {
				DataInputStream fromPlayer1 =  new DataInputStream(player1.getInputStream());
				DataInputStream fromPlayer2 =  new DataInputStream(player2.getInputStream());
				DataOutputStream toPlayer1 = new DataOutputStream(player1.getOutputStream());
				DataOutputStream toPlayer2 = new DataOutputStream(player2.getOutputStream());
				
				toPlayer1.writeInt(1);
				
				while (true) {
					int column = fromPlayer1.readInt();
					serverState.piecePlay(column - 1, "X");
					// Was used for testing purposes System.out.println(serverState.displayBoard());
					
					if(serverState.winCheck()) {
						sendMove(toPlayer2, column);
						toPlayer1.writeInt(PLAYER1_WON);
						toPlayer2.writeInt(PLAYER1_WON);
						break;
					}
					else if (serverState.checkForSpace()) {
						sendMove(toPlayer2, column);
						toPlayer1.writeInt(DRAW);
						toPlayer2.writeInt(DRAW);
						break;
					}
					else {
						toPlayer2.writeInt(CONTINUE);
						sendMove(toPlayer2, column);
					}
					column = fromPlayer2.readInt();
					serverState.piecePlay(column - 1, "O");
					// Was used for testing purposes System.out.println(serverState.displayBoard());
					
					
					if(serverState.winCheck()) {
						sendMove(toPlayer1, column);
						toPlayer1.writeInt(PLAYER2_WON);
						toPlayer2.writeInt(PLAYER2_WON);
						break;
					}
					else if (serverState.checkForSpace()) {
						sendMove(toPlayer1, column);
						toPlayer1.writeInt(DRAW);
						toPlayer2.writeInt(DRAW);
						break;
					}
					else {
						toPlayer1.writeInt(CONTINUE);
						sendMove(toPlayer1, column);
					}
				}
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		
		/** sendMove send out moves to the clients so their boards are the same
		 * 
		 * @param out is a DataOutputStream which sends it out to the specific player
		 * @param column is used to specify the specific column which was chosen
		 * @throws IOException if they occur
		 */
		public void sendMove(DataOutputStream out, int column) throws IOException{
			out.writeInt(column);
		}
	}
	
	/**main runs the code 
	 * @param args
	 */
  public static void main(String[] args) {
    launch(args);
  }
}