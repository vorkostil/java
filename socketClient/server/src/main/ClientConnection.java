package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import network.NetworkHelper;

import common.MessageType;

class ClientConnection implements Runnable {
	int numClient;
	private Socket client;
	PrintWriter writer = null;
	BufferedReader reader = null;
	ClientConnectionManager father = null;
	boolean connected = false;
	
	String login;
	
	public ClientConnection( Socket socket, int numClient, ClientConnectionManager father )
	{
		this.client = socket;
		this.numClient = numClient;
		this.father = father;
		
		try 
		{
			this.writer = new PrintWriter( this.client.getOutputStream() );
			this.reader = new BufferedReader( new InputStreamReader( this.client.getInputStream() ) );
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void end() throws IOException 
	{
		NetworkHelper.writeOnSocket( writer, 
									 MessageType.MessageSystem + " " + MessageType.MessageClose );
		
		System.out.println( "Client " + numClient + " end the connection.");
		
		writer = null;
		reader = null;
		father.decreaseClientNumber( this );
		this.client.close();
	}

	public ClientConnectionManager getFather() 
	{
		return father;
	}

	public void forwardMessage( String str )
	{
		if ( this.connected == true )
		{
			NetworkHelper.writeOnSocket( writer,
									     str );
		}
	}
	
	public void run()
	{
		try {
			// wait for the init message
			String line = NetworkHelper.readOnSocket( reader );
			while ( line.compareTo( MessageType.MessageInit ) != 0 )
			{
				line = NetworkHelper.readOnSocket( reader );;
			}
			
			// ask for login / mdp
			NetworkHelper.writeOnSocket( writer,
										 MessageType.MessageSystem + " " + MessageType.MessageLoginAsked );
			
			// and wait for login / passwsd to be received
			line = NetworkHelper.readOnSocket( reader );

			String[] lineComponents = line.split( ":" );
			login = lineComponents[ 0 ];
			String mdp = lineComponents[ 1 ];
			
			if ( login.compareTo( mdp ) == 0 )
			{
				this.connected = true;
				
				System.out.println( "Client " + numClient + ", login accepted: " + login );
				NetworkHelper.writeOnSocket( writer,
											 MessageType.MessageSystem + " " + MessageType.MessageLoginAccepted );
				
				Thread listener = new Thread( new ClientListener( client,
																  login,
																  this ) );
				listener.start();
				
				// wait for the client to initialize its listener
				Thread.sleep( 100 );
				getFather().clientListChanged();
			}
			else
			{
				System.out.println( "Client " + numClient + ", login refused");
				
				NetworkHelper.writeOnSocket( writer,
											 MessageType.MessageSystem + " " + MessageType.MessageLoginRefused );
				
				end();
			}
		} 
		catch (SocketException e) 
		{
			System.err.println( "Client " + numClient + " has broken the link, it's sad but it's true");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

	public int getId() 
	{
		return numClient;
	}

	public String getLogin() 
	{
		return login;
	}

}