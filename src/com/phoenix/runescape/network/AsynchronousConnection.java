package com.phoenix.runescape.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.events.AsynchronousAcceptingEvent;
import com.phoenix.runescape.network.packet.cryption.CryptionAlgorithmicPair;
import com.phoenix.runescape.network.protocol.RuneScapeProtocolListener;
import com.phoenix.runescape.network.protocol.login.RuneScapeLoginProtocolListener;
import com.phoenix.runescape.network.protocol.login.RuneScapeLoginProtocolOperator;
import com.phoenix.runescape.network.protocol.login.impl.RuneScapeHandshakeOperator;

/**
 * @author Dylan Vicchiarelli
 * 
 * Represents an incoming connection accepted by the Selector.
 */
public final class AsynchronousConnection {

	/**
	 * The selection key registered to the connection.
	 */
	private final SelectionKey selectionKey;

	/**
	 * The socket channel the connection utilized.
	 */
	private final SocketChannel socketChannel;

	/**
	 * The procedural steps for deciphering the RuneScape login protocol.
	 */
	private RuneScapeLoginProtocolOperator loginProtocolOperator = new RuneScapeHandshakeOperator();

	/**
	 * The underlying vector for reading.
	 */
	private final AsynchronousBuffer reader = new AsynchronousBuffer(ByteBuffer.allocate(512));

	/**
	 * The cipher and decipher for packet encryption. This system uses the ISAAC
	 * algorithm to read and write packet opcodes in a encrypted format.
	 */
	private CryptionAlgorithmicPair cryptionPair;

	/**
	 * The player associated with the connection.
	 */
	private Player player;

	/**
	 * The protocol response listener. This handles the two main protocol
	 * related reading events; the login procedure, and the packet reception.
	 */
	private RuneScapeProtocolListener listener = new RuneScapeLoginProtocolListener();

	/**
	 * The handler for events derived directly from the selector reactor
	 * pattern. Once received the event is dispatched to an implemented handler.
	 */
	private AsynchronousEventListener eventHandler = new AsynchronousAcceptingEvent();

	/**
	 * The default class constructor.
	 * 
	 * @param key The selection key registered to the connection.
	 * 
	 * @param channel The socket channel the connection utilized.
	 */
	public AsynchronousConnection(SelectionKey key, SocketChannel channel) {
		this.selectionKey = key;
		this.socketChannel = channel;
	}

	/**
	 * Returns the socket channel utilized.
	 * 
	 * @return The returned socket channel.
	 */
	public SocketChannel getSocketChannel() {
		
		return socketChannel;
	}

	/**
	 * Returns the selection key registered to the connection.
	 * 
	 * @return The returned selection key.
	 */
	public SelectionKey getSelectionKey() {
		
		return selectionKey;
	}

	/**
	 * Returns the underlying reading vector.
	 * 
	 * @return The returned vector.
	 */
	public AsynchronousBuffer getReader() {
		
		return reader;
	}

	/**
	 * Returns the cumulative protocol listener.
	 * 
	 * @return The returned listener.
	 */
	public RuneScapeProtocolListener getListener() {
		
		return listener;
	}

	/**
	 * Modifies the cumulative protocol listener.
	 * 
	 * @param listener The new modification.
	 */
	public void setListener(RuneScapeProtocolListener listener) {
		
		this.listener = listener;
	}

	/**
	 * Returns the handler for selector derived events.
	 * 
	 * @return The returned handler.
	 */
	public AsynchronousEventListener getEventHandler() {
		
		return eventHandler;
	}

	/**
	 * Modifies the handler for selector derived events.
	 * 
	 * @return The new modification.
	 */
	public void setEventHandler(AsynchronousEventListener eventHandler) {
		
		this.eventHandler = eventHandler;
	}

	/**
	 * Returns the player association.
	 * 
	 * @return The returned player.
	 */
	public Player getPlayer() {
		
		return player;
	}

	/**
	 * Modifies the player association.
	 * 
	 * @param player The new modification.
	 */
	public void setPlayer(Player player) {
		
		this.player = player;
	}

	/**
	 * Returns the encapsulated instance of the login protocol operator.
	 * 
	 * @return The returned instance.
	 */
	public RuneScapeLoginProtocolOperator getLoginProtocolOperator() {
		
		return loginProtocolOperator;
	}

	/**
	 * Modifies the instance of the login protocol operator.
	 * 
	 * @param loginProtocol The new modification.
	 */
	public void setLoginProtocolOperator(RuneScapeLoginProtocolOperator loginProtocol) {
		
		this.loginProtocolOperator = loginProtocol;
	}

	/**
	 * Returns the algorithmic pairs for ciphering and deciphering opcodes.
	 * 
	 * @return The returned algorithmic pairs.
	 */
	public CryptionAlgorithmicPair getCryptionPair() {
		
		return cryptionPair;
	}

	/**
	 * Modifies the algorithmic pairs for ciphering and deciphering opcodes.
	 * 
	 * @return The returned algorithmic pairs.
	 */
	public void setCryptionPair(CryptionAlgorithmicPair cryptionPair) {
		
		this.cryptionPair = cryptionPair;
	}

	/**
	 * Writes an outgoing message to the RuneScape client.
	 * 
	 * @param vector The dynamic backing of the message to be written.
	 */
	public void write(ByteBuffer vector) {
		try {
			
			vector.flip();
			
			getSocketChannel().write(vector);
		} catch (IOException exception) {
			
			exception.printStackTrace();
			
			getPlayer().remove();
		}
	}
}