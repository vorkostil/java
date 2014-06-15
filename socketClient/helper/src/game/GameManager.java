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
	
	public void closeAllGames() throws InterruptedException 
	{
		for ( AbstractGameClientFrame game : games.values() )
		{
			connectionClient.forwardInfo( "Game close with id: " + game.getId() );
			game.disposeView();
		}
		games.clear();
	}

	public void handleGameMessage(String message) 
	{
		String[] splitted = message.split( " ", 2 );
		String action = splitted[ 0 ];
		String remain = splitted[ 1 ];
		
		if ( action.compareTo( MessageType.MessageClose ) == 0 )
		{
			String[] information = remain.split( " ", 2 );
			closeGame( information[ 0 ], information[ 1 ] );
		}
		else if ( action.compareTo( MessageType.MessageGameJoinRefused ) == 0 )
		{
			String[] information = remain.split( " ", 2 );
			closeGame( information[ 0 ], information[ 1 ] );
		}
		else if ( action.compareTo( MessageType.MessageGameIsRefused ) == 0 )
		{
			connectionClient.forwardAlert( remain );
		}
		else if ( action.compareTo( MessageType.MessageGameIsAccepted ) == 0 )
		{
			String[] information = remain.split( " " );
			openGame( information[ 0 ], information[ 1 ] );
		}
//		else if ( action.compareTo( MessageType.MessageReady ) == 0 )
//		{
//			readyGame( gameId, message[ 4 ] );
//		}
//		else if ( action.compareTo( MessageType.MessageStart ) == 0 )
//		{
//			startGame( gameId );
//		}
//		else if ( action.compareTo( MessageType.MessageStartSoon ) == 0 )
//		{
//			startGameSoon( gameId );
//		}
//		else if ( action.compareTo( MessageType.MessageEnd ) == 0 )
//		{
//			endGame( gameId, message[ 4 ] );
//		}
//		else if ( action.compareTo( MessageType.MessageOpen ) == 0 )
//		{
//			openGame( gameId, message[ 4 ], message[ 5 ], message[ 6 ] );
//		}
//		else if ( action.compareTo( MessageType.MessageAsked ) == 0 )
//		{
//			// in fact, gameId is the name of the asker and the fourth element is the game kind
//			askForGameFrom( gameId, message[ 4 ] );
//		}
		else
		{
			// in a standard game message, action is the gameID
			forwardGameMessage(  action, remain.split( " " ) );
		}
	}

	private void closeGame( String gameId, String message) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			connectionClient.forwardInfo( "Game close with id: " + gameId + " for the following reason: " + message );
			games.get( gameId ).disposeView();
			games.remove( gameId );
		}
	}

	private void openGame( String gameId, String gameName ) 
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
					
					games.put( gameId, game);
					
					// send the join game message
					connectionClient.sendMessageIfConnected( MessageType.MessageSystemGameJoin + " " + gameId );
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
	
	private void endGame( String gameId, String winner ) throws InterruptedException 
	{
		if ( games.containsKey( gameId ) == true )
		{
			connectionClient.forwardInfo( "Game (" + gameId + ") end, winner is : " + winner );
			games.get( gameId ).end( winner );
			games.get( gameId ).disposeView();
			games.remove( gameId );
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
