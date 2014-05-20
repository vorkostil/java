package main;

import game.TronGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import common.MessageType;

public class ClientConnectionManager implements Runnable {

   private ServerSocket socketserver;
   private int currentIdClient = 0;
   private int currentClientSize = 0;
   private int maxClient = 0;
   private List< ClientConnection > clients = new ArrayList< ClientConnection >();
   
	HashMap< String, TronGame > games = new HashMap< String, TronGame >();

	public ClientConnectionManager(ServerSocket s, int maxClient) 
	{
		this.socketserver = s;
		this.maxClient = maxClient;
	}
	
	public void run() {

        try {
        	while ( true ){
        		if ( currentClientSize < maxClient)
        		{
        			// there is enough room in the pool, accept whatever
					Socket socket = socketserver.accept(); // Un client se connecte on l'accepte
					currentClientSize++;
					System.out.println("Le client id " + currentIdClient + " est connecté !");
					ClientConnection client = new ClientConnection( socket, currentIdClient, this );
					Thread t = new Thread( client );
					System.out.println( "Client " + currentIdClient + " take a space in the pool (" +  currentClientSize + ")" );
					currentIdClient++;
					clients.add( client );
					t.start();
        		}
        		else
        		{
        			
        		}
        		Thread.sleep( 10 );
        	}
        
        } catch (IOException e) {
			e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void decreaseClientNumber( ClientConnection client )
	{
		currentClientSize--;
		clients.remove( client );
		System.out.println( "Client " + client.getId() + " released a space in the pool (" +  currentClientSize + ") / games: " + games.size() );
		clientListChanged();
	}

	public synchronized void forwardToClients( String str )
	{
		for ( ClientConnection client : clients )
		{
			client.forwardMessage ( str );
		}
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

	public void forwardToClient(String name, String command) 
	{
		for ( ClientConnection client : clients )
		{
			if ( client.getLogin().compareTo( name ) == 0 )
			{
				client.forwardMessage ( command );
				break;
			}
		}
	}
	
	public void beginGame( String login, String player ) 
	{
		String gameId = "tron_" + player + "_" + login;
		if ( games.containsKey( gameId ) == false )
		{
			games.put( gameId, new TronGame( player, login, gameId, this ) );
		}
	}

	public void updateGamePlayerReady(String gameId, String player) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			TronGame game = games.get( gameId );
			game.setReady( player );
		}
	}

	public void updateGamePlayerChangeDirection(String gameId, String player, String dir) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			TronGame game = games.get( gameId );
			game.changePlayerDirection( player, dir );
		}
	}

	public void closeGame(String gameId) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			TronGame game = games.get( gameId );
			games.remove( gameId );
			
			game.stop();
			game = null;
		}
	}
}