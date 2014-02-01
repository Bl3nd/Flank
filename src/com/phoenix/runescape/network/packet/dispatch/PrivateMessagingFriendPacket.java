package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Displays a friend's name and their current world in the list of a player.
 */
public final class PrivateMessagingFriendPacket extends OutgoingPacket {

	/**
	 * The name of the player formatted as a long.
	 */
	private final long name;

	/**
	 * The current world of the player.
	 */
	private final int world;

	/**
	 * The default class constructor.
	 * 
	 * @param name The name of the player.
	 * 
	 * @param world The current world of the player.
	 */
	public PrivateMessagingFriendPacket(long name, int world) {
		super(50);
		this.name = name;
		this.world = world;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(10));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeLong(name);
		buffer.writeByte(world == 0 ? 0 : world + 9);
		return buffer;
	}
}