package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.item.floor.FloorItem;
import com.phoenix.runescape.item.floor.FloorItems;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;

/**
 * The packet opcode which this listener implementation handles.
 */
@IncomingPacketOpcode( 87 )

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the action of dropping an item.
 */
public final class DropItemPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		final int index = packet.readAdditionalShort();
		packet.getBuffer().getShort();
		final int indexSlot = packet.readAdditionalShort();
		
		if (player.hasAttribute("banking")) {
			
			return;
		}
		
		if (!player.getInventoryContainer().containsItem(index)) {
			
			return;
		}
		
		final Coordinate coordinate = new Coordinate(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getPlane());
		
		final FloorItem floorItem = new FloorItem(index, player.getInventoryContainer().getItems()[indexSlot].getAmount(), player, coordinate);

		player.getInventoryContainer().removeItemFromSlot(indexSlot, player.getInventoryContainer().getItems()[indexSlot].getAmount());

		FloorItems.getInstance().getFloorItems().add(floorItem);
		
		FloorItems.getInstance().displayItem(player, floorItem);
	}
}