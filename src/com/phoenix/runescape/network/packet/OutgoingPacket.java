package com.phoenix.runescape.network.packet;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;

/**
 * @author Dylan Vicchiarelli
 *
 * Represents an outgoing packet.
 */
public abstract class OutgoingPacket {

	/**
	 * The opcode of the packet.
	 */
	private final int opcode;

	/**
	 * Dispatches the packet to the associated channel.
	 * 
	 * @param player The distributor.
	 */
	public abstract AsynchronousBuffer dispatch(Player player);

	/**
	 * The default class constructor.
	 * 
	 * @param opcode The opcode of the packet.
	 */
	public OutgoingPacket(int opcode) {
		this.opcode = opcode;
	}

	/**
	 * Returns the opcode of the packet.
	 * 
	 * @return The returned opcode.
	 */
	public final int getOpcode() {
		return opcode;
	}
}