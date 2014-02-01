package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;

/**
 * The packet opcode which this listener implementation handles.
 */
@IncomingPacketOpcode( 4 )

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the communication packet used for visibility of spoken text.
 */
public final class CommunicationPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		int messageEffects = packet.readSignedSubtrahend();
		int messageColor = packet.readSignedSubtrahend();
		int messageLength = (packet.getLength() - 2);
		byte[] message = packet.readBytesAddition(messageLength);
		player.setChatTextEffects(messageEffects);
		player.setChatTextColor(messageColor);
		player.setChatText(message);
		player.getUpdateFlags().add("chat");
	}
}