package game;

import java.io.IOException;

import common.MessageType;

public class GameThread implements Runnable 
{
	private ConsumerGameManager gameManager;
	private String gameName;
	private String gameId;

	public GameThread( String gameName, 
					   String gameId, 
					   ConsumerGameManager gameManager ) 
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
			AbstractGameClientFrame game = gameManager.getConsumerConnectionClient().requireGame( gameName );
			if ( game != null )
			{
				game.setConnectionClient( gameManager.getConsumerConnectionClient() );
				game.setId( gameId );
				
				gameManager.insertGame( gameId, 
										game );
				
				// send the join game message
				gameManager.getConsumerConnectionClient().sendMessageIfConnected( MessageType.MessageSystemGameJoin + " " + gameId );
			}
		}
		catch (IOException e)
		{
			gameManager.getConsumerConnectionClient().forwardAlert( "Something bad happens during game creation ('" + gameName +"') > " + e.getMessage() );
		}
	}

}
