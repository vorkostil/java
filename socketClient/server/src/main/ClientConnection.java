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
		writer.print( MessageType.MessageSystem + " " + MessageType.MessageClose );
		writer.flush();
		
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
			this.writer.print( str );
			this.writer.flush();
			try 
			{
				Thread.sleep( 3 );
			} 
			catch (InterruptedException e) 
			{
				System.err.println( "Error while sending a message: " + e.getMessage() );
			}
		}
	}
	
	public void run()
	{
		try {
			// wait for the init message
			String line = NetworkHelper.fullRead( reader );
			while ( line.compareTo( MessageType.MessageInit ) != 0 )
			{
				line = NetworkHelper.fullRead( reader );;
			}
			
			// ask for login / mdp
			writer.print( MessageType.MessageSystem + " " + MessageType.MessageLoginAsked );
			writer.flush();
			
			// and wait for login / passwsd to be received
			line = NetworkHelper.fullRead( reader );

			String[] lineComponents = line.split( ":" );
			login = lineComponents[ 0 ];
			String mdp = lineComponents[ 1 ];
			
			if ( login.compareTo( mdp ) == 0 )
			{
				this.connected = true;
				
				System.out.println( "Client " + numClient + ", login accepted: " + login );
				writer.print( MessageType.MessageSystem + " " + MessageType.MessageLoginAccepted );
				writer.flush();
				
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
				
				writer.print( MessageType.MessageSystem + " " + MessageType.MessageLoginRefused );
				writer.flush();
				
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