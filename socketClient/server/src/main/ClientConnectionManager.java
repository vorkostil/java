package main;

import game.AbstractGameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import network.ConnectionServer;
import server.ChessGameServer;
import server.TronGameServer;

import common.MessageType;

public class ClientConnectionManager implements Runnable, ConnectionServer  {

   private static final String GRAPH_DISPLAYER_NAME = "Graph";
   
   private ServerSocket socketserver;
   private int currentIdClient = 0;
   private int currentClientSize = 0;
   private int maxClient = 0;
   private List< ClientConnection > clients = new ArrayList< ClientConnection >();
   
	HashMap< String, AbstractGameServer > games = new HashMap< String, AbstractGameServer >();

	public ClientConnectionManager(ServerSocket s, int maxClient) 
	{
		this.socketserver = s;
		this.maxClient = maxClient;
	}
	
	public void run() 
	{
        try 
        {
        	while ( true )
        	{
        		if ( currentClientSize < maxClient)
        		{
        			// there is enough room in the pool, accept whatever
					Socket socket = socketserver.accept(); // Un client se connecte on l'accepte
					
					currentClientSize++;
					System.out.println( "Client '" + currentIdClient + "' is now connected");
					
					// create the  Client connection
					ClientConnection client = new ClientConnection( socket, 
																	currentIdClient, 
																	this );
					
					
					System.out.println( "Client " + currentIdClient + " take a space in the pool (" +  currentClientSize + ")" );
					currentIdClient++;
					clients.add( client );
					
					// and launch the client connection listening
					Thread t = new Thread( client );
					t.start();
        		}
        		Thread.sleep( 10 );
        	}
        
        } 
        catch (IOException | InterruptedException e) 
        {
			e.printStackTrace();
		}
	}
	
	public synchronized void decreaseClientNumber( ClientConnection client )
	{
		currentClientSize--;
		clients.remove( client );
		List< String > gameToClose = new ArrayList< String >();
		for ( AbstractGameServer game : games.values() )
		{
			if ( game.containsPlayer( client.getLogin() ) == true )
			{
				gameToClose.add( game.getId() );
			}
		}
		for ( String gameId : gameToClose )
		{
			closeGame( gameId );
		}
		
		System.out.println( "Client " + client.getId() + " released a space in the pool (" +  currentClientSize + ") / games: " + games.size() );
		clientListChanged();
	}

	public void clientListChanged() 
	{
		String clientList = new String();
		for ( ClientConnection client : clients )
		{
			clientList += client.getLogin() + "µ"; // a weird separator but whatever
		}
		forwardToClients( MessageType.MessageSystem + " " + MessageType.MessageContactListSnapshot + " " + clientList );
	}

	public synchronized void forwardToClients( String str )
	{
		for ( ClientConnection client : clients )
		{
			client.forwardMessage ( str );
		}
	}
	
	public synchronized void forwardToClient(String name, String command) 
	{
		for ( ClientConnection client : clients )
		{
			if ( client.getLogin().compareTo( name ) == 0 )
			{
				client.forwardMessage( command );
				break;
			}
		}
	}
	
	public void beginGame( String login, String player, String gameKind ) 
	{
		String gameId = gameKind + "_" + player + "_" + login;
		if ( games.containsKey( gameId ) == false )
		{
			if ( gameKind.compareTo( TronGameServer.NAME ) == 0 )
			{
				games.put( gameId, new TronGameServer( player, login, gameId, this ) );
			}
			else if ( gameKind.compareTo( ChessGameServer.NAME ) == 0 )
			{
				games.put( gameId, new ChessGameServer( player, login, gameId, this ) );
			}
		}
	}
	
	public void beginSoloGame( String login, String gameKind ) 
	{
		String gameId = gameKind + "_SOLO_" + login;
		if ( games.containsKey( gameId ) == false )
		{
			if ( gameKind.compareTo( GRAPH_DISPLAYER_NAME ) == 0 )
			{
				// send the open game message
				forwardToClient( login, MessageType.MessageSystem + " " + MessageType.MessageGameOpen + " " + gameId + " " + gameKind + " " + login + " SOLO");
			}
		}
	}

	public void updateGamePlayerReady(String gameId, String player) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			AbstractGameServer game = games.get( gameId );
			game.setReady( player );
		}
	}

	public void updateGameSpecificMessage( String gameId, String command ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			AbstractGameServer game = games.get( gameId );
			game.manageSpecificMessage( command );
		}
	}

	public void closeGame(String gameId) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			AbstractGameServer game = games.get( gameId );
			games.remove( gameId );
			
			game.stop();
			game = null;
		}
	}
}