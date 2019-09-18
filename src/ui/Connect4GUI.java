package ui;

import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.animation.TranslateTransition;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Random;
import javax.swing.JOptionPane;

/**Connect4GUI, allows a user to experience a digital connect 4 game wither against another player or a computer
 * 
 * @author Chris Trefts
 * @version 1.0, Mar 29th 2019
 */
public class Connect4GUI extends Application {
	private static final int WIDTH = 7; // columns
	private static final int HEIGHT = 6; // rows
	private static final int TILE_SIZE = 80;
	/** redMove is a boolean value which will tell the program whether the piece it drops will be red or yellow */
	private boolean redMove = true;
	/** grid is a 2D Disc array which will hold all of our Disc objects or pieces */
	private Disc[][] grid = new Disc[WIDTH][HEIGHT];
	private Pane discRoot = new Pane();
	/** Allows us to track how many pieces are on the board and tells us when a tie game has occurred */
	private int totalPieces = 0;
	/** Random object which allows the AI to place pieces if no blocks or win conditions have been found */
	private static Random rand = new Random();
	private static int rNum;

	//The 3 Strings below help create the initial windows for user choice 
	private static String[] consoleChoice = { "Graphical Interface", "Text Based" };
	private static String[] gameType = { " Player vs. Player", "Player vs. Computer" };
	private static String playerType;

	/**
	 * createContent is the top method of this class calling every other method required and makes up the Connect4GUI game
	 * @return pane which we are playing on
	 */
	public Parent createContent() {
		Pane pane = new Pane();
		pane.getChildren().add(discRoot);
		Shape gridShape = makeGrid();
		pane.getChildren().add(gridShape);
		try {
			pane.getChildren().addAll(defineColumns());
		} catch (IllegalArgumentException iae) {

		}
		return pane;
	}

