package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.mobile.player.skill.SkillingActionCoordinator;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.runescape.network.packet.dispatch.CloseInterfacePacket;

/**
 * The packet opcodes which this listener implementation handles.
 */
@IncomingPacketOpcode ({ 248, 98, 164 })

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the action of walking or running on the global map palate.
 */
public class MovementPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		if (player.hasAttribute("performing-walk-to-action")) {
			
			player.removeAttribute("performing-walk-to-action");
		}
		if (player.hasAttribute("teleporting")) {
			
			return;
		}
		if (player.hasAttribute("changing-appearance")) {
			
			player.removeAttribute("changing-appearance");
		}
		
		if (player.hasAttribute("banking")) {
			
			player.removeAttribute("banking");
		}
		
		if (player.getSkilling() != null) {
			
			SkillingActionCoordinator.getSingleton().stopSkillingAction(player);
		}
		
		player.send(new CloseInterfacePacket());
		
		int packetLength = packet.getLength();
		
		if (packet.getOpcode() == 248) {
			packetLength -= 14;
		}
		
		final int steps = (packetLength - 5) / 2;
		final int[][] path = new int[steps][2];
		final int targetX = packet.readLittleEndianShortAddition();
		for (int i = 0; i < steps; i++) {
			path[i][0] = packet.getBuffer().get();
			path[i][1] = packet.getBuffer().get();
		}
		final int targetY = packet.readLittleEndianShort();
		player.getMovement().resetMovement();
		player.getMovement().setRunningQueueEnabled(packet.readNegatedByte() == 1);
		player.getMovement().addExternalStep(targetX, targetY);
		for (int i = 0; i < steps; i++) {
			path[i][0] += targetX;
			path[i][1] += targetY;
			player.getMovement().addExternalStep(path[i][0], path[i][1]);
		}
		player.getMovement().finishMovement();
	}
}