package com.phoenix.runescape.mobile.player.skill;

import com.phoenix.runescape.mobile.player.Player;

/**
 * @author Dylan Vicchiarelli
 * 
 * The abstract base class for all skilling actions.
 */
public abstract class SkillingAction {
	
	/**
	 * Executes a skilling action.
	 * 
	 * @param player The player executing the action.
	 */
	public abstract void performSkillingAction(Player player);
	
	/**
	 * Stops a skilling action.
	 * 
	 * @param player The owner of the action being canceled.
	 */
	public abstract void stopSkillingAction(Player player);
}