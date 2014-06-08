package game;

import java.util.ArrayList;
import java.util.List;

import network.ConnectionServer;

import common.MessageType;

public abstract class AbstractGameServer 
{
	class PlayerProperties
	{
		String name;
		boolean isReady;
		
		public PlayerProperties(String name) 
		{
			this.name = name;
			isReady = false;
		}

		public String getName() 
		{
			return name;
		}

		public void setReady( boolean status ) 
		{
			isReady = status;
		}
		
		public boolean getReady() 
		{
			return isReady;
		}
	}
	
	protected String id;
	protected List< PlayerProperties > players;
	protected ConnectionServer connectionServer;
	
	protected AbstractGameServer( String id, ConnectionServer connectionServer )
	{
		this.id = id;
		this.connectionServer = connectionServer;
		
		players = new ArrayList< PlayerProperties >();
	}
	
	// return the game id
	public String getId() 
	{
		return id;
	}

	// add a player to the list of players
	protected void addPlayer( String playerName ) 
	{
		players.add( new PlayerProperties( playerName ) );
	}
	
	// set the readiness status of the player to TRUE and forward the change to all players
	public void setReady( String playerName )
	{
		// set the readiness status of the player
		for ( PlayerProperties player : players )
		{
			if ( playerName.compareTo( player.getName() ) == 0 )
			{
				player.setReady( true );
				break;
			}
		}
		
		// forward the status to all the players
		forwardMessageToAllPlayer( MessageType.MessageSystem + " " + MessageType.MessageGameReady + " " + id + " " + playerName );
		
		// check if all players are ready
		boolean allReady = true;
		for ( PlayerProperties player : players )
		{
			allReady &= player.getReady();
		}
		
		// and send the start soon message
		if ( allReady == true )
		{
			forwardMessageToAllPlayer( MessageType.MessageSystem + " " + MessageType.MessageGameStartSoon + " " + id );
			
			// then launch the gameStart message to the game
			callGameStart();
		}
	}
	
	// return true if the player is known in this game
	public boolean containsPlayer( String login )
	{
		for ( PlayerProperties player : players )
		{
			if ( login.compareTo( player.getName() ) == 0 )
			{
				return true;
			}
		}
		return false;
	}

	// create the generic open message and send it to all the players
	public void sendOpenMessageToPlayers() 
	{
		// create the message to send
		String msg = MessageType.MessageSystem + " " + MessageType.MessageGameOpen + " " + id + " " + getName();
		for ( PlayerProperties player : players )
		{
			msg += " " + player.getName();
		}
		
		// send the message to the players
		forwardMessageToAllPlayer( msg );
	}
	
	// forward the given message to all the players
	public void forwardMessageToAllPlayer( String message )
	{
		for ( PlayerProperties player : players )
		{
			connectionServer.forwardToClient( player.getName(), message);
		}
	}

	// send the game start message to all player
	public void sendGameStartMessage()
	{
		forwardMessageToAllPlayer( MessageType.MessageSystem + " " + MessageType.MessageGameStart + " " + id );		
	}
	
	// send the game stop message to all player with the winner inside
	// and ask the manager to close the game
	public void sendGameEndMessage( String winner )
	{
		forwardMessageToAllPlayer( MessageType.MessageSystem + " " + MessageType.MessageGameEnd + " " + id + " " + winner );
		connectionServer.closeGame( id );
	}
	
	// the implementation of this callback MUST call the sendGameStartMessage
	abstract protected void callGameStart();

	// the stop method 
	abstract public void stop();

	// use to manage specific message, obviously different for each game  
	abstract public void manageSpecificMessage(String command);
	
	// get the name identifier for the game
	abstract protected String getName();
	
}
