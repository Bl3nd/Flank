package com.phoenix.runescape.item;

import java.io.DataInputStream;
import java.io.FileInputStream;

import com.phoenix.utilities.Utilities;

/**
 * @author Dylan Vicchiarelli
 * 
 * Handles the fixed extraction and loading of the #317 item definitions.
 */
public class ItemDefinition {

	/**
	 * The static instantiation of the loaded definitions.
	 */
	private static ItemDefinition[] definitions;

	/**
	 * The index of the extracted item definition.
	 */
	private int index;

	/**
	 * The noted index of the extracted item definition.
	 */
	private int notedIndex; 

	/**
	 * The price of the extracted item in the market.
	 */
	private int storePrice; 

	/**
	 * The price of the extracted item when used with low alchemy.
	 */
	private int lowAlchValue; 

	/**
	 * The price of the extracted item when used with high alchemy.
	 */
	private int highAlchValue; 

	/**
	 * The equipment slot of the extracted item.
	 */
	private int equipmentSlot;

	/**
	 * The name of the extracted item.
	 */
	private String itemName;

	/**
	 * The description of the extracted item.
	 */
	private String itemDescription;

	/**
	 * If the extracted item is stack-able.
	 */
	private boolean isStackable;

	/**
	 * If the extracted into index is noted.
	 */
	private boolean isNoted;

	/**
	 * If the extracted item requires both weapon and shield slot.
	 */
	private boolean isTwoHanded;

	/**
	 * The equipment bonuses of the extracted item.
	 */
	private int[] bonus = new int[12];

	/**
	 * Returns the equipment bonuses of the extracted item.
	 * 
	 * @return The returned bonuses.
	 */
	public int[] getBonus() {

		return bonus;
	}

	/**
	 * Modifies the equipment bonuses of the extracted item.
	 * 
	 * @param bonus The new modification.
	 */
	public void setBonus(int[] bonus) {

		this.bonus = bonus;
	}

	/**
	 * Returns the static encapsulation of the loaded item definitions.
	 * 
	 * @return The returned encapsulation.
	 */
	public static ItemDefinition[] getDefinitions() {

		return definitions;
	}

	/**
	 * Modifies the static encapsulation of the loaded item definitions.
	 * 
	 * @param definitions The new modification.
	 */
	public static void setDefinitions(ItemDefinition[] definitions) {

		ItemDefinition.definitions = definitions;
	}

	/**
	 * Returns the name of the extracted item.
	 * 
	 * @return The returned name.
	 */
	public String getItemName() {

		return itemName;
	}

	/**
	 * Modifies the name of the extracted item.
	 * 
	 * @param itemName The new modification.
	 */
	public void setItemName(String itemName) {

		this.itemName = itemName;
	}

	/**
	 * Returns the description of the extracted item.
	 * 
	 * @return The returned description.
	 */
	public String getItemDescription() {

		return itemDescription;
	}

	/**
	 * Modifies the description of the extracted item.
	 * 
	 * @param itemDescription The new modification.
	 */
	public void setItemDescription(String itemDescription) {

		this.itemDescription = itemDescription;
	}

	/**
	 * Returns the index of the extracted item.
	 * 
	 * @return The returned index.
	 */
	public int getIndex() {

		return index;
	}

	/**
	 * Modifies the index of the extracted item.
	 * 
	 * @param index The new modification.
	 */
	public void setIndex(int index) {

		this.index = index;
	}

	/**
	 * Returns if the extracted item is stack-able
	 * 
	 * @return The result.
	 */
	public boolean isStackable() {

		return isStackable;
	}

	/**
	 * Modifies if the extracted item is stack-able.
	 * 
	 * @param isStackable The new modification.
	 */
	public void setStackable(boolean isStackable) {

		this.isStackable = isStackable;
	}

	/**
	 * Returns if the extracted items index is noted.
	 * 
	 * @return The result.
	 */
	public boolean isNoted() {

		return isNoted;
	}

	/**
	 * Modifies if the extracted items index is noted.
	 * 
	 * @param isNoted The new modification.
	 */
	public void setNoted(boolean isNoted) {

		this.isNoted = isNoted;
	}

