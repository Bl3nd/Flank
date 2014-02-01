package com.phoenix.runescape.pulse.impl;

import com.phoenix.Phoenix;
import com.phoenix.runescape.World;
import com.phoenix.runescape.item.floor.FloorItem;
import com.phoenix.runescape.item.floor.FloorItems;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.pulse.Pulse;

/**
 * @author Dylan Vicchiarelli
 *
 * A pulse dedicated to the fixed processing of the floor items in
 * the virtual world.
 */
public final class FloorItemPulse extends Pulse {

	/**
	 * The default class constructor for this pulse implementation.
	 */
	public FloorItemPulse() {
		
		super(600, Phoenix.class);
	}

	@Override
	public void execute() {
		for (final FloorItem floorItem : FloorItems.getInstance().getFloorItems()) {
			
			if (floorItem.getTicks() > 240) {
				
				FloorItems.getInstance().getFloorItems().remove(floorItem);
				
				break;
			
			} else if (floorItem.getTicks() == 120) {
				
				for (final Player player : World.getInstance().getPlayers().values()) {
					
					if (player.getAccountName().equals(floorItem.getPlayer().getAccountName())) {
						
						continue;
					}
					if (player.getPosition().isWithinDistance(floorItem.getPosition(), 15)) {
						
						FloorItems.getInstance().displayItem(player, floorItem);
					}
				}
			} else if (floorItem.getTicks() == 240) {
				
				for (final Player player : World.getInstance().getPlayers().values()) {
					
					if (player.getPosition().isWithinDistance(floorItem.getPosition(), 15)) {
						
						FloorItems.getInstance().removeItem(player, floorItem);
					}
				}
			}
			
			floorItem.setTicks(floorItem.getTicks() + 1);
		}
	}

	@Override
	public void stop() {
		
		System.exit(0);
	}
}