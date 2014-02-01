package com.phoenix.runescape.pulse;

/**
 * @author Dylan Vicchiarelli
 *
 * An implementation of the {@link PulseListener} to perform pulses on demand.
 */
public final class PulseObserver implements PulseListener {

	@Override
	public void execute(Pulse pulse) {
		
		if (pulse.isActive()) {
			
			pulse.execute();
		}
	}
}