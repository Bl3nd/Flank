package com.phoenix.runescape.item.floor;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.mobile.player.Player;

/**
 * @author Dylan Vicchiarelli
 *
 * Represents an item that has been dropped.
 */
public final class FloorItem {

	/**
	 * The position of the dropped item on the global palate.
	 */
	private final Coordinate position;

	/**
	 * The identity index of the dropped item.
	 */
	private final int index;

	/**
	 * The amount of the dropped item.
	 */
	private final int amount;

	/**
	 * The amount of ticks the dropped item has performed.
	 */
	private int ticks;

	/**
	 * The player who dropped the item.
	 */
	private final Player player;

	/**
	 * The default class constructor.
	 * 
	 * @param index The identity index of the dropped item.
	 * 
	 * @param ticks The amount of ticks the dropped item has performed.
	 * 
	 * @param player The player who dropped the item.
	 * 
	 * @param position The position of the dropped item on the global palate.
	 */
	public FloorItem(int index, int amount, Player player, Coordinate position) {
		this.index = index;
		this.amount = amount;
		this.position = position;
		this.player = player;
		this.ticks = 0;
	}

	/**
	 * Returns the amount of ticks a dropped item has performed.
	 * 
	 * @return The returned amount of ticks.
	 */
	public int getTicks() {
		
		return ticks;
	}

	/**
	 * Modifies the amount of ticks a dropped item has performed.
	 * 
	 * @param ticks The new modification.
	 */
	public void setTicks(int ticks) {
		
		this.ticks = ticks;
	}

	/**
	 * Returns the identity index of a dropped item item.
	 * 
	 * @return The returned identity index.
	 */
	public int getIndex() {
		
		return index;
	}

	/**
	 * Returns the position of the dropped item on the global palate.
	 * 
	 * @return The returned position.
	 */
	public Coordinate getPosition() {
		
		return position;
	}

	/**
	 * Returns the collect amount of the dropped item.
	 * 
	 * @return The returned amount.
	 */
	public int getAmount() {
		
		return amount;
	}

	/**
	 * Returns the player that dropped the item.
	 * 
	 * @return The returned player.
	 */
	public Player getPlayer() {
		
		return player;
	}
}