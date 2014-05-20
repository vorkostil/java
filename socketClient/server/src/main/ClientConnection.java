package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void end() throws IOException 
	{
		writer.println( MessageType.MessageSystem + " " + MessageType.MessageClose );
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
			this.writer.println( str );
			this.writer.flush();
		}
	}
	
	public void run()
	{
		try {
			// ask for login / mdp
			writer.println( MessageType.MessageSystem + " " + MessageType.MessageLoginAsked );
			writer.flush();
			
			login = reader.readLine();
			String mdp = reader.readLine();
			
			if ( login.compareTo( mdp ) == 0 )
			{
				this.connected = true;
				
				System.out.println( "Client " + numClient + ", login accepted: " + login );
				writer.println( MessageType.MessageSystem + " " + MessageType.MessageLoginAccepted );
				writer.flush();
				
				Thread listener = new Thread( new ClientListener( client,
																  login,
																  this ) );
				listener.start();
				
				// wait 1s for the client to initialize its listener
				Thread.sleep( 1000 );
				getFather().clientListChanged();
			}
			else
			{
				System.out.println( "Client " + numClient + ", login refused");
				
				writer.println( MessageType.MessageSystem + " " + MessageType.MessageLoginRefused );
				writer.flush();
				
				end();
			}
		} 
		catch (SocketException e) 
		{
			System.out.println( "Client " + numClient + " has broken the link, it's sad but it's true");
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