package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Dispatches the configuration packet. This is used to toggle a multitude
 * of different events in the RuneScape client. One of its many purposes is modifying
 * the selection of a button.
 */
public final class ConfigurationPacket extends OutgoingPacket {

	/**
	 * The numeral index of the configuration.
	 */
	private final int index;

	/**
	 * The value of the configuration.
	 */
	private final int value;

	/**
	 * The default class constructor.
	 * 
	 * @param index The numeral index of the configuration.
	 * 
	 * @param value The value of the configuration.
	 */
	public ConfigurationPacket(int index, int value) {
		super(36);
		this.index = index;
		this.value = value;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(4));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeLittleEndianShort(index);
		buffer.writeByte(value);
		return buffer;
	}
}