package com.phoenix.runescape.pulse.impl;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.item.floor.FloorItems;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.mobile.player.PlayerWalkToActions;
import com.phoenix.runescape.pulse.Pulse;

/**
 * @author Dylan Vicchiarelli
 *
 * Delays all entity interaction until the player is within proper distance
 * of the entity.
 */
public final class InteractionDistancePulse extends Pulse {

	/**
	 * The type of interaction being performed.
	 */
	private final InteractionType interactionType;
	
	/**
	 * The destination of the entity.
	 */
	private final Coordinate destination;
	
	/**
	 * The player performing the interaction.
	 */
	private final Player player;
	
	/**
	 * Defines the possible entity interaction types.
	 */
	public enum InteractionType { RETREIVE_ITEM, OBJECT_INTERACTION_FIRST_CLICK }
	
	/**
	 * The default class constructor for this pulse implementation.
	 * 
	 * @param player The player performing the interaction.
	 * 
	 * @param interaction The type of interaction being performed.
	 */
	public InteractionDistancePulse(Player player, InteractionType interaction) {
		super(600, player);
		
		this.interactionType = interaction;
		this.player = player;
		this.destination = new Coordinate(player.getAttributeAsInteger("click-x"), player.getAttributeAsInteger("click-y"), player.getPosition().getPlane());
	}

	@Override
	public void execute() {
		player.putAttribute("performing-walk-to-action");
		
		switch (interactionType) {
		
		case OBJECT_INTERACTION_FIRST_CLICK:
			
			if (player.getPosition().isWithinDistance(destination, 1)) {
				
				setActive(false);
			}
			break;
		
		case RETREIVE_ITEM:
			
			if (player.getPosition().coordinatesEqual(destination)) {
				
				setActive(false);
			}
			break;
		}
	}

	@Override
	public void stop() {
		if (!player.hasAttribute("performing-walk-to-action")) {
			return;
		}
		switch (interactionType) {
		
		case OBJECT_INTERACTION_FIRST_CLICK:
			
			if (player.getPosition().isWithinDistance(destination, 1)) {
				
				PlayerWalkToActions.objectFirstClick(player, destination, player.getAttributeAsInteger("click-index"));
			}
			break;
		
		case RETREIVE_ITEM:
			
			if (player.getPosition().coordinatesEqual(destination)) {
				
				FloorItems.getInstance().pickupItem(player, player.getAttributeAsInteger("click-index"), destination);
			}
			break;
		}
	}
}