package com.phoenix.runescape.mobile.player.skill.impl;

import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.mobile.player.skill.SkillingAction;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;

public final class ThievingSkillingAction extends SkillingAction {
	
	private final int index;
	
	public ThievingSkillingAction(final int index) {
		this.index = index;
	}
	
	public enum NPC {
		MAN(1, 8, 3),
		WOMAN(2, 8, 100);
		
		private final int index;
		
		private final double experience;
		
		private final int coins;
		
		private NPC(final int index, final double exp, final int coins) {
			this.index = index;
			this.experience = exp;
			this.coins = coins;
		}
		
		public int getIndex() {
			return index;
		}
		
		public double getExperience() {
			return experience;
		}
		
		public int getCoins() {
			return coins;
		}
	}

	@Override
	public void performSkillingAction(Player player) {
		if (player.hasAttribute("thieving-npc") && (System.currentTimeMillis() - player.getAttributeAsLong("thieving-npc") < 2500)) {
			return;
		}
		
		if (player.getSkilling() == null) {
			return;
		}
		
		for (NPC npc : NPC.values()) {
			if (npc.getIndex() == getIndex()) {
				player.send(new ChatBoxMessagePacket("Attempting to steal"));
				player.performAnimation(832, 0);
				player.getSkills().addExperience(17, npc.getExperience());
				player.getInventoryContainer().addItem(new Item(995, npc.getCoins()));
			}
		}
	}

	@Override
	public void stopSkillingAction(Player player) {
		
	}
	
	public int getIndex() {
		return index;
	}
}