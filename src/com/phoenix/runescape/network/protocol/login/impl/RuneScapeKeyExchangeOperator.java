package com.phoenix.runescape.network.protocol.login.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.protocol.login.RuneScapeLoginProtocolOperator;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the distribution of the session keys to the RuneScape client.
 */
public final class RuneScapeKeyExchangeOperator implements RuneScapeLoginProtocolOperator {

	@Override
	public void executeOperator(AsynchronousConnection asynchronousConnection) throws IOException {
		
		AsynchronousBuffer keyExchange = new AsynchronousBuffer(ByteBuffer.allocate(17));
		keyExchange.writeLong(0);
		keyExchange.writeByte(0);
		keyExchange.writeLong(new SecureRandom().nextLong());
		asynchronousConnection.write(keyExchange.getBuffer());
		asynchronousConnection.setLoginProtocolOperator(new RuneScapeLoginRequestOperator());
	}
}