package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * The outgoing packet for printing a message in a player's chat-box.
 */
public final class ChatBoxMessagePacket extends OutgoingPacket {

	/**
	 * The message to be printed in the chat-box.
	 */
	private final String message;

	/**
	 * The default class constructor.
	 * 
	 * @param message The message to be printed.
	 */
	public ChatBoxMessagePacket(String message) {
		super(253);
		this.message = message;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(message.length() + 3));
		buffer.writeVariableBytePacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeString(message);
		buffer.finishVariableBytePacketHeader();
		return buffer;
	}
}