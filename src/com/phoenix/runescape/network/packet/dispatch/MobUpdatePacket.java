package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;
import java.util.Iterator;

import com.phoenix.runescape.World;
import com.phoenix.runescape.mobile.mob.Mob;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the outgoing packet for the non-player-character updating procedure.
 */
public final class MobUpdatePacket extends OutgoingPacket {

	/**
	 * The default class constructor.
	 */
	public MobUpdatePacket() {
		super(65);
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer packet = new AsynchronousBuffer(ByteBuffer.allocate(16384));
		AsynchronousBuffer update = new AsynchronousBuffer(ByteBuffer.allocate(8192));
		packet.writeVariableShortPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		packet.startBitAccess();
		packet.writeBits(8, player.getLocalMobs().size());
		for (Iterator<Mob> iterator = player.getLocalMobs().iterator(); iterator.hasNext();) {
			Mob mob = iterator.next();
			if (mob.getPosition().isWithinDistance(player.getPosition(), 15)  && World.getInstance().getMobs().containsKey(mob.getIndex())) {
				mob.getListener().updateMovement(mob, packet);
				if (mob.isUpdateRequired()) {
					mob.getListener().updateBlock(mob, update);
				}
			} else {
				iterator.remove();
				packet.writeBits(1, 1);
				packet.writeBits(2, 3);
			}
		}
		for (Mob mob : World.getInstance().getMobs().values()) {
			if (mob == null || player.getLocalMobs().contains(mob)) {
				continue;
			}
			if (mob.getPosition().isWithinDistance(player.getPosition(), 15)) {
				player.getLocalMobs().add(mob);
				/*
				 * When a new non-player-character is added into a player's local list a default
				 * update flag must be enlisted to collect the extra byte written in the update block.
				 * If this flag is not registered the pay-load will be incorrect when sent to the client
				 * resulting in the disconnection of anyone in the radius of this non-player-character.
				 */
				mob.getUpdateFlags().add("registered-in-local-players");
				packet.writeBits(14, mob.getIndex());
				packet.writeBits(5, mob.getPosition().getY() - player.getPosition().getY());
				packet.writeBits(5, mob.getPosition().getX() - player.getPosition().getX());
				packet.writeBits(1, 1);
				packet.writeBits(12, mob.getIdentityIndex());
				packet.writeBits(1, 1);
				if (mob.isUpdateRequired()) {
					mob.getListener().updateBlock(mob, update);
				}
			}
		}
		if (update.getBuffer().position() > 0) {
			packet.writeBits(14, 16383);
			packet.startByteAcces();
			packet.writeBytes(update.getBuffer());
		} else {
			packet.startByteAcces();
		}
		packet.finishVariableShortPacketHeader();
		return packet;
	}
}