package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 * 
 * Handles the updating of items on specified interface.
 */
public final class ItemInterfaceUpdatePacket extends OutgoingPacket {

	/**
	 * The index of the interface.
	 */
	private final int interfaceIndex;

	/**
	 * The collection of contained items.
	 */
	private final Item[] items;

	/**
	 * The default class constructor.
	 * 
	 * @param interfaceIndex The interface index.
	 * 
	 * @param items The item collection.
	 */
	public ItemInterfaceUpdatePacket(int interfaceIndex, Item[] items) {
		super(53);
		this.items = items;
		this.interfaceIndex = interfaceIndex;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(2048));
		buffer.writeVariableShortPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeShort(interfaceIndex);
		buffer.writeShort(items.length);
		for (Item item : items) {
			if (item != null) {
				if (item.getAmount() > 254) {
					buffer.writeByte(255);
					buffer.writeBigInteger(item.getAmount());
				} else {
					buffer.writeByte(item.getAmount());
				}
				buffer.writeLittleEndianShortAddition(item.getIndex() + 1);
			} else {
				buffer.writeByte(0);
				buffer.writeLittleEndianShortAddition(0);
			}
		}
		buffer.finishVariableShortPacketHeader();
		return buffer;
	}
}