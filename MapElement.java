package application;

import javafx.scene.layout.Pane;

// Berat Metehan Çakmak 150123547
// Eray Hoşavcıoğlu 150121005

/**
 * Abstract class representing a generic element on the map.
 * This class serves as a base for all drawable elements within the game,
 * providing common properties and enforcing the implementation of a draw method.
 */
public abstract class MapElement {
    protected double x; // The x-coordinate of the element on the map
    protected double y; // The y-coordinate of the element on the map
    protected final double cellSize = 800.0 / 15.0; // Size of each grid cell on the map

    /**
     * Constructs a MapElement with specified coordinates.
     *
     * @param x The x-coordinate of the element.
     * @param y The y-coordinate of the element.
     */
    public MapElement(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Abstract method to draw the element on a provided Pane.
     * Each subclass must implement this method to define its specific drawing behavior.
     *
     * @param pane The pane on which the element is to be drawn.
     */
    public abstract void draw(Pane pane);
}
