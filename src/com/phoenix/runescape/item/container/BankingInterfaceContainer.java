package com.phoenix.runescape.item.container;

import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.item.ItemContainer;
import com.phoenix.runescape.item.ItemDefinition;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;
import com.phoenix.runescape.network.packet.dispatch.ConfigurationPacket;
import com.phoenix.runescape.network.packet.dispatch.InventorySidebarPacket;
import com.phoenix.runescape.network.packet.dispatch.ItemInterfaceUpdatePacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the collection of items in a player's bank account.
 */
public final class BankingInterfaceContainer extends ItemContainer {

	/**
	 * The default class constructor.
	 * 
	 * @param capacity The container's capacity.
	 * 
	 * @param player The owner of the container.
	 */
	public BankingInterfaceContainer(int capacity, Player player) {
		
		super(capacity, player);
	}

	@Override
	public void refreshContainer() {
		getPlayer().send(new ItemInterfaceUpdatePacket(5382, getPlayer().getBankingContainer().getItems()));
		
		getPlayer().send(new ItemInterfaceUpdatePacket(5064, getPlayer().getInventoryContainer().getItems()));
	}

	@Override
	public void addItem(Item item) {
		
		refreshContainer();
	}

	@Override
	public void removeItem(int index, int amount) {
		
		refreshContainer();
	}
	
	/**
	 * @author Dylan Vicchiarelli
	 *
	 * A utility class for the deposition and withdrawal of items in the player's
	 * bank account.
	 */
	public static final class BankingInterfaceComposer {
		
		/**
		 * Performs the deposition of an item.
		 * 
		 * @param player The owner of the container being acted upon.
		 * 
		 * @param item The item being deposited.
		 * 
		 * @param slot The slot the item will be placed in.
		 */
		public static final void depositItem(final Player player, final Item item, final int slot) {
			
			if (player.getBankingContainer().getFreeSlots() == 0) {
				
				player.send(new ChatBoxMessagePacket("There is not enough room in your bank account to deposit this item."));
				
				return;
			}
			
			int amount = item.getAmount();
			
			int index = item.getIndex();
			
			if (amount > player.getInventoryContainer().getCumulativeQuantity(item.getIndex())) {
				
				amount = player.getInventoryContainer().getCumulativeQuantity(item.getIndex());
			}
			
			if (ItemDefinition.getDefinitions()[index].isNoted()) {
				
				index -= 1;
			}

			for (int i = 0; i < player.getBankingContainer().getCapacity(); i ++) {
				
				if (player.getBankingContainer().getItems()[i] == null) {
					
					player.getBankingContainer().set(i, new Item(index, amount));
					
					break;
				}
				
				if (player.getBankingContainer().getItems()[i].getIndex() == index && player.getBankingContainer().getItems()[i] != null) {
					
					player.getBankingContainer().set(i, new Item(index, amount + player.getBankingContainer().getItems()[i].getAmount()));
					
					break;
				}
			}

			if (amount > 1) {
				
				player.getInventoryContainer().removeItem(item.getIndex(), amount);
			} else {
				
				player.getInventoryContainer().removeItemFromSlot(slot, 1);
			}
			
			player.getBankingContainer().refreshContainer();
		}
		
		/**
		 * Performs the withdrawal of an item.
		 * 
		 * @param player The owner of the container being acted upon.
		 * 
		 * @param amount The amount of the item to be withdrew.
		 * 
		 * @param slot The slow the item of focus resides in.
		 */
		public static final void withdrawItem(final Player player, final int amount, final int slot) {
			
			if (!player.getInventoryContainer().canHoldItem(player.getBankingContainer().getItems()[slot].getIndex())) {
				
				player.send(new ChatBoxMessagePacket("There is not enough room in your inventory to withdraw this item."));
				
				return;
			}
			
			int amountCalc = amount;
			
			if (amountCalc > player.getBankingContainer().getCumulativeQuantity(player.getBankingContainer().getItems()[slot].getIndex())) {
				
				amountCalc = player.getBankingContainer().getCumulativeQuantity(player.getBankingContainer().getItems()[slot].getIndex());
			}
			
			if (player.hasAttribute("note")) {
				if (!ItemDefinition.getDefinitions()[player.getBankingContainer().getItems()[slot].getIndex() + 1].isNoted()) {
					
					player.getInventoryContainer().addItem(new Item(player.getBankingContainer().getItems()[slot].getIndex(), amountCalc));
					
					player.send(new ChatBoxMessagePacket("This item cannot be withdraw from your bank account as a note."));
				} else {
					
					player.getInventoryContainer().addItem(new Item(player.getBankingContainer().getItems()[slot].getIndex() + 1, amountCalc));
				}
			} else {
				
				player.getInventoryContainer().addItem(new Item(player.getBankingContainer().getItems()[slot].getIndex(), amountCalc));
			}
			
			for (int i = 0; i < player.getBankingContainer().getCapacity(); i ++) {
				
				if (i == slot && player.getBankingContainer().getItems()[slot] != null) {
					
					if (amountCalc >= player.getBankingContainer().getItems()[slot].getAmount()) {
						
						player.getBankingContainer().getItems()[slot] = new Item(-1, 0);
					}
					
					if (amountCalc < player.getBankingContainer().getItems()[slot].getAmount()) {
						
						player.getBankingContainer().getItems()[slot].setAmount(player.getBankingContainer().getItems()[slot].getAmount() - amountCalc);
					}
					
					if (player.getBankingContainer().getItems()[slot].getIndex() == -1) {
						
						player.getBankingContainer().getItems()[slot] = null;
					}
				}
			}
			
			player.getBankingContainer().correctItemSlots();
			
			player.getBankingContainer().refreshContainer();
		}
		
		/**
		 * Opens the bank account of a player.
		 * 
		 * @param player The player who's account is being opened.
		 */
		public static void openBankAccount(Player player) {
			
			player.putAttribute("banking");
			
			player.send(new InventorySidebarPacket(5292, 5063));
			
			player.send(new ItemInterfaceUpdatePacket(5064, player.getInventoryContainer().getItems()));
			
			player.send(new ItemInterfaceUpdatePacket(5382, player.getBankingContainer().getItems()));
		}
		
		/**
		 * Sends the initial banking configurations when a player is registered into
		 * the virtual world. The configurations include setting the default attributes and
		 * reseting the selection of in widget buttons.
		 * 
		 * @param player The player who's bank is being configured.
		 */
		public static void sendInitialBankingConfigurations(Player player) {
			player.putAttribute("swap");
			
			player.send(new ConfigurationPacket(304, 0));
			
			player.send(new ConfigurationPacket(115, 0));
		}
	}
}
