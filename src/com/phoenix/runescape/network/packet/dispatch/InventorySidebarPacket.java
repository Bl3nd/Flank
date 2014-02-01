package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the displaying of the inventory interface while another interface
 * is open. This packet is most commonly used for shopping or banking.
 */
public final class InventorySidebarPacket extends OutgoingPacket {

	/**
	 * The main frame widget index.
	 */
	private final int widgetIndex;
	
	/**
	 * The inventory index.
	 */
	private final int inventoryIndex;
	
	/**
	 * The default class constructor.
	 * 
	 * @param widgetIndex The main frame widget index.
	 * 
	 * @param inventoryIndex The inventory index.
	 */
	public InventorySidebarPacket(int widgetIndex, int inventoryIndex) {
		super(248);
		this.widgetIndex = widgetIndex;
		this.inventoryIndex = inventoryIndex;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(5));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeAdditionalShort(widgetIndex);
		buffer.getBuffer().putShort((short) inventoryIndex);
		return buffer;
	}
}