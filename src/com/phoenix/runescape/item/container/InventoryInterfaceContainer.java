package com.phoenix.runescape.item.container;

import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.item.ItemContainer;
import com.phoenix.runescape.item.ItemDefinition;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;
import com.phoenix.runescape.network.packet.dispatch.ItemInterfaceUpdatePacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the containment of the items in a player's inventory.
 */
public final class InventoryInterfaceContainer extends ItemContainer {

	/**
	 * The default class constructor.
	 * 
	 * @param capacity The capacity of the inventory container.
	 * 
	 * @param player The owner of the container.
	 */
	public InventoryInterfaceContainer(int capacity, Player player) {
		
		super(capacity, player);
	}

	@Override
	public void addItem(Item item) {
		if (!canHoldItem(item.getIndex())) {
			getPlayer().send(new ChatBoxMessagePacket("You don't have the required inventory space to hold this item."));
			return;
		}
		for (int slot = 0; slot < getCapacity(); slot ++) {
			if (getItems()[slot] == null) {
				continue;
			}
			if (getItems()[slot].getIndex() == item.getIndex()) {
				if (ItemDefinition.getDefinitions()[item.getIndex()].isStackable()) {
					getItems()[slot].setAmount(getItems()[slot].getAmount() + item.getAmount());
					refreshContainer();
					return;
				}
			}
		}
		if (!ItemDefinition.getDefinitions()[item.getIndex()].isStackable() && item.getAmount() > 1) {
			int itemAmount = item.getAmount();
			for (int amount = 0; amount < itemAmount; amount ++) {
				for (int slot = 0; slot < getCapacity(); slot ++) {
					if (getItems()[slot] == null) {
						getItems()[slot] = item;
						item.setAmount(1);
						break;
					}
				}
			}
			refreshContainer();
			return;
		}
		for (int slot = 0; slot < getCapacity(); slot ++) {
			if (getItems()[slot] == null) {
				getItems()[slot] = item;
				refreshContainer();
				break;	
			}
		}
	}

	@Override
	public void removeItem(int index, int amount) {

		int deleteCount = 0;
		
		for (int slot = 0; slot < getCapacity(); slot ++) {

			if (getItems()[slot] == null) {
				
				continue;
			}

			if (getItems()[slot].getIndex() == index) {

				if (deleteCount == amount) {
					
					break;
				}
				
				if (getItems()[slot].getAmount() > amount && ItemDefinition.getDefinitions()[index].isStackable()) {
					getItems()[slot].setAmount(getItems()[slot].getAmount() - amount);
					refreshContainer();
					break;
				}
				
				getItems()[slot] = new Item(-1, 0);
				getItems()[slot] = null;
				refreshContainer();
				
				deleteCount ++;
			}
		}
	}

	@Override
	public void refreshContainer() {
		
		getPlayer().send(new ItemInterfaceUpdatePacket(3214, getItems()));
	}
}