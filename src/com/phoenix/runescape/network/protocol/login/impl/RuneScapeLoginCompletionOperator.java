package com.phoenix.runescape.network.protocol.login.impl;

import java.io.IOException;

import com.phoenix.runescape.Constants;
import com.phoenix.runescape.World;
import com.phoenix.runescape.item.container.BankingInterfaceContainer.BankingInterfaceComposer;
import com.phoenix.runescape.mobile.EntityIndex;
import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;
import com.phoenix.runescape.network.packet.dispatch.ConfigurationPacket;
import com.phoenix.runescape.network.packet.dispatch.DisplayInterfacePacket;
import com.phoenix.runescape.network.packet.dispatch.RunningEnergyPacket;
import com.phoenix.runescape.network.packet.dispatch.ShowSkillPacket;
import com.phoenix.runescape.network.packet.dispatch.SideBarInterfacePacket;
import com.phoenix.runescape.network.packet.receive.EquipmentPacketListener;
import com.phoenix.runescape.network.protocol.login.RuneScapeLoginProtocolOperator;
import com.phoenix.runescape.network.protocol.packet.RuneScapePacketProtocolListener;
import com.phoenix.utilities.ClientText;

/**
 * @author Dylan Vicchiarelli
 * 
 * Handles the event executed upon completion of the RuneScape login protocol.
 */
public final class RuneScapeLoginCompletionOperator implements RuneScapeLoginProtocolOperator {

	@Override
	public void executeOperator(final AsynchronousConnection asynchronousConnection) throws IOException {
		asynchronousConnection.getPlayer().setIndex(EntityIndex.findFreePlayerIndex());
		
		World.getInstance().getPlayers().put(asynchronousConnection.getPlayer().getIndex(), asynchronousConnection.getPlayer());
		
		asynchronousConnection.getPlayer().getUpdateFlags().add("map-region");
		asynchronousConnection.getPlayer().getUpdateFlags().add("appearance");
		
		asynchronousConnection.getPlayer().send(new RunningEnergyPacket());
		asynchronousConnection.getPlayer().send(new ConfigurationPacket(173, 0));
		
		if (asynchronousConnection.getPlayer().getFirstLogin() == 0) {
			asynchronousConnection.getPlayer().send(new DisplayInterfacePacket(3559));
			asynchronousConnection.getPlayer().setFirstLogin(1);
		}/* else if (asynchronousConnection.getPlayer().getFirstLogin() == 1) {
			return;
		}*/
		
		BankingInterfaceComposer.sendInitialBankingConfigurations(asynchronousConnection.getPlayer());
		
		asynchronousConnection.getPlayer().send(new ChatBoxMessagePacket("Welcome to Phoenix."));
		
		asynchronousConnection.getPlayer().getPrivateMessaging().displayFriends();
		asynchronousConnection.getPlayer().getPrivateMessaging().register();
		asynchronousConnection.getPlayer().getInventoryContainer().refreshContainer();
		asynchronousConnection.getPlayer().getEquipmentContainer().refreshContainer();
		
		ClientText.getClassInstance().display(asynchronousConnection.getPlayer());
		
		for (int value = 0; value < Constants.SIDE_BARS.length; value ++) {
			
			asynchronousConnection.getPlayer().send(new SideBarInterfacePacket(value, Constants.SIDE_BARS[value]));
        }
		
		for (int i = 0; i < 21; i ++) {
			
			asynchronousConnection.getPlayer().send(new ShowSkillPacket(i));
		}
		
		EquipmentPacketListener.weaponUpdate(asynchronousConnection.getPlayer());
		
		asynchronousConnection.setListener(new RuneScapePacketProtocolListener());
	}
}