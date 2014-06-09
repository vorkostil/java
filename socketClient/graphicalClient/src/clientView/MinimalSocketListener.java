package clientView;

import java.io.IOException;
import java.net.Socket;

import common.MessageType;

import network.client.AbstractSocketListenerClientSide;
import network.client.ConnectionClient;

public class MinimalSocketListener extends AbstractSocketListenerClientSide {

	public MinimalSocketListener( Socket socket,
								  ConnectionClient connectionClient) throws IOException
	{
		super(socket, connectionClient);
	}

	@Override
	protected void lineReceived(String line) 
	{
		String[] splitted = line.split( " " );
		
		// manage system message
		if (  ( splitted.length > 1 )
			&&( splitted[0].compareTo( MessageType.MessageSystem ) == 0 )  )
		{
			// close message
			if (  ( splitted.length == 2 )
				&&( splitted[1].compareTo( MessageType.MessageClose ) == 0 )  )
			{
				connectionClient.closeConnection();
			}
			// manage the contact list modification
			else if (  ( splitted.length == 3 )
					&&( splitted[1].compareTo( MessageType.MessageContactListSnapshot ) == 0 )  )
			{
				connectionClient.clientListChange( splitted[2].split( "µ" ) );
			}
			// game communication
			// formalism MESSAGE_SYSTEM GAME gameID <ACTION> <parameters> 
			else if (  ( splitted.length > 3 )
					 &&( splitted[1].compareTo( MessageType.MessageGame ) == 0 )  )
			{
				connectionClient.forwardGameMessage( splitted );
			}
		}
	}
}