	/**
	 * Returns if the item requires both weapon and shield slots.
	 * 
	 * @return The result.
	 */
	public boolean isTwoHanded() {

		return isTwoHanded;
	}

	/**
	 * Modifies if the item requires both weapon and shield slots.
	 * 
	 * @param isTwoHanded The new modification.
	 */
	public void setTwoHanded(boolean isTwoHanded) {

		this.isTwoHanded = isTwoHanded;
	}

	/**
	 * Returns the noted index of the item.
	 * 
	 * @return The returned index.
	 */
	public int getNotedIndex() {

		return notedIndex;
	}

	/**
	 * Modifies the noted index of the item.
	 * 
	 * @param notedIndex The new modification.
	 */
	public void setNotedIndex(int notedIndex) {

		this.notedIndex = notedIndex;
	}

	/**
	 * Returns the market price of the item.
	 * 
	 * @return The returned price.
	 */
	public int getStorePrice() {

		return storePrice;
	}

	/**
	 * Modifies the market price of the item.
	 * 
	 * @param storePrice The new modification.
	 */
	public void setStorePrice(int storePrice) {

		this.storePrice = storePrice;
	}

	/**
	 * Returns the low alchemy price of the item.
	 * 
	 * @return The returned price.
	 */
	public int getLowAlchValue() {

		return lowAlchValue;
	}

	/**
	 * Modifies the low alchemy price of the item.
	 * 
	 * @param lowAlchValue The new modification.
	 */
	public void setLowAlchValue(int lowAlchValue) {

		this.lowAlchValue = lowAlchValue;
	}

	/**
	 * Returns the high alchemy price of the item.
	 * 
	 * @return The returned price.
	 */
	public int getHighAlchValue() {

		return highAlchValue;
	}

	/**
	 * Modifies the high alchemy price of the item.
	 * 
	 * @param highAlchValue The new modification.
	 */
	public void setHighAlchValue(int highAlchValue) {

		this.highAlchValue = highAlchValue;
	}

	/**
	 * Returns the equipment slot of the item.
	 * 
	 * @return The returned slot index.
	 */
	public int getEquipmentSlot() {

		return equipmentSlot;
	}

	/**
	 * Modifies the equipment slot of the item.
	 * 
	 * @param equipmentSlot The new modification.
	 */
	public void setEquipmentSlot(int equipmentSlot) {

		this.equipmentSlot = equipmentSlot;
	}

	/**
	 * Parses the item definition file upon start-up.
	 */
	public static void loadItemDefinitions() {
		try {
			int parsedIndex = 0;
			setDefinitions(new ItemDefinition[7956]);
			DataInputStream inputStream = new DataInputStream(new FileInputStream("./data/item_definition/ItemDefinitions.dat"));
			while (inputStream.available() > 0) {
				ItemDefinition itemDefinition = new ItemDefinition();
				itemDefinition.setIndex(inputStream.readShort());
				itemDefinition.setItemName(Utilities.formatInputString(Utilities.readInputString(inputStream)));
				itemDefinition.setItemDescription(Utilities.readInputString(inputStream));
				itemDefinition.setStackable(inputStream.readByte() == 1);
				itemDefinition.setNotedIndex(inputStream.readShort());
				if (itemDefinition.getNotedIndex() != -1) {

					itemDefinition.setNoted(inputStream.readByte() == 1);
				}
				itemDefinition.setStorePrice(inputStream.readInt());
				itemDefinition.setLowAlchValue(inputStream.readInt());
				itemDefinition.setHighAlchValue(inputStream.readInt());
				itemDefinition.setEquipmentSlot(inputStream.readByte());
				if (itemDefinition.getEquipmentSlot() != -1) {
					for (int i = 0; i < itemDefinition.getBonus().length; i++) {

						itemDefinition.getBonus()[i] = inputStream.readByte();
					}
				}
				if (itemDefinition.getItemName().contains("2h") || itemDefinition.getItemName().contains("bow") || itemDefinition.getItemName().contains("Maul")) {

					itemDefinition.setTwoHanded(true);
				}
				getDefinitions()[parsedIndex] = itemDefinition;
				parsedIndex ++;
			}
			inputStream.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}