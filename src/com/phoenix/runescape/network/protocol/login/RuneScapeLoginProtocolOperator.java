package com.phoenix.runescape.network.protocol.login;

import java.io.IOException;

import com.phoenix.runescape.network.AsynchronousConnection;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the manipulation of the procedural steps in the RuneScape login protocol.
 */
public interface RuneScapeLoginProtocolOperator {
	
	/**
	 * Executes the procedural step in the form of an abstract operator.
	 * 
	 * @param connection The connection of interest.
	 * 
	 * @throws IOException The exception thrown if an error occurs.
	 */
	void executeOperator(AsynchronousConnection connection) throws IOException;
}