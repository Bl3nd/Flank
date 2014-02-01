package com.phoenix.runescape.item;

/**
 * @author Dylan Vicchiarelli
 * 
 * Represents an inventory item.
 */
public final class Item {

	/**
	 * The index of the item.
	 */
	private int index;

	/**
	 * The contained amount of the item.
	 */
	private int amount;

	/**
	 * The default class constructor.
	 * 
	 * @param index The index of the item.
	 * 
	 * @param amount The contained amount of the item.
	 */
	public Item(int index, int amount) {
		
		setIndex(index);
		
		setAmount(amount);
	}

	/**
	 * Returns the index of the item.
	 * 
	 * @return The returned index.
	 */
	public final int getIndex() {

		return index;
	}

	/**
	 * Modifies the index of the item.
	 * 
	 * @param index The new modification.
	 */
	public final void setIndex(int index) {

		this.index = index;
	}

	/**
	 * Returns the contained amount of the item.
	 * 
	 * @return The returned amount.
	 */
	public final int getAmount() {

		return amount;
	}

	/**
	 * Modifies the contained amount of the item.
	 * 
	 * @param amount The new modification.
	 */
	public final void setAmount(int amount) {

		this.amount = amount;
	}
}