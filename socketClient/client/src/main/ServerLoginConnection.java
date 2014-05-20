package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import common.MessageType;

import frame.ConnectionInfo;

public class ServerLoginConnection implements Runnable 
{
	private Socket socket = null;
	private GraphicalClient gClient = null;
	private ConnectionInfo connectionInfo = null;
	
	public ServerLoginConnection( Socket client, GraphicalClient graphicalClient, ConnectionInfo info ) 
	{
		this.socket = client;
		this.gClient = graphicalClient;
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
			
			gClient.changeCurrentState( GraphicalClient.State.DURING_LOGIN );

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
				gClient.changeCurrentState( GraphicalClient.State.CONNECTED );
				gClient.setLogin( connectionInfo.getLogin() );
				gClient.appendToChatArea( "Connection accepted, you can chat as " + connectionInfo.getLogin() + "\n", GraphicalClient.serverFont, GraphicalClient.serverColor );
				
				Thread listenerThread = new Thread ( new SocketListenerClientSide( socket, gClient ) );
				listenerThread.setName( "Listen" );
				listenerThread.start();
			}
			else
			{
				gClient.appendToChatArea( "Connection refused\n", GraphicalClient.serverFont, GraphicalClient.serverColor );
				socket.close();
				socket = null;
				gClient.changeCurrentState( GraphicalClient.State.WAITING_FOR_SERVER );
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			gClient.appendToChatArea( "Connection broken, server is dead\n", GraphicalClient.errorFont, GraphicalClient.errorColor );
		}
	}
}
