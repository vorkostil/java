package network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import network.NetworkHelper;

public abstract class AbstractSocketListenerClientSide implements Runnable 
{

	private Socket listenSocket = null;
	private BufferedReader reader = null;
	protected ConnectionClient connectionClient = null;
	boolean debug = false;
	
	public AbstractSocketListenerClientSide( Socket socket, ConnectionClient connectionClient ) throws IOException
	{
		// create and store interaction object
		listenSocket = socket;
		this.connectionClient = connectionClient;
		
		reader = new BufferedReader( new InputStreamReader( listenSocket.getInputStream() ) );
	}
	
	@Override
	public void run() {
		// listen on the socket
		try 
		{
			String line = NetworkHelper.readOnSocket( reader );
			while ( line != null )
			{
				if ( debug == true )
				{
					System.err.println( "message> " + line );
				}
				lineReceived( line );
				line = NetworkHelper.readOnSocket( reader );
			} 
		} 
		catch (SocketException e )
		{
			connectionClient.forwardAlert( "Server is unavailable> " + e.getMessage() );
		}
		catch (IOException e) 
		{
			connectionClient.forwardAlert( "Server is unavailable> " + e.getMessage() );
		}
		
		connectionClient.forwardAlert( "Communication broken with the server" );
		connectionClient.changeCurrentState( ConnectionClient.State.WAITING_FOR_SERVER );
		reader = null;
	}

	// call when a line is received on the socket
	abstract protected void lineReceived(String line);
	
}
