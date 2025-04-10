package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * Manages the main logic of the simulation, including the initialization and
 * updating of game states. This class handles the creation of cars, management
 * of traffic elements, and checks for win or loss conditions. It utilizes
 * singleton pattern to ensure that only one instance of the game is active at
 * any given time.
 */
public class Game {
	private static volatile Game instance;
	private List<MapElement> elements;
	private Map<Integer, Path> paths;
	private double gridWidth;
	private double gridHeight;
	private double cellSize;
	public List<Car> cars = new ArrayList<>();
	private GameUI gameUI; // Reference to the user interface
	private long spawnInterval = 2_000_000_000; // Interval for car spawning (2 seconds)
	private double lastSpawnTime = 0; // Time since last car was spawned
	private AnimationTimer timer;
	protected int crashes = 0; // Count of crashes
	protected int carsArrived = 0; // Count of cars that have completed their paths
	protected int winCount;
	protected int maxCrashCount;

	/**
	 * Constructs a Game instance with specified grid dimensions.
	 * 
	 * @param gridWidth  Width of the game grid
	 * @param gridHeight Height of the game grid
	 */
	public Game(double gridWidth, double gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.cellSize = 800.0 / 15.0; // Calculate cell size based on a standard grid
		this.elements = new ArrayList<>();
		this.paths = new HashMap<>();
	}

	/**
	 * Sets the user interface for the game. This method is responsible for linking
	 * the game logic with the graphical interface.
	 *
	 * @param gameUI The game UI to be set.
	 */
	public void setGameUI(GameUI gameUI) {
		this.gameUI = gameUI;
	}

	/**
	 * Ensures the game UI is set only once if it hasn't been initialized already.
	 * This method prevents overriding an existing UI, maintaining a single
	 * consistent interface throughout the game.
	 *
	 * @param ui The GameUI instance to check and set if not already set.
	 */
	public void checkAndSetGameUI(GameUI ui) {
		if (this.gameUI == null) {
			setGameUI(ui);
		}
	}

