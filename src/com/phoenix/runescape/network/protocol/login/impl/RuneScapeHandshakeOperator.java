package com.phoenix.runescape.network.protocol.login.impl;

import java.io.IOException;

import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.protocol.login.RuneScapeLoginProtocolOperator;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the reading of the authorization code and the name hash from the RuneScpe
 * client.
 */
public final class RuneScapeHandshakeOperator implements RuneScapeLoginProtocolOperator {

	@Override
	public void executeOperator(AsynchronousConnection asynchronousConnection) throws IOException {
		
		if ((asynchronousConnection.getReader().readByte() & 0xFF) != 14) {
			
			asynchronousConnection.getSocketChannel().close();
			return;
		}
		
		@SuppressWarnings("unused")
		int nameHash = asynchronousConnection.getReader().readByte() & 0xFF;
		
		asynchronousConnection.setLoginProtocolOperator(new RuneScapeKeyExchangeOperator());
	}
}