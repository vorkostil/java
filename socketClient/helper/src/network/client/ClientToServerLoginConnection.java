package network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import network.NetworkHelper;

import common.MessageType;

public class ClientToServerLoginConnection implements Runnable 
{
	private Socket socket = null;
	private ConnectionClient connectionClient = null;
	private ConnectionInfo connectionInfo = null;
	
	public ClientToServerLoginConnection( Socket client, ConnectionClient graphicalClient, ConnectionInfo info ) 
	{
		this.socket = client;
		this.connectionClient = graphicalClient;
		this.connectionInfo = info;
	}
	
	@Override
	public void run() {
		try 
		{
			// initialize the system connection
			PrintWriter writer = new PrintWriter( socket.getOutputStream() );
			BufferedReader reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
			
			// send the init message to ask the login protocol to the server
			NetworkHelper.writeOnSocket( writer, 
										 MessageType.MessageSystemInit );
			
			// listen on the socket waiting for a login question
			String line = NetworkHelper.readOnSocket( reader );
			while ( line.compareTo( MessageType.MessageSystemLoginAsked ) != 0 )
			{
				line = NetworkHelper.readOnSocket( reader );
			}
			
			connectionClient.changeCurrentState( ConnectionClient.State.DURING_LOGIN );

			NetworkHelper.writeOnSocket( writer, 
										 connectionInfo.getLogin() + ":" + connectionInfo.getPasswd() );
			
			// waiting for server answer
			line = NetworkHelper.readOnSocket( reader );
			while (  ( line.compareTo( MessageType.MessageSystemLoginAccepted ) != 0 )
				   &&( line.compareTo( MessageType.MessageSystemLoginRefused ) != 0 )  )
			{
				line = NetworkHelper.readOnSocket( reader );
			}
			
			if ( line.compareTo( MessageType.MessageSystemLoginAccepted ) == 0 )
			{
				connectionClient.changeCurrentState( ConnectionClient.State.CONNECTED );
				connectionClient.setLogin( connectionInfo.getLogin() );
				connectionClient.forwardInfo( "Connection accepted, you are known as " + connectionInfo.getLogin() );
				
				Thread listenerThread = new Thread ( connectionClient.createSocketListener( socket ) );
				listenerThread.setName( "Listen" );
				listenerThread.start();
			}
			else
			{
				connectionClient.forwardInfo( "Connection refused" );
				socket.close();
				socket = null;
				connectionClient.changeCurrentState( ConnectionClient.State.WAITING_FOR_SERVER );
			}
		} 
		catch (IOException e) 
		{
			connectionClient.forwardAlert( "Connection broken, server is dead" );
		}
	}
}
