package com.phoenix.runescape.pulse;

/**
 * @author Dylan Vicchiarelli
 *
 * Represents a pulse which has a fixed execution rate.
 */
public abstract class Pulse {
	
	/**
	 * The execution rate of the pulse.
	 */
	private final int executionRate;
	
	/**
	 * If the pulse is currently active.
	 */
	private boolean isActive;
	
	/**
	 * The owner or attachment of the pulse.
	 */
	private final Object owner;
	
	/**
	 * Executes the pulse on demand.
	 */
	public abstract void execute();
	
	/**
	 * Executes the last wishes of the pulse before it is
	 * removed from the system.
	 */
	public abstract void stop();
	
	/**
	 * The default class constructor.
	 * 
	 * @param executionRate The execution rate of the pulse.
	 * 
	 * @param owner The owner or attachment of the pulse.
	 */
	public Pulse(int executionRate, Object owner) {
		this.executionRate = executionRate;
		this.owner = owner;
		this.isActive = true;
	}

	/**
	 * Returns the execution rate of the pulse.
	 * 
	 * @return The returned rate.
	 */
	public final int getExecutionRate() {
		
		return executionRate;
	}

	/**
	 * Returns if the pulse is currently active.
	 * 
	 * @return The returned state.
	 */
	public final boolean isActive() {
		
		return isActive;
	}

	/**
	 * Modifies if the pulse is currently active.
	 * 
	 * @param isActive The new modification.
	 */
	public final void setActive(boolean isActive) {
		
		this.isActive = isActive;
	}

	/**
	 * Returns the owner or attachment of the pulse.
	 * 
	 * @return The returned pulse attachment.
	 */
	public final Object getOwner() {
		
		return owner;
	}
}