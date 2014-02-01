package com.phoenix.runescape.mobile.mob;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.World;
import com.phoenix.runescape.mobile.Entity;
import com.phoenix.runescape.mobile.EntityIndex;
import com.phoenix.runescape.mobile.EntityUpdateListener;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.pulse.PulseScheduler;

/**
 * @author Dylan Vicchiarelli
 *
 * Represents a mob or non-player character in the virtual world.
 */
public final class Mob extends Entity {

	/**
	 * The identity index of the mob.
	 */
	private int identityIndex;
	
	/**
	 * The listener to handle any updating requests during run-time.
	 */
	private final MobUpdateListener listener = new MobUpdateListenerEvent();
	
	/**
	 * The default class constructor.
	 * 
	 * @param identityIndex The identity index of the entity.
	 * 
	 * @param slotIndex The index slot of the entity in the global collection.
	 * 
	 * @param position The coordinates of the entity.
	 */
	public Mob(int identityIndex, int slotIndex, Coordinate position) {
		setIdentityIndex(identityIndex);
		setPosition(position);
		setIndex(slotIndex);
	}
	
	/**
	 * The update executed prior to the main update block.
	 */
	public void prepare() {
		
		getMovement().handleEntityMovement();
	}
	
	/**
	 * The update executed after the main update block.
	 */
	public void reset() {
		
		getUpdateFlags().clear();
	}

	/**
	 * Returns the identity index of the mob.
	 * 
	 * @return The returned index.
	 */
	public int getIdentityIndex() {
		
		return identityIndex;
	}

	/**
	 * Modifies the identity index of the mob.
	 * 
	 * @param identityIndex The new modification.
	 */
	public void setIdentityIndex(int identityIndex) {
		
		this.identityIndex = identityIndex;
	}
	
	/**
	 * Returns the listener for any updating requests.
	 * 
	 * @return The returned listener.
	 */
	public MobUpdateListener getListener() {
		
		return listener;
	}
	
	@Override
	public EntityUpdateListener<Player> getUpdateListener() {
		
		return listener;
	}

	@Override
	public void remove() {
		
		World.getInstance().getMobs().remove(getIndex());
		EntityIndex.discardMobIndex(getIndex());
		PulseScheduler.getInstance().destoryPulsesForOwner(this);
	}
}