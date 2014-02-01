package com.phoenix.runescape.network.protocol.login.impl;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.phoenix.runescape.World;
import com.phoenix.runescape.mobile.player.Player;
import com.phoenix.runescape.network.AsynchronousBuffer;
import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.packet.cryption.CryptionAlgorithm;
import com.phoenix.runescape.network.packet.cryption.CryptionAlgorithmicPair;
import com.phoenix.runescape.network.protocol.login.RuneScapeLoginProtocolOperator;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the multiple step connection request sent by the RuneScape client.
 */
public final class RuneScapeLoginRequestOperator implements RuneScapeLoginProtocolOperator {

	@Override
	public void executeOperator(AsynchronousConnection asynchronousConnection) throws IOException {
		if (asynchronousConnection.getReader().getBuffer().remaining() < 2) {
			
			asynchronousConnection.getReader().getBuffer().compact();
            return;
        }
		
		int loginProtocolOpcode = asynchronousConnection.getReader().readByte() & 0xFF;
		
		if (loginProtocolOpcode != 16 && loginProtocolOpcode != 18) {
			
			asynchronousConnection.getSocketChannel().close();
			return;
		}
		
		int loginBlockLength = asynchronousConnection.getReader().readByte() & 0xFF;
		
		if (asynchronousConnection.getReader().getBuffer().remaining() < loginBlockLength) {
			
			asynchronousConnection.getReader().getBuffer().compact().flip();
			return;
		}
		
		asynchronousConnection.getReader().readByte();
		
		if (asynchronousConnection.getReader().readShort() != 317) {
			
			asynchronousConnection.getSocketChannel().close();
			return;
		}
		asynchronousConnection.getReader().readByte();
		
		for (int i = 0; i < 9; i ++) {
			
			asynchronousConnection.getReader().readInt();
		}
		
		asynchronousConnection.getReader().readByte();
		
		if ((asynchronousConnection.getReader().readByte() & 0xFF) != 10) {
			
			asynchronousConnection.getSocketChannel().close();
			return;
		}
		
		long clientHalf = asynchronousConnection.getReader().readLong();
		
		long serverHalf = asynchronousConnection.getReader().readLong();
		
		int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
		
		CryptionAlgorithm secureRead = new CryptionAlgorithm(isaacSeed);
		
		for (int i = 0; i < isaacSeed.length; i++) {
			
			isaacSeed[i] += 50;
		}
		
		asynchronousConnection.getReader().readInt();
		
		CryptionAlgorithm secureWrite = new CryptionAlgorithm(isaacSeed);
		asynchronousConnection.setCryptionPair(new CryptionAlgorithmicPair(secureRead, secureWrite));
		
		String accountName = asynchronousConnection.getReader().readString(asynchronousConnection.getReader().getBuffer()).trim();
		String accountPassword = asynchronousConnection.getReader().readString(asynchronousConnection.getReader().getBuffer()).trim();
		
		asynchronousConnection.setPlayer(new Player(asynchronousConnection, accountName, accountPassword));
		AsynchronousBuffer response = new AsynchronousBuffer(ByteBuffer.allocate(4));
		
		int responseIndex = 2;
		
		if (!asynchronousConnection.getPlayer().load()) {
			
			responseIndex = 3;
		}
        
		if (asynchronousConnection.getPlayer().getAccountName().length() > 12) {
            
			responseIndex = 8;
        }
        
		if (World.getInstance().isLoggedIn(asynchronousConnection.getPlayer().getAccountName())) {
            
			responseIndex = 5;
        }
		
		response.writeByte(responseIndex);
		response.writeByte((int) asynchronousConnection.getPlayer().getAttributeAsInteger("player-rights"));
		response.writeByte(0);
		
		asynchronousConnection.write(response.getBuffer());
		
		if (responseIndex != 2) {
			
			asynchronousConnection.getSocketChannel().close();
			return;
		}
		
		asynchronousConnection.setLoginProtocolOperator(new RuneScapeLoginCompletionOperator());
	}
}