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
				connectionClient.forwardGameMessage(splitted);
			}
		}
		else
		{
			// normal communication
			gClient.appendToChatArea( line, GraphicalClient.normalFont, GraphicalClient.normalColor );
		}
	}

}
