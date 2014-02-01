package com.phoenix.runescape.mobile.player.skill.impl;

import com.phoenix.runescape.item.ItemDefinition;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.mobile.player.skill.SkillingAction;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the action of burying bones to receive Prayer experience.
 */
public final class BoneBuryingSkillingAction extends SkillingAction {

	/**
	 * The index number of the item. This is the
	 * index derived from the activation packet. If this index
	 * is determined not to be a valid bone then nothing happens.
	 */
	private final int index;
	
	/**
	 * The slot of the item. The is the slot the item
	 * will be deleted from upon burying of the bone.
	 */
	private final int slot;
	
	/**
	 * The default class constructor.
	 * 
	 * @param index The index number of the bone.
	 * 
	 * @param slot The slot of the bone in the inventory.
	 */
	public BoneBuryingSkillingAction(final int index, final int slot) {
		
		this.index = index;
		this.slot = slot;
	}
	
	/**
	 * An enumeration for the storage of valid bones indexes
	 * and their reward experience.
	 */
	public enum Bones{
		
		REGULAR(526, 100),
		
		DRAGON_BONES(536, 1000),
		
		BIG_BONES(532, 500);
		
		/*
		 * The index number of the bones.
		 */
		private final int index;
		
		/*
		 * The reward experience of the bones.
		 */
		private final double experience;
		
		/*
		 * The default enumeration constructor.
		 */
		private Bones(final int index, final double experience) {
			
			this.index = index;
			this.experience = experience;
		}

		/*
		 * Returns the index number of the bones.
		 */
		public int getIndex() {
			
			return index;
		}

		/*
		 * Returns the experience given by the bones.
		 */
		public double getExperience() {
			
			return experience;
		}
	}
	
	@Override
	public void performSkillingAction(Player player) {
		
		if (player.hasAttribute("bone-burying-delay") && (System.currentTimeMillis() - player.getAttributeAsLong("bone-burying-delay")) < 1900) {

			return;
		}
		
		if (player.getSkilling() == null) {
			
			return;
		}
		
		for (Bones bone : Bones.values()) {
			
			if (bone.getIndex() == getIndex()) {
				
				player.send(new ChatBoxMessagePacket("You dig a hole in the ground and bury the " + ItemDefinition.getDefinitions()[getIndex()].getItemName() + "."));
				
				player.performAnimation(827, 0);
				
				player.getInventoryContainer().removeItemFromSlot(getSlot(), 1);
				
				player.getSkills().addExperience(5, bone.experience);	
			}
		}
		
		/*
		 * Since the burying of bones does not cycle the action is stopped
		 * directly after the block of code is executed.
		 */
		stopSkillingAction(player);
	}

	@Override
	public void stopSkillingAction(Player player) {
		
		player.putAttribute("bone-burying-delay", System.currentTimeMillis());
	}

	/**
	 * Returns the slot of the item.
	 * 
	 * @return The returned slot.
	 */
	public int getSlot() {
		
		return slot;
	}

	/**
	 * Returns the index of the item.
	 * 
	 * @return The returned index.
	 */
	public int getIndex() {
		
		return index;
	}
}