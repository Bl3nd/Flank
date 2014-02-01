package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the distribution of a private message over the server's network.
 */
public final class PrivateMessagePacket extends OutgoingPacket {

	/**
	 * The name of the message receiver in long format.
	 */
	private final long receiverName;
	
	/**
	 * The rights of the player. The message size derived directly from it's length.
	 */
	private final int rights, messageSize;
	
	/**
	 * The message broken down into an array of bytes to be reconstructed on the other end.
	 */
	private final byte[] message;
	
	/**
	 * The default class constructor.
	 * 
	 * @param receiverName The receiver name.
	 * 
	 * @param rights The receiver rights.
	 * 
	 * @param messageSize The message size
	 * 
	 * @param message The message components.
	 */
	public PrivateMessagePacket(long receiverName, int rights, int messageSize, byte[] message) {
		super(196);
		this.receiverName = receiverName;
		this.rights = rights;
		this.messageSize = messageSize;
		this.message = message;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(messageSize + 15));
		buffer.writeVariableBytePacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeLong(receiverName);
		buffer.writeBigInteger(player.getPrivateMessaging().getLastMessageIndex());
		buffer.writeByte(rights);
		buffer.getBuffer().put(message, 0, messageSize);
		buffer.finishVariableBytePacketHeader();
		return buffer;
	}
}