package application;

import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * Defines the path that cars will follow. This class supports complex paths
 * with multiple segments, providing functionality to move cars smoothly along
 * these paths using MoveTo and LineTo commands.
 */
public class Path {
	private javafx.scene.shape.Path path; // Represents the geometric path
	private double length; // Total length of the path
	private int currentSegment = 0; // Index of the current path segment being processed

	public Path() {
		this.path = new javafx.scene.shape.Path(); // Initialize the Path object
	}

	/**
	 * Sets a new starting point for the path or a new segment within the existing
	 * path.
	 *
	 * @param x X coordinate of the new point.
	 * @param y Y coordinate of the new point.
	 */
	public void moveTo(double x, double y) {
		MoveTo moveTo = new MoveTo(x, y);
		path.getElements().add(moveTo);
		if (path.getElements().size() == 1) {
			this.length = 0; // Reset path length when first move is added
		}
	}

	/**
	 * Adds a line from the last point in the path to a new point, extending the
	 * path.
	 *
	 * @param x X coordinate of the endpoint of the new line.
	 * @param y Y coordinate of the endpoint of the new line.
	 */
	public void lineTo(double x, double y) {
		if (path.getElements().isEmpty()) {
			moveTo(x, y); // Start path here if no initial MoveTo command
			return;
		}

		Point2D lastPoint = getLastPoint();
		LineTo lineTo = new LineTo(x, y);
		path.getElements().add(lineTo);

		Point2D newPoint = new Point2D(x, y);
		this.length += lastPoint.distance(newPoint); // Update path length based on the distance to the new point
	}

	/**
	 * Retrieves the next point in the path, advancing the current segment.
	 * 
	 * @return the next Point2D or the last point if the end is reached
	 */
	public Point2D nextPoint() {
		if (currentSegment < path.getElements().size()) {
			PathElement element = path.getElements().get(currentSegment);
			if (element instanceof MoveTo) {
				MoveTo moveTo = (MoveTo) element;
				currentSegment++;
				return new Point2D(moveTo.getX(), moveTo.getY());
			} else if (element instanceof LineTo) {
				LineTo lineTo = (LineTo) element;
				currentSegment++;
				return new Point2D(lineTo.getX(), lineTo.getY());
			}
		}
		return getLastPoint();
	}

	/**
	 * Retrieves the starting point of the path.
	 * 
	 * @return the first MoveTo Point2D if available, null otherwise
	 */
	public Point2D getStartPoint() {
		for (PathElement element : path.getElements()) {
			if (element instanceof MoveTo) {
				MoveTo moveTo = (MoveTo) element;
				return new Point2D(moveTo.getX(), moveTo.getY());
			}
		}
		return null;
	}

	/**
	 * Retrieves the X coordinate of the start point of the path. If no start point
	 * is defined, returns 0 as a default value.
	 *
	 * @return double representing the X coordinate of the path's starting point.
	 */
	public double getStartX() {
		Point2D startPoint = getStartPoint();
		return startPoint != null ? startPoint.getX() : 0;
	}

	/**
	 * Retrieves the Y coordinate of the start point of the path. If no start point
	 * is defined, returns 0 as a default value.
	 *
	 * @return double representing the Y coordinate of the path's starting point.
	 */
	public double getStartY() {
		Point2D startPoint = getStartPoint();
		return startPoint != null ? startPoint.getY() : 0;
	}

	/**
	 * Retrieves the last point added to the path, either as a MoveTo or LineTo
	 * command. If the path is empty, returns a default point (0,0).
	 *
	 * @return Point2D representing the last point on the path.
	 */
	private Point2D getLastPoint() {
		if (!path.getElements().isEmpty()) {
			PathElement element = path.getElements().get(path.getElements().size() - 1);
			if (element instanceof MoveTo) {
				MoveTo moveTo = (MoveTo) element;
				return new Point2D(moveTo.getX(), moveTo.getY());
			} else if (element instanceof LineTo) {
				LineTo lineTo = (LineTo) element;
				return new Point2D(lineTo.getX(), lineTo.getY());
			}
		}
		return new Point2D(0, 0); // Return a default point if path is empty
	}

	/**
	 * Retrieves the JavaFX Path object which visually represents the sequence of
	 * movements and lines.
	 *
	 * @return javafx.scene.shape.Path containing all the graphical elements of the
	 *         path.
	 */
	public javafx.scene.shape.Path getFXPath() {
		return this.path;
	}

	/**
	 * Determines whether the path has been fully constructed based on the current
	 * segment index.
	 *
	 * @return boolean indicating if the path construction is complete.
	 */
	public boolean isComplete() {
		return currentSegment >= path.getElements().size();
	}

	/**
	 * Calculates the total length of the path based on the distance between
	 * successive points.
	 *
	 * @return double representing the total length of the path.
	 */
	public double getLength() {
		return this.length;
	}

	/**
	 * Prints details about each segment of the path.
	 */
	public void printPathDetails() {
		StringBuilder sb = new StringBuilder("Path Details: ");
		for (PathElement element : path.getElements()) {
			if (element instanceof MoveTo) {
				MoveTo moveTo = (MoveTo) element;
				sb.append(String.format("MoveTo(%.2f, %.2f) ", moveTo.getX(), moveTo.getY()));
			} else if (element instanceof LineTo) {
				LineTo lineTo = (LineTo) element;
				sb.append(String.format("LineTo(%.2f, %.2f) ", lineTo.getX(), lineTo.getY()));
			}
		}
		System.out.println(sb.toString());
	}
}
