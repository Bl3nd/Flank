package com.phoenix.runescape.network;

import java.nio.ByteBuffer;

import com.phoenix.runescape.network.packet.cryption.CryptionAlgorithm;

/**
 * @author Dylan Vicchiarelli
 *
 * A utility class for building transmission structures.
 */
public final class AsynchronousBuffer {

	/**
	 * The underlying buffer.
	 */
	private ByteBuffer buffer;

	/**
	 * The current position of the bit modifier.
	 */
	private int bitPosition = 0;

	/**
	 * The current position of the packet length modifier.
	 */
	private int packetLengthPosition = 0;

	/**
	 * The containment for the bit mask modifiers.
	 */
	public static final int[] BIT_MASK = new int[32];

	/**
	 * The default class constructor.
	 * 
	 * @param bufer The underlying buffer.
	 */
	public AsynchronousBuffer(ByteBuffer buffer) {
		
		this.buffer = buffer;
	}

	/**
	 * Returns the underlying buffer.
	 * 
	 * @return The returned buffer.
	 */
	public final ByteBuffer getBuffer() {
		
		return buffer;
	}

	/**
	 * Reads a single signed short.
	 * 
	 * @return The short that was read.
	 */
	public final short readShort() {
		
		return getBuffer().getShort();
	}

	/**
	 * Starts sequential byte access.
	 */
	public final void startByteAcces() {
		
		getBuffer().position((getBitPosition() + 7) / 8);
	}

	/**
	 * Starts sequential bit access.
	 */
	public final void startBitAccess() {
		
		setBitPosition(getBuffer().position() * 8);
	}

	/**
	 * Writes a little endian short.
	 * 
	 * @param value The short to be written.
	 */
	public final void writeLittleEndianShort(int value) {
		writeByte(value);
		writeByte(value >> 8);
	}

	/**
	 * Writes a byte with a negated value.
	 * 
	 * @param value The value to negate.
	 */
	public final void writeNegatedByte(int value) {
		
		writeByte(-value);
	}

	/**
	 * Writes a signed subtrahend.
	 * 
	 * @param value The value of the subtrahend.
	 */
	public final void writeSubtrahend(int value) {
		
		writeByte(128 - value);
	}

	/**
	 * Reads a single signed integer.
	 * 
	 * @return The integer that was read.
	 */
	public final int readInt() {
		
		return getBuffer().getInt();
	}

	/**
	 * Reads a single signed byte.
	 * 
	 * @return The byte that was read.
	 */
	public final int readByte() {
		
		return getBuffer().get();
	}

	/**
	 * Reads a single signed long.
	 * 
	 * @return The long that was read.
	 */
	public final long readLong() {
		
		return getBuffer().getLong();
	}

	/**
	 * Writes a single signed long.
	 * 
	 * @param out The long to be written.
	 */
	public final void writeLong(long out) {
		
		getBuffer().putLong(out);
	}

	/**
	 * Writes a single signed byte.
	 * 
	 * @param out The byte to be written.
	 */
	public final void writeByte(int out) {
		
		getBuffer().put((byte) out);
	}

	/**
	 * Writes a single signed short.
	 * 
	 * @param out The short to be written.
	 */
	public final void writeShort(int out) {
		
		getBuffer().putShort((short) out);
	}	

	/**
	 * Returns the current bit position.
	 * 
	 * @return The returned position.
	 */
	public final int getBitPosition() {
		
		return bitPosition;
	}

	/**
	 * Modifies the current bit position.
	 * 
	 * @param bitPosition The new modification.
	 */
	public final void setBitPosition(int bitPosition) {
		
		this.bitPosition = bitPosition;
	}

	/**
	 * Returns the current packet length position.
	 * 
	 * @return the returned position.
	 */
	public final int getPacketLengthPosition() {
		
		return packetLengthPosition;
	}

	/**
	 * Modifies the current packet length position.
	 * 
	 * @param packetLengthPosition The new modification.
	 */
	public final void setPacketLengthPosition(int packetLengthPosition) {
		
		this.packetLengthPosition = packetLengthPosition;
	}

	/**
	 * Writes a series of bytes derived from the underlying buffer.
	 * 
	 * @param source The buffer the bytes where derived from.
	 */
	public final void writeBytes(ByteBuffer source) {
		for (int i = 0; i < source.position(); i++) {
			
			writeByte(source.get(i));
		}
	}

	/**
	 * Writes a single signed big integer.
	 * 
	 * @param value The value of the integer being written.
	 */
	public final void writeBigInteger(int value) {
		writeByte(value >> 16);
		writeByte(value >> 24);
		writeByte(value);
		writeByte(value >> 8);
	}

	/**
	 * Writes a single signed inverse integer.
	 * 
	 * @param value The value of the integer being written.
	 */
	public final void writeInverseInteger(int value) {
		writeByte(value >> 8);
		writeByte(value);
		writeByte(value >> 24);
		writeByte(value >> 16);
	}