	/**makeGrid make the rectangle which will make up our GUI, this method all so includes the cut out circles and the lighting effects of the GUI 
	 * 
	 * @return the shape that will make up the rectangle in our GUI
	 */
	public Shape makeGrid() {
		Shape shape = new Rectangle((WIDTH + 1) * TILE_SIZE, (HEIGHT + 1) * TILE_SIZE);

		// note we start with height, because we want out grid to build from the top left down
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				Circle circle = new Circle(TILE_SIZE / 2);
				circle.setCenterX(TILE_SIZE / 2);
				circle.setCenterY(TILE_SIZE / 2);
				// Then translation below are use to give good margins to the circles we place in our rectangle board
				circle.setTranslateX(j * (TILE_SIZE + 5) + TILE_SIZE / 4);
				circle.setTranslateY(i * (TILE_SIZE + 5) + TILE_SIZE / 4);
				shape = Shape.subtract(shape, circle);
			}
		}
		// lighting which will give our board a more defined look
		Light.Distant light = new Light.Distant();
		light.setAzimuth(45.0);
		light.setAzimuth(30.0);
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(5.0);
		shape.setFill(Color.BLUE);
		shape.setEffect(lighting);
		return shape;
	}

	/**define Columns call upon our method to place piece for both player vs. player and player vs. computer play and provides an Overlay for the player to see when they are selecting a column to place their piece
	 * 
	 * @returns a list of where everything in our Rectangle ArrayList
	 */
	public List<Rectangle> defineColumns() {
		List<Rectangle> list = new ArrayList<>();

		for (int i = 0; i < WIDTH; i++) {
			Rectangle rec = new Rectangle(TILE_SIZE, (HEIGHT + 1) * TILE_SIZE);
			rec.setTranslateX(i * (TILE_SIZE + 5) + TILE_SIZE / 4);
			rec.setFill(Color.TRANSPARENT);
			rec.setOnMouseEntered(e -> rec.setFill(Color.rgb(200, 200, 50, 0.3)));
			rec.setOnMouseExited(e -> rec.setFill(Color.TRANSPARENT));
			final int column = i;
			if (playerType.equals("p")) {
				rec.setOnMouseClicked(e -> playPiece(new Disc(redMove), column));
				list.add(rec);
			}
			if (playerType.equals("c")) {
				if (redMove) {
					rec.setOnMouseClicked(e -> {
						playPiece(new Disc(redMove), column);
						redMove = false;
						playPiece(new Disc(redMove), compGameCheck());
						redMove = true;
					});
					list.add(rec);
				}

			}
		}
		return list;
	}

	/**playPiece creates Disc object which will then be placed within our Rectangle, and plays the animations which give the GUI life
	 * 
	 * @param disc is an object which will be placed on the graph
	 * @param column is an int representation of WIDTH
	 */
	private void playPiece(Disc disc, int column) {
		int row = HEIGHT - 1;
		if (totalPieces == 41) {
			endGame();
		}

		do {
			if (!getPiece(column, row).isPresent()) {
				break;
			}
			row--;
		} while (row >= 0);
		if (row < 0) {
			return;
		}

		grid[column][row] = disc;
		discRoot.getChildren().add(disc);
		disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

		final int currentRow = row;

		TranslateTransition drop = new TranslateTransition(Duration.seconds(0.2), disc);
		drop.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);

		drop.setOnFinished(e -> {
			if (gameOver(column, currentRow)) {
				endGame();
			}

			// switches player if no one won
			redMove = !redMove;
		});

		totalPieces++;
		drop.play();
	}

	
	/** getPiece returns Optional which allows us to get away without our GUI breaking due to things being labeled null 
	 * 
	 * @param column is an int representation of WIDTH
	 * @param row is an int representation of HEIGHT
	 * @return Optional which allows our pieces to be labeled as Optional rather than null, which allows us to avoid null pointer errors
	 */
	private Optional<Disc> getPiece(int column, int row) {
		if (column < 0 || column >= WIDTH || row < 0 || row >= HEIGHT) {
			return Optional.empty();
		}
		// This allow things to be returned as Option rather than null to avoid Null
		// Pointer Errors
		return Optional.ofNullable(grid[column][row]);
	}

	/**gameOver sees whether the game is over by detecting 4 pieces in a row
	 * 
	 * @param column is an int representation of WIDTH
	 * @param row is an int representation of HEIGHT
	 * @return Returns true or false and tells the program whether someone has connected 4 or not
	 */
	private boolean gameOver(int column, int row) {
		List<Point2D> vertical = IntStream.rangeClosed(row - 3, row + 3).mapToObj(r -> new Point2D(column, r))
				.collect(Collectors.toList());
		List<Point2D> horizontal = IntStream.rangeClosed(column - 3, column + 3).mapToObj(c -> new Point2D(c, row))
				.collect(Collectors.toList());

		Point2D topLeft = new Point2D(column - 3, row - 3);
		// finds diagonal from top left to bottom right
		List<Point2D> botRightDiag = IntStream.rangeClosed(0, 6).mapToObj(i -> topLeft.add(i, i))
				.collect(Collectors.toList());

		Point2D botLeft = new Point2D(column - 3, row + 3);
		// finds diagonal from bot left to top right
		List<Point2D> topLeftDiag = IntStream.rangeClosed(0, 6).mapToObj(i -> botLeft.add(i, -i))
				.collect(Collectors.toList());

		return checkRange(vertical) || checkRange(horizontal) || checkRange(botRightDiag) || checkRange(topLeftDiag);
	}

	/**checkRange checks if their are 4 pieces in a row and is a helper method to gameOver
	 * 
	 * @param points is the point on the list which the program will look from
	 * @return true if checkRange detects 4 pieces in a row and false if not
	 */
	public boolean checkRange(List<Point2D> points) {
		int chain = 0;

		// range of points
		for (Point2D p : points) {
			int column = (int) p.getX();
			int row = (int) p.getY();

			Disc disc = getPiece(column, row).orElse(new Disc(!redMove));
			if (disc.red == redMove) {
				chain++;
				if (chain == 4) {
					return true;
				}
			} else {
				chain = 0;
			}
		}
		return false;
	}

	/**
	 * compGameCheck allows the computer to check if their are 3 pieces in a row
	 * @return an int at which the computer will place its piece and a random int if it detects nothing
	 */
	private int compGameCheck() {
		for (int v = 0; v < HEIGHT; v++) {
			for (int j = 0; j < WIDTH; j++) {
				int width = j;
				int height = v;
				List<Point2D> vertical = IntStream.rangeClosed(v - 3, v + 3).mapToObj(r -> new Point2D(width, r))
						.collect(Collectors.toList());
				List<Point2D> horizontalRight = IntStream.rangeClosed(j, j + 3).mapToObj(c -> new Point2D(c, height))
						.collect(Collectors.toList());
				List<Point2D> horizontalLeft = IntStream.rangeClosed(j - 3, j).mapToObj(c -> new Point2D(c, height))
						.collect(Collectors.toList());
				Point2D botRight = new Point2D(j + 3, v + 3);
				// finds diagonal from bottom right to top left
				List<Point2D> botRightDiag = IntStream.rangeClosed(0, 6).mapToObj(i -> botRight.add(-i, -i))
						.collect(Collectors.toList());
				Point2D botLeft = new Point2D(j + 3, v - 3);
				// finds diagonal from bottom left to top right
				List<Point2D> botLeftDiag = IntStream.rangeClosed(0, 6).mapToObj(i -> botLeft.add(i, -i))
						.collect(Collectors.toList());

				if (compCheckRange(vertical)) {
					return v;
				}
				if (compCheckRange(horizontalRight)) {
					return j + 3;
				}
				if (compCheckRange(horizontalLeft)) {
					return j - 3;
				}
				if (compCheckRange(botRightDiag)) {
					return j - 3;
				}
				if (compCheckRange(botLeftDiag)) {
					return j + 3;
				}
			}
		}

		rNum = rand.nextInt(7);
		return rNum + 1;

	}

	/** compCheckRange check if a 3 piece sequence is in range for the computer
	 * 
	 * @param points is the point on the list which the program will look from
	 * @return true or false based on whether the computer find a 3 piece sequence
	 */
	private boolean compCheckRange(List<Point2D> points) {
		int chain = 0;

		// range of points
		for (Point2D p : points) {
			int column = (int) p.getX();
			int row = (int) p.getY();

			Disc disc = getPiece(column, row).orElse(new Disc(!redMove));
			if (disc.red == redMove) {
				chain++;
				if (chain == 3) {
					return true;
				}
			} else {
				chain = 0;
			}
		}
		return false;
	}

	/**
	 *Disc is a hidden class is our disc object which will be used to represent our pieces on the board
	 */
	public static class Disc extends Circle {
		private final boolean red;

		public Disc(boolean red) {
			// ? means if and : means if not then, in fx logic
			super(TILE_SIZE / 2, red ? Color.RED : Color.YELLOW);
			this.red = red;

			setCenterX(TILE_SIZE / 2);
			setCenterY(TILE_SIZE / 2);
		}
	}

	/**
	 * endGame ends the game when a victory or tie occurs
	 */
	private void endGame() {
		if (totalPieces == 41) {
			JOptionPane.showMessageDialog(null, "The game is a draw, please relaunch the program to play again");
			System.exit(0);
		}

		JOptionPane.showMessageDialog(null, "Player " + (redMove ? "Red" : "Yellow") + ", is the victor, please relaunch the program to play again");
		System.exit(0);

	}

	@Override
	/**
	 * Starts the game and shows our GUI to the user
	 */
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
	}

	/**
	 * main starts the game and allows the user to pick whether they want console or GUI based and based on that decision take then to Connect4TextConsole or to pick player vs. player or player vs. computer in the GUI code
	 * @param args helps to run the program
	 */
	public static void main(String[] args) {
		int typeChoice = JOptionPane.showOptionDialog(null,
				"Would you like a Graphical interface or a console based one?", "GUI or Console",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, consoleChoice, consoleChoice[0]);
		if (typeChoice == 0) {
			int gameChoice = JOptionPane.showOptionDialog(null,
					"Would you like to have a player vs player game or a player vs computer game?", "pvp or pvc",
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, gameType, gameType[0]);
			if (gameChoice == 0) {
				playerType = "p";
			} else {
				playerType = "c";
			}
			launch(args);
		} else if (typeChoice == 1) {
			Connect4TextConsole.main(args);
		}

	}

}
