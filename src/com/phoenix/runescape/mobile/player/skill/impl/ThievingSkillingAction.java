package com.phoenix.runescape.mobile.player.skill.impl;

import com.phoenix.runescape.Constants;
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
		MAN(1, 1, 8, 3, "man"),
		WOMAN(2, 1, 8, 3, "woman"),
		FARMER(3, 10, 14.5, 9, "farmer"),
		FHAM(4, 15, 18.5, 21, "female H.A.M member"),
		MHAM(5, 20, 22.2, 21, "male H.A.M member");
		
		private final int index;
		
		private final int levelRequired;
		
		private final double experience;
		
		private final int coins;
		
		private final String name;
		
		private NPC(final int index, final int levelReq, final double exp, final int coins, final String name) {
			this.index = index;
			this.levelRequired = levelReq;
			this.experience = exp;
			this.coins = coins;
			this.name = name;
		}
		
		public int getIndex() {
			return index;
		}
		
		public int getRequiredLevel() {
			return levelRequired;
		}
		
		public double getExperience() {
			return experience;
		}
		
		public int getCoins() {
			return coins;
		}
		
		public String getName() {
			return name;
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
				if (player.getSkills().getSkills()[Constants.THIEVING].getLevel() >= npc.getRequiredLevel()) {
					player.send(new ChatBoxMessagePacket("You attempt to steal the " + npc.getName() +"'s pocket..."));
					player.performAnimation(832, 0);
					player.getInventoryContainer().addItem(new Item(995, npc.getCoins()));
					player.getSkills().addExperience(17, npc.getExperience());
				} else {
					player.send(new ChatBoxMessagePacket("You need a Thieving level of " + npc.getRequiredLevel() + " to steal from this npc."));
				}
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
