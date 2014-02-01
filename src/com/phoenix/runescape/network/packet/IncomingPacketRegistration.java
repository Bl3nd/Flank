package com.phoenix.runescape.network.packet;

import java.util.HashMap;
import java.util.Map;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.receive.ActionButtonPacketListener;
import com.phoenix.runescape.network.packet.receive.AppearancePacketListener;
import com.phoenix.runescape.network.packet.receive.ClickItemPacketListener;
import com.phoenix.runescape.network.packet.receive.CommunicationPacketListener;
import com.phoenix.runescape.network.packet.receive.DialoguePacketListener;
import com.phoenix.runescape.network.packet.receive.DropItemPacketListener;
import com.phoenix.runescape.network.packet.receive.EnterSetAmountPacketListener;
import com.phoenix.runescape.network.packet.receive.EquipmentPacketListener;
import com.phoenix.runescape.network.packet.receive.InterfaceItemActionPacketListener;
import com.phoenix.runescape.network.packet.receive.ItemTransitionPacketListener;
import com.phoenix.runescape.network.packet.receive.MobClickingPacketListener;
import com.phoenix.runescape.network.packet.receive.MovementPacketListener;
import com.phoenix.runescape.network.packet.receive.ObjectActionPacketListener;
import com.phoenix.runescape.network.packet.receive.PickupItemPacketListener;
import com.phoenix.runescape.network.packet.receive.PrivateMessagingPacketListener;
import com.phoenix.runescape.network.packet.receive.RegionalUpdatePacketListener;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the registration and operation of incoming packets.
 */
public final class IncomingPacketRegistration {

	/**
	 * A collection of the incoming packets that where registered during initialization.
	 */
	private final static Map<Integer, IncomingPacketListener> INCOMING_PACKETS = new HashMap<>();

	/**
	 * The default class constructor. Populates the packet collection
	 * upon class reference.
	 */
	public IncomingPacketRegistration() {
		registerPacket(new MovementPacketListener());
		registerPacket(new CommunicationPacketListener());
		registerPacket(new ActionButtonPacketListener());
		registerPacket(new PrivateMessagingPacketListener());
		registerPacket(new EquipmentPacketListener());
		registerPacket(new ItemTransitionPacketListener());
		registerPacket(new InterfaceItemActionPacketListener());
		registerPacket(new DropItemPacketListener());
		registerPacket(new PickupItemPacketListener());
		registerPacket(new RegionalUpdatePacketListener());
		registerPacket(new EnterSetAmountPacketListener());
		registerPacket(new ObjectActionPacketListener());
		registerPacket(new AppearancePacketListener());
		registerPacket(new ClickItemPacketListener());
		registerPacket(new DialoguePacketListener());
		registerPacket(new MobClickingPacketListener());
	}

	/**
	 * Registers the annotated opcode(s) possessed by the packet listener.
	 * 
	 * @param listener The listener implementation - the owner of the annotated opcodes.
	 */
	private static final void registerPacket(IncomingPacketListener listener) {
		IncomingPacketOpcode annotation = listener.getClass().getAnnotation(IncomingPacketOpcode.class);
		if (annotation != null) {
			for (int opcode : annotation.value()) {
				INCOMING_PACKETS.put(opcode, listener);
			}
		}
	}

	/**
	 * Handles a packet request sent by the RuneScape client. Once the
	 * request has been received it is dispatched to the corresponding listener.
	 * 
	 * @param packet The packet being handled.
	 * 
	 * @param player The player association.
	 */
	public static final void dispatchToListener(IncomingPacket packet, Player player) {
		IncomingPacketListener listener = INCOMING_PACKETS.get(packet.getOpcode());
		if (listener != null) {
			listener.operate(player, packet);
		}
	}
}