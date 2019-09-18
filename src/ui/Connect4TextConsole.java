package ui;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import core.Connect4;
import core.Connect4ComputerPlayer;

/**
 * connect4UI is a the main interface of the connect 4 game and utilizes classes in order to simulate the full connect 4 experience and also has an AI option if you don't have a friend play with
 * 
 * @author Chris Trefts
 * @version 2.0 Mar 18, 2019
 */
public class Connect4TextConsole {
	/** Scanner object is created to allow user input */
	static Scanner scan = new Scanner(System.in);
	/**
	 * connect4CORE object is created, so we have a board to play with and to use all the methods that were created to make the connect 4 game run
	 */
	static Connect4 connect4game = new Connect4();
	/**
	 * player is a global string which with each while loop will from X and O to denote which player is playing
	 */
	static String player = "X";
	/**
	 * play is a boolean which acts as a flag to whether the game should continue or not
	 */
	static boolean play = true;
	/**
	 * num is an int which will take in the user user input and act as the parameter for some of the methods in connect4CORE
	 */
	static int num = 0;
	/**
	 * Monty is the object created, for Connect4ComputerPlayer and is used to have an AI play against a real player.
	 */
	static Connect4ComputerPlayer Monty = new Connect4ComputerPlayer(connect4game);
	/**
	 * decision is a String which, which stores user input and helps the program decide whether it will run connect 4 with an Ai or another player
	 */
	static String decision;

	/**
	 * Is the main method that essentially consists of a while loop that will continue until specific conditions are met, resulting in a winner or tie game
	 */

	public static void main(String[] args) {
		while (play == true) {
			System.out.println(
					"Begin Game. Enter 'P' if you want to play against another player; enter 'C' to play against a computer");
			decision = scan.nextLine();
			while (!(decision.equals("p") || decision.equals("P") || decision.equals("c") || decision.equals("C"))) {
				System.out.println(
						"Please enter 'P' if you want to play against another player; enter 'C' to play against a computer");
				decision = scan.nextLine();
			}
			// This part of the code activates if the user decides to play against an AI
			if (decision.equals("c") || decision.equals("C")) {
				System.out.println(
						"Hello, my name is Monty, it is a pleasure to play against you, but know I am not very good at games");
				System.out.println(
						"----------------------------------------------------------------------------------------------------");
				System.out.println(connect4game.displayBoard());
				while (play == true) {

					System.out.println(
							"Please select column a between [ 1 - 7 ] that you would like to place your piece in ");

					num = connect4game.checkInt();

					while (num < 1 || num > 7 || connect4game.checkForLimit(num - 1)) {
						if (num < 1 || num > 7) {
							System.out
									.println("You did not choose a number bewteen 1 and 7 please choose again player ");
							num = scan.nextInt();
						} else if (connect4game.checkForLimit(num - 1)) {
							System.out.println(
									"You have selected a column which is already full, please choose another coulmn player ");
							num = scan.nextInt();
						}
					}
					connect4game.piecePlay((num - 1), player);
					if (connect4game.winCheck() == true) {
						play = false;
						System.out.println("The player has connected 4 and has won. GOOD GAME WELL PLAYED!");
						System.out.println("Ahhh, you won well, Monty hopes you come back and play again");
					} else if (connect4game.checkForSpace() == true) {
						play = false;
						System.out.println("There are no more spaces to play, TIE GAME!");
					}
					System.out.println(connect4game.displayBoard());

					if (player == "X")
						player = "O";
					else
						player = "X";

					if (play == true) {
						System.out.println("Now its Monty's time to play");
						connect4game.piecePlay(Monty.winCheck() - 1, player);

						System.out.println("Monty is thinking...");
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if (connect4game.winCheck() == true) {
							play = false;
							System.out.println("Monty has connected 4 and has won. GOOD GAME WELL PLAYED!");
							System.out.println(
									"YEAH MONTY HAS WON, thank you for playing wiht me, and come back any time.");
						} else if (connect4game.checkForSpace() == true) {
							play = false;
							System.out.println("There are no more spaces to play, TIE GAME!");
							System.out.println("Hmmmm, it seems the game has ended in tie, I want to play again");
						}

						System.out.println(connect4game.displayBoard());
						if (player == "X")
							player = "O";
						else
							player = "X";
					}
				}
			}

			// This part of the code activates if the user decides to play against a another player
			else if (decision.equals("p") || decision.equals("P")) {
				System.out.println(connect4game.displayBoard());
				while (play == true) {
					System.out.println(
							"Please select a column between [ 1 - 7 ] that you would like to place your piece in player "
									+ player);
					num = connect4game.checkInt();
					while (num < 1 || num > 7 || connect4game.checkForLimit(num - 1)) {
						if (num < 1 || num > 7) {
							System.out.println(
									"You did not choose a number bewteen 1 and 7 please choose again player " + player);
							num = scan.nextInt();
						} else if (connect4game.checkForLimit(num - 1)) {
							System.out.println(
									"You have selected a column which is already full, please choose another coulmn player "
											+ player);
							num = scan.nextInt();
						}
					}
					connect4game.piecePlay((num - 1), player);
					if (connect4game.winCheck() == true) {
						play = false;
						System.out.println("Player " + player + " has connected 4, " + player
								+ " has won. GOOD GAME WELL PLAYED!");
					} else if (connect4game.checkForSpace() == true) {
						play = false;
						System.out.println("There are no more spaces to play, TIE GAME!");
					}
					System.out.println(connect4game.displayBoard());
					if (player == "X")
						player = "O";
					else
						player = "X";
				}
			}
		}
	}
}
