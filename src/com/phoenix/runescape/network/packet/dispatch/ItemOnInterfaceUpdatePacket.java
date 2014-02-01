package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the displaying of an item on an interface. This packet is different from
 * {@link ItemInterfaceUpdatePacket} and has explicit option requirements.
 */
public final class ItemOnInterfaceUpdatePacket extends OutgoingPacket {

	/**
	 * The index of the widget, the zoom percentage, and the item model.
	 */
	private final int index, zoom, model;
	
	/**
	 * The default class constructor.
	 * 
	 * @param index The index of the widget.
	 * 
	 * @param zoom The zoom percentage of the displayed model.
	 * 
	 * @param model The item model.
	 */
	public ItemOnInterfaceUpdatePacket(int index, int zoom, int model) {
		super(246);
		this.index = index;
		this.zoom = zoom;
		this.model = model;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(7));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeLittleEndianShort(index);
		buffer.writeShort(zoom);
		buffer.writeShort(model);
		return buffer;
	}
}