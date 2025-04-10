package application;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * The entry point of the application, setting up the primary stage and scene.
 * It includes the main menu and manages the starting, importing, and (intended)
 * creation of game levels through user interactions.
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		// Create a VBox layout with vertical spacing between child elements
		VBox menu = new VBox(10);
		menu.setPadding(new Insets(20)); // Set padding around the VBox

		// Buttons for starting, importing, and creating levels
		Button btnStart = new Button("Start Game");
		customizeButton(btnStart);
		Button btnImport = new Button("Import Level");
		customizeButton(btnImport);
		Button btnCreate = new Button("Create Level");
		customizeButton(btnCreate);

		// Set the action for the 'Start Game' button to open a file chooser and load a
		// game level
		btnStart.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			configureFileChooser(fileChooser);
			fileChooser.setTitle("Open Level File");
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				try {
					Game game = Game.getInstance(15, 15); // Create a new game instance
					GameUI gameUI = new GameUI(game, primaryStage); // Create a UI for the game
					game.checkAndSetGameUI(gameUI); // Set the game UI
					LevelLoader.loadLevel(game, file.getPath()); // Load the level from the selected file
					gameUI.createTraffic(); // Start traffic in the game
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Set the action for the 'Import Level' button to open a file chooser and load
		// a
		// game level
		btnImport.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			configureFileChooserForImport(fileChooser); // Configure file chooser for importing levels
			fileChooser.setTitle("Import Level File");
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				// Load the level from the selected file
				try {
					Game game = Game.getInstance(15, 15); // Create a new game instance
					GameUI gameUI = new GameUI(game, primaryStage); // Create a UI for the game
					game.checkAndSetGameUI(gameUI); // Set the game UI
					LevelLoader.loadLevel(game, file.getPath()); // Load the level from the selected file
					gameUI.createTraffic(); // Start traffic in the game
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		// This button should trigger the level creation feature, allowing users to
		// design and export maps as text files.
		// However, this functionality has not been implemented in this version of the
		// game.
		btnCreate.setOnAction(e -> {
			System.out.println("This game doesn't have this feature yet.");
		});

		// Add buttons to the menu
		menu.getChildren().addAll(btnStart, btnImport, btnCreate);

		// Create the main application layout pane and set up the scene
		Pane root = new Pane(menu);
		Scene scene = new Scene(root, 800, 600);
		primaryStage.setTitle("Traffic Control Simulator Menu");

		// Set a background image for the menu
		Image image = new Image("./images/menuBackground.png");
		Image icon1 = new Image("./images/icon1.png");
		BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		root.setBackground(new Background(bgImage));

		// Set the application icon
		primaryStage.getIcons().add(icon1);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Configures a FileChooser to select level files from a specific directory.
	 * 
	 * @param fileChooser The FileChooser to configure.
	 */
	private void configureFileChooser(FileChooser fileChooser) {
		String currentPath = System.getProperty("user.dir");
		fileChooser.setInitialDirectory(new File(currentPath + File.separator + "levels")); // Default directory
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
	}

	/**
	 * Configures a FileChooser to open at the root directory for importing levels.
	 * 
	 * @param fileChooser The FileChooser to configure.
	 */
	private void configureFileChooserForImport(FileChooser fileChooser) {
		File rootPath = new File(System.getProperty("user.home")); // Use user's home directory as a more accessible
																	// root
		fileChooser.setInitialDirectory(rootPath); // Set the initial directory to the user's home directory
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
	}

	/**
	 * Applies custom styling to buttons used in the application.
	 * 
	 * @param button The button to customize.
	 */
	private void customizeButton(Button button) {
		button.setFont(Font.font("Arial", 22)); // Set font size and family
		button.setTextFill(Color.WHITE); // Set text color
		button.setStyle("-fx-background-color: #6a5acd; -fx-background-radius: 15;"); // Set background style
		button.setPrefWidth(200);
		button.setPrefHeight(50);
	}

	public static void main(String[] args) {
		launch(args); // Launch the JavaFX application
	}
}
