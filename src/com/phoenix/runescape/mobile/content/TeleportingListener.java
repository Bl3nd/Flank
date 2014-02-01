package com.phoenix.runescape.mobile.content;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.mobile.Entity;
import com.phoenix.runescape.pulse.Pulse;
import com.phoenix.runescape.pulse.PulseScheduler;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the porting of an entity to a different location.
 */
public final class TeleportingListener {
	
	/**
	 * The class instance to avoid direct static access.
	 */
	private static final TeleportingListener instance = new TeleportingListener();
	
	/**
	 * Returns a visible encapsulation of the class instance.
	 * 
	 * @return The returned encapsulated instance.
	 */
	public final static TeleportingListener getInstance() {
		
		return instance;
	}
	
	/**
	 * Ports an entity with the animation and graphic.
	 * 
	 * @param entity The entity being ported.
	 * 
	 * @param position The new coordinate of the entity.
	 */
	public final void teleport(final Entity entity, final Coordinate position) {
		
		if (entity.hasAttribute("teleporting")) {
			
			return;
		}
		PulseScheduler.getInstance().register(new Pulse(2100, entity) {

			@Override
			public void execute() {
				entity.putAttribute("teleporting");
				entity.performAnimation(65535, 0);
				entity.performAnimation(714, 0);
				entity.executeGraphic(308, 52, 100);
				
				setActive(false);
			}

			@Override
			public void stop() {
				entity.getUpdateFlags().add("teleporting");
				entity.getUpdateFlags().add("map-region");
				entity.performAnimation(715, 0);
				entity.getPosition().setPosition(position);
				entity.removeAttribute("teleporting");
			}
		});
	}
	
	/**
	 * Moves an entity to a new position.
	 * 
	 * @param entity The entity to be moved.
	 * 
	 * @param position The new position;
	 */
	public final void move(final Entity entity, final Coordinate position) {
		
		entity.getUpdateFlags().add("map-region");
		
		entity.getPosition().setPosition(position);
	}
}