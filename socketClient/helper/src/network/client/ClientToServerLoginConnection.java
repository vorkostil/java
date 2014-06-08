package network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
			// listen on the socket waiting for a login question
			BufferedReader reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
			String line = reader.readLine();
			while ( line.compareTo( MessageType.MessageSystem + " " + MessageType.MessageLoginAsked ) != 0 )
			{
				line = reader.readLine();
			}
			
			connectionClient.changeCurrentState( ConnectionClient.State.DURING_LOGIN );

			PrintWriter writer = new PrintWriter( socket.getOutputStream() );
			writer.println( connectionInfo.getLogin() );
			writer.println( connectionInfo.getPasswd() );
			writer.flush();
			
			// waiting for server answer
			line = reader.readLine();
			while (  ( line.compareTo( MessageType.MessageSystem + " " + MessageType.MessageLoginAccepted ) != 0 )
				   &&( line.compareTo( MessageType.MessageSystem + " " + MessageType.MessageLoginRefused ) != 0 )  )
			{
				line = reader.readLine();
			}
			
			if ( line.compareTo( MessageType.MessageSystem + " " + MessageType.MessageLoginAccepted ) == 0 )
			{
				connectionClient.changeCurrentState( ConnectionClient.State.CONNECTED );
				connectionClient.setLogin( connectionInfo.getLogin() );
				connectionClient.forwardInfo( "Connection accepted, you can chat as " + connectionInfo.getLogin() );
				
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
