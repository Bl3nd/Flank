package com.phoenix.runescape.mobile.player.skill;

import com.phoenix.runescape.mobile.player.Player;

/**
 * @author Dylan Vicchiarelli
 *
 * A static coordinator the performing on cancellation of skilling
 * actions.
 */
public final class SkillingActionCoordinator {
	
	/**
	 * The singleton pattern implementation to prevent direct static
	 * access.
	 */
	private static SkillingActionCoordinator singleton;

	/**
	 * Returns the singleton pattern implementation.
	 * 
	 * @return The returned singleton pattern implementation.
	 */
	public static SkillingActionCoordinator getSingleton() {
		
		if (singleton == null) {
			
			singleton = new SkillingActionCoordinator();
		}
		
		return singleton;
	}
	
	/**
	 * Starts and registers a skilling action for a player.
	 * 
	 * @param player The player.
	 * 
	 * @param action The action to be registered.
	 */
	public final void startSkillingAction(Player player, SkillingAction action) {
		
		player.setSkilling(action);
		
		player.getSkilling().performSkillingAction(player);
	}
	
	/**
	 * Cancels and removes a skilling action for a player.
	 * 
	 * @param player The player who's action is being canceled.
	 */
	public final void stopSkillingAction(Player player) {
		
		player.getSkilling().stopSkillingAction(player);
		
		player.setSkilling(null);
	}
}