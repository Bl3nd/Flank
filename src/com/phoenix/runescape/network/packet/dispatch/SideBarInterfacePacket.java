package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 * 
 * Adds a side-bar interface onto the player's game-frame.
 */
public final class SideBarInterfacePacket extends OutgoingPacket {

	/**
	 * The form index of the side-bar.
	 */
	private final int form;
	
	/**
	 * The interface context of the side-bar.
	 */
	private final int context;
	
	/**
	 * The default class constructor.
	 * 
	 * @param context The interface context.
	 * 
	 * @param form The form index.
	 */
	public SideBarInterfacePacket(int context, int form) {
		super(71);
		this.form = form;
		this.context = context;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(4));
        buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
        buffer.writeShort(form);
        buffer.writeByte(context + 128);
        return buffer;
	}	
}