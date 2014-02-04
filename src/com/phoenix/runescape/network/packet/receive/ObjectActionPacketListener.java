package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.runescape.pulse.PulseScheduler;
import com.phoenix.runescape.pulse.impl.InteractionDistancePulse;
import com.phoenix.runescape.pulse.impl.InteractionDistancePulse.InteractionType;

/**
 * The packet opcodes which this listener implementation handles.
 */
@IncomingPacketOpcode ({ 132, 252, 70, 192 })

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the clicking of an object. The request is prioritized in an event.
 */
public final class ObjectActionPacketListener extends IncomingPacketListener {

	@Override
	public void operate(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {
		
		case 132:
			final int x = packet.readLittleEndianShortAddition();
			final int index = packet.getBuffer().getShort();
			final int y = packet.readAdditionalShort();
			player.putAttribute("click-y", y);
			player.putAttribute("click-index", index);
			player.putAttribute("click-x", x);
			PulseScheduler.getInstance().register(new InteractionDistancePulse(player, InteractionType.OBJECT_INTERACTION_FIRST_CLICK));
			switch (index) {
			case 1278:
				SkillingActionCoordinator.getSingleton().startSkillingAction(player, new WoodcuttingSkillingAction(index));
				break;
			}
			break;
		}
	}
}
