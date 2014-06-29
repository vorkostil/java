package game;

import network.client.ConnectionClient;

public abstract class AbstractGameProvider 
{
	protected String id;
	protected GameProvider gameProvider;
	protected ConnectionClient connectionClient;
	
	public AbstractGameProvider(String gameId, GameProvider gameProvider) 
	{
		this.id = gameId;
		this.gameProvider = gameProvider;
		this.connectionClient = gameProvider.getConnectionClient();
	}

	abstract public void playerJoinGame(String playerName);

	abstract public void playerLeaveGame(String playerName);

	abstract public void handleMessage(String action, String remain);

}
