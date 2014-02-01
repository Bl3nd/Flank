package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.item.container.BankingInterfaceContainer.BankingInterfaceComposer;
import com.phoenix.runescape.mobile.content.TeleportingListener;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;
import com.phoenix.runescape.network.packet.dispatch.CloseInterfacePacket;
import com.phoenix.runescape.network.packet.dispatch.DisplayInterfacePacket;
import com.phoenix.runescape.object.GameObject;
import com.phoenix.runescape.object.GameObjectHandler;

/**
 * The packet opcode which this listener implementation handles.
 */
@IncomingPacketOpcode( 185 )

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the action of activating an action button on an interface.
 */
public final class ActionButtonPacketListener extends IncomingPacketListener {

	@Override
	public void operate(final Player player, IncomingPacket packet) {
		
		final int button = packet.getBuffer().getShort();
		
		player.send(new ChatBoxMessagePacket("Action Button: [" + button + "]."));
		
		switch (button) {
		
		case 7339:
			GameObjectHandler.getSingleton().createObject(new GameObject(1, new Coordinate(3222, 3218, 0), 10, 1));
			break;
			
		case 7383:
			if (player.getAttributeAsInteger("player-rights") == 2) {
			EnterSetAmountPacketListener.dispatchEnterAmountPacket(player);
			player.putAttribute("spawn-item");
			} else {
				player.send(new ChatBoxMessagePacket("You are not able to do this action."));
			}
			break;

		case 3651:
	        player.getUpdateFlags().add("appearance");
	        player.removeAttribute("changing-appearance");
			player.send(new CloseInterfacePacket());
			break;
			
		case 7332:
			player.putAttribute("changing-appearance");
			player.send(new DisplayInterfacePacket(3559));
			break;
			
		case 7334:
			player.save();
			player.send(new ChatBoxMessagePacket("Your account has been saved."));
			break;
			
		case 7336:
			player.send(new ChatBoxMessagePacket("X Coordinate [" + player.getPosition().getX() + "] Y Coordinate [" + player.getPosition().getY() + "] Height Plane [" + player.getPosition().getPlane() + "]."));
			break;
			
		case 5386:
			player.putAttribute("note");
			break;
			
		case 5387:
			player.removeAttribute("note");
			break;
			
		case 8130:
			player.putAttribute("swap");
			break;
			
		case 8131:
			player.removeAttribute("swap");
			break;
			
		case 7333:
			BankingInterfaceComposer.openBankAccount(player);
			break;
			
		case 1164:
			TeleportingListener.getInstance().teleport(player, new Coordinate(3210, 3424, 0));
			break;
			
		case 1170:
			TeleportingListener.getInstance().teleport(player, new Coordinate(2964, 3378, 0));
			break;
			
		case 1174:
			TeleportingListener.getInstance().teleport(player, new Coordinate(2757, 3477, 0));
			break;
			
		case 1167:
			TeleportingListener.getInstance().teleport(player, new Coordinate(3222, 3218, 0));
			break;
		
		 case 153:
             player.getMovement().setRunning(true);
             player.getMovement().setRunningQueueEnabled(true);
             break;

         case 152:
             player.getMovement().setRunning(false);
             player.getMovement().setRunningQueueEnabled(false);
             break;
             
		case 2458:
			player.remove();
			break;
			
		case 161:
			player.performAnimation(860, 0);
			break;
			
		case 162:
			player.performAnimation(857, 0);
			break;
			
		case 163:
			player.performAnimation(863, 0);
			break;
			
		case 164:
			player.performAnimation(858, 0);
			break;
			
		case 165:
			player.performAnimation(859, 0);
			break;
			
		case 166:
			player.performAnimation(866, 0);
			break;
			
		case 167:
			player.performAnimation(864, 0);
			break;
			
		case 168:
			player.performAnimation(855, 0);
			break;
			
		case 169:
			player.performAnimation(856, 0);
			break;
			
		case 170:
			player.performAnimation(861, 0);
			break;
			
		case 171:
			player.performAnimation(862, 0);
			break;
			
		case 172:
			player.performAnimation(865, 0);
			break;
			
		case 13362:
			player.performAnimation(2105, 0);
			break;
			
		case 13363:
			player.performAnimation(2106, 0);
			break;
			
		case 13364:
			player.performAnimation(2107, 0);
			break;
			
		case 13365:
			player.performAnimation(2108, 0);
			break;
			
		case 13366:
			player.performAnimation(2109, 0);
			break;
			
		case 13367:
			player.performAnimation(2110, 0);
			break;
			
		case 13368:
			player.performAnimation(2111, 0);
			break;
			
		case 13383:
			player.performAnimation(2127, 0);
			break;
			
		case 13384:
			player.performAnimation(2128, 0);
			break;
			
		case 13369:
			player.performAnimation(2112, 0);
			break;
			
		case 13370:
			player.performAnimation(2113, 0);
			break;
			
		case 11100:
			player.performAnimation(1368, 0);
			break;
			
		case 667:
			player.performAnimation(1131, 0);
			break;
			
		case 6503:
			player.performAnimation(1130, 0);
			break;
			
		case 6506:
			player.performAnimation(1129, 0);
			break;
			
		case 666:
			player.performAnimation(1128, 0);
			break;
		}
	}
}