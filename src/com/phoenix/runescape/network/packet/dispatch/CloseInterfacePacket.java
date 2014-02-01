package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

public final class CloseInterfacePacket extends OutgoingPacket {

	public CloseInterfacePacket() {
		
		super(219);
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(1));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		return buffer;
	}
}