package main;

import helper.StringHelper;

import java.io.IOException;
import java.net.Socket;

import network.client.AbstractSocketListenerClientSide;
import network.client.ConnectionClient;

import common.MessageType;

public class SocketListenerClientSide extends AbstractSocketListenerClientSide {

	GraphicalClient gClient;
	private boolean debug = false;
	
	public SocketListenerClientSide(GraphicalClient graphicalClient, Socket socket, ConnectionClient connectionClient) throws IOException 
	{
		super(socket, connectionClient);
		gClient = graphicalClient;
	}

	@Override
	protected void lineReceived(String line) 
	{
		String[] splitted = line.split( " " );
		
		// manage system message
		if (  ( splitted.length > 1 )
			&&( splitted[0].compareTo( MessageType.MessageSystem ) == 0 )  )
		{
			// display it for debug purpose only
			if ( debug )
			{
				connectionClient.forwardInfo( line );
			}
			
			// close message
			if (  ( splitted.length > 2 )
				&&( splitted[1].compareTo( MessageType.MessageClose ) == 0 )  )
			{
				connectionClient.closeConnection();
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
			gClient.appendToChatArea( line, GraphicalClient.normalFont, GraphicalClient.normalColor );
		}
	}

}
