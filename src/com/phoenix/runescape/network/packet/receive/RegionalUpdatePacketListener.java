package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.item.floor.FloorItem;
import com.phoenix.runescape.item.floor.FloorItems;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.runescape.network.packet.dispatch.CreateObjectPacket;
import com.phoenix.runescape.object.GameObject;
import com.phoenix.runescape.object.GameObjectHandler;

/**
 * The packet opcodes which this listener implementation handles.
 */
@IncomingPacketOpcode( { 121,  210 } )

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the updating of certain entities upon region change.
 */
public final class RegionalUpdatePacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		updateGroundItems(player);
		
		updateRegionalObjects(player);
	}
	
	/**
	 * Updates the regional objects for a player upon region change.
	 * 
	 * @param player The player who's world is being updated.
	 */
	public final void updateRegionalObjects(final Player player) {
		for (GameObject object : GameObjectHandler.getSingleton().getObjects()) {
				
			if (player.getPosition().isWithinDistance(object.getCoordinate(), 50)) {
					
				player.send(new CreateObjectPacket(object));
			}
		}
	}
	
	/**
	 * Updates the regional ground items for a player upon region change.
	 * 
	 * @param player The player who's world is being updated.
	 */
	public void updateGroundItems(final Player player) {
		for (FloorItem item : FloorItems.getInstance().getFloorItems()) {

			if (player.getPosition().isWithinDistance(item.getPosition(), 50)) {

				if (item.getPlayer().getAccountName().equals(player.getAccountName()) || item.getTicks() >= 120) {
					FloorItems.getInstance().displayItem(player, item);
				}
			}
		}
	}
}