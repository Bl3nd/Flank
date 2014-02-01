package com.phoenix.runescape.mobile.player;

import com.phoenix.runescape.mobile.EntityUpdateListener;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.dispatch.MobUpdatePacket;
import com.phoenix.runescape.network.packet.dispatch.PlayerUpdatePacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Performs the player updating procedure according to #317 protocol.
 */
public abstract class PlayerUpdateListener extends EntityUpdateListener<Player> {

	/**
	 * Updates the movement of the other players in the world.
	 * 
	 * @param player The player being updated.
	 * 
	 * @param packet The packet transit.
	 */
	public abstract void updateOtherPlayerMovement(Player player, AsynchronousBuffer packet);
	
	/**
	 * Updates the movement of the master player in the world.
	 * 
	 * @param player The master player.
	 * 
	 * @param packet The packet transit.
	 */
	public abstract void updateThisPlayerMovement(Player player, AsynchronousBuffer packet);
	
	/**
	 * Updates the state of the players in the world.
	 * 
	 * @param player The player of interest.
	 * 
	 * @param update The update transit.
	 * 
	 * @param forced If the update was forced.
	 * 
	 * @param chat If a chat update is allowed during this state update.
	 */
	public abstract void updatePlayerState(Player player, AsynchronousBuffer update, boolean forced, boolean chat);

	@Override
	public final void update(Player player) {
		player.send(new PlayerUpdatePacket());
		
		player.send(new MobUpdatePacket());
	}
}