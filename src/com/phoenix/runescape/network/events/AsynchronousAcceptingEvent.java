package com.phoenix.runescape.network.events;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.AsynchronousEventListener;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the asynchronous event of accepting an incoming connection.
 */
public final class AsynchronousAcceptingEvent implements AsynchronousEventListener {

	@Override
	public void executeEvent(AsynchronousConnection connection) throws IOException {
		connection.getSocketChannel().configureBlocking(false);
		connection.getSocketChannel().register(connection.getSelectionKey().selector(), SelectionKey.OP_READ, connection);
		connection.setEventHandler(new AsynchronousReadingEvent());
	}
}