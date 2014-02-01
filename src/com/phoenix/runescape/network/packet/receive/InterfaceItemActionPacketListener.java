package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.item.container.BankingInterfaceContainer.BankingInterfaceComposer;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;

/**
 * The packet opcodes which this listener implementation handles.
 */
@IncomingPacketOpcode ({ 145, 43, 117, 129 })

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the action of operating items displayed on an interface.
 */
public final class InterfaceItemActionPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {
		
		case 145:
			int widgetIndex = packet.readAdditionalShort();
			int itemSlot = packet.readAdditionalShort();
			int itemIndex = packet.readAdditionalShort();
			
			switch (widgetIndex) {
			
			case 1688:
				player.getInventoryContainer().addItem(new Item(itemIndex, player.getEquipmentContainer().getItems()[itemSlot].getAmount()));
				player.getEquipmentContainer().removeItemFromSlot(itemSlot,  player.getEquipmentContainer().getItems()[itemSlot].getAmount());
				player.getEquipmentContainer().refreshContainer();
				EquipmentPacketListener.weaponUpdate(player);
				player.getUpdateFlags().add("appearance");
				break;
				
			case 5064:
				BankingInterfaceComposer.depositItem(player, new Item(itemIndex, 1), itemSlot);
				break;
				
			case 5382:
				BankingInterfaceComposer.withdrawItem(player, 1, itemSlot);
				break;
				
			}
			break;
			
		case 117:
			widgetIndex = packet.readLittleEndianShortAddition();
			itemIndex = packet.readLittleEndianShortAddition();
			itemSlot = packet.readLittleEndianShort();
			
			switch (widgetIndex) {
			
			case 5064:
				BankingInterfaceComposer.depositItem(player, new Item(itemIndex, 5), itemSlot);
				break;
				
			case 5382:
				BankingInterfaceComposer.withdrawItem(player, 5, itemSlot);
				break;
			}
			break;
			
		case 43:
			widgetIndex = packet.readLittleEndianShort();
			itemIndex = packet.readAdditionalShort();
			itemSlot = packet.readAdditionalShort();
			
			switch (widgetIndex) {
			
			case 5064:
				BankingInterfaceComposer.depositItem(player, new Item(itemIndex, 10), itemSlot);
				break;
				
			case 5382:
				BankingInterfaceComposer.withdrawItem(player, 10, itemSlot);
				break;
			}
			break;
			
		case 129:
			itemSlot = packet.readAdditionalShort();
			widgetIndex = packet.getBuffer().getShort();
			itemIndex = packet.readAdditionalShort();
		
			switch (widgetIndex) {
			
			case 5064:
				BankingInterfaceComposer.depositItem(player, new Item(itemIndex, player.getInventoryContainer().getCumulativeQuantity(itemIndex)), itemSlot);
				break;
				
			case 5382:
				BankingInterfaceComposer.withdrawItem(player, player.getBankingContainer().getCumulativeQuantity(itemIndex), itemSlot);
				break;
			}
			break;
		}
	}
}
