package com.phoenix.runescape.mobile.player;

import java.nio.ByteBuffer;

import com.phoenix.runescape.Constants;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.packet.receive.EquipmentPacketListener;
import com.phoenix.utilities.Utilities;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the procedural steps in the player updating process.
 * These steps include the updating of our players movement, the updating
 * of the other players movements, and the updating of the incoming mask blocks.
 */
public class PlayerUpdateListenerEvent extends PlayerUpdateListener {

	@Override
	public void updateOtherPlayerMovement(Player player, AsynchronousBuffer packet) {
		if (player.getWalkingDirection() == -1) {
			if (player.isUpdateRequired()) {
				packet.writeBits(1, 1);
				packet.writeBits(2, 0);
			} else {
				packet.writeBits(1, 0);
			}
		} else {
			if (player.getRunningDirection() == -1) {
				packet.writeBits(1, 1);
				packet.writeBits(2, 1);
				packet.writeBits(3, player.getWalkingDirection());
				packet.writeBits(1, player.isUpdateRequired() ? 1 : 0);
			} else {
				packet.writeBits(1, 1);
				packet.writeBits(2, 2);
				packet.writeBits(3, player.getWalkingDirection());
				packet.writeBits(3, player.getRunningDirection());
				packet.writeBits(1, player.isUpdateRequired() ? 1 : 0);
			}
		}
	}

	@Override
	public void updateThisPlayerMovement(Player player, AsynchronousBuffer packet) {
		if (player.getUpdateFlags().contains("teleporting") || player.getUpdateFlags().contains("map-region")) {
			packet.writeBits(1, 1);
			packet.writeBits(2, 3);
			packet.writeBits(2, player.getPosition().getPlane());
			packet.writeBits(1, 1);
			packet.writeBits(1, player.isUpdateRequired() ? 1 : 0);
			packet.writeBits(7, player.getPosition().getLocalY(player.getLastPosition()));
			packet.writeBits(7, player.getPosition().getLocalX(player.getLastPosition()));
		} else {
			if (player.getWalkingDirection() == -1) {
				if (player.isUpdateRequired()) {
					packet.writeBits(1, 1);
					packet.writeBits(2, 0);
				} else {
					packet.writeBits(1, 0);
				}
			} else {
				if (player.getRunningDirection() == -1) {
					packet.writeBits(1, 1);
					packet.writeBits(2, 1);
					packet.writeBits(3, player.getWalkingDirection());
					packet.writeBits(1, player.isUpdateRequired() ? 1 : 0);
				} else {
					packet.writeBits(1, 1);
					packet.writeBits(2, 2);
					packet.writeBits(3, player.getWalkingDirection());
					packet.writeBits(3, player.getRunningDirection());
					packet.writeBits(1, player.isUpdateRequired() ? 1 : 0);
				}
			}
		}
	}

	@Override
	public void updatePlayerState(Player player, AsynchronousBuffer update, boolean forced, boolean chat) {
		synchronized(player) {
			int mask = 0x0;
			if (player.getUpdateFlags().contains("graphics")) {
				mask |= 0x100;
			}
			if (player.getUpdateFlags().contains("animation")) {
				mask |= 0x8;
			}
			if (player.getUpdateFlags().contains("chat") && !chat) {
				mask |= 0x80;
			}
			if (player.getUpdateFlags().contains("appearance") || forced) {
				mask |= 0x10;
			}
			if (mask >= 0x100) {
				mask |= 0x40;
				update.writeByte((byte) (mask & 0xFF));
				update.writeByte((byte) (mask >> 8));
			} else {
				update.writeByte((byte) mask);
			}
			if (player.getUpdateFlags().contains("graphics")) {
				update.writeLittleEndianShort(player.getAttributeAsInteger("graphic"));
				update.getBuffer().putInt(player.getAttributeAsInteger("graphic-delay"));
			}
			if (player.getUpdateFlags().contains("animation")) {
				update.writeLittleEndianShort(player.getAttributeAsInteger("animation"));
				update.writeByte((byte) -player.getAttributeAsInteger("animation-delay"));
			}
			if (player.getUpdateFlags().contains("chat") && !chat) {
				update.writeLittleEndianShort(((player.getChatTextColor() & 0xFF) << 8) + (player.getChatTextEffects() & 0xFF));
				update.writeByte(0);
				update.writeByte((byte) -player.getChatText().length);
				update.getBuffer().put(player.getChatText());
			}
			if (player.getUpdateFlags().contains("appearance") || forced) {
				AsynchronousBuffer properties = new AsynchronousBuffer(ByteBuffer.allocate(128));
				
				properties.writeByte(player.getGender());

				properties.writeByte(0);

				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_HEAD] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_HEAD].getIndex());
				} else {
					properties.writeByte(0);
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_CAPE] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_CAPE].getIndex());
				} else {
					properties.writeByte(0);
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_AMULET] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_AMULET].getIndex());
				} else {
					properties.writeByte(0);
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_WEAPON].getIndex());
				} else {
					properties.writeByte(0);
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_CHEST] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_CHEST].getIndex());
				} else {
					properties.writeShort((short) (0x100 + player.getAppearanceValues()[0]));
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_SHIELD] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_SHIELD].getIndex());
				} else {
					properties.writeByte(0);
				}

				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_CHEST] != null) {
					if (!EquipmentPacketListener.isFullChestGear(player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_CHEST].getIndex())) {
						properties.writeShort(0x100 + player.getAppearanceValues()[1]);
					} else {
						properties.writeByte(0);
					}
				} else {
					properties.writeShort((short) (0x100 + player.getAppearanceValues()[1]));
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_LEGS] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_LEGS].getIndex());
				} else {
					properties.writeShort((short) (0x100 + player.getAppearanceValues()[2]));
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_HEAD] != null) {
					if (!EquipmentPacketListener.isFullHeadGear(player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_HEAD].getIndex())) {
						properties.writeShort(0x100 + player.getAppearanceValues()[3]);
					} else {
						properties.writeByte(0);
					}
				} else {
					properties.writeShort((short) (0x100 + player.getAppearanceValues()[3]));
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_FEET] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_FEET].getIndex());
				} else {
					properties.writeShort((short) (0x100 + player.getAppearanceValues()[4]));
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_HANDS] != null) {
					properties.writeShort(0x200 + player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_HANDS].getIndex());
				} else {
					properties.writeShort((short) (0x100 + player.getAppearanceValues()[5]));
				}
				
				if (player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_HEAD] != null) {
					if (!EquipmentPacketListener.isFullHeadGear(player.getEquipmentContainer().getItems()[Constants.EQUIPMENT_SLOT_HEAD].getIndex())) {
						properties.writeShort((short) (0x100 + player.getAppearanceValues()[6]));
					} else {
						properties.writeByte(0);
					}
				} else {
					if (player.getGender() == 0) {
						properties.writeShort((short) (0x100 + player.getAppearanceValues()[6]));
					} else {
						properties.writeByte(0);
					}
				}
				
				properties.writeByte((byte) player.getColorValues()[0]);
				properties.writeByte((byte) player.getColorValues()[1]);
				properties.writeByte((byte) player.getColorValues()[2]);
				properties.writeByte((byte) player.getColorValues()[3]);
				properties.writeByte((byte) player.getColorValues()[4]);
				properties.writeShort((short) player.getAttributeAsInteger("stand-index")); 
				properties.writeShort((short) player.getAttributeAsInteger("stand-turn-index")); 
				properties.writeShort((short) player.getAttributeAsInteger("walk-index")); 
				properties.writeShort((short) 0x334); 
				properties.writeShort((short) 0x335); 
				properties.writeShort((short) 0x336); 
				properties.writeShort((short) player.getAttributeAsInteger("run-index"));
				properties.writeLong(Utilities.convertStringToLong(player.getAccountName()));
				properties.writeByte(3);
				properties.writeShort((short) 0);
				update.writeByte((byte) -properties.getBuffer().position());
				update.writeBytes(properties.getBuffer());
			}
		}
	}
}