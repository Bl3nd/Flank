package com.phoenix.runescape.network.packet;

import com.phoenix.runescape.mobile.player.Player;

/**
 * @author Dylan Vicchiarelli
 *
 * Listens for any and all incoming packet requests sent from the RuneScape client.
 */
public abstract class IncomingPacketListener {

	/**
	 * Handles the operation of the incoming packet request.
	 * 
	 * @param player The player association.
	 * 
	 * @param packet The incoming packet request.
	 */
	public abstract void operate(Player player, IncomingPacket packet);
}