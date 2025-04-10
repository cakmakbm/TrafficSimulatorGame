package application;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * Represents a static building element within the game. Buildings can have
 * different types and rotations, serving as decorative elements within the game
 * grid.
 */
public class Building extends MapElement {
	private int type;
	private int rotation;
	private Color color;
	private Color strokeColor;
	private static final Color[] colors = { Color.MEDIUMPURPLE, Color.ORANGE, Color.LIME, Color.GOLD }; // Building
																										// colors
	private static final Color[] strokeColors = { Color.PURPLE, Color.DARKORANGE, Color.LIMEGREEN, Color.GOLDENROD }; // Edge
																														// colors

	/**
	 * Represents a building within the game environment. Buildings are static
	 * structures defined by their type, rotation, and color.
	 *
	 * @param type       The type of building, which determines its architectural
	 *                   style.
	 * @param rotation   The rotation angle of the building, affecting its
	 *                   orientation on the map.
	 * @param colorIndex The index for the building's color and stroke from
	 *                   predefined arrays.
	 * @param x          The x-coordinate of the building's position.
	 * @param y          The y-coordinate of the building's position.
	 */
	public Building(int type, int rotation, int colorIndex, double x, double y) {
		super(x, y);
		this.type = type;
		this.rotation = rotation;
		this.color = colors[colorIndex];
		this.strokeColor = strokeColors[colorIndex];
	}

	/**
	 * Draws the building on the game pane based on its type. The method switches
	 * between different architectural styles that are predefined in the methods
	 * drawBuilding0, drawBuilding1, and drawBuilding2.
	 *
	 * @param pane The pane on which the building will be drawn.
	 */
	@Override
	public void draw(Pane pane) {
		switch (type) {
		case 0:
			drawBuilding0(pane);
			break;
		case 1:
			drawBuilding1(pane);
			break;
		case 2:
			drawBuilding2(pane);
			break;
		default:
			break;
		}
	}

	/**
	 * Draws a rectangular building with rounded corners, representing the default
	 * architectural style. The building's orientation and position are adjusted
	 * based on its rotation.
	 *
	 * @param pane The pane on which the building is drawn.
	 */
	private void drawBuilding0(Pane pane) {
		Rotate rotate = null;
		Group buildingGroup = new Group();
		// Creates the land on which the building will be placed
		Rectangle land = new Rectangle(x - 5, y - 5, (2.0 * cellSize) + 10, (3.0 * cellSize) + 10);
		// Main building rectangle
		Rectangle building = new Rectangle(x + 10, y + 10, (2.0 * cellSize) - 20, (2.0 * cellSize) - 20);
		building.setFill(color);
		building.setArcHeight(35);
		building.setArcWidth(35);
		building.setStroke(strokeColor);
		building.setStrokeWidth(5);
		land.setArcHeight(30);
		land.setArcWidth(30);
		land.setStroke(Color.DODGERBLUE);
		land.setStrokeWidth(2);
		land.setFill(Color.LAVENDER);
		buildingGroup.getChildren().addAll(land, building);
		// Handle rotation of the building
		if (rotation == 0 || rotation == 180) {
			rotate = new Rotate(-rotation, (x - 5) + ((2.0 * cellSize) + 10) / 2,
					(y - 5) + ((3.0 * cellSize) + 10) / 2);
		} else {
			rotate = new Rotate(-rotation, (x - 5) + ((2.0 * cellSize) + 10) / 2,
					(y - 33) + ((3.0 * cellSize) + 10) / 2);
		}
		buildingGroup.getTransforms().add(rotate);
		pane.getChildren().add(buildingGroup);
	}

	/**
	 * Draws a circular building with a landscaped border, highlighting an
	 * alternative architectural style.
	 *
	 * @param pane The pane on which the building is drawn.
	 */
	private void drawBuilding1(Pane pane) {
		Rotate rotate = null;
		Group buildingGroup = new Group();
		// Land for the circular building
		Rectangle land = new Rectangle(x - 5, y - 5, (2 * cellSize) + 10, (3 * cellSize) + 10);
		// Circular building
		Circle building = new Circle(x + cellSize, y + cellSize, cellSize - 10, color);
		building.setFill(color);
		building.setStroke(strokeColor);
		building.setStrokeWidth(5);
		land.setArcHeight(30);
		land.setArcWidth(30);
		land.setStroke(Color.DODGERBLUE);
		land.setStrokeWidth(3);
		land.setFill(Color.LAVENDER);
		buildingGroup.getChildren().addAll(land, building);
		// Handle rotation
		if (rotation == 0 || rotation == 180) {
			rotate = new Rotate(-rotation, (x - 5) + ((2.0 * cellSize) + 10) / 2,
					(y - 5) + ((3.0 * cellSize) + 10) / 2);
		} else {
			rotate = new Rotate(-rotation, (x + 20) + ((2.0 * cellSize) + 10) / 2,
					(y - 5) + ((3.0 * cellSize) + 10) / 2);
		}
		buildingGroup.getTransforms().add(rotate);
		pane.getChildren().add(buildingGroup);
	}

	/**
	 * Draws a simple square building, representing a minimalist architectural style.
	 *
	 * @param pane The pane on which the building is drawn.
	 */
	private void drawBuilding2(Pane pane) {
		// Simple rectangular building
		Rectangle building = new Rectangle(x - 5, y - 5, cellSize + 10, cellSize + 10);
		building.setFill(color);
		building.setStroke(strokeColor);
		building.setStrokeWidth(5);
		building.setArcHeight(30);
		building.setArcWidth(30);
		pane.getChildren().add(building);
	}

}
