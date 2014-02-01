package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;
import java.util.Iterator;

import com.phoenix.runescape.World;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the outgoing packet for the player updating procedure.
 */
public final class PlayerUpdatePacket extends OutgoingPacket {

	/**
	 * The default class constructor.
	 */
	public PlayerUpdatePacket() {
		super(81);
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		if (player.getUpdateFlags().contains("map-region")) {
			player.send(new RegionalUpdatePacket());
		}
		AsynchronousBuffer packet = new AsynchronousBuffer(ByteBuffer.allocate(16384));
		AsynchronousBuffer update = new AsynchronousBuffer(ByteBuffer.allocate(8192));
		packet.writeVariableShortPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		packet.startBitAccess();
		player.getListener().updateThisPlayerMovement(player, packet);
		if (player.isUpdateRequired()) {
			player.getListener().updatePlayerState(player, update, false, true);
		}
		packet.writeBits(8, player.getLocalPlayers().size());
		for(Iterator<Player> iterator = player.getLocalPlayers().iterator(); iterator.hasNext();) {
			Player otherPlayer = iterator.next();
			if (World.getInstance().getPlayers().containsValue(otherPlayer) && otherPlayer.getPosition().isWithinDistance(player.getPosition(), 15) && !otherPlayer.getUpdateFlags().contains("teleporting")) {
				player.getListener().updateOtherPlayerMovement(otherPlayer, packet);
				if (otherPlayer.isUpdateRequired()) {
					player.getListener().updatePlayerState(otherPlayer, update, false, false);
				}
			} else {
				iterator.remove();
				packet.writeBits(1, 1);
				packet.writeBits(2, 3);
			}
		}
		for (Player otherPlayer : World.getInstance().getPlayers().values()) {
			if (otherPlayer.getLocalPlayers().size() >= 255) {
				break;
			}
			if (otherPlayer == player || player.getLocalPlayers().contains(otherPlayer)) {
				continue;
			}
			if (otherPlayer.getPosition().isWithinDistance(player.getPosition(), 15)) {
				player.getLocalPlayers().add(otherPlayer);
				packet.writeBits(11, otherPlayer.getIndex());
				packet.writeBits(1, 1);
				packet.writeBits(1, 1);
				packet.writeBits(5, otherPlayer.getPosition().getY() - player.getPosition().getY());
				packet.writeBits(5, otherPlayer.getPosition().getX() - player.getPosition().getX());
				player.getListener().updatePlayerState(otherPlayer, update, true, false);
			}
		}
		if (update.getBuffer().position() > 0) {
			packet.writeBits(11, 2047);
			packet.startByteAcces();
			packet.writeBytes(update.getBuffer());
		} else {
			packet.startByteAcces();
		}
		packet.finishVariableShortPacketHeader();
		return packet;
	}
}