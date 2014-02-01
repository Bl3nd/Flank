package com.phoenix.runescape.network.packet.cryption;

/**
 * @author Dylan Vicchiarelli
 *
 * Represents a pair of algorithms used for the ciphering and deciphering of.
 * packet opcodes
 */
public final class CryptionAlgorithmicPair {
	
	/**
	 * The algorithm for deciphering packet opcodes.
	 */
	private final CryptionAlgorithm reader;
	
	/**
	 * The algorithm for ciphering packet opcodes.
	 */
	private final CryptionAlgorithm writer;
	
	/**
	 * The default class constructor.
	 * 
	 * @param reader The algorithm for deciphering packet opcodes.
	 * 
	 * @param writer The algorithm for ciphering packet opcodes.
	 */
	public CryptionAlgorithmicPair(CryptionAlgorithm reader, CryptionAlgorithm writer) {
		this.reader = reader;
		this.writer = writer;
	}

	/**
	 * Returns the algorithm for ciphering packet opcodes.
	 * 
	 * @return The returned algorithm.
	 */
	public CryptionAlgorithm getWriter() {
		return writer;
	}
	
	/**
	 * Returns the algorithm for deciphering packet opcodes.
	 * 
	 * @return The returned algorithm.
	 */
	public CryptionAlgorithm getReader() {
		return reader;
	}
}