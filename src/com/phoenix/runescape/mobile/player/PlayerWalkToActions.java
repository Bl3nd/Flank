package com.phoenix.runescape.mobile.player;

import com.phoenix.runescape.Coordinate;
import com.phoenix.runescape.item.container.BankingInterfaceContainer.BankingInterfaceComposer;

public final class PlayerWalkToActions {
	
	public static final void objectFirstClick(final Player player, final Coordinate point, final int index) {

		switch (index) {
		
		case 2213:
			BankingInterfaceComposer.openBankAccount(player);
			break;
		}
	}
}