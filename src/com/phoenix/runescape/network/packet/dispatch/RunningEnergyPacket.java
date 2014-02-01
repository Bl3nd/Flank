package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Displays the current energy amount of the player in the settings tab.
 */
public final class RunningEnergyPacket extends OutgoingPacket {

	/**
	 * The default class constructor.
	 */
	public RunningEnergyPacket() {
		super(110);
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(2));
        buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
        buffer.writeByte(100);
        return buffer;
	}
}