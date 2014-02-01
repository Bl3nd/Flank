package com.phoenix.runescape.network.packet.dispatch;

import java.nio.ByteBuffer;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.OutgoingPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Displays the information pertaining to a skill in the appropriate
 * index box.
 */
public final class ShowSkillPacket extends OutgoingPacket {

	/**
	 * The skill being displayed.
	 */
	private int skill;
	
	/**
	 * The default class constructor.
	 * 
	 * @param skill The skill being displayed.
	 * 
	 * @param level The level that skills experience translates to.
	 */
	public ShowSkillPacket(int skill) {
		super(134);
		this.skill = skill;
	}

	@Override
	public AsynchronousBuffer dispatch(Player player) {
		AsynchronousBuffer buffer = new AsynchronousBuffer(ByteBuffer.allocate(7));
		buffer.writeRawPacketHeader(player.getConnection().getCryptionPair().getWriter(), getOpcode());
		buffer.writeByte(skill);
		buffer.writeInverseInteger((int) player.getSkills().getSkills()[skill].getExperience());
		buffer.writeByte(player.getSkills().getSkills()[skill].getLevel());
		return buffer;
	}
}