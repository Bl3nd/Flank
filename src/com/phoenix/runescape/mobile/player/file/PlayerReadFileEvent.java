package com.phoenix.runescape.mobile.player.file;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.mobile.player.PlayerFileEvent;
import com.phoenix.runescape.mobile.player.skill.SkillSet.Skill;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the event of reading from a player's saved file.
 */
public final class PlayerReadFileEvent extends PlayerFileEvent {

	/**
	 * The default class constructor.
	 * 
	 * @param player The owner of the file that is being read.
	 */
	public PlayerReadFileEvent(Player player) {
		super(player);
		
		if (!getFile().exists()) { 
			
			getPlayer().save(); 
		}
	}

	@Override
	public boolean parseFile() {
		try {
			
			final JsonParser fileParser = new JsonParser();
			final Gson builder = new GsonBuilder().create();
			final Object object = fileParser.parse(new FileReader(getFile()));
			final JsonObject reader = (JsonObject) object;
			final String name = reader.get("account-name").getAsString();
			final String password = reader.get("account-password").getAsString();
			final int gender = reader.get("gender").getAsInt();
			final int firstLogin = reader.get("first-login").getAsInt();
			
			if (!name.equals(getPlayer().getAccountName()) || !password.equals(getPlayer().getAccountPassword())) {
				return false;
			}
			
			getPlayer().setPosition(new Coordinate(reader.get("x-position").getAsInt(), reader.get("y-position").getAsInt(), reader.get("height-plane").getAsInt()));
			
			getPlayer().setGender(gender);
			
			getPlayer().setFirstLogin(firstLogin);
			
			final JsonArray inventory = reader.get("inventory").getAsJsonArray();
			final JsonArray equipment = reader.get("equipment").getAsJsonArray();
			final JsonArray banking = reader.get("banking").getAsJsonArray();
			final JsonArray friends = reader.get("friends").getAsJsonArray();
			final JsonArray ignores = reader.get("ignores").getAsJsonArray();
			final JsonArray skills = reader.get("skills").getAsJsonArray();
			final JsonArray clothes = reader.get("clothing").getAsJsonArray();
			final JsonArray clothesColors = reader.get("clothing-colors").getAsJsonArray();
			final int[] clothing = builder.fromJson(clothes, int[].class);
			final int[] clothingColors = builder.fromJson(clothesColors, int[].class);
			final Item[] inventoryItems = builder.fromJson(inventory, Item[].class);
			final Item[] equipmentItems = builder.fromJson(equipment, Item[].class);
			final Item[] bankingItems = builder.fromJson(banking, Item[].class);
			final Long[] friendsList = builder.fromJson(friends, Long[].class);
			final Long[] ignoresList = builder.fromJson(ignores, Long[].class);
			final Skill[] skillSet = builder.fromJson(skills, Skill[].class);
			
			for (int i = 0; i < inventoryItems.length; i ++) {
				
				getPlayer().getInventoryContainer().getItems()[i] = inventoryItems[i];
			}
			
			for (int i = 0; i < equipmentItems.length; i ++) {
				
				getPlayer().getEquipmentContainer().getItems()[i] = equipmentItems[i];
			}
			
			for (int i = 0; i < bankingItems.length; i ++) {
				
				getPlayer().getBankingContainer().getItems()[i] = bankingItems[i];
			}
			
			for (int i = 0; i < friendsList.length; i ++) {
				
				getPlayer().getPrivateMessaging().getFriends().add(friendsList[i]);
			}
			
			for (int i = 0; i < ignoresList.length; i ++) {
				
				getPlayer().getPrivateMessaging().getIgnores().add(ignoresList[i]);
			}
			
			for (int i = 0; i < 21; i ++) {
				
				getPlayer().getSkills().getSkills()[i] = skillSet[i];
			}
			
			for (int i = 0; i < clothing.length; i ++) {
				
				getPlayer().getAppearanceValues()[i] = clothing[i];
			}
			
			for (int i = 0; i < clothingColors.length; i ++) {
				
				getPlayer().getColorValues()[i] = clothingColors[i];
			}
		} catch (FileNotFoundException exception) {
			
			exception.printStackTrace();
		}
		return true;
	}
}