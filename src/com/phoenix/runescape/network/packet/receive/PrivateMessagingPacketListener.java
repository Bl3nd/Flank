package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;

/**
 * The packet opcodes which this listener implementation handles.
 */
@IncomingPacketOpcode ({ 188, 215, 133, 74, 126})

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the private messaging packet which allows two players to send
 * messages back and forth over the server's network.
 */
public class PrivateMessagingPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {

		/*
		 * Enlists a specified player as a friend.
		 */
		case 188:
			long name = packet.getBuffer().getLong();
			player.getPrivateMessaging().addFriendToList(name);
			break;

		/*
		 * Removes a specified player as a friend.
		 */
		case 215:
			name = packet.getBuffer().getLong();
			player.getPrivateMessaging().getFriends().remove(name);
			break;

		/*
		 * Enlists a specified player as an ignore.
		 */
		case 133:
			name = packet.getBuffer().getLong();
			player.getPrivateMessaging().addIgnoreToList(name);
			break;
			
		/*
		 * Removes a specified player as an ignore.
		 */
		case 74:
			name = packet.getBuffer().getLong();
			player.getPrivateMessaging().getIgnores().remove(name);
			break;
			
		/*
		 * Dispatches a private message over the server's network.
		 * This message is broken down into an array of bytes and re-constructed
		 * on the other end. The name is read as a long and run through an algorithm to
		 * reverse obfuscate the actual name context.
		 */
		case 126:
			name = packet.getBuffer().getLong();
			int messageSize = packet.getLength() - 8;
			byte message[] = packet.readBytes(messageSize);
			player.getPrivateMessaging().sendPrivateMessage(player, name, message, messageSize);
			break;
		}
	}
}