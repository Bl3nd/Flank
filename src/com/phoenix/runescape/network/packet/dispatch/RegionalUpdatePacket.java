package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * The outgoing packet which updates the regional view of the player.
 */
public final class RegionalUpdatePacket extends OutgoingPacket {

	/**
	 * The default class constructor.
	 */
	public RegionalUpdatePacket() {
		super(73);
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(5));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeAdditionalShort(player.getPosition().getRegionalX() + 6);
		buffer.writeShort(player.getPosition().getRegionalY() + 6);
		player.getLastPosition().setPosition(player.getPosition());
		return buffer;
	}
}