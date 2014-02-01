package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.utilities.Utilities;

/**
 * The packet opcode which this listener implementation handles.
 */
@IncomingPacketOpcode( 214 )

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the action of switching the slots of two items on an interface
 */
public final class ItemTransitionPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		int widget = packet.readLittleEndianShortAddition();
		packet.readNegatedByte();
		int from = packet.readLittleEndianShortAddition();
		int to = packet.readLittleEndianShort();
		
		switch (widget) {
		
		case 3214:
			player.getInventoryContainer().swapItems(from, to);
			break;
			
		case 5382:
			if (player.hasAttribute("swap")) {
				player.getBankingContainer().swapItems(from, to);
			} else {
				Utilities.insert(player.getBankingContainer().getItems(), from, to);
			}
			
			player.getBankingContainer().refreshContainer();
			break;
		}
	}
}