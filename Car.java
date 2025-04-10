package application;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * Handles the loading of game levels from external text files. This class
 * parses the text file to create and place game elements like roads, buildings,
 * and traffic lights according to specified configurations.
 */
public class Car extends MapElement {
	private static final Interpolator Interpolator = null;
	private PathTransition transition; // Manages the animation of the car along the path
	private Rectangle carShape; // Visual representation of the car
	private boolean isStopped; // Indicates whether the car is currently stopped
	private final double speed = 45.0; // A constant speed value for all cars
	private application.Path path; // A reference to store the path data inside the car object
	private boolean ada = true;
	private static ArrayList<Car> allCars = new ArrayList<>();

	/**
	 * Constructs a car that follows a predefined path.
	 * 
	 * @param path The path object that the car will follow.
	 */
	public Car(Path path) {
		super(path.getStartX(), path.getStartY()); // Initialize at the starting point of the path
		carShape = new Rectangle(path.getStartX(), path.getStartY(), 20, 10);
		carShape.setFill(javafx.scene.paint.Color.BLUE);
		this.path = path;

		// Setup the path transition for the car
		initializeTransition(path, speed);
		isStopped = false;
		allCars.add(this);
	}

	private void initializeTransition(Path path, double speed) {
		this.transition = new PathTransition();
		double pathLength = path.getLength();
		double durationSeconds = pathLength / speed;

		transition.setNode(this.carShape);
		transition.setPath(path.getFXPath());
		transition.setDuration(Duration.seconds(durationSeconds));
		transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		transition.setInterpolator(Interpolator.LINEAR);
		transition.setCycleCount(1);
		transition.setOnFinished(event -> onPathComplete());
		transition.play();
	}

	/**
	 * Checks the status of the nearest traffic light and stops or starts the car
	 * based on the light's color.
	 * 
	 * @param light The traffic light to check against.
	 */

	private void onPathComplete() {
		transition.stop();
		Game.getInstance(15, 15).carCompleted(this);
	}

	public void checkTrafficLight(TrafficLight light) {

		boolean isNear = nearTrafficLight(light);

		// If the traffic light is red and the car is near, stop the car.
		if (!light.isGreen() && isNear) {
			ada = false;
			if (!isStopped) {
				stopCar();
			}
		} else if (light.isGreen() && isStopped && isNear) {
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ae -> {
				ada = true;
			}));

			startCar();
			timeline.play();

			// If the light turns green and the car is near and was stopped, start the car.

		}
	}

	/**
	 * Determines if the car is near a given traffic light.
	 * 
	 * @param light The traffic light to measure distance to.
	 * @return true if the car is within 20 units of the traffic light.
	 */
	private boolean nearTrafficLight(TrafficLight light) {
		// Calculating the current absolute position by adding the original position and
		// the translation.
		double carCurrentX = carShape.getX() + carShape.getTranslateX() + 8;
		double carCurrentY = carShape.getY() + carShape.getTranslateY() + 4;
		Point2D carPos = new Point2D(carCurrentX, carCurrentY);

		Point2D lightPos = light.getPosition();
		double distance = carPos.distance(lightPos);

		return distance < 20;
	}

	@Override
	public void draw(Pane pane) {
		if (!pane.getChildren().contains(carShape)) {
			pane.getChildren().add(carShape);
			// Ensure the car is added to the pane if not already present
		}
		carShape.toFront();
	}

	public Rectangle getCarShape() {
		return carShape;
	}

	public void stopCar() {
		if (!isStopped) {
			transition.pause();
			isStopped = true;
		}
	}

	public void startCar() {
		if (isStopped) {
			transition.play();
			isStopped = false;
		}
	}

	public boolean isBehind(Car otherCar) {

		if (ada) {
			Point2D myPosition = new Point2D(carShape.getX() + carShape.getTranslateX() + 8,
					carShape.getY() + carShape.getTranslateY() + 4);
			Point2D otherPosition = new Point2D(otherCar.carShape.getX() + otherCar.carShape.getTranslateX() + 8,
					otherCar.carShape.getY() + otherCar.carShape.getTranslateY() + 4);
			return myPosition.distance(otherPosition) < 35;

		}
		return false;

	}

	public double getX() {
		return carShape.getX();
	}

	public double getY() {
		return carShape.getY();
	}

	public Path getPath() {
		return this.path;
	}

	public boolean isStopped() {
		return isStopped;
	}

	public void setStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}

}
