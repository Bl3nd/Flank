package com.phoenix.runescape.mobile.player.file;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.mobile.player.PlayerFileEvent;

/**
 * @author Dylan Vicchiarelli
 * 
 * Handles the event of saving a player's account file.
 */
public final class PlayerSaveFileEvent extends PlayerFileEvent {

	/**
	 * The default class constructor.
	 * 
	 * @param player The owner of the file being saved.
	 */
	public PlayerSaveFileEvent(Player player) {
		
		super(player);
	}

	@Override
	public boolean parseFile() {
		try {
			/*
			 * When a save is requested if the file for that player can't be located a new 
			 * one is created with the abstract path from the above file instance.
			 */
			if (!getFile().exists()) {
				
				getFile().createNewFile();
			}
			/*
			 * Performs the operation of saving all of the variables which are not
			 * classified as run-time attributes.
			 */
			final Gson builder = new GsonBuilder().setPrettyPrinting().create();
			final JsonObject objectCollection = new JsonObject();
			objectCollection.addProperty("account-name", getPlayer().getAccountName().trim());
			objectCollection.addProperty("account-password", getPlayer().getAccountPassword().trim());
			objectCollection.addProperty("x-position", new Integer(getPlayer().getPosition().getX()));
			objectCollection.addProperty("y-position", new Integer(getPlayer().getPosition().getY()));
		    objectCollection.addProperty("height-plane", new Integer(getPlayer().getPosition().getPlane()));
		    objectCollection.addProperty("gender", new Integer(getPlayer().getGender()));
			objectCollection.addProperty("first-login", new Integer(getPlayer().getFirstLogin()));
		    objectCollection.add("inventory", builder.toJsonTree(getPlayer().getInventoryContainer().getSimplifiedItemCollection()));
		    objectCollection.add("equipment", builder.toJsonTree(getPlayer().getEquipmentContainer().getSimplifiedItemCollection()));
		    objectCollection.add("banking", builder.toJsonTree(getPlayer().getBankingContainer().getSimplifiedItemCollection()));
			objectCollection.add("friends", builder.toJsonTree(getPlayer().getPrivateMessaging().getFriends()));
			objectCollection.add("ignores", builder.toJsonTree(getPlayer().getPrivateMessaging().getIgnores()));
			objectCollection.add("skills", builder.toJsonTree(getPlayer().getSkills().getSkills()));
			objectCollection.add("clothing", builder.toJsonTree(getPlayer().getAppearanceValues()));
			objectCollection.add("clothing-colors", builder.toJsonTree(getPlayer().getColorValues()));
		    FileWriter fileWriter = new FileWriter(getFile());
			fileWriter.write(builder.toJson(objectCollection));
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException exception) {
			
			exception.printStackTrace();
		}
		return true;
	}
}