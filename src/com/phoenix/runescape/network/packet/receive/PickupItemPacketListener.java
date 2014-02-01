package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;
import com.phoenix.runescape.pulse.PulseScheduler;
import com.phoenix.runescape.pulse.impl.InteractionDistancePulse;
import com.phoenix.runescape.pulse.impl.InteractionDistancePulse.InteractionType;

/**
 * The packet opcode which this listener implementation handles.
 */
@IncomingPacketOpcode( 236 )

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the action of removing an item from the equipment collection.
 */
public final class PickupItemPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		int clickY = packet.readLittleEndianShort();
		int clickIndex = packet.getBuffer().getShort();
		int clickX = packet.readLittleEndianShort();
		
		player.putAttribute("click-y", clickY);
		player.putAttribute("click-index", clickIndex);
		player.putAttribute("click-x", clickX);
		
		if (player.hasAttribute("banking")) {
			return;
		}
		
		if (!player.getInventoryContainer().canHoldItem(player.getAttributeAsInteger("click-index"))) {
			player.send(new ChatBoxMessagePacket("You don't have the required inventory space to hold this item."));
			return;
		}
		
		PulseScheduler.getInstance().register(new InteractionDistancePulse(player, InteractionType.RETREIVE_ITEM));
	}
}