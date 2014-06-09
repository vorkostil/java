package game;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import network.client.ConnectionClient;

import common.MessageType;

public class GameManager 
{
	HashMap< String, AbstractGameClientFrame > games = new HashMap< String, AbstractGameClientFrame >();
	ConnectionClient connectionClient;

	public GameManager( ConnectionClient connectionClient )
	{
		this.connectionClient = connectionClient;
	}
	
	public void closeAllGames() 
	{
		for ( AbstractGameClientFrame game : games.values() )
		{
			connectionClient.forwardInfo( "Game close with id: " + game.getId() );
			game.dispose();
			games.remove( game.getId() );
		}
	}

	public void handleGameMessage(String[] messageComponents) 
	{
		String action = messageComponents[ 2 ];
		String gameId = messageComponents[ 3 ];
		
		if ( action.compareTo( MessageType.MessageClose ) == 0 )
		{
			closeGame( gameId, true );
		}
		else if ( action.compareTo( MessageType.MessageReady ) == 0 )
		{
			readyGame( gameId, messageComponents[ 4 ] );
		}
		else if ( action.compareTo( MessageType.MessageStart ) == 0 )
		{
			startGame( gameId );
		}
		else if ( action.compareTo( MessageType.MessageStartSoon ) == 0 )
		{
			startGameSoon( gameId );
		}
		else if ( action.compareTo( MessageType.MessageEnd ) == 0 )
		{
			endGame( gameId, messageComponents[ 4 ] );
		}
		else if ( action.compareTo( MessageType.MessageOpen ) == 0 )
		{
			openGame( gameId, messageComponents[ 4 ], messageComponents[ 5 ], messageComponents[ 6 ] );
		}
		else if ( action.compareTo( MessageType.MessageAsked ) == 0 )
		{
			// in fact, gameId is the name of the asker and the fourth element is the game kind
			askForGameFrom( gameId, messageComponents[ 4 ] );
		}
		else
		{
			forwardGameMessage( gameId, messageComponents );
		}
	}

	private void closeGame( String gameId, boolean destroy) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			connectionClient.forwardInfo( "Game close with id: " + gameId );
			if ( destroy == true )
				games.get( gameId ).dispose();
			games.remove( gameId );
		}
	}

	private void openGame( String gameId, String gameName, String firstPlayer, String secondPlayer ) 
	{
		if ( games.containsKey( gameId ) == false )
		{
			connectionClient.forwardInfo( "Game " + gameName + " open with id: " + gameId );
			try
			{
				AbstractGameClientFrame game = connectionClient.requireGame( gameName );
				if ( game != null )
				{
					game.setConnectionClient( connectionClient );
					game.setId( gameId );
					game.addPlayer( firstPlayer );
					game.addPlayer( secondPlayer );
					
					games.put( gameId, game);
				}
			}
			catch (IOException e)
			{
				connectionClient.forwardAlert( "Something bad happens during game creation ('" + gameName +"') > " + e.getMessage() );
			}
		}
	}

	private void readyGame( String gameId, String name ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			connectionClient.forwardInfo( "Game " + gameId + ", player " + name + " is ready" );
			games.get( gameId ).ready( name );
		}
	}

	private void startGame( String gameId ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			connectionClient.forwardInfo( "Game " + gameId + " will start" );
			games.get( gameId ).start();
		}
	}

	private void startGameSoon(String gameId) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			connectionClient.forwardInfo( "Game " + gameId + " will start soon" );
			games.get( gameId ).startSoon();
		}
	}

	private void askForGameFrom( String opponentName, String gameName) 
	{
		int response = JOptionPane.showConfirmDialog(null, "Would you play " + gameName + " with " + opponentName + " ?", "Game launch", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if ( response == JOptionPane.OK_OPTION)
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameAccepted + " " + opponentName + " " + gameName );
		}
		else
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameRefused + " " + opponentName );
		}
	}
	
	private void endGame( String gameId, String winner ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			games.get( gameId ).end( winner );
			closeGame(gameId, true);
		}
	}

	private void forwardGameMessage(String gameId, String[] messageComponents) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			games.get( gameId ).handleServerMessage( messageComponents );
		}
	}
}
