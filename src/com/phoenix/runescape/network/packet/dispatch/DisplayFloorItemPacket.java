package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.item.floor.FloorItem;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handling the creation of a floor item for a player or set of players.
 */
public final class DisplayFloorItemPacket extends OutgoingPacket {

	/**
	 * The item to be displayed.
	 */
	private final FloorItem item;

	/**
	 * The default class constructor.
	 * 
	 * @param item The item to be displayed.
	 */
	public DisplayFloorItemPacket(FloorItem item) {
		super(44);
		this.item = item;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		player.send(new CreateCoordinatePacket(item.getPosition()));
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(6));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeLittleEndianShortAddition(item.getIndex());
		buffer.writeShort(item.getAmount());
		buffer.writeByte(0);
		return buffer;
	}
}