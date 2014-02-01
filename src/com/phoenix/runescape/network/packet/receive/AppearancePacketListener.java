package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;

@IncomingPacketOpcode( 101 )

public final class AppearancePacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		player.setGender(packet.getBuffer().get());
        player.getAppearanceValues()[3] = packet.getBuffer().get();
        player.getAppearanceValues()[6] = packet.getBuffer().get();
        player.getAppearanceValues()[0] = packet.getBuffer().get();
        player.getAppearanceValues()[1] = packet.getBuffer().get();
        player.getAppearanceValues()[4] = packet.getBuffer().get();
        player.getAppearanceValues()[2] = packet.getBuffer().get();
        player.getAppearanceValues()[5] = packet.getBuffer().get();
        player.getColorValues()[0] = packet.getBuffer().get();
        player.getColorValues()[1] = packet.getBuffer().get();
        player.getColorValues()[2] = packet.getBuffer().get();
        player.getColorValues()[3] = packet.getBuffer().get();
        player.getColorValues()[4] = packet.getBuffer().get();
	}
}
