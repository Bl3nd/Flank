package com.phoenix.runescape.mobile.mob;

import com.phoenix.runescape.mobile.EntityUpdateListener;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;

/**
 * @author Dylan Vicchiarelli
 *  
 * An abstract listener to handle any updating requests for mobs.
 */
public abstract class MobUpdateListener extends EntityUpdateListener<Player> {
	
	/**
	 * Performs the updating for the mob's movement on the global palate.
	 * 
	 * @param character The mob performing the movement.
	 * 
	 * @param packet The packet for dispatching the request.
	 */
	public abstract void updateMovement(Mob character, AsynchronousBuffer packet);
	
	/**
	 * Performs the updating for any update block the mob has flagged.
	 * 
	 * @param character The mob performing the flagging.
	 * 
	 * @param update The update for receiving the request.
	 */
	public abstract void updateBlock(Mob character, AsynchronousBuffer update);
	
	@Override
	public void update(Player player) {
		/*
		 * Leave this method blank as the update packet has already been 
		 * dispatched along-side the player update packet.
		 */
	}
}