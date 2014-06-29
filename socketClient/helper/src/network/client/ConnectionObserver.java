package network.client;

import java.io.IOException;
import java.net.Socket;

import network.client.ConnectionClient.State;

public interface ConnectionObserver {

	// call when an alert is raise related to the connection status
	abstract void raiseAlert( String message );

	// call when an info is raise related to the connection status
	abstract void raiseInfo( String message );
	
	// call when the server is disconnected
	abstract void serverDisconnection();

	// call when an alert is raise related to the connection status
	abstract void connectionStatusChange( State currentState );

	// call to get the socket listener used when a line is received
	abstract AbstractSocketListenerClientSide createSocketListener(Socket socket) throws IOException;

	// manage message explicitly marked as GAME
	abstract void handleGameMessage(String message);

	// the callback for login acceptation, use to register on the BBServer
	abstract void onLoginAccepted();

	// manage message explicitly not marked as GAME
	abstract void handleSystemMessage(String message);
}
