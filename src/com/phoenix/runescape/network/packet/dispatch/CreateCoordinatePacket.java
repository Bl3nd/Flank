package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the creation of a coordinate location.
 */
public final class CreateCoordinatePacket extends OutgoingPacket {

	/**
	 * The coordinate location to be created.
	 */
	private final Coordinate coordinates;

	/**
	 * The default class constructor.
	 * 
	 * @param coordinates The coordinate location to be created.
	 */
	public CreateCoordinatePacket(Coordinate coordinates) {
		super(85);
		this.coordinates = coordinates;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(3));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeNegatedByte(coordinates.getY() - 8 * player.getLastPosition().getRegionalY());
		buffer.writeNegatedByte(coordinates.getX() - 8 * player.getLastPosition().getRegionalX());
		return buffer;
	}
}