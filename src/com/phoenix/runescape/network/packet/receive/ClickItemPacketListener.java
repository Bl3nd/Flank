package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.mobile.player.skill.SkillingActionCoordinator;
import com.phoenix.runescape.mobile.player.skill.impl.BoneBuryingSkillingAction;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;

@IncomingPacketOpcode( 122 )

public final class ClickItemPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		packet.readAdditionalShort();
		
		final int itemSlot = packet.readAdditionalShort();
		
		final int itemIndex = packet.readLittleEndianShort();
		
		SkillingActionCoordinator.getSingleton().startSkillingAction(player, new BoneBuryingSkillingAction(itemIndex, itemSlot));
	}
}