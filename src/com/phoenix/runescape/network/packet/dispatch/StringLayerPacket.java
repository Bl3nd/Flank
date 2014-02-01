package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * The outgoing packet for overriding a line of text on an interface.
 */
public final class StringLayerPacket extends OutgoingPacket {

	/**
	 * The string layer to override the widget component.
	 */
	private final String layer;
	
	/**
	 * The index of the widget component to override.
	 */
	private final int widget;
	
	/**
	 * The default class constructor.
	 * 
	 * @param layer The string layer to override the widget component.
	 * 
	 * @param widget The index of the widget component to override.
	 */
	public StringLayerPacket(String layer, int widget) {
		super(126);
		this.layer = layer;
		this.widget = widget;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(layer.length() + 6));
		buffer.writeVariableShortPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeString(layer);
		buffer.writeAdditionalShort(widget);
		buffer.finishVariableShortPacketHeader();
		return buffer;
	}
}