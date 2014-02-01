package com.phoenix.runescape;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.phoenix.runescape.mobile.mob.Mob;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousNetworking;
import com.phoenix.runescape.pulse.PulseScheduler;
import com.phoenix.runescape.pulse.impl.FloorItemPulse;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the processing and updating of the virtual world.
 */
public final class World implements Runnable {

	/**
	 * A collection of the active players in the virtual world.
	 */
	private final Map<Integer, Player> players = new ConcurrentHashMap<Integer, Player>();

	/**
	 * A collection of the active mobs in the virtual world.
	 */
	private final Map<Integer, Mob> mobs = new ConcurrentHashMap<Integer, Mob>();

	/**
	 * The class instance to avoid direct static access upon reference to this
	 * class file.
	 */
	private static final World instance = new World();

	/**
	 * Returns a collection of the players in the virtual world.
	 * 
	 * @return The returned collection of players.
	 */
	public final Map<Integer, Player> getPlayers() {

		return players;
	}

	/**
	 * Returns the collection of active mobs in the virtual world.
	 * 
	 * @return The returned mob collection.
	 */
	public final Map<Integer, Mob> getMobs() {

		return mobs;
	}

	/**
	 * Returns the encapsulated instance of this class.
	 * 
	 * @return The returned instance.
	 */
	public final static World getInstance() {

		return instance;
	}

	/**
	 * Checks if a player with a specific name is registered in the virtual world.
	 * 
	 * @param accountName The name of the player to check.
	 * 
	 * @return Whether the player is registered in the virtual world or not.
	 */
	public final boolean isLoggedIn(String accountName) {
		for (Player player : getPlayers().values()) {

			if (player != null) {

				if (player.getAccountName().equalsIgnoreCase(accountName)) {

					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Submits the threads which required fixed processing upon launch.
	 * 
	 * @throws IOException The exception thrown if a networking error occurs.
	 * 
	 * @throws InterruptedException The exception thrown if a thread is interrupted.
	 */
	public final void submitInitialThreads() throws IOException, InterruptedException {
		/*
		 * Initializes the main threads backing the application. The networking thread must process at a much greater
		 * rate than the game engine thread to allow for quicker reception of connections and incoming messages. The
		 * RuneScape cycle rate has been confirmed to be 600 milliseconds. The size of the processor will atomically grow
		 * when a new thread is introduced into it's environment. There is no need to keep a variable count of the submitted
		 * threads.
		 */
		ScheduledExecutorService processor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		/*
		 * The networking thread is initially delayed by five seconds to keep all connections in
		 * synchronization with the application. Without this delay a connection can get out of synchronization 
		 * when connecting quickly which will disable the connection from reading incoming packets.
		 */
		processor.scheduleAtFixedRate(new AsynchronousNetworking().bind(Constants.PORT), 5000, 10, TimeUnit.MILLISECONDS);
		/*
		 * There is no delay on these threads, how-ever the fixed processing rate is much higher than that
		 * of the networking thread. RuneScape processes it's attributes at a rate of 600 milliseconds.
		 */
		processor.scheduleAtFixedRate(instance, 0, 600, TimeUnit.MILLISECONDS);
		/*
		 * Registers the floor item event for fixed processing. This operation does not require
		 * the memory dedicated to it's own thread.
		 */
		PulseScheduler.getInstance().register(new FloorItemPulse());
	}

	@Override
	public void run() {

		synchronized(this) {

			for (final Player player : getPlayers().values()) {

				if (player == null) {

					continue;
				}

				player.prepare();
			}
			for (final Mob mob : getMobs().values()) {

				if (mob == null) {

					continue;
				}

				mob.prepare();
			}
			for (final Player player : getPlayers().values()) {

				if (player == null) {

					continue;
				}

				player.getUpdateListener().update(player);
			}
			for (final Player player : getPlayers().values()) {

				if (player == null) {

					continue;
				}

				player.reset();
			}
			for (final Mob mob : getMobs().values()) {

				if (mob == null) {

					continue;
				}

				mob.reset();
			}
		}
	}
}