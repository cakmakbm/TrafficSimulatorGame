package application;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * Represents the graphical user interface for the game. It is responsible for
 * rendering all visual components like cars, traffic lights, and the game grid.
 * This class also handles user interactions and updates the display based on
 * changes in the game state.
 */
public class GameUI {
	private Game game;
	private Pane root;
	private Scene scene;
	private Rectangle background;
	private AnimationTimer timer;
	private Map<Car, Rectangle> carGraphics = new HashMap<>();
	private Label scoreLabel;
	private Label crashLabel;
	int a = 0;

	/**
	 * Constructs the game UI and initializes components.
	 * 
	 * @param game  The game instance this UI is tied to.
	 * @param stage The primary stage of the application.
	 */
	public GameUI(Game game, Stage stage) {
		this.game = game;
		game.setGridSize(15, 15);
		root = new Pane();
		scene = new Scene(root, 800, 800);
		stage.setScene(scene);

		createLabels();

		game.createTraffic();
		stage.show();
		background = new Rectangle(0, 0, 800, 800);
		background.setFill(Color.LIGHTBLUE);
		root.getChildren().add(background);

		initializeUI();
		createTraffic();
	}

	/**
	 * Initializes UI elements and draws initial map elements.
	 */
	private void initializeUI() {
		Rectangle rect = new Rectangle(0, 0, 800, 800); // Creating rectangle for the background
		rect.setFill(Color.LIGHTBLUE);
		root.getChildren().add(rect);
		game.getElements().forEach(elem -> elem.draw(root));
	}

	/**
	 * Draws the grid lines on the game field.
	 */
	private void drawGrid() {
		// Draw horizontal grid lines
		for (double i = 0; i <= game.getGridHeight(); i++) {
			Line horizontalLine = new Line(0, i * game.getCellSize(), game.getGridWidth() * game.getCellSize(),
					i * game.getCellSize());
			horizontalLine.setStroke(Color.GRAY);
			root.getChildren().add(horizontalLine);
		}

		// Draw vertical grid lines
		for (double i = 0; i <= game.getGridWidth(); i++) {
			Line verticalLine = new Line(i * game.getCellSize(), 0, i * game.getCellSize(),
					game.getGridHeight() * game.getCellSize());
			verticalLine.setStroke(Color.GRAY);
			root.getChildren().add(verticalLine);
		}
	}

	/**
	 * Creates the score and crash count labels.
	 */
	private void createLabels() {
		scoreLabel = new Label("Score: " + game.carsArrived + "/" + game.winCount);
		crashLabel = new Label("Crashes: " + game.crashes + "/" + game.maxCrashCount);
		scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		crashLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		scoreLabel.setLayoutX(10);
		scoreLabel.setLayoutY(10);
		crashLabel.setLayoutX(10);
		crashLabel.setLayoutY(40);
		root.getChildren().addAll(scoreLabel, crashLabel);
	}

	/**
	 * Updates the text of the score and crash labels.
	 */
	private void updateLabels() {
		scoreLabel.setText("Score: " + game.getCarsArrived() + "/" + game.getWinCount());
		crashLabel.setText("Crashes: " + game.getCrashes() + "/" + game.getMaxCrashCount());
		scoreLabel.toFront();
		crashLabel.toFront();
	}

	/**
	 * Updates the entire UI, including map elements and cars.
	 */
	private void updateUI() {
		for (MapElement element : game.getElements()) {
			if (element instanceof TrafficLight) {
				TrafficLight light = (TrafficLight) element;
				light.draw(root);
			}
		}
		for (Car car : game.getCars()) {
			car.draw(root);
			updateCarPosition(car); // Update position for each car
		}
		updateLabels();
	}

	/**
	 * Adds a car's visual representation to the pane.
	 * 
	 * @param car The car to add.
	 */
	public void addCarToPane(Car car) {
		root.getChildren().add(car.getCarShape());
	}

	/**
	 * Updates the graphical representation of a car's position.
	 * 
	 * @param car The car to update.
	 */
	public void updateCarPosition(Car car) {
		Rectangle carShape = carGraphics.get(car);
		if (carShape != null) {
			carShape.setX(car.getX());
			carShape.setY(car.getY());
			carShape.toFront();
		}
	}

	/**
	 * Displays an end game screen with a message indicating whether the player won
	 * or lost. The screen includes a label with a message centered on a black
	 * background rectangle. After displaying the message, the game exits after a
	 * delay of 7 seconds.
	 *
	 * @param won A boolean indicating whether the player won (true) or lost
	 *            (false).
	 */
	public void displayEndGameScreen(boolean won) {
		String message = won ? "You Won!" : "You Lost!";

		// Create a new Pane for the end game message that does not inherit opacity
		Pane messagePane = new Pane();
		messagePane.setPrefSize(root.getWidth(), root.getHeight());

		// Create label for the message
		Label endGameLabel = new Label(message);
		endGameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 52));
		endGameLabel.setTextFill(won ? Color.GREEN : Color.RED); // Set text color to red or green

		// Calculate position
		double labelWidth = 240; // Approximate width of the label
		double labelHeight = 80; // Approximate height of the label
		double posX = (root.getWidth() - labelWidth) / 2; // Center horizontally

		// Create a black rectangle as the background
		Rectangle background = new Rectangle(posX - 20, 100, labelWidth + 40, labelHeight + 30);
		background.setFill(Color.BLACK);
		background.setArcWidth(20);
		background.setArcHeight(20);

		// Position the label on the rectangle
		endGameLabel.setLayoutX(posX);
		endGameLabel.setLayoutY(120);

		// Add the rectangle and label to the message pane
		messagePane.getChildren().addAll(background, endGameLabel);

		Platform.runLater(() -> {
			root.getChildren().add(messagePane); // Add both the rectangle and label to the root
			new Timeline(new KeyFrame(Duration.seconds(7), ae -> Platform.exit())).play();
		});
	}

	/**
	 * Removes a car's graphical representation from the pane.
	 * 
	 * @param car The car to remove.
	 */
	public void removeCarFromPane(Car car) {
		Rectangle carShape = car.getCarShape();
		if (carShape != null && root.getChildren().contains(carShape)) {
			Platform.runLater(() -> {
				root.getChildren().remove(carShape);
			});
		}
	}

	/**
	 * Initializes traffic and UI updates through an animation timer.
	 */
	public void createTraffic() {
		timer = new AnimationTimer() {

			private long lastUpdate = 0;

			@Override
			public void handle(long now) {
				if (a == 0) {
					drawGrid();
					for (MapElement element : game.getElements()) {
						element.draw(root);
					}
				}
				a++;
				if (lastUpdate == 0 || now - lastUpdate >= 1_000_000) { // Update every 160 milliseconds
					game.update();
					updateUI(); // Update the UI to reflect changes
					lastUpdate = now;
				}
			}
		};
		timer.start();
	}
}
