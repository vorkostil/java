package game;

import network.client.ConnectionClient;

public interface GameProvider 
{

	// get the connection client used to send message
	ConnectionClient getConnectionClient();

	// call back after sending a end game message with a winner
	void closeGame(String id);

}
