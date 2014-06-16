package game;

import java.io.IOException;

import common.MessageType;

public class GameThread implements Runnable 
{
	private GameManager gameManager;
	private String gameName;
	private String gameId;

	public GameThread( String gameName, 
					   String gameId, 
					   GameManager gameManager ) 
	{
		this.gameName = gameName;
		this.gameId = gameId;
		this.gameManager = gameManager;
	}

	@Override
	public void run() 
	{
		try
		{
			AbstractGameClientFrame game = gameManager.getConnectionClient().requireGame( gameName );
			if ( game != null )
			{
				game.setConnectionClient( gameManager.getConnectionClient() );
				game.setId( gameId );
				
				gameManager.insertGame( gameId, 
										game );
				
				// send the join game message
				gameManager.getConnectionClient().sendMessageIfConnected( MessageType.MessageSystemGameJoin + " " + gameId );
			}
		}
		catch (IOException e)
		{
			gameManager.getConnectionClient().forwardAlert( "Something bad happens during game creation ('" + gameName +"') > " + e.getMessage() );
		}
	}

}
