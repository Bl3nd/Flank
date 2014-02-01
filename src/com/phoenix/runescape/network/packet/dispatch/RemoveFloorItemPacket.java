package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.item.floor.FloorItem;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the removal of a floor item from the virtual world.
 */
public final class RemoveFloorItemPacket extends OutgoingPacket {

	/**
	 * The item to be removed from the world.
	 */
	private final FloorItem item;
	
	/**
	 * The default class constructor.
	 * 
	 * @param item The item to be removed from the world.
	 */
	public RemoveFloorItemPacket(FloorItem item) {
		super(156);
		this.item = item;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		player.send(new CreateCoordinatePacket(item.getPosition()));
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(4));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeSubtrahend(0);
		buffer.writeShort(item.getIndex());
		return buffer;
	}
}