	/**
	 * Initializes traffic by creating an AnimationTimer that periodically spawns
	 * cars.
	 */
	public void createTraffic() {
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (lastSpawnTime == 0 || (now - lastSpawnTime) >= spawnInterval) {
					if (Math.random() <= 0.8) {
						spawnCar();
					}
					lastSpawnTime = now; // Update last spawn time
				}
			}
		};
		timer.start();
	}

	/**
	 * Updates the state of the game, checking traffic lights and handling
	 * collisions.
	 */
	public void update() {

		List<Car> carsCopy = new ArrayList<>(cars);
		for (Car car : carsCopy) {
			checkCarProximity(car);
			for (MapElement element : new ArrayList<>(getElements())) {
				if (element instanceof TrafficLight) {
					TrafficLight light = (TrafficLight) element;
					car.checkTrafficLight(light);
				}
			}
		}

		checkCollisions(); // Check and handle collisions

		// Check win condition
		if (carsArrived >= winCount) {
			endGame(true); // true for win
		} else if (crashes >= maxCrashCount) {
			endGame(false); // false for loss
		}
	}

	/**
	 * Ends the game with a win or loss.
	 * 
	 * @param won true if the player won, false if lost.
	 */
	private void endGame(boolean won) {
		Platform.runLater(() -> gameUI.displayEndGameScreen(won));
		timer.stop(); // Stop the game updates
		for (Car car : cars) {
			cars.remove(car);
			gameUI.removeCarFromPane(car);
		}
	}

	/**
	 * Checks for collisions between cars and removes any cars that have collided.
	 */
	private void checkCollisions() {
		List<Car> toRemove = new ArrayList<>();
		for (int i = 0; i < cars.size(); i++) {
			for (int j = i + 1; j < cars.size(); j++) {
				if (cars.get(i).getCarShape().getBoundsInParent()
						.intersects(cars.get(j).getCarShape().getBoundsInParent())) {
					if (!toRemove.contains(cars.get(i))) {
						toRemove.add(cars.get(i));
					}
					if (!toRemove.contains(cars.get(j))) {
						toRemove.add(cars.get(j));
					}
					crashes++; // Increment crash count
				}
			}
		}
		cars.removeAll(toRemove); // Remove collided cars after iteration
		toRemove.forEach(car -> gameUI.removeCarFromPane(car)); // Update UI after removing cars
	}

	/**
	 * Handles the completion of a car's journey by removing it from the UI and the
	 * game's car list. It also increments the counter for cars that have
	 * successfully completed their paths.
	 *
	 * @param car The car that has completed its journey.
	 */
	public void carCompleted(Car car) {
		if (gameUI != null) {
			gameUI.removeCarFromPane(car); // Remove from UI
			cars.remove(car); // Remove from the car list
			carsArrived++; // Increment the score for cars that have completed their path
		} else {
			System.out.println("GameUI is null, cannot remove car from pane. Check initialization.");
		}
	}

	/**
	 * Checks the proximity of a given car to all cars ahead of it on the same path.
	 * If a car ahead is stopped at a red traffic light the current car will also
	 * stop. If the way is clear, the current car will continue moving.
	 *
	 * @param currentCar The car whose proximity to other cars is being checked.
	 */
	private void checkCarProximity(Car currentCar) {
		int mama = cars.indexOf(currentCar);
		for (int i = 0; i < mama; i++) {
			if (isOnSamePath(currentCar, cars.get(i))) {
				if (currentCar.isBehind(cars.get(i)) && cars.get(i).isStopped()) {
					currentCar.stopCar();
				} else {
					currentCar.startCar();
					currentCar.setStopped(false);

				}
			}
		}
	}

	/**
	 * Determines if two cars are on the same path. This can be used to manage
	 * interactions such as stopping due to a traffic light or other cars.
	 *
	 * @param car1 The first car to compare.
	 * @param car2 The second car to compare.
	 * @return true if both cars are on the same path, otherwise false.
	 */
	private boolean isOnSamePath(Car car1, Car car2) {
		return car1.getPath() == car2.getPath();
	}

	/**
	 * Spawns a car on a randomly selected path from the available paths.
	 */
	private void spawnCar() {
		if (paths.isEmpty()) {
			System.out.println("No paths available to spawn cars.");
			return;
		}
		List<Integer> keys = new ArrayList<>(paths.keySet());
		Random random = new Random();
		int randomKeyIndex = random.nextInt(keys.size());
		int pathKey = keys.get(randomKeyIndex);
		Path path = paths.get(pathKey);
		Car newCar = new Car(path);
		cars.add(newCar);
		gameUI.addCarToPane(newCar); // Add the new car to the game UI
	}

	/**
	 * Singleton accessor for the Game instance. This method ensures that only one
	 * instance of the Game class is created throughout the application. If an
	 * instance already exists, it returns that existing instance. If no instance
	 * exists, it creates a new one using the specified grid dimensions.
	 *
	 * @param gridWidth  the width of the game grid, used to initialize the game
	 *                   instance if one does not exist.
	 * @param gridHeight the height of the game grid, used to initialize the game
	 *                   instance if one does not exist.
	 * @return the single instance of the Game class.
	 */
	public static Game getInstance(double gridWidth, double gridHeight) {
		if (instance == null) {
			if (instance == null) {
				instance = new Game(gridWidth, gridHeight);
			}
		}
		return instance;
	}

	/**
	 * Sets the dimensions of the game grid. This method updates the grid width and
	 * height of the game.
	 *
	 * @param gridWidth  the new width of the game grid.
	 * @param gridHeight the new height of the game grid.
	 */
	public void setGridSize(double gridWidth, double gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
	}

	public List<Car> getCars() {
		return cars; // Returns the current list of cars
	}

	public void setCellSize(double cellSize) {
		this.cellSize = cellSize;
	}

	/**
	 * Sets the conditions under which the game is considered won or lost. This
	 * method specifies the number of cars that must arrive at their destinations to
	 * win the game and the maximum number of crashes allowed before the game is
	 * considered lost.
	 *
	 * @param winCount   the number of cars that need to successfully complete their
	 *                   paths to win the game.
	 * @param crashCount the maximum number of crashes allowed before the game is
	 *                   lost.
	 */
	public void setWinConditions(int winCount, int crashCount) {
		this.winCount = winCount;
		this.maxCrashCount = crashCount;
	}

	/**
	 * Adds a new map element to the game. This method is used to populate the
	 * game's environment with various objects that can interact or be interacted
	 * with, such as buildings, cars, or roads.
	 *
	 * @param element the MapElement object to be added to the game's list of
	 *                elements.
	 */
	public void addElement(MapElement element) {
		elements.add(element);
	}

	/**
	 * Retrieves a list of all map elements currently present in the game. This
	 * includes any object that can be part of the game's environment, providing
	 * easy access for rendering, interaction, and manipulation within the game
	 * logic.
	 *
	 * @return a list of MapElement objects currently in the game.
	 */
	public List<MapElement> getElements() {
		return elements;
	}

	/**
	 * Adds a new path to the game with a specific index. This method stores the
	 * path in a map where the key is the index and the value is the path itself.
	 * This allows for easy access and management of multiple paths within the game
	 * environment.
	 *
	 * @param index the unique identifier for the path.
	 * @param path  the Path object to be added to the game.
	 */
	public void addPath(int index, Path path) {
		paths.put(index, path);
	}

	public Path getPath(int index) {
		return paths.get(index);
	}

	/**
	 * Retrieves a map of all paths stored within the game. Each path is associated
	 * with an integer index which acts as a unique identifier. This map allows for
	 * easy access to specific paths based on their index, facilitating operations
	 * like updating and rendering..
	 *
	 * @return a map where each key is an integer index and each value is a Path
	 *         object.
	 */
	public Map<Integer, Path> getPaths() {
		return paths;
	}

	public double getGridWidth() {
		return gridWidth;
	}

	public double getGridHeight() {
		return gridHeight;
	}

	public double getCellSize() {
		return cellSize;
	}

	public int getCrashes() {
		return crashes;
	}

	public int getCarsArrived() {
		return carsArrived;
	}

	public int getWinCount() {
		return winCount;
	}

	public int getMaxCrashCount() {
		return maxCrashCount;
	}

}
