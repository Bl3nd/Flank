package com.phoenix.runescape.network.protocol.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.phoenix.runescape.Constants;
import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketRegistration;
import com.phoenix.runescape.network.protocol.RuneScapeProtocolListener;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the operation of decoding of the RuneScape packet protocol.
 */
public final class RuneScapePacketProtocolListener implements RuneScapeProtocolListener {

	/**
	 * The numerical length and opcode of the packet.
	 */
	private int length = -1, opcode = -1;
	
	/**
	 * Returns the numerical length of the packet.
	 * 
	 * @return The returned length.
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Modifies the numerical length of the packet.
	 * 
	 * @param length The new modification.
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Returns the numerical opcode of the packet.
	 * 
	 * @return The returned opcode.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Modifies the numerical opcode of the packet.
	 * 
	 * @param opcode The new modification.
	 */
	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}
	
	@Override
	public void receiveProtocol(AsynchronousConnection asynchronousConnection) throws IOException {
		while (asynchronousConnection.getReader().getBuffer().hasRemaining()) {
			if (getOpcode() == -1) {
				setOpcode(asynchronousConnection.getReader().getBuffer().get() - asynchronousConnection.getCryptionPair().getReader().getNextValue() & 0xFF);
				setLength(Constants.PACKET_SIZES[getOpcode()]);
			}
			if (getLength() == -1) {
				if (!asynchronousConnection.getReader().getBuffer().hasRemaining()) {
					asynchronousConnection.getReader().getBuffer().clear();
					return;
				}
				setLength(asynchronousConnection.getReader().getBuffer().get() & 0xFF);
			}
			if (asynchronousConnection.getReader().getBuffer().remaining() < getLength()) {
				asynchronousConnection.getReader().getBuffer().compact();
				return;
			}
			byte[] payloadLength = new byte[getLength()];
			asynchronousConnection.getReader().getBuffer().get(payloadLength);
			ByteBuffer packetPayload = ByteBuffer.allocate(getLength());
			packetPayload.put(payloadLength);
			packetPayload.flip();
			IncomingPacket packetRead = new IncomingPacket(packetPayload, getOpcode(), getLength());
			IncomingPacketRegistration.dispatchToListener(packetRead, asynchronousConnection.getPlayer());
			setOpcode(-1);
			setLength(-1);
		}
	}
}