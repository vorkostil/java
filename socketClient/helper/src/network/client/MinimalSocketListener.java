package network.client;

import java.io.IOException;
import java.net.Socket;

import common.MessageType;


public class MinimalSocketListener extends AbstractSocketListenerClientSide 
{
	private boolean debug = true;
	
	public MinimalSocketListener( Socket socket,
								  ConnectionClient connectionClient) throws IOException
	{
		super(socket, connectionClient);
	}

	@Override
	protected void lineReceived(String line) 
	{
		// display it for debug purpose only
		if ( debug )
		{
			connectionClient.forwardInfo( line );
		}
		
		// split between command and remaining message
		String[] splitted = line.split( " ", 2 );
		String command = splitted[ 0 ];
		
		// close message
		if ( command.compareTo( MessageType.MessageSystemClose ) == 0 )
		{
			connectionClient.closeConnection();
		}
		// game message
		else if ( command.compareTo( MessageType.MessageGame ) == 0 )
		{
			connectionClient.handleGameMessage( splitted[ 1 ] );
		}
	}
}
