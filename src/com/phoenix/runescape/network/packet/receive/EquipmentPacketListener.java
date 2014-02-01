package com.phoenix.runescape.network.packet.receive;

import java.util.Arrays;

import com.phoenix.runescape.Constants;
import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.item.ItemDefinition;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;
import com.phoenix.runescape.network.packet.dispatch.ItemOnInterfaceUpdatePacket;
import com.phoenix.runescape.network.packet.dispatch.SideBarInterfacePacket;
import com.phoenix.runescape.network.packet.dispatch.StringLayerPacket;

/**
 * The packet opcode which this listener implementation handles.
 */
@IncomingPacketOpcode( 41 )

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the action of adding an item to the equipment collection.
 */
public final class EquipmentPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		int identity = packet.getBuffer().getShort();
		int slot = packet.readAdditionalShort();
		int widget = packet.readAdditionalShort();
		if (player.hasAttribute("banking")) {

			return;
		}
		if (widget == 3214) {
			
			int stackableAmount = 0;
			
			if (ItemDefinition.getDefinitions()[identity].isTwoHanded() && player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_SHIELD] != null) {
				if (player.getInventoryContainer().getFreeSlots() < 1) {
					player.send(new ChatBoxMessagePacket("You don't have the required inventory space to equip this item."));
					return;
				}
			}
			
			if (ItemDefinition.getDefinitions()[identity].isStackable()) {
				stackableAmount = player.getInventoryContainer().getItems()[slot].getAmount();
				player.getInventoryContainer().removeItemFromSlot(slot, player.getInventoryContainer().getItems()[slot].getAmount());
			} else {
				player.getInventoryContainer().removeItemFromSlot(slot, 1);
			}

			if (player.getEquipmentContainer().getItems()[ItemDefinition.getDefinitions()[identity].getEquipmentSlot()] != null && ItemDefinition.getDefinitions()[identity].isStackable()) {
				if (!player.getEquipmentContainer().containsItem(identity)) {
					player.getInventoryContainer().set(slot, new Item(player.getEquipmentContainer().getItems()[ItemDefinition.getDefinitions()[identity].getEquipmentSlot()].getIndex(), player.getEquipmentContainer().getItems()[ItemDefinition.getDefinitions()[identity].getEquipmentSlot()].getAmount()));
				}
			} else if (player.getEquipmentContainer().getItems()[ItemDefinition.getDefinitions()[identity].getEquipmentSlot()] != null){
				player.getInventoryContainer().set(slot, new Item(player.getEquipmentContainer().getItems()[ItemDefinition.getDefinitions()[identity].getEquipmentSlot()].getIndex(), player.getEquipmentContainer().getItems()[ItemDefinition.getDefinitions()[identity].getEquipmentSlot()].getAmount()));
			}
			
			if (ItemDefinition.getDefinitions()[identity].isStackable()) {

				if (player.getEquipmentContainer().containsItem(identity)) {
					
					player.getEquipmentContainer().getItems()[ItemDefinition.getDefinitions()[identity].getEquipmentSlot()].setAmount(player.getEquipmentContainer().getItems()[ItemDefinition.getDefinitions()[identity].getEquipmentSlot()].getAmount() + stackableAmount);
				} else {
					player.getEquipmentContainer().set(ItemDefinition.getDefinitions()[identity].getEquipmentSlot(), new Item(identity, stackableAmount));
				}
				
			} else {
				if (ItemDefinition.getDefinitions()[identity].isTwoHanded() && player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_SHIELD] != null) {
					player.getInventoryContainer().addItem(new Item(player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_SHIELD].getIndex(), 1));
					player.getEquipmentContainer().removeItemFromSlot(Constants.EQUIPMENT_SLOT_SHIELD,  1);
					player.getEquipmentContainer().refreshContainer();
				} else if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON] != null && ItemDefinition.getDefinitions()[player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON].getIndex()].isTwoHanded() && ItemDefinition.getDefinitions()[identity].getEquipmentSlot() == Constants.EQUIPMENT_SLOT_SHIELD) {
					player.getInventoryContainer().addItem(new Item(player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON].getIndex(), 1));
					player.getEquipmentContainer().removeItemFromSlot(Constants.EQUIPMENT_SLOT_WEAPON,  1);
					player.getEquipmentContainer().refreshContainer();
				}
				player.getEquipmentContainer().set(ItemDefinition.getDefinitions()[identity].getEquipmentSlot(), new Item(identity, 1));
			}
			
			player.getEquipmentContainer().refreshContainer();
			
			weaponUpdate(player);
			
			player.getUpdateFlags().add("appearance");
		}
	}
	
	/**
	 * Determines the movement animations for the weapon the player has equipped. This
	 * method also handles the displaying of the weapon's interface.
	 * 
	 * @param player The player who's animations are being determined.
	 */
	public static void weaponUpdate(Player player) {
		displayWeaponsInterface(player);
		if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON] != null) {
			if (ItemDefinition.getDefinitions()[player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON].getIndex()].getItemName().contains("2h")) {
				player.putAttribute("stand-index", 2561);
				player.putAttribute("walk-index", 2562);
				player.putAttribute("run-index", 2563);
				return;
			}
			switch (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON].getIndex()) {

			case 4153:
				player.putAttribute("stand-index", 1662);
				player.putAttribute("walk-index", 1663);
				player.putAttribute("run-index", 1664);
				break;

			case 4151:
				player.putAttribute("stand-index", 0x328);
				player.putAttribute("walk-index", 1660);
				player.putAttribute("run-index", 1661);
				break;

			default:
				player.assignDefaultMovementIndices();
				break;
			}
		} else {
			player.assignDefaultMovementIndices();
		}
	}
	
	/**
	 * Displays the proper interface in correlation to the player's weapon index.
	 * 
	 * @param player The player who's interface is being manipulated.
	 */
	public static void displayWeaponsInterface(Player player) {
		if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON] == null) {
			player.send(new SideBarInterfacePacket(0, 5855));
			player.send(new StringLayerPacket("Unarmed", 5857));
		} else {
			player.send(new SideBarInterfacePacket(0, getWeaponInterface(player)));
			player.send(new StringLayerPacket(ItemDefinition.getDefinitions()[player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON].getIndex()].getItemName(), getWeaponInterface(player) + 3));
			player.send(new ItemOnInterfaceUpdatePacket(getWeaponInterface(player) + 1, 200, player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON].getIndex()));
		}
	}
	
	/**
	 * Determines which interface is in relation to the player's weapon index.
	 * 
	 * @param player The player who's interface is being manipulated.
	 * 
	 * @return The result of the operation. Returned in Integer primitive.
	 */
	public static int getWeaponInterface(Player player) {
        if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON] == null) {
            return 5855;
        }
        for (int i = 0; i < Constants.WEAPON_DATA.length; i ++) {
        	if (ItemDefinition.getDefinitions()[player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON].getIndex()].getItemName().toLowerCase().contains((String) Constants.WEAPON_DATA[i][0])) {
        		return (Integer) Constants.WEAPON_DATA[i][1];
        	}
        }
        return 5855;
	}
	
	/**
	 * If an item is considered to have complete head coverage.
	 * 
	 * @param identity The identity of the item.
	 * 
	 * @return The result of the operation.
	 */
	public static boolean isFullHeadGear(int identity) {
		return Arrays.binarySearch(Constants.FULL_HEAD_GEAR, identity) > 0;
	}
	
	/**
	 * If an item is considered to have complete chest coverage.
	 * 
	 * @param identity The identity of the item.
	 * 
	 * @return The result of the operation.
	 */
	public static boolean isFullChestGear(int identity) {
		return Arrays.binarySearch(Constants.PLATE_BODY, identity) > 0;
	}
}