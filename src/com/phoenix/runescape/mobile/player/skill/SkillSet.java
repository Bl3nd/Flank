package com.phoenix.runescape.mobile.player.skill;

import com.phoenix.runescape.Constants;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.dispatch.ChatInterfacePacket;
import com.phoenix.runescape.network.packet.dispatch.ShowSkillPacket;
import com.phoenix.runescape.network.packet.dispatch.StringLayerPacket;
import com.phoenix.utilities.Utilities;

/**
 * @author Dylan Vicchiarelli
 * 
 * Represents a set of skills belonging to the player.
 */
public final class SkillSet {

	/**
	 * The player the skill set belongs to.
	 */
	private final Player player;

	/**
	 * The skill set belonging to the player.
	 */
	private final Skill[] skills = new Skill[21];

	/**
	 * The default class constructor.
	 * 
	 * @param player The owner of the skill set.
	 */
	public SkillSet(Player player) {
		
		this.player = player;
	}

	/**
	 * Adds a given amount of experience to a specified skill.
	 * 
	 * @param index The index of the skill.
	 * 
	 * @param amount The amount of experience to be added.
	 */
	public final void addExperience(int index, double amount) {
		
		final int previousLevel = getSkills()[index].getLevel();
		
		getSkills()[index].setExperience(getSkills()[index].getExperience() + amount);
		
		getSkills()[index].setLevel(Utilities.levelForExperience(player, index));
		
		final int postLevel = getSkills()[index].getLevel();
		
		if ((postLevel - previousLevel) > 0) {
			
			showLevelUpInterface(index);
		}
		
		getPlayer().send(new ShowSkillPacket(index));
	}

	/**
	 * Returns the owner of the skill set.
	 * 
	 * @return The returned skill set owner.
	 */
	public final Player getPlayer() {
		
		return player;
	}

	/**
	 * Returns the skill set.
	 * 
	 * @return The returned skill set.
	 */
	public final Skill[] getSkills() {
		
		return skills;
	}

	/**
	 * @author Dylan Vicchiarelli
	 *
	 * Represents a skill.
	 */
	public static final class Skill {

		/**
		 * The level of the skill in set.
		 */
		private int level;

		/**
		 * The experience of the skill in the set.
		 */
		private double experience;

		/**
		 * The default class constructor.
		 * 
		 * @param level The level of the skill in the set.
		 * 
		 * @param experience The experience of the skill in the set.
		 */
		public Skill(int level, double experience) {
			this.level = level;
			this.experience = experience;
		}

		/**
		 * Returns the level of the skill in the set.
		 * 
		 * @return The returned level.
		 */
		public final int getLevel() {
			
			return level;
		}

		/**
		 * Modifies the level of the skill in the set.
		 * 
		 * @param index The new modification.
		 */
		public final void setLevel(int level) {
			
			this.level = level;
		}

		/**
		 * Returns the experience of the skill in the set.
		 * 
		 * @return The returned experience.
		 */
		public final double getExperience() {
			
			return experience;
		}

		/**
		 * Modifies the experience of the skill in the set.
		 * 
		 * @param experience The new modification,
		 */
		public final void setExperience(double experience) {
			
			this.experience = experience;
		}
	}
	
	/**
	 * Displays the level up interface for a specified skill index.
	 * 
	 * @param index The skill index that was specified in the parameters.
	 */
	private final void showLevelUpInterface(final int index) {
		
		final String firstLine = "@dbl@Congratulations! You've just advanced a "  + Constants.SKILL_NAME[index] + " level!";
		
		final String secondLine = "You have reached level "  + skills[index].getLevel() + "!";
		
		for (final int[] interfaceComponenets : Constants.LEVEL_UP_COMPONENTS) {
			
			if (interfaceComponenets[0] == index) {
				
				player.send(new ChatInterfacePacket(interfaceComponenets[1]));
				
				if (index != Constants.RANGE && index != Constants.MINING && index != Constants.THIEVING &&  index != Constants.FARMING) {
					
					player.send(new StringLayerPacket(firstLine, interfaceComponenets[1] + 1));
					
					player.send(new StringLayerPacket(secondLine, interfaceComponenets[1] + 2));
				} else {
					
					player.send(new StringLayerPacket(firstLine, interfaceComponenets[2]));
					
					player.send(new StringLayerPacket(secondLine, interfaceComponenets[3]));
				}
			}
		}
	}
}