	/**
	 * Writes a little endian short with an addition.
	 * 
	 * @param value The value of the short.
	 */
	public final void writeLittleEndianShortAddition(int value) {
		getBuffer().put((byte) (value + 128));
		getBuffer().put((byte) (value >> 8));
	}

	/**
	 * Reads a string composed of a series of signed bytes.
	 * 
	 * @param buffer The buffer containment.
	 * 
	 * @return The result of the operation.
	 */
	public final String readString(ByteBuffer buffer) {
		StringBuilder builder = new StringBuilder();
		
		for (char character = '\0'; buffer.hasRemaining() && character != '\n'; character = (char) (buffer.get() & 0xFF)) {
			
			builder.append(character);
		}
		return builder.toString();
	}

	/**
	 * Writes a string broken down into a series of bytes.
	 * 
	 * @param buffer The buffer containment.
	 * 
	 * @param string The string to be written.
	 */
	public final void writeString(String string) {
		getBuffer().put(string.getBytes());
		getBuffer().put((byte) 10);
	}

	/**
	 * Writes a raw or standard packet header with no modifier.
	 * 
	 * @param secure The encryption key for encoding the message.
	 * 
	 * @param value The value of the message.
	 */
	public final void writeRawPacketHeader(CryptionAlgorithm secure, int value) {
		
		writeByte(value + secure.getNextValue());
	}

	/**
	 * Writes a variable packet header modified by a byte.
	 * 
	 * @param secure The encryption key for encoding the message.
	 * 
	 * @param value The value of the message.
	 */
	public final void writeVariableBytePacketHeader(CryptionAlgorithm secure, int value) {
		writeRawPacketHeader(secure, value);
		setPacketLengthPosition(getBuffer().position());
		writeByte(0);
	}

	/**
	 * Writes a variable packet header modified by a short.
	 * 
	 * @param secure The encryption key for encoding the message.
	 * 
	 * @param value The value of the message.
	 */
	public final void writeVariableShortPacketHeader(CryptionAlgorithm secure, int value) {
		writeRawPacketHeader(secure, value);
		setPacketLengthPosition(getBuffer().position());
		writeShort((short) 0);
	}

	/**
	 * Completes the variable byte packet header.
	 */
	public final void finishVariableBytePacketHeader() {
		
		getBuffer().put(getPacketLengthPosition(), (byte) (getBuffer().position() - getPacketLengthPosition() - 1));
	}

	/**
	 * Completes the variable short packet header.
	 */
	public final void finishVariableShortPacketHeader() {
		
		getBuffer().putShort(getPacketLengthPosition(), (short) (getBuffer().position() - getPacketLengthPosition() - 2));
	}

	/**
	 * Writes an additional short.
	 * 
	 * @param value The value of the additional short.
	 */
	public final void writeAdditionalShort(int value) {
		writeByte((byte) (value >> 8));
		writeByte((byte) (value + 128));
	}

	/**
	 * Writes a specified set of bits.
	 * 
	 * @param amount The amount of bits to be written.
	 * 
	 * @param value The value of the bits being written.
	 */
	public final void writeBits(int amount, int value) {
		int bytePosition = getBitPosition() >> 3;
		int bitOffset = 8 - (getBitPosition() & 7);
		setBitPosition(getBitPosition() + amount);
		int requiredSpace = bytePosition - getBuffer().position() + 1;
		requiredSpace += (amount + 7) / 8;
		if (getBuffer().remaining() < requiredSpace) {
			ByteBuffer old = getBuffer();
			this.buffer = ByteBuffer.allocate(old.capacity() + requiredSpace);
			old.flip();
			getBuffer().put(old);
		}
		for (; amount > bitOffset; bitOffset = 8) {
			byte temporary = getBuffer().get(bytePosition);
			temporary &= ~BIT_MASK[bitOffset];
			temporary |= (value >> (amount - bitOffset)) & BIT_MASK[bitOffset];
			getBuffer().put(bytePosition++, temporary);
			amount -= bitOffset;
		}
		if (amount == bitOffset) {
			byte temporary = getBuffer().get(bytePosition);
			temporary &= ~BIT_MASK[bitOffset];
			temporary |= value & BIT_MASK[bitOffset];
			getBuffer().put(bytePosition, temporary);
		} else {
			byte temporary = getBuffer().get(bytePosition);
			temporary &= ~(BIT_MASK[amount] << (bitOffset - amount));
			temporary |= (value & BIT_MASK[amount]) << (bitOffset - amount);
			getBuffer().put(bytePosition, temporary);
		}
	}

	/**
	 * Statically initializes the bit masks upon class reference.
	 */
	static {
		for (int i = 0; i < BIT_MASK.length; i++) {
			
			BIT_MASK[i] = (1 << i) - 1;
		}
	}
}