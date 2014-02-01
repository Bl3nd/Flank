package com.phoenix.runescape.mobile.mob;

import com.phoenix.runescape.network.AsynchronousBuffer;

/**
 * @author Dylan Vicchiarelli
 * 
 * An abstract listener implementation to perform the actual event operations for
 * the non-player-character's updating procedure.
 */
public final class MobUpdateListenerEvent extends MobUpdateListener {

	@Override
	public void updateMovement(Mob character, AsynchronousBuffer packet) {
		if (character.getWalkingDirection() == -1) {
			if (character.isUpdateRequired()) {
				packet.writeBits(1, 1);
				packet.writeBits(2, 0);
			} else {
				packet.writeBits(1, 0);
			}
		} else {
			packet.writeBits(1, 1);
			packet.writeBits(2, 1);
			packet.writeBits(3, character.getWalkingDirection());
			packet.writeBits(1, 1);
		}
	}

	@Override
	public void updateBlock(Mob character, AsynchronousBuffer update) {
		
		update.writeByte(0);
	}
}