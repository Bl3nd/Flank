package com.phoenix.runescape.item;

import com.phoenix.runescape.mobile.player.Player;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the containment of items on different interfaces.
 */
public abstract class ItemContainer {

	/**
	 * The items in the player's inventory.
	 */
	private Item[] items;

	/**
	 * The player the container belongs to.
	 */
	private final Player player;

	/**
	 * The capacity of the container.
	 */
	private final int capacity;

	/**
	 * Handles the refreshment of the container.
	 */
	public abstract void refreshContainer();

	/**
	 * Handles the addition of an item to the container.
	 * 
	 * @param item The item addition.
	 */
	public abstract void addItem(Item item);

	/**
	 * Handles the removal of an item from the container.
	 * 
	 * @param index The index of the item.
	 * 
	 * @param amount The amount to be removed.
	 */
	public abstract void removeItem(int index, int amount);

	/**
	 * The default class constructor.
	 * 
	 * @param capacity The capacity of the container.
	 * 
	 * @param player The player.
	 */
	public ItemContainer(int capacity, Player player) {
		this.player = player;
		this.capacity = capacity;
		this.items = new Item[capacity];
	}

	/**
	 * Returns the items in the container.
	 * 
	 * @return The returned item compaction.
	 */
	public final Item[] getItems() {

		return items;
	}

	/**
	 * Returns the owner of the container.
	 * 
	 * @return The returned player.
	 */
	public final Player getPlayer() {

		return player;
	}

	/**
	 * Returns the capacity of the container.
	 * 
	 * @return The returned capacity.
	 */
	public final int getCapacity() {

		return capacity;
	}

	/**
	 * Returns the amount of free space in the container.
	 * 
	 * @return The amount of free space calculated.
	 */
	public final int getFreeSlots() {
		int count = 0;

		for (int i = 0; i < getCapacity(); i ++) {
			if (getItems()[i] == null) {
				count ++;
			}
		}
		return count;
	}

	/**
	 * Performs a check to see if a player has room to receive an item.
	 * 
	 * @param index The index of the item. This is required to take into account the factor of
	 * if the item can be stacked and the player already has a set stack in the desired container.
	 * 
	 * @return The result of the operation.
	 */
	public final boolean canHoldItem(int index) {

		if (getFreeSlots() > 0) {
			return true;
		}

		if (getPlayer().getInventoryContainer().containsItem(index) && ItemDefinition.getDefinitions()[index].isStackable()) {
			return true;
		}

		return false;
	}

	/**
	 * Performs a check to see if the container contains the specified item.
	 * 
	 * @param index The index of the item being checked.
	 * 
	 * @return The result of the check in boolean primitive.
	 */
	public final boolean containsItem(int index) {
		for (int i = 0; i < getCapacity(); i ++) {

			if (getItems()[i] == null) {
				continue;
			}

			if (getItems()[i].getIndex() == index) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds an item to a specified container slot.
	 * 
	 * @param slot The slot of interest.
	 * 
	 * @param item The item to be added.
	 */
	public final void set(int slot, Item item) {
		getItems()[slot] = item;
		refreshContainer();
	}

	/**
	 * Switches the index slots of two items.
	 * 
	 * @param first The first index slot to switch.
	 * 
	 * @param second The second index slot to switch.
	 */
	public final void swapItems(int first, int second) {
		Item temporary = getItems()[first];

		set(first, getItems()[second]);
		set(second, temporary);

		refreshContainer();
	}

	/**
	 * Returns the cumulative quantity of all items with a specific index 
	 * in the container.
	 * 
	 * @param index The index to key for.
	 * 
	 * @return The result of the operation
	 */
	public final int getCumulativeQuantity(int index) {
		int accumulator = 0;
		for (int i = 0; i < getCapacity(); i ++) {

			if (getItems()[i] != null && getItems()[i].getIndex() == index) {

				if (ItemDefinition.getDefinitions()[getItems()[i].getIndex()].isStackable()) {

					return getItems()[i].getAmount();
				} else {

					accumulator += getItems()[i].getAmount();
				}
			}
		}
		return accumulator;
	}

	/**
	 * Removes a set amount of an item from a container slot.
	 * 
	 * @param slot The slot of the item being removed.
	 * 
	 * @param amount The amount of the item to be removed.
	 */
	public final void removeItemFromSlot(int slot, int amount) {
		if (getItems()[slot] != null) {
			if (ItemDefinition.getDefinitions()[getItems()[slot].getIndex()].isStackable()) {
				if (getItems()[slot].getAmount() > amount) {
					getItems()[slot].setAmount(getItems()[slot].getAmount() - amount);
				} else {
					getItems()[slot] = new Item(-1, 0);
				}
			} else {
				getItems()[slot] = new Item(-1, 0);
			}
			if (getItems()[slot].getIndex() == -1) {
				getItems()[slot] = null;
			}
			refreshContainer();
		}
	}

	/**
	 * Returns a collection of items with all of the nulled values removed.
	 * 
	 * @return The result of the operation, the non-nulled values returned.
	 */
	public final Item[] getSimplifiedItemCollection() {
		Item[] collection = new Item[getCapacity()];

		for (int i = 0; i < collection.length; i ++) {

			if (getItems()[i] != null) {

				collection[i] = getItems()[i];
			}
		}
		return collection;
	}

	/**
	 * Fills in any nulled item slots by shifting the pointers back
	 * by until the nulled slots are eliminated.
	 */
	public void correctItemSlots() {
		final Item[] priorItems = getItems();

		items = new Item[getCapacity()];

		int shiftedIndex = 0;

		for (int i = 0; i < getItems().length; i ++) {

			if (priorItems[i] != null) {
				getItems()[shiftedIndex] = priorItems[i];
				shiftedIndex ++;
			}
		}
	}
}