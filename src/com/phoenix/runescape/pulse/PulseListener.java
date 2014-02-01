package com.phoenix.runescape.pulse;

/**
 * @author Dylan Vicchiarelli
 *
 * Listens for a pulse execution request and then passes that request
 * on to an observer.
 */
public interface PulseListener {
	
	/**
	 * Executes the requested pulse on demand.
	 * 
	 * @param pulse The pulse being executed.
	 */
	void execute(Pulse pulse);
}