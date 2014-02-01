package com.phoenix.runescape.item.floor;

import java.util.HashSet;
import java.util.Set;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.World;
import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.dispatch.DisplayFloorItemPacket;
import com.phoenix.runescape.network.packet.dispatch.RemoveFloorItemPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the operation of floor items.
 */
public final class FloorItems {

	/**
	 * The class instance to avoid direct static access.
	 */
	private static final FloorItems instance = new FloorItems();

	/**
	 * A collection of all the dropped items in the world.
	 */
	private Set<FloorItem> floorItems = new HashSet<FloorItem>();

	/**
	 * An encapsulation of the class instance.
	 * 
	 * @return The returned encapsulated instance.
	 */
	public static FloorItems getInstance() {
		
		return instance;
	}

	/**
	 * Returns an encapsulation of the floor item collection.
	 * 
	 * @return The returned encapsulated collection.
	 */
	public Set<FloorItem> getFloorItems() {
		
		return floorItems;
	}

	/**
	 * Handles the action of displaying a floor item.
	 * 
	 * @param player The player who is receiving the display.
	 * 
	 * @param item The item to be displayed.
	 */
	public void displayItem(Player player, FloorItem item) {
		
		player.send(new DisplayFloorItemPacket(item));
	}

	/**
	 * Handles the action of removing a floor item.
	 * 
	 * @param player The player who is receiving the removal.
	 * 
	 * @param item The item to be removed.
	 */
	public void removeItem(Player player, FloorItem item) {
		
		player.send(new RemoveFloorItemPacket(item));
	}

	/**
	 * Handles the action of retrieving a dropped item.
	 * 
	 * @param player The retriever of the dropped item.
	 * 
	 * @param index The index of the item being retrieved
	 * 
	 * @param position The position of the item on the global coordinate palate.
	 */
	public void pickupItem(Player player, int index, Coordinate position) {
		for (FloorItem floorItem : getFloorItems()) {
			
			if (position.getX() == floorItem.getPosition().getX() && position.getY() == floorItem.getPosition().getY() && position.getPlane() == floorItem.getPosition().getPlane() && index == floorItem.getIndex()) {
				
				player.getInventoryContainer().addItem(new Item(floorItem.getIndex(), floorItem.getAmount()));
				
				getFloorItems().remove(floorItem);
				
				for (final Player other : World.getInstance().getPlayers().values()) {
					
					if (other.getPosition().isWithinDistance(floorItem.getPosition(), 15)) {
						
						removeItem(other, floorItem);
					}
				}
				break;
			}
		}
	}
}