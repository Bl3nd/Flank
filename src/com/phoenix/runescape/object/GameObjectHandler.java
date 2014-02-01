package com.phoenix.runescape.object;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.World;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.dispatch.CreateObjectPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * A utility class for the management of global objects.
 */
public final class GameObjectHandler {
	
	/**
	 * The singleton pattern implementation to prevent direct static
	 * access.
	 */
	private static GameObjectHandler singleton;

	/**
	 * A collection of the globals objects in the virtual world.
	 */
	private final Set<GameObject> objects = new LinkedHashSet<GameObject>();
	
	/**
	 * Returns the singleton pattern implementation.
	 * 
	 * @return The returned pattern implementation.
	 */
	public static final GameObjectHandler getSingleton() {
		
		if (singleton == null) {
			
			singleton = new GameObjectHandler();
		}
		
		return singleton;
	}
	
	/**
	 * Creates a global object for all players within a 30 tile radius.
	 * 
	 * @param object The new object to be created.
	 */
	public final void createObject(GameObject object) {
		
		for (Player players : World.getInstance().getPlayers().values()) {
			
			if (players.getPosition().isWithinDistance(object.getCoordinate(), 30)) {
				
				players.send(new CreateObjectPacket(object));
			}
		}
		objects.add(object);
	}
	
	/**
	 * Loads a collection of objects from an external text file.
	 * 
	 * @throws FileNotFoundException The exception thrown if the file can't be found.
	 */
	public final void load() throws FileNotFoundException {

		final Scanner scanner = new Scanner(new File("./data/configuration/object-spawns.txt"));
		
		while (scanner.hasNext()) {
			
			if (scanner.next().equals("[create]")) {
				
				if (scanner.hasNextInt()) {
					
					final int index = scanner.nextInt();

					final int coordinateX = scanner.nextInt();
					
					final int coordinateY = scanner.nextInt();
					
					final int coordinatePlane = scanner.nextInt();
					
					final int indexType = scanner.nextInt();
					
					final int direction = scanner.nextInt();
					
					objects.add(new GameObject(index, new Coordinate(coordinateX, coordinateY, coordinatePlane), indexType, direction));
				}
			}
		}
		scanner.close();
	}
	
	/**
	 * Returns the global objects in the virtual world.
	 * 
	 * @return The returned global object collection.
	 */
	public final Set<GameObject> getObjects() {
		
		return objects;
	}
}