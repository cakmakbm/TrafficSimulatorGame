package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Berat Metehan Çakmak 150123547
//Eray Hoşavcıoğlu 150121005

/**
 * Handles the loading of game levels from external text files. This class
 * parses the text file to create and place game elements like roads, buildings,
 * paths and traffic lights according to specified configurations.
 */
public class LevelLoader {

	public static void loadLevel(Game game, String filePath) throws FileNotFoundException {

		File file = new File(filePath);
		Scanner scanner = new Scanner(file);

		if (scanner.hasNextLine()) {
			String metadataLine = scanner.nextLine();
			parseMetadata(game, metadataLine);
		}

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			parseLine(game, line);
		}
		scanner.close();
		game.getPaths().forEach((index, path) -> {
		});
	}

	/**
	 * Parses the metadata line from a level configuration file to set up game
	 * parameters such as grid dimensions and win/loss conditions.
	 *
	 * @param game The game instance to configure.
	 * @param line A string containing the metadata for the game configuration.
	 */
	private static void parseMetadata(Game game, String line) {
		String[] tokens = line.split(" ");
		if (tokens.length >= 7) {
			// Grid width and height
			double width = Double.parseDouble(tokens[1]);
			double height = Double.parseDouble(tokens[2]);
			// Grid cell number (x, y)
			int gridCellsX = Integer.parseInt(tokens[3]);
			int gridCellsY = Integer.parseInt(tokens[4]);
			// Set the game win and lose condition.
			int carsToWin = Integer.parseInt(tokens[6]);
			int maxCrashes = Integer.parseInt(tokens[7]);

			game.setGridSize(gridCellsX, gridCellsY);
			game.setWinConditions(carsToWin, maxCrashes);
		}
	}

	/**
	 * Parses individual lines from a level configuration file to add various
	 * elements like buildings, roads, and traffic lights to the game.
	 *
	 * @param game The game instance where elements are added.
	 * @param line A string representing a single line from the configuration file.
	 */
	private static void parseLine(Game game, String line) {
		String[] tokens = line.split(" ");
		String type = tokens[0];

		try {
			switch (type) {
			case "Building":
				if (tokens.length >= 6) {
					int typeIndex = Integer.parseInt(tokens[1]);
					int rotation = Integer.parseInt(tokens[2]);
					int colorIndex = Integer.parseInt(tokens[3]);
					double x = Double.parseDouble(tokens[4]) * game.getCellSize();
					double y = Double.parseDouble(tokens[5]) * game.getCellSize();
					game.addElement(new Building(typeIndex, rotation, colorIndex, x, y));

				} else {
					System.out.println("Invalid Building line format: " + line);
				}
				break;
			case "RoadTile":
				if (tokens.length >= 5) {
					int typeIndex = Integer.parseInt(tokens[1]);
					int rotation = Integer.parseInt(tokens[2]);
					double x = Double.parseDouble(tokens[3]) * game.getCellSize();
					double y = Double.parseDouble(tokens[4]) * game.getCellSize();
					game.addElement(new RoadTile(typeIndex, rotation, x, y));

				} else {
					System.out.println("Invalid RoadTile line format: " + line);
				}
				break;
			case "TrafficLight":
				if (tokens.length >= 5) {
					double startX = Double.parseDouble(tokens[1]);
					double startY = Double.parseDouble(tokens[2]);
					double endX = Double.parseDouble(tokens[3]);
					double endY = Double.parseDouble(tokens[4]);
					game.addElement(new TrafficLight(startX, startY, endX, endY));
				} else {
					System.out.println("Invalid TrafficLight line format: " + line);
				}
				break;
			case "Path":

				handlePath(game, tokens);
				break;
			default:
				System.out.println("Invalid line format: " + line);
				break;
			}
		} catch (NumberFormatException e) {
			System.out.println("Number format error in line: " + line);
		}
	}

	/**
	 * Handles path commands in the level configuration, creating or modifying path
	 * elements for the game.
	 *
	 * @param game   The game instance to modify.
	 * @param tokens Array of string tokens representing the path commands and
	 *               parameters.
	 */
	private static void handlePath(Game game, String[] tokens) {
		if (tokens.length < 5)
			return; // Must have at least 5 tokens: Path, index, command, x, y

		int pathIndex = Integer.parseInt(tokens[1]);
		Path path = game.getPath(pathIndex);
		if (path == null) {
			path = new Path();
			game.addPath(pathIndex, path);
		}

		String command = tokens[2];
		double x = Double.parseDouble(tokens[3]);
		double y = Double.parseDouble(tokens[4]);
		if ("MoveTo".equals(command)) {
			path.moveTo(x, y);
		} else if ("LineTo".equals(command)) {
			path.lineTo(x, y);
		}
	}

}