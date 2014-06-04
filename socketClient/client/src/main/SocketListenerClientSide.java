package main;

import helper.StringHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import common.MessageType;

public class SocketListenerClientSide implements Runnable {

	private Socket listenSocket = null;
	private BufferedReader reader = null;
	private GraphicalClient gClient = null;
	private boolean debug = false;
	
	public SocketListenerClientSide( Socket socket, GraphicalClient gClient ) throws IOException
	{
		// create and store interaction object
		listenSocket = socket;
		this.gClient = gClient;
		
		reader = new BufferedReader( new InputStreamReader( listenSocket.getInputStream() ) );
	}
	
	@Override
	public void run() {
		// listen on the socket
		try 
		{
			String line = null;
			line = reader.readLine();
			while ( line != null )
			{
				String[] splitted = line.split( " " );
				
				// manage system message
				if (  ( splitted.length > 1 )
					&&( splitted[0].compareTo( MessageType.MessageSystem ) == 0 )  )
				{
					// display it for debug purpose only
					if ( debug )
					{
						gClient.appendToChatArea( line + "\n", GraphicalClient.serverFont, GraphicalClient.serverColor );
					}
					
					// close message
					if (  ( splitted.length > 2 )
						&&( splitted[1].compareTo( MessageType.MessageClose ) == 0 )  )
					{
						break;
					}
					// update contact list
					else if (  ( splitted.length > 2 )
							&&( splitted[1].compareTo( MessageType.MessageContactListSnapshot ) == 0 )  )
					{
						gClient.updateContactList( splitted[2].split( "µ" ) );
					}
					// peer to peer communication
					else if (  ( splitted.length > 2 )
							&&( splitted[1].compareTo( MessageType.MessageCommunicationSpecific ) == 0 )  )
					{
						if (  ( splitted.length > 4 )
							&&( splitted[2].compareTo( MessageType.MessageClose ) == 0 )  )
						{
							gClient.closeSpecificCommunication( splitted[ 4 ], true );
						}
						else if (  ( splitted.length > 4 )
								&&( splitted[2].compareTo( MessageType.MessageOpen ) == 0 )  )
						{
							gClient.openSpecificCommunication( splitted[ 4 ] );
						}
						else
						{
							gClient.forwardSpecificCommunication( splitted[ 3 ], StringHelper.concat( splitted, 4 ) );
						}
					}
					// game communication
					// formalism MESSAGE_SYSTEM GAME gameID <ACTION> <parameters> 
					else if (  ( splitted.length > 3 )
							 &&( splitted[1].compareTo( MessageType.MessageGame ) == 0 )  )
					{
						String action = splitted[ 2 ];
						String gameId = splitted[ 3 ];
						
						if ( action.compareTo( MessageType.MessageClose ) == 0 )
						{
							gClient.closeGame( gameId, true );
						}
						else if ( action.compareTo( MessageType.MessageReady ) == 0 )
						{
							gClient.readyGame( gameId, splitted[ 4 ] );
						}
						else if ( action.compareTo( MessageType.MessageStart ) == 0 )
						{
							gClient.startGame( gameId );
						}
						else if ( action.compareTo( MessageType.MessageStartSoon ) == 0 )
						{
							gClient.startGameSoon( gameId );
						}
						else if ( action.compareTo( MessageType.MessageEnd ) == 0 )
						{
							gClient.endGame( gameId, splitted[ 4 ] );
						}
						else if ( action.compareTo( MessageType.MessageOpen ) == 0 )
						{
							gClient.openGame( gameId, splitted[ 4 ], splitted[ 5 ], splitted[ 6 ] );
						}
						else if ( action.compareTo( MessageType.MessageAsked ) == 0 )
						{
							// in fact, gameId is the name of the asker and the fourth element is the game kind
							gClient.askForGameFrom( gameId, splitted[ 4 ] );
						}
						else
						{
							gClient.forwardGameMessage( gameId, line );
						}
					}
				}
				else
				{
					// normal communication
					gClient.appendToChatArea( line + "\n", GraphicalClient.normalFont, GraphicalClient.normalColor );
				}
				line = reader.readLine();
			} 
		} 
		catch (SocketException e )
		{
			gClient.appendToChatArea( "Server is unavailable\n", GraphicalClient.errorFont, GraphicalClient.errorColor );
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		gClient.appendToChatArea( "Communication broken with the server\n", GraphicalClient.errorFont, GraphicalClient.errorColor );
		gClient.changeCurrentState( GraphicalClient.State.WAITING_FOR_SERVER );
		reader = null;
	}

}
