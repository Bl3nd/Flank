package com.phoenix.runescape.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author Dylan Vicchiarelli
 *
 * The main asynchronous networking backing the application.
 */
public final class AsynchronousNetworking implements Runnable {

	/**
	 * The core of the asynchronous networking backing the application.
	 */
	private ServerSocketChannel serverSocketChannel;
	
	/**
	 * The selector reactor pattern implementation. Handles the dispatching
	 * of selected keys to their associated handlers.
	 */
	private Selector selector;
	
	/**
	 * The logger instance for recording viable data.
	 */
	private final Logger logger = Logger.getLogger(AsynchronousNetworking.class.getName());
	
	/**
	 * Binds the application to a fixed address.
	 * 
	 * @param address The fixed address.
	 * 
	 * @return The super type for chaining.
	 * 
	 * @throws IOException The exception thrown if a networking error occurs.
	 */
	public AsynchronousNetworking bind(int address) throws IOException {
		setServerSocketChannel(ServerSocketChannel.open());
		setSelector(Selector.open());
		getServerSocketChannel().configureBlocking(false);
		getServerSocketChannel().register(getSelector(), SelectionKey.OP_ACCEPT);
		getServerSocketChannel().bind(new InetSocketAddress(address));
		getLogger().info("Bound Phoenix.");
		
		return this;
	}

	/**
	 * Returns the main asynchronous networking backing the application.
	 * 
	 * @return The returned networking.
	 */
	public final ServerSocketChannel getServerSocketChannel() {
		
		return serverSocketChannel;
	}

	/**
	 * Modifies the main asynchronous networking backing the application.
	 * 
	 * @param serverSocketChannel The new modification.
	 */
	public final void setServerSocketChannel(ServerSocketChannel serverSocketChannel) {
		
		this.serverSocketChannel = serverSocketChannel;
	}

	/**
	 * Returns the selector reactor pattern.
	 * 
	 * @return The returned pattern.
	 */
	public final Selector getSelector() {
		
		return selector;
	}

	/**
	 * Modifies the selector reactor pattern.
	 * 
	 * @param selector The new modification.
	 */
	public final void setSelector(Selector selector) {
		
		this.selector = selector;
	}
	
	/**
	 * Returns the logger used for recording viable data.
	 * 
	 * @return The returned logger.
	 */
	public final Logger getLogger() {
		
		return logger;
	}
	
	@Override
	public void run() {
		try {
			
			getSelector().selectNow();
			
			Iterator<SelectionKey> selectionKeys = getSelector().selectedKeys().iterator();
			
			while(selectionKeys.hasNext()) {
				
				SelectionKey selectedKey = selectionKeys.next();
				
				if (selectedKey.isValid()) {
					
					if (selectedKey.isAcceptable()) {
						
						for (int i = 0; i < 10; i ++) {
							
							ServerSocketChannel connectionChannel = (ServerSocketChannel) selectedKey.channel();
							SocketChannel socketChannel = connectionChannel.accept();
							
							if (socketChannel == null || !socketChannel.isConnected()) {
								
								continue;
							}
							AsynchronousConnection connection = new AsynchronousConnection(selectedKey, socketChannel);
							connection.getEventHandler().executeEvent(connection);
						}
					}
					if (selectedKey.isReadable()) {
						AsynchronousConnection connection = (AsynchronousConnection) selectedKey.attachment();
						
						if (connection == null || !selectedKey.channel().isOpen()) {
							
							continue;
						}

						connection.getEventHandler().executeEvent(connection);
					}
				}
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}