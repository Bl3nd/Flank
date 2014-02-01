package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;
import com.phoenix.runescape.object.GameObject;

public final class CreateObjectPacket extends OutgoingPacket {

	private final GameObject object;
	
	public CreateObjectPacket(GameObject object) {
		super(151);
		
		this.object = object;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		player.send(new CreateCoordinatePacket(object.getCoordinate()));
		
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(5));
		
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		
		buffer.writeSubtrahend(0);
		
		buffer.writeLittleEndianShort(object.getIndex());
		
		buffer.writeSubtrahend(((object.getIndexType() << 2) + (object.getFace() & 3)));
		
		return buffer;
	}	
}