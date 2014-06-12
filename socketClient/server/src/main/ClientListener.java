package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import network.NetworkHelper;

import common.MessageType;

class ClientListener implements Runnable 
{
	private Socket clientSocket = null;
	private String clientLogin = null;
	private ClientConnection connection = null;
	
	private BufferedReader reader = null;
	
	private boolean debug = true;
	
	public ClientListener( Socket socket, 
						   String login,
						   ClientConnection server ) throws IOException
	{
		this.clientSocket = socket;
		this.clientLogin = login;
		this.connection = server;
		reader = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
	}
	
	public void run()
	{
		try 
		{
			String command = null;
			command = NetworkHelper.fullRead( reader );
			while ( command != null )
			{
				String[] splitted = command.split( " " );
				
				// manage system message
				if (  ( splitted.length > 1 )
					&&( splitted[0].compareTo( MessageType.MessageSystem ) == 0 )  )
				{
					// display it for debug purpose only
					if ( debug )
					{
						System.out.println( clientLogin + "> " + command );
					}
					
					if (  ( splitted.length >= 2 )
						&&( splitted[1].compareTo( MessageType.MessageClose ) == 0 )  )
					{
						break;
					}
					else if (  ( splitted.length > 2 )
							 &&( splitted[1].compareTo( MessageType.MessageCommunicationSpecific ) == 0 )  )
					{
						String name = splitted[ 2 ];
						if (  ( splitted.length > 3 )
							&&(  ( splitted[2].compareTo( MessageType.MessageClose ) == 0 )
							   ||( splitted[2].compareTo( MessageType.MessageOpen ) == 0 )  )  )
						{
							name = splitted[ 3 ];
						}
						connection.getFather().forwardToClient( name, command );
					}
					// game communication
					// formalism MESSAGE_SYSTEM GAME gameID <ACTION> <parameters> 
					else if (  ( splitted.length > 3 )
							 &&( splitted[1].compareTo( MessageType.MessageGame ) == 0 )  )
					{
						String action = splitted[2]; 
						if ( action.compareTo( MessageType.MessageAsked ) == 0 )
						{
							// check the solo game status
							if ( splitted[ 3 ].compareTo( MessageType.MessageGameSoloGameOpponent ) == 0 )
							{
								// it's a solo game, send the accepted message
								connection.getFather().beginSoloGame( clientLogin, splitted[ 4 ] );
							}
							else
							{
								// at least one player to ask for a game
								connection.getFather().forwardToClient( splitted[ 3 ], MessageType.MessageSystem + " " + MessageType.MessageGameAsked + " " + clientLogin + " " + splitted[ 4 ] );
							}
						}
						else if ( action.compareTo( MessageType.MessageRefused ) == 0 )
						{
							connection.getFather().forwardToClient( splitted[ 3 ], MessageType.MessageSystem + " " + MessageType.MessageGameRefused + " " + clientLogin );
						}
						else if ( action.compareTo( MessageType.MessageClose ) == 0 )
						{
							connection.getFather().closeGame( splitted[ 3 ] );
							connection.getFather().forwardToClients( command );
						}
						else if ( action.compareTo( MessageType.MessageReady ) == 0 )
						{
							connection.getFather().updateGamePlayerReady( splitted[ 3 ], splitted[ 4 ] );
						}
						else if ( action.compareTo( MessageType.MessageAccepted ) == 0 )
						{
							connection.getFather().beginGame( clientLogin, splitted[ 3 ], splitted[ 4 ] );
						}
						else
						{
							connection.getFather().updateGameSpecificMessage( splitted[ 3 ], command );
						}
						
					}
				}
				else
				{
					connection.getFather().forwardToClients( clientLogin + "> " + command );
				}
				command = NetworkHelper.fullRead( reader );
			}
			connection.end();
		} 
		catch (IOException e) 
		{
			System.err.println( "connection broken with " + clientLogin );
			try 
			{
				connection.end();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
	}
}