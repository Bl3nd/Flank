package com.phoenix;

import com.phoenix.runescape.World;
import com.phoenix.runescape.item.ItemDefinition;
import com.phoenix.runescape.mobile.mob.Mobs;
import com.phoenix.runescape.network.packet.IncomingPacketRegistration;
import com.phoenix.runescape.object.GameObjectHandler;
import com.phoenix.utilities.ClientText;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the initialization of the #317 revision RuneScape emulator.
 */
public final class Phoenix {

	/**
	 * The entry point for the application.
	 * 
	 * @param commandLineArguments The program's command line arguments.
	 * 
	 * @throws Throwable Thrown if an error occurs during initialization.
	 */
	public static final void main(String... commandLineArguments) throws Throwable {

		/*
		 * Initializes the constructor which populates the incoming
		 * packet collection.
		 */
		new IncomingPacketRegistration();

		/*
		 * Parses an external text file to determine which client text layers
		 * will be overridden.
		 */
		ClientText.getClassInstance().loadDefinitions();

		/*
		 * Parses an external text file to determine which objects have 
		 * constant spawn locations.
		 */
		GameObjectHandler.getSingleton().load();

		/*
		 * Parses an external text file to determine which non-player-characters
		 * have constant spawn locations.
		 */
		Mobs.loadMobSpawns();

		/*
		 * Parses an external database file to load all #317 item definitions into
		 * a usable collection.
		 */
		ItemDefinition.loadItemDefinitions();

		/*
		 * Submits the initial threads which perform game logic processing.
		 * These threads are pushed last because we need to wait until all other 
		 * prior operations are completed before a player is able to connect.
		 */
		World.getInstance().submitInitialThreads();
	}
}