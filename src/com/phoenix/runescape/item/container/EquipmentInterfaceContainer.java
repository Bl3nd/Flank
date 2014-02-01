package com.phoenix.runescape.item.container;

import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.item.ItemContainer;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.dispatch.ItemInterfaceUpdatePacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the containment of the items worn by the player.
 */
public final class EquipmentInterfaceContainer extends ItemContainer {

	/**
	 * The default class constructor.
	 * 
	 * @param capacity The capacity of the container.
	 * 
	 * @param player The owner of the container.
	 */
	public EquipmentInterfaceContainer(int capacity, Player player) {
		
		super(capacity, player);
	}

	@Override
	public void addItem(Item item) {
		
		refreshContainer();
	}

	@Override
	public void removeItem(int index, int amount) {
		
		refreshContainer();
	}

	@Override
	public void refreshContainer() {
		
		getPlayer().send(new ItemInterfaceUpdatePacket(1688, getItems()));
	}
}