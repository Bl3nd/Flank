package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Sends the list updates for the private messaging server.
 */
public final class PrivateMessagingServerPacket extends OutgoingPacket {

	/**
	 * The current state of the server.
	 */
	private final int state;

	/**
	 * The default class constructor.
	 * 
	 * @param state The state of the server.
	 */
	public PrivateMessagingServerPacket(int state) {
		super(221);
		this.state = state;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(2));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeByte(state);
		return buffer;
	}
}