package com.phoenix.runescape.mobile.player;

import java.util.ArrayList;
import java.util.List;

import com.phoenix.runescape.World;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;
import com.phoenix.runescape.network.packet.dispatch.PrivateMessagePacket;
import com.phoenix.runescape.network.packet.dispatch.PrivateMessagingFriendPacket;
import com.phoenix.runescape.network.packet.dispatch.PrivateMessagingServerPacket;
import com.phoenix.utilities.Utilities;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the internal infrastructure of the private messaging system.
 */
public final class PrivateMessaging {
	
	/**
	 * The player instance.
	 */
	private final Player player;
	
	/**
	 * The index of the last message sent.
	 */
	private static int lastMessageIndex = 1;
	
	/**
	 * The friends the player has added.
	 */
	private final List<Long> friends = new ArrayList<Long>(200);

	/**
	 * The ignores the player has added.
	 */
	private final List<Long> ignores = new ArrayList<Long>(100);
	
	/**
	 * The default class constructor.
	 * 
	 * @param player The player instance.
	 */
	public PrivateMessaging(Player player) {
		this.player = player;
	}
	
	/**
	 * Returns the player instance.
	 * 
	 * @return The returned player instance.
	 */
	public final Player getPlayer() {
		
		return player;
	}

	/**
	 * Returns the index of the last message sent.
	 * 
	 * @return The returned index.
	 */
	public final int getLastMessageIndex() {
		
		return lastMessageIndex ++;
	}

	/**
	 * Returns a collection of the player's friends.
	 * 
	 * @return The returned collection.
	 */
	public final List<Long> getFriends() {
		
		return friends;
	}

	/**
	 * Returns a collection of the player's ignores.
	 * 
	 * @return The returned collection.
	 */
	public final List<Long> getIgnores() {
		
		return ignores;
	}
	
	/**
	 * Displays the name and status of the friends the player has added.
	 * 
	 * This is populated upon login before the private messaging system is set-up.
	 */
	public final void displayFriends() {
		getPlayer().send(new PrivateMessagingServerPacket(2));
		
		for (Long friend : getFriends()) {
			
			getPlayer().send(new PrivateMessagingFriendPacket(friend, checkOnlineState(friend)));
		}
	}
	
	/**
	 * When a player is registered into the virtual world this method is called.
	 * This informs all other players in the virtual world that a new player has logged in.
	 */
	public final void register() {
		for (Player players : World.getInstance().getPlayers().values()) {
			
			if (players != null) {
				
				getPlayer().getPrivateMessaging().performRegistration(players);
			}
		}
	}

	/**
	 * Once the player has been registered into the private messaging system the system then
	 * loops through all online players to check if any of them have the new player added as a 
	 * friend. If they do, they are informed that the new player is now online.
	 * 
	 * @param other The other player to be informed.
	 */
	private final void performRegistration(Player other) {
		if (other.getPrivateMessaging().getFriends().contains(Utilities.convertStringToLong(getPlayer().getAccountName()))) {
			
			other.send(new PrivateMessagingFriendPacket(Utilities.convertStringToLong(getPlayer().getAccountName()), checkOnlineState(Utilities.convertStringToLong(getPlayer().getAccountName()))));
		}
	}

	/**
	 * When a player is removed from the virtual world this method is called. This method 
	 * informs all other players in the virtual world that this player has logged out.
	 */
	public final void remove() {
		for (Player players : World.getInstance().getPlayers().values()) {
			
			if (players != null) {
				
				players.getPrivateMessaging().performRemoval(getPlayer());
			}
		}
	}

	/**
	 * Once a player has been removed from the private messaging system the system then
	 * loops through all online players to check if any of them have the removed player added as a 
	 * friend. If they do, they are informed that the player is no longer online.
	 * 
	 * @param other The other player to be informed.
	 */
	private final void performRemoval(Player other) {
		if (getPlayer().getPrivateMessaging().getFriends().contains(Utilities.convertStringToLong(other.getAccountName()))) {
			
			getPlayer().send(new PrivateMessagingFriendPacket(Utilities.convertStringToLong(other.getAccountName()), 0));
		}
	}
	
	/**
	 * Dispatches a private message to a specified player.
	 * 
	 * @param sender The dispatcher.
	 *  
	 * @param receiver The name of the receiver.
	 * 
	 * @param message The message broken down into an array of bytes.
	 * 
	 * @param messageSize The message size derived from the length.
	 */
	public final void sendPrivateMessage(Player sender, long receiver, byte[] message, int messageSize) {
		for (Player reception : World.getInstance().getPlayers().values()) {
			
			if (Utilities.convertStringToLong(reception.getAccountName()) == receiver) {
				
				reception.send(new PrivateMessagePacket(Utilities.convertStringToLong(sender.getAccountName()), (int) sender.getAttributeAsInteger("player-rights"), messageSize, message));
			}
		}
	}
	
	/**
	 * Checks to see if a specified player is online or off-line.
	 * 
	 * @param friend The player being checked.
	 * 
	 * @return The result of the operation.
	 */
	private final int checkOnlineState(long friend) {
		for (Player players : World.getInstance().getPlayers().values()) {
			
			if (players != null) {
				
				if (Utilities.convertStringToLong(players.getAccountName()) == friend) {
					
					return 1;
				}
			}
		}
		return 0;
	}
	
	/**
	 * Adds a player to the instanced player's friend list.
	 * 
	 * @param name The name of the player to add.
	 */
	public final void addFriendToList(long name) {
		if (getFriends().size() >= 200) {
			
			getPlayer().send(new ChatBoxMessagePacket("Your Friends list is currently full."));
			
			return;
		}
		if (getFriends().contains(name)) {
			
			getPlayer().send(new ChatBoxMessagePacket("This player is already on your Friends list."));
			
			return;
		}
		getFriends().add(name);
		
		getPlayer().send(new PrivateMessagingFriendPacket(name, checkOnlineState(name)));
	}
	
	/**
	 * Adds a player to the instanced player's ignore list.
	 * 
	 * @param name The name of the player to add.
	 */
	public final void addIgnoreToList(long name) {
		if (getFriends().size() >= 100) {
			
			getPlayer().send(new ChatBoxMessagePacket("Your Ignore list is currently full."));
			
			return;
		}
		if (getFriends().contains(name)) {
			
			getPlayer().send(new ChatBoxMessagePacket("This player is already on your Ignore list."));
			
			return;
		}
		getIgnores().add(name);
	}
}