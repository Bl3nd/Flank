package com.phoenix.runescape.network.packet.receive;

import java.nio.ByteBuffer;

import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.item.container.BankingInterfaceContainer.BankingInterfaceComposer;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * The packet opcodes which this listener implementation handles.
 */
@IncomingPacketOpcode ({ 135, 208 })

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the entering of specific numerals in a chat-box interface.
 */
public final class EnterSetAmountPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {
		
		case 135:
			displayEnterAmountInterface(player, packet);
			break;
			
		case 208:
			confirmEnterAmountInterface(player, packet);
			break;
		}
	}
	
	/**
	 * Displays the interface which allows a player to enter a specific numeral.
	 * 
	 * @param player The player entering the numeral.
	 * 
	 * @param packet The packet conviction.
	 */
	public static final void displayEnterAmountInterface(Player player, IncomingPacket packet) {
		player.putAttribute("enter-x-slot", (int) packet.readLittleEndianShort());
		player.putAttribute("enter-x-interface", (int) packet.readAdditionalShort());
		player.putAttribute("enter-x-index", (int) packet.readLittleEndianShort());
		dispatchEnterAmountPacket(player);
	}
	
	/**
	 * Dispatches the packet for displaying the enter amount chat-box interface.
	 * 
	 * @param player The player dispatching the packet.
	 */
	public static final void dispatchEnterAmountPacket(Player player) {
		player.send(new OutgoingPacket(27) {

			@Override
			public AsynchronousBuffer dispatch(Player player) {
				final AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocateDirect(2));
				
				buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
				return buffer;
			}
		});
	}
	
	/**
	 * Handles the confirmation of the entered numeral in the chat-box.
	 * 
	 * @param player The player entering the numeral.
	 * 
	 * @param packet The packet conviction.
	 */
	private final void confirmEnterAmountInterface(Player player, IncomingPacket packet) {
		
		final int amount = packet.getBuffer().getInt();
		
		if (player.hasAttribute("spawn-item")) {
			
			player.getInventoryContainer().addItem(new Item(amount, 1));
			
			player.removeAttribute("spawn-item");
			
			return;
		}
		
		switch (player.getAttributeAsInteger("enter-x-interface")) {
		
		case 5064:
			BankingInterfaceComposer.depositItem(player, new Item(player.getAttributeAsInteger("enter-x-index"), amount), player.getAttributeAsInteger("enter-x-slot"));
			break;
			
		case 5382:
			BankingInterfaceComposer.withdrawItem(player, amount, player.getAttributeAsInteger("enter-x-slot"));
			break;
		}
	}
}