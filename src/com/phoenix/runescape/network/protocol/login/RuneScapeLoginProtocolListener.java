package com.phoenix.runescape.network.protocol.login;

import java.io.IOException;

import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.protocol.RuneScapeProtocolListener;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the event of decoding the steps in the RuneScape login protocol.
 */
public final class RuneScapeLoginProtocolListener implements RuneScapeProtocolListener {
	
	@Override
	public void receiveProtocol(AsynchronousConnection asynchronousConnection) throws IOException {
		
		asynchronousConnection.getLoginProtocolOperator().executeOperator(asynchronousConnection);
	}
}