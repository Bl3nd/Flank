package com.phoenix.runescape.pulse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Dylan Vicchiarelli
 *
 * A utility class for the scheduling and execution of pulses.
 */
public final class PulseScheduler {
	
	/**
	 * The collection of active pulses in the system.
	 */
	private final Set<Pulse> pulses = new HashSet<Pulse>();
	
	/**
	 * The class instance to avoid direct static access upon class
	 * reference.
	 */
	private static final PulseScheduler instance = new PulseScheduler();
	
	/**
	 * The service for the fixed execution of pulses in a closed environment.
	 */
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	/**
	 * The listener to execute all destined pulses.
	 */
	private final PulseListener pulseListener = new PulseObserver();

	/**
	 * Destroys all the pulses of a specified owner or attachment.
	 * 
	 * @param owner The owner or set attachment of the pulses.
	 */
	public final void destoryPulsesForOwner(Object owner) {
		for (Pulse pulse : getPulses()) {
			
			if (pulse.getOwner().getClass() == owner.getClass()) {
				
				pulse.setActive(false);
			}
		}
	}
	
	/**
	 * Registers a new pulse for fixed execution.
	 * 
	 * @param pulse The pulse to be registered.
	 */
	public final void register(final Pulse pulse) {
		getPulses().add(pulse);
		
		getExecutor().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				synchronized(this) {
					
					for (Iterator<Pulse> iterator = getPulses().iterator(); iterator.hasNext();) {
						
						Pulse iterated = iterator.next();
						
						if (pulse == iterated) {
							
							if (pulse.isActive()) {
								
								pulseListener.execute(pulse);
							} else {
								
								pulse.stop();
								
								iterator.remove();
							}
						}
					}
				}
			}
		}, 0, pulse.getExecutionRate(), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Returns the encapsulated class instance.
	 * 
	 * @return The returned instance.
	 */
	public static final PulseScheduler getInstance() {
		
		return instance;
	}

	/**
	 * Returns the collection of active pulses.
	 * 
	 * @return The returned collection.
	 */
	public final Set<Pulse> getPulses() {
		
		return pulses;
	}

	/**
	 * Returns the execution service for pulses.
	 * 
	 * @return The returned service.
	 */
	public final ScheduledExecutorService getExecutor() {
		
		return executor;
	}
}