package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Closes a player's view of the virtual world and sends them back to the 
 * main RuneScape login screen. This packet is not responsible for the actual
 * removal of a player, just the modification of the player's screen.
 */
public final class LogOutPacket extends OutgoingPacket {

	/**
	 * The default class constructor.
	 */
	public LogOutPacket() {
		super(109);
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(1));
        buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
        return buffer;
	}
}