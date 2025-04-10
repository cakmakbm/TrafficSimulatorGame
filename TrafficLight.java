package application;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * Simulates a traffic light at intersections. It can toggle between green and
 * red states and affects the flow of car traffic. Cars will react to the
 * light's current state to stop or proceed.
 */
public class TrafficLight extends MapElement {
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	private boolean isGreen; // Indicates if the traffic light is green
	private Circle light; // Visual representation of the traffic light
	private Line line;

	/**
	 * Constructs a TrafficLight object with specified start and end coordinates. It
	 * initializes the traffic light as green.
	 */
	public TrafficLight(double startX, double startY, double endX, double endY) {
		super(startX, startY);
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.isGreen = true; // Initializes the light as green

		// Create the visual line element between start and end points
		this.line = new Line(startX, startY, endX, endY);
		this.line.setStrokeWidth(1);
		this.line.setStroke(Color.BLACK);

		// Calculate the center position for the traffic light
		double centerX = (startX + endX) / 2;
		double centerY = (startY + endY) / 2;
		this.light = new Circle(centerX, centerY, 5, isGreen ? Color.GREEN : Color.RED);
		this.light.setOnMouseClicked(this::toggleLight); // Add click event to change light color
	}

	/**
	 * Adds the traffic light visuals to the provided Pane if they are not already
	 * added. Ensures that the light and the line are visible by bringing them to
	 * the front.
	 *
	 * @param pane the Pane where the traffic light will be drawn.
	 */
	public void draw(Pane pane) {
		if (!pane.getChildren().contains(line)) {
			pane.getChildren().add(line);
		}
		if (!pane.getChildren().contains(light)) {
			pane.getChildren().add(light);
		}
		line.toFront();
		light.toFront();
	}

	/**
	 * Toggles the state of the traffic light between green and red when clicked.
	 * This method is triggered by a mouse event.
	 *
	 * @param event The mouse event that triggers this method.
	 */
	public void toggleLight(MouseEvent event) {
		this.isGreen = !isGreen; // Toggle the state of the light
		light.setFill(isGreen ? Color.GREEN : Color.RED); // Update the light's color based on the new state
	}

	/**
	 * Returns the current state of the traffic light.
	 *
	 * @return true if the light is green, false if it is red.
	 */
	public boolean isGreen() {
		return isGreen;
	}

	/**
	 * Calculates and returns the position of the traffic light based on its start
	 * and end coordinates.
	 *
	 * @return A Point2D representing the center point between the start and end
	 *         coordinates.
	 */
	public Point2D getPosition() {
		return new Point2D((startX + endX) / 2, (startY + endY) / 2);
	}
}
