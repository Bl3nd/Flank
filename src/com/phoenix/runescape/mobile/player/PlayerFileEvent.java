package com.phoenix.runescape.mobile.player;

import java.io.File;

/**
 * @author Dylan Vicchiarelli
 *
 * An event which handles an action of the player's account file.
 */
public abstract class PlayerFileEvent {
	
	/**
	 * The owner of the file.
	 */
	private final Player player;
	
	/**
	 * The path to the file directory.
	 */
	private final File file;
	
	/**
	 * Performs the parsing action on the file.
	 * 
	 * @return The result of the operation in Boolean primitive.
	 */
	public abstract boolean parseFile();

	/**
	 * The default class constructor.
	 * 
	 * @param player The owner of the file.
	 */
	public PlayerFileEvent(Player player) {
		this.player = player;
		
		this.file = new File("./data/characters/" + getPlayer().getAccountName() + ".json");
	}
	
	/**
	 * Returns the owner of the file.
	 * 
	 * @return The returned owner.
	 */
	public Player getPlayer() {
		
		return player;
	}

	/**
	 * Returns an encapsulated instance of the player's file path.
	 * 
	 * @return The returned instance.
	 */
	public File getFile() {
		
		return file;
	}
}