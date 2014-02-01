package com.phoenix.runescape.network;

import java.io.IOException;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles an asynchronous event derived from the selector's reactor pattern.
 */
public interface AsynchronousEventListener {

	/**
	 * Handles the manipulation of the event derived from the selector's reactor pattern.
	 * 
	 * @param connection The connection correlation to the event.
	 * 
	 * @throws IOException The exception thrown if a networking error occurs.
	 */
	void executeEvent(AsynchronousConnection connection) throws IOException;
}