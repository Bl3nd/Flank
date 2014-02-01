package com.phoenix.runescape.network.events;

import java.io.IOException;

import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.AsynchronousEventListener;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the asynchronous event of reading an incoming transmission.
 */
public final class AsynchronousReadingEvent implements AsynchronousEventListener {

	@Override
	public void executeEvent(AsynchronousConnection connection)  throws IOException {
		if (connection.getSocketChannel().read(connection.getReader().getBuffer()) == -1) {
			return;
		}
		connection.getReader().getBuffer().flip();
		connection.getListener().receiveProtocol(connection);
		connection.getReader().getBuffer().clear();
	}
}