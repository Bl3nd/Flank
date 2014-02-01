package com.phoenix.runescape.network.protocol;

import java.io.IOException;

import com.phoenix.runescape.network.AsynchronousConnection;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the protocol requests sent by the RuneScape client.
 */
public interface RuneScapeProtocolListener {

	/**
	 * Handles the manipulation of the protocol request.
	 * 
	 * @param asynchronousConnection The asynchronous connection of interest.
	 * 
	 * @throws IOException The exception thrown if a networking error occurs.
	 */
	void receiveProtocol(AsynchronousConnection asynchronousConnection) throws IOException;
}