package main;

import game.AbstractGameProvider;
import game.GameProvider;
import helper.DataRepository;
import helper.DataRepository.DataInformation;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import network.client.AbstractSocketListenerClientSide;
import network.client.ConnectionClient;
import network.client.ConnectionClient.State;
import network.client.ConnectionInfo;
import network.client.ConnectionObserver;
import network.client.MinimalSocketListener;
import server.ChessGameServer;
import server.TronGameServer;

import common.ChessCommonInformation;
import common.MessageType;
import common.TronCommonInformation;

public class ClientConnectionManager implements ConnectionObserver, GameProvider  
{
	// the constant part
	private static final String CONFIG_PATH = "resources/config/javaProvider.cfg";
	private static final int MAX_GAME = 1;
	
	// use to retrieve configuration
	// server connection for example
	protected DataRepository repository = new DataRepository();
	private DataInformation connection;
	private ConnectionInfo info;
	private int reconnectionTries;
	private int reconnectionDelay;
	
	// Network relevant information
	private ConnectionClient connectionClient;

	// the game map indexed by gameId
	HashMap< String, AbstractGameProvider > games = new HashMap< String, AbstractGameProvider >();

	public ClientConnectionManager() 
	{
		// load repository of information
		repository.addFromFile( CONFIG_PATH );
		connection = repository.getData( "connection_information" );

		// set the reconnection information
		reconnectionTries = connection.getIntegerValue( "reconnection_try" );
		reconnectionDelay = connection.getIntegerValue( "reconnection_delay" );
		
		// initialize the connection
		info = new ConnectionInfo( connection.getStringValue( "server_host_name" ),
								   connection.getStringValue( "server_port" ),
								   connection.getStringValue( "server_login" ),
								   connection.getStringValue( "server_password" ) );
		
		// connect to the master server
		connectionClient = new ConnectionClient( this );
	}
	
	// launch the server
	public void start() 
	{
		int tries = 0;
		while ( tries++ < reconnectionTries )
		{
			try 
			{
				connectionClient.launchConnection( info, true );
				Thread.sleep( reconnectionDelay );
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void raiseAlert(String message) 
	{
		System.err.println( "ChatServer::Alert> " + message );
	}

	@Override
	public void raiseInfo(String message) 
	{
		System.out.println( "ChatServer::Info > " + message );
	}

	@Override
	public void serverDisconnection() 
	{
		games.clear();
		raiseAlert( "No server exist anymore, try to reconnect each second" );
		start();
	}

	@Override
	public void connectionStatusChange(State currentState) 
	{
		raiseAlert( "Internal connection status change to " + currentState );
	}

	@Override
	public AbstractSocketListenerClientSide createSocketListener(Socket socket) throws IOException 
	{
		return new MinimalSocketListener( socket, connectionClient );
	}

	@Override
	public void handleGameMessage(String message) 
	{
		String[] parts = message.split( " ", 3 );
		if ( parts[ 0 ].compareTo( MessageType.MessageGameCreated) == 0 )
		{
			if ( games.size() < MAX_GAME )
			{
				createNewGame( parts[ 1 ], parts[ 2 ] );
			}
			else
			{
				// send the refused message
				connectionClient.sendMessageIfConnected( MessageType.MessageSystemGameCreationRefused + " " + parts[ 1 ] + " No more slot available" );
			}
		}
		else if ( parts[ 1 ].compareTo( MessageType.PlayerJoinGame) == 0 )
		{
			playerJoinGame( parts[ 0 ], parts[ 2 ] );
		}
		else if ( parts[ 1 ].compareTo( MessageType.PlayerLeaveGame) == 0 )
		{
			playerLeaveGame( parts[ 0 ], parts[ 2 ] );
		}
		else
		{
			forwardMessageToGame( parts[ 0 ], parts[ 1 ], parts[ 2 ] );
		}
	}

	private void forwardMessageToGame(String gameId, String action, String remain) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			((AbstractGameProvider) games.get(gameId)).handleMessage( action, remain );
		}
	}

	private void playerLeaveGame(String gameId, String playerName) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			((AbstractGameProvider) games.get(gameId)).playerLeaveGame( playerName );
		}
	}

	private void playerJoinGame(String gameId, String playerName) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			((AbstractGameProvider) games.get(gameId)).playerJoinGame( playerName );
		}
	}

	private void createNewGame(String gameId, String gameKind) 
	{
		if ( gameKind.compareTo( ChessCommonInformation.GAME_NAME ) == 0 )
		{
			games.put(gameId, new ChessGameServer( gameId, this ) );
		}
		else if ( gameKind.compareTo( TronCommonInformation.GAME_NAME ) == 0 )
		{
			games.put(gameId, new TronGameServer( gameId, this ) );
		}
	}

	@Override
	public void onLoginAccepted() 
	{
	   // register as provider
	   // [GameName MinPlayer MaxPlayer IAAvailable]
	   connectionClient.sendMessageIfConnected( MessageType.MessageSystemRegister + " " + MessageType.RegistrationAsProvider + " " + TronCommonInformation.GAME_NAME + " 2 2 0 " + ChessCommonInformation.GAME_NAME + " 2 2 0" );
	}

	@Override
	public void handleSystemMessage(String message) 
	{
		{
			raiseAlert( "[HandleSystemMessage])> " + message );
		}
	}

	@Override
	public ConnectionClient getConnectionClient() 
	{
		return connectionClient;
	}

	@Override
	public void closeGame(String id) 
	{
		if ( games.containsKey( id ) == true )
		{
			games.remove( id );
			connectionClient.sendMessageIfConnected( MessageType.MessageSystemLeaveGame + " " + id );
		}
	}
}