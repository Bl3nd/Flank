package com.phoenix.runescape.mobile.mob;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.World;
import com.phoenix.runescape.mobile.EntityIndex;

/**
 * @author Dylan Vicchiarelli
 * 
 * Provides ease of access utility methods for the manipulation of
 * non-player-characters in the virtual world.
 */
public final class Mobs {
	
	/**
	 * The class instance to avoid direct static access when referencing this
	 * class file.
	 */
	private static final Mobs instance = new Mobs();

	/**
	 * Registers a new mob into the virtual world.
	 * 
	 * @param identity The identity index of the mob.
	 * 
	 * @param coordinate The coordinate of the mob on the global palate.
	 * 
	 * @return The super type which is used for method chaining.
	 */
	public final Mob registerMob(final int identity, final Coordinate coordinate) {
		Mob register = new Mob(identity, EntityIndex.findFreeMobIndex(), coordinate);
		
		World.getInstance().getMobs().put(register.getIndex(), register);
		
		return register;
	}
	
	/**
	 * Performs a light weight parse of a text file containing mob spawning information. This does
	 * not use the JSON library since the parse is performed before the application is online and latency
	 * is not an issue.
	 * 
	 * @throws FileNotFoundException The exception thrown if an error occurs.
	 */
	public static void loadMobSpawns() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("./data/configuration/mob-spawns.txt"));
		
		while (scanner.hasNext()) {
			
			if (scanner.next().equals("[create]")) {
				
				while (scanner.hasNextInt()) {
					
					Mobs.getInstance().registerMob(scanner.nextInt(), new Coordinate(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
				}
			}
		}
		scanner.close();
	}
	
	/**
	 * Returns the encapsulated class instance.
	 * 
	 * @return The returned instance.
	 */
	public static final Mobs getInstance() {
		
		return instance;
	}
}