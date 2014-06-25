package server;

import game.AbstractGameClientFrame;
import helper.DataRepository;
import helper.DataRepository.DataInformation;

import java.io.IOException;
import java.net.Socket;

import network.client.AbstractSocketListenerClientSide;
import network.client.ConnectionClient;
import network.client.ConnectionClient.State;
import network.client.ConnectionInfo;
import network.client.ConnectionObserver;
import network.client.MinimalSocketListener;

import common.ChatCommonInformation;
import common.MessageType;

public class ChatServer implements ConnectionObserver 
{
	// the constant part
	private static final String CONFIG_PATH = "resources/config/chatServer.cfg";

	// use to retrieve configuration
	// server connection for example
	protected DataRepository repository = new DataRepository();
	private DataInformation connection;
	private ConnectionInfo info;
	private int reconnectionTries;
	private int reconnectionDelay;
	
	// Network relevant information
	private ConnectionClient connectionClient;
	private String gameId;

	
	public ChatServer() 
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
				connectionClient.launchConnection( info  );
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
	public void manageGameMessage(String message) 
	{
		String[] parts = message.split( " ", 4 );
		if ( parts[ 0 ].compareTo( MessageType.MessageGameCreated) == 0 )
		{
			gameId = parts[ 1 ];
		}
		else if (  ( parts[ 0 ].compareTo( gameId ) == 0 )
				 &&( parts[ 1 ].compareTo( MessageType.MessageChatSendAll) == 0 )  )
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + parts[ 2 ] + "> " + parts[ 3 ] );			
		}
		else
		{
			raiseInfo( "( manageGameMessage )> " + message );
		}
	}

	@Override
	public AbstractGameClientFrame requireGame(String gameName) throws IOException 
	{
		return null;
	}

	@Override
	public void onLoginAccepted() 
	{
	   // register as provider
	   // [GameName MinPlayer MaxPlayer IAAvailable]
	   connectionClient.sendMessageIfConnected( MessageType.MessageSystemRegister + " " + MessageType.RegistrationAsProvider + " " + ChatCommonInformation.GAME_NAME + " 1 128 0" );
	}
}
