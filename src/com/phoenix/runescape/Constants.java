package com.phoenix.runescape;

/**
 * @author Dylan Vicchiarelli
 *
 * Holds a series of constants that are used throughout the application.
 */
public final class Constants {

	/**
	 * The numerical port the application will listen on.
	 */
	public static final int PORT = 43594;

	/**
	 * The starting coordinates of the player.
	 */
	public static final int STARTING_X = 3253, STARTING_Y = 3421;

	/**
	 * The directions for pedestrian X coordinate movement.
	 */
	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };

	/**
	 * The directions for pedestrian Y coordinate movement.
	 */
	public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };

	/**
	 * Valid characters that are supported by the RuneScape chat engine.
	 */
	public static final char VALID_CHARACTERS[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[', ']', '|', '?', '/', '`' };

	/**
	 * The widget indexes for weapon equipment.
	 */
	public static final Object[][] WEAPON_DATA = { { "whip", 12290 }, { "bow", 1764 }, { "stand", 328 }, { "wand", 328 }, { "dart", 4446 }, { "knife", 4446 }, { "javelin", 4446 }, { "dagger", 2276 }, { "sword", 2276 }, { "scimitar", 2276 }, { "pickaxe", 5570 }, { "axe", 1698 }, { "battleaxe", 1698 }, { "halberd", 8460 }, { "halberd", 8460 }, { "spear", 4679 }, { "mace", 3796 }, { "warhammer", 425 }, { "maul", 425 } };

	/**
	 * The names of the available skills.
	 */
    public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting" };
	
    /**
	 * Items which have complete head coverage.
	 */
	public static final int[] FULL_HEAD_GEAR = { 1153, 1155, 1157, 1159, 1161, 1163, 1165, 2587, 2595, 2605, 2613, 2619, 2627, 2657, 2673, 3486, 4745, 1053, 1055, 1057 };

	/**
	 * Items which have complete chest coverage.
	 */
	public static final int[] PLATE_BODY = { 3140, 1115, 1117, 1119, 1121, 1123, 1125, 1127, 2583, 2591, 2599, 2607, 2615, 2623, 2653, 2669, 3481, 4720, 4728, 4749, 4712 };

	/**
	 * The side-bar interfaces for the player's game-frame.
	 */
	public static final int[] SIDE_BARS = { 2423, 3917, 638, 3213, 1644, 5608, 1151, -1, 5065, 5715, 2449, 904, 147, 962 };

	/**
	 * Constants for the skill number indexes.
	 */
	public static final int	ATTACK	= 0, DEFENCE = 1, STRENGTH = 2,HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6,COOKING = 7, WOODCUTTING = 8, FLETCHING = 9,FISHING = 10, FIREMAKING = 11, CRAFTING = 12,SMITHING = 13, MINING = 14, HERBLORE = 15,AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20;

	/**
	 * The configurations for the level up messages when a new skill level is reached.
	 */
	public static final int[][] LEVEL_UP_COMPONENTS = { { ATTACK, 6247, 0, 0 }, { DEFENCE, 6253, 0, 0 }, { STRENGTH, 6206, 0, 0 }, { HITPOINTS, 6216, 0, 0 }, { RANGE, 4443, 5453, 6114 }, { PRAYER, 6242, 0, 0 }, { MAGIC, 6211, 0, 0 }, { COOKING, 6226, 0, 0 }, { WOODCUTTING, 4272, 0, 0 }, { FLETCHING, 6231, 0, 0 }, { FISHING, 6258, 0, 0 }, { FIREMAKING, 4282, 0, 0 }, { CRAFTING, 6263, 0, 0 }, { SMITHING, 6221, 0, 0 }, { MINING, 4416, 4417, 4438 }, { HERBLORE, 6237, 0, 0 }, { AGILITY, 4277, 0, 0 }, { THIEVING, 4261, 4263, 4264 }, { SLAYER, 12122, 0, 0 }, { FARMING, 4887, 4889, 4890 }, { RUNECRAFTING, 4267, 0, 0 }, };

	/**
	 * The equipment slot for helmets and hats.
	 */
	public static final int EQUIPMENT_SLOT_HEAD = 0;

	/**
	 * The equipment slot for capes.
	 */
	public static final int EQUIPMENT_SLOT_CAPE = 1;

	/**
	 * The equipment slot for amulets.
	 */
	public static final int EQUIPMENT_SLOT_AMULET = 2;

	/**
	 * The equipment slot for weapons.
	 */
	public static final int EQUIPMENT_SLOT_WEAPON = 3;

	/**
	 * The equipment slot for tops.
	 */
	public static final int EQUIPMENT_SLOT_CHEST = 4;

	/**
	 * The equipment slot for shields.
	 */
	public static final int EQUIPMENT_SLOT_SHIELD = 5;

	/**
	 * The equipment slot for legs.
	 */
	public static final int EQUIPMENT_SLOT_LEGS = 7;

	/**
	 * The equipment slot for gloves.
	 */
	public static final int EQUIPMENT_SLOT_HANDS = 9;

	/**
	 * The equipment slot for boots.
	 */
	public static final int EQUIPMENT_SLOT_FEET = 10;

	/**
	 * The equipment slot for rings and bracelets.
	 */
	public static final int EQUIPMENT_SLOT_RING = 12;

	/**
	 * The equipment slot for arrows.
	 */
	public static final int EQUIPMENT_SLOT_ARROWS = 13;

	/**
	 * The possible incoming packet sizes for the #317 protocol.
	 */
	public static final int PACKET_SIZES[] = {  0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 8, 0, 6, 2, 2, 0, 0, 2, 0, 6, 0, 12, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 8, 4, 0, 0, 2, 2, 6, 0, 6, 0, -1, 0, 0, 0, 0, 0, 0, 0, 12,
		0, 0, 0, 0, 8, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 2, 2, 8, 6,
		0, -1, 0, 6, 0, 0, 0, 0, 0, 1, 4, 6, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0,
		-1, 0, 0, 13, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0,
		0, 1, 0, 6, 0, 0, 0, -1, 0, 2, 6, 0, 4, 6, 8, 0, 6, 0, 0, 0, 2, 0,
		0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 1, 2, 0, 2, 6, 0, 0, 0, 0, 0, 0,
		0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 3, 0,
		2, 0, 0, 8, 1, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0,
		0, 4, 0, 4, 0, 0, 0, 7, 8, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, -1, 0, 6,
		0, 1, 0, 0, 0, 6, 0, 6, 8, 1, 0, 0, 4, 0, 0, 0, 0, -1, 0, -1, 4, 0,
		0, 6, 6, 0, 0, 0 
	};
}