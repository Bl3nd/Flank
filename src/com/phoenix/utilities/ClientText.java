package com.phoenix.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.packet.dispatch.StringLayerPacket;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles ease of access for the use of packet one twenty six which 
 * allows you to override text in the RuneScape client.
 */
public final class ClientText {
	
	/**
	 * The class instance to avoid direct static access upon class 
	 * reference.
	 */
	private static final ClientText classInstance = new ClientText();
	
	/**
	 * A collection of the developer defined text layers.
	 */
	private static final Set<Layer> definitions = new HashSet<Layer>();
	
	/**
	 * Displays the parsed definitions for a player upon initial connection.
	 * 
	 * @param player The player to display the text definitions for.
	 */
	public final void display(Player player) {
		
		for (final Layer layer : getDefinitions()) {
			
			player.send(new StringLayerPacket(layer.getText(), layer.getOpcode()));
		}
	}
	
	/**
	 * Loads the text definitions for an external text file.
	 * 
	 * @throws FileNotFoundException The exception thrown if an error occurs
	 * during the location of this text file.
	 */
	public final void loadDefinitions() throws FileNotFoundException {
		final Scanner scanner = new Scanner(new File("./data/configuration/text-line.txt"));
		
		while (scanner.hasNext()) {
			
			if (scanner.next().equals("[create]")) {
				
				if (scanner.hasNextInt()) {
					final int opcode = scanner.nextInt();
					
					if (scanner.next().equals("[define]")) {
						
						getDefinitions().add(new Layer(opcode, scanner.next().replace("_", " ")));
					}
				}
			}
		}
		scanner.close();
	}

	/**
	 * Returns an encapsulation of the class instance.
	 * 
	 * @return The returned instance.
	 */
	public static final ClientText getClassInstance() {
		
		return classInstance;
	}
	
	/**
	 * Returns an encapsulation of the definition collection.
	 * 
	 * @return The returned definitions.
	 */
	public static final Set<Layer> getDefinitions() {
		
		return definitions;
	}

	/**
	 * A nested class to represent the composition of the layer being
	 *  overwritten.
	 */
	public static final class Layer {
		
		/**
		 * The new text to display.
		 */
		private final String text;
		
		/**
		 * The opcode of the line.
		 */
		private final int opcode;
		
		/**
		 * The default class constructor.
		 * 
		 * @param text The new text to display.
		 * 
		 * @param opcode The opcode of the line.
		 */
		public Layer(int opcode, String text) {
			
			this.text = text;
			this.opcode = opcode;
		}

		/**
		 * Returns an encapsulation of the new text.
		 * 
		 * @return The returned text string.
		 */
		public String getText() {
			
			return text;
		}

		/**
		 * Returns an encapsulation of the layer's opcode.
		 * 
		 * @return The returned opcode.
		 */
		public int getOpcode() {
			
			return opcode;
		}
	}
}