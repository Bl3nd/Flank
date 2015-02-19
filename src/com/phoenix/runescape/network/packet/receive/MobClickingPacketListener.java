package com.phoenix.runescape.network.packet.receive;

import com.phoenix.runescape.item.container.BankingInterfaceContainer.BankingInterfaceComposer;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.mobile.player.skill.SkillingActionCoordinator;
import com.phoenix.runescape.mobile.player.skill.impl.ThievingSkillingAction;
import com.phoenix.runescape.network.packet.IncomingPacket;
import com.phoenix.runescape.network.packet.IncomingPacketListener;
import com.phoenix.runescape.network.packet.IncomingPacketOpcode;
import com.phoenix.runescape.network.packet.dispatch.ChatBoxMessagePacket;

/**
 * The packet opcode which this listener implementation handles.
 */
@IncomingPacketOpcode({155, 17, 21, 72})

public final class MobClickingPacketListener extends IncomingPacketListener {

    private final int OPTION1 = 155;
    private final int OPTION2 = 17;
    private final int OPTION3 = 21;
    private final int ATTACK_MOB = 72;


    @Override
    public void operate(Player player, IncomingPacket packet) {
        switch (packet.getOpcode()) {
            case OPTION1:
                option1(player, packet);
                break;
            case OPTION2:
                option2(player, packet);
                break;
            case OPTION3:
                option3(player, packet);
                break;
            case ATTACK_MOB:
                attackMob(player, packet);
                break;
        }
    }

    public void option1(final Player player, IncomingPacket packet) {
        int index = packet.getBuffer().getShort();
        player.send(new ChatBoxMessagePacket("Option 1 Index: " + index));
        switch (index) {
            case 768:
                BankingInterfaceComposer.openBankAccount(player);
                break;
        }
    }

    public void option2(final Player player, IncomingPacket packet) {
        int index = packet.readLittleEndianShortAddition();
        player.send(new ChatBoxMessagePacket("Option 2 Index: " + index));
        switch (index) {

            case -32000:
                BankingInterfaceComposer.openBankAccount(player);
                break;

            case 1:
            case 2:
            case 3:
            case 4:
                SkillingActionCoordinator.getSingleton().startSkillingAction(player, new ThievingSkillingAction(index));
                break;
        }
    }

    public void option3(final Player player, IncomingPacket packet) {
        int index = packet.getBuffer().getShort();
        player.send(new ChatBoxMessagePacket("Option 3 Index: " + index));
        switch (index) {

        }
    }

    public void attackMob(final Player player, IncomingPacket packet) {
        int index = packet.getBuffer().getShort();
        player.send(new ChatBoxMessagePacket("Attack Mob Index: " + index));
        switch (index) {

        }
    }
}
