package application;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * Represents different types of road tiles in the game, such as straight roads,
 * curved roads, and intersections. This class is responsible for drawing these
 * tiles on the game grid based on their type and orientation.
 */
public class RoadTile extends MapElement {
	private int type;
	private int rotation;

	public RoadTile(int type, int rotation, double x, double y) {
		super(x, y);
		this.type = type;
		this.rotation = rotation;
	}

	/**
	 * Draws the road tile based on its type and rotation, handling different road
	 * shapes and layouts. Utilizes separate helper methods for each road type to
	 * keep the drawing logic organized.
	 *
	 * @param pane the Pane where the road tile will be drawn.
	 */
	@Override
	public void draw(Pane pane) {
		switch (type) {
		case 0:
			drawStraightRoad(pane);
			break;
		case 1:
			drawCurvedRoad(pane);
			break;
		case 2:
			drawIntersectionFour(pane);
			break;
		case 3:
			drawIntersectionThree(pane);
			break;
		default:
			drawStraightRoad(pane); // Default to straight road if type is unknown
			break;
		}
	}

	/**
	 * Draws a straight road section, either horizontal or vertical based on the
	 * rotation.
	 *
	 * @param pane the Pane where this road segment will be drawn.
	 */
	private void drawStraightRoad(Pane pane) {
		Rectangle rect;
		if (rotation == 0 || rotation == 180) {
			// Horizontal road
			rect = new Rectangle(x, y + 4, cellSize, cellSize - 8);
		} else {
			// Vertical road
			rect = new Rectangle(x + 4, y, cellSize - 8, cellSize);
		}
		rect.setFill(Color.GRAY);
		pane.getChildren().add(rect);
	}

	/**
	 * Draws a curved road section. The curve's quadrant is determined by the
	 * rotation property.
	 *
	 * @param pane the Pane where this curved road segment will be drawn.
	 */
	private void drawCurvedRoad(Pane pane) {
		double arcX = 0;
		double arcY = 0;

		// Determine the arc's position based on rotation
		switch (rotation) {
		case 0: // Top right corner
			arcX = x;
			arcY = y + cellSize;
			break;
		case 90: // Top left corner
			arcX = x + cellSize;
			arcY = y + cellSize;
			break;
		case 180: // Bottom left corner
			arcX = x + cellSize;
			arcY = y;
			break;
		case 270: // Bottom right corner
			arcX = x;
			arcY = y;
			break;
		}

		Arc arc = new Arc(arcX, arcY, cellSize - 4, cellSize - 4, rotation, 90);
		arc.setType(ArcType.ROUND);
		Circle circle = new Circle(arcX, arcY, 4);
		Shape road = Shape.subtract(arc, circle);
		road.setFill(Color.GRAY);
		pane.getChildren().add(road);
	}

	/**
	 * Draws a four-way intersection. This method subtracts small rectangles from
	 * each corner of a larger rectangle to create the intersection shape.
	 *
	 * @param pane the Pane where this intersection will be drawn.
	 */
	private void drawIntersectionFour(Pane pane) {
		Rectangle rect = new Rectangle(x, y, cellSize, cellSize);

		// Create a four-way intersection by subtracting small rectangles from each
		// corner
		Shape intersec = Shape.subtract(rect, new Rectangle(x, y, 4, 4)); // Top left corner
		intersec = Shape.subtract(intersec, new Rectangle(x, y + cellSize - 4, 4, 4)); // Bottom left corner
		intersec = Shape.subtract(intersec, new Rectangle(x + cellSize - 4, y, 4, 4)); // Top right corner
		intersec = Shape.subtract(intersec, new Rectangle(x + cellSize - 4, y + cellSize - 4, 4, 4)); // Bottom right
																										// corner
		intersec.setFill(Color.GRAY);
		pane.getChildren().add(intersec);
	}

	/**
	 * Draws a three-way intersection based on the rotation. This method creates the
	 * intersection by subtracting a straight segment from one side of a square.
	 *
	 * @param pane the Pane where this intersection will be drawn.
	 */
	private void drawIntersectionThree(Pane pane) {
		Rectangle rect = new Rectangle(x, y, cellSize, cellSize);
		Shape intersec3 = null;

		// Create a three-way intersection based on the rotation
		switch (rotation) {
		case 0: // Straight on top
			intersec3 = Shape.subtract(rect, new Rectangle(x, y, cellSize, 4));
			intersec3 = Shape.subtract(intersec3, new Rectangle(x, y + cellSize - 4, 4, 4));
			intersec3 = Shape.subtract(intersec3, new Rectangle(x + cellSize - 4, y + cellSize - 4, 4, 4));
			break;
		case 90: // Straight on the right
			intersec3 = Shape.subtract(rect, new Rectangle(x + cellSize - 4, y, 4, cellSize));
			intersec3 = Shape.subtract(intersec3, new Rectangle(x, y, 4, 4));
			intersec3 = Shape.subtract(intersec3, new Rectangle(x, y + cellSize - 4, 4, 4));
			break;
		case 180: // Straight on the bottom
			intersec3 = Shape.subtract(rect, new Rectangle(x, y + cellSize - 4, cellSize, 4));
			intersec3 = Shape.subtract(intersec3, new Rectangle(x + cellSize - 4, y, 4, 4));
			intersec3 = Shape.subtract(intersec3, new Rectangle(x, y, 4, 4));
			break;
		case 270: // Straight on the left
			intersec3 = Shape.subtract(rect, new Rectangle(x, y, 4, cellSize));
			intersec3 = Shape.subtract(intersec3, new Rectangle(x + cellSize - 4, y, 4, 4));
			intersec3 = Shape.subtract(intersec3, new Rectangle(x + cellSize - 4, y + cellSize - 4, 4, 4));
			break;
		}
		intersec3.setFill(Color.GRAY);
		pane.getChildren().add(intersec3);
	}
}
