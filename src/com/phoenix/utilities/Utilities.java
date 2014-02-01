package com.phoenix.utilities;

import java.io.DataInputStream;
import java.io.IOException;

import com.phoenix.runescape.Constants;
import com.phoenix.runescape.item.Item;
import com.phoenix.runescape.mobile.player.Player;

public class Utilities {
	
	public static final int levelForExperience(Player player, int skillIndex) {
		int calculation = 0;
		int result = 0;
		for (int level = 1; level <= 99; level++) {
			
			calculation = (int) (calculation + Math.floor(level + 300.0D * Math.pow(2.0D, level / 7.0D)));
			
			result = (int) Math.floor(calculation / 4);
			if (result >= player.getSkills().getSkills()[skillIndex].getExperience()) {
				
				return level;
			}
		}
		return 99;
	}

	public static String formatInputString(String string) {
		String result = "";
		for (String part : string.toLowerCase().split(" ")) {
			result = result + part.substring(0, 1).toUpperCase() + part.substring(1) + " ";
		}
		return result.trim();
	}

	public static String readInputString(DataInputStream input) throws IOException {
		StringBuilder builder = new StringBuilder();
		
		byte data;
		
		while ((data = input.readByte()) != 0) {
			
			builder.append((char) data);
		}
		return builder.toString();
	}

	public static String longToName(long name) {
		int i = 0;
		char[] ac = new char[12];
		while (name != 0L) {
			long l1 = name;
			name /= 37L;
			ac[(11 - i++)] = Constants.VALID_CHARACTERS[((int) (l1 - name * 37L))];
		}
		return new String(ac, 12 - i, i);
	}

	public static long convertStringToLong(String context) {
		long l = 0L;
		int i = 0;
		do {
			char c = context.charAt(i);
			l *= 37L;
			if ((c >= 'A') && (c <= 'Z')) {
				l += '\001' + c - 65;
			} else if ((c >= 'a') && (c <= 'z')) {
				l += '\001' + c - 97;
			} else if ((c >= '0') && (c <= '9')) {
				l += '\033' + c - 48;
			}
			i++;
			if (i >= context.length()) {
				break;
			}
		} while (i < 12);
		while ((l % 37L == 0L) && (l != 0L)) {
			l /= 37L;
		}
		return l;
	}

	public static int parseDirection(int deltaX, int deltaY) {
		if (deltaX < 0) {
			if (deltaY < 0) {
				return 5;
			}
			if (deltaY > 0) {
				return 0;
			}
			return 3;
		}
		if (deltaX > 0) {
			if (deltaY < 0) {
				return 7;
			}
			if (deltaY > 0) {
				return 2;
			}
			return 4;
		}
		if (deltaY < 0) {
			return 6;
		}
		if (deltaY > 0) {
			return 1;
		}
		return -1;
	}

	public static final void insert(Item[] items, int fromSlot, int toSlot) {
		Item from = items[fromSlot];
		if (from == null) {
			return;
		}
		items[fromSlot] = null;
		if (fromSlot > toSlot) {
			int shiftFrom = toSlot;
			int shiftTo = fromSlot;
			for (int i = toSlot + 1; i < fromSlot; i++) {
				if (items[i] == null) {
					shiftTo = i;
					break;
				}
			}
			Item[] slice = new Item[shiftTo - shiftFrom];
			System.arraycopy(items, shiftFrom, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, shiftFrom + 1, slice.length);
		} else {
			int sliceStart = fromSlot + 1;
			int sliceEnd = toSlot;
			for (int i = sliceEnd - 1; i >= sliceStart; i--) {
				if (items[i] == null) {
					sliceStart = i;
					break;
				}
			}
			Item[] slice = new Item[sliceEnd - sliceStart + 1];
			System.arraycopy(items, sliceStart, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, sliceStart - 1, slice.length);
		}
		items[toSlot] = from;
	}
}