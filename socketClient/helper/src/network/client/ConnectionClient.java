package network.client;

import game.AbstractGameClientFrame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import common.MessageType;

public class ConnectionClient 
{
	public enum State { WAITING_FOR_SERVER, WAITING_FOR_LOGIN, CONNECTED, DURING_LOGIN };
	 
	boolean isConnected = false;
	State currentState = State.WAITING_FOR_SERVER;
	
	// the server connection information
	Socket socket = null;
	private ConnectionObserver observer;
	private PrintWriter writer;
	ConnectionInfo info;
	String login;
	
	// store the list of connected people
	List< String > clients = new ArrayList< String >();
	
	public ConnectionClient( ConnectionObserver observer )
	{
		this.observer = observer;
	}
	
	// change the status of the connection
	public void changeCurrentState( State newState )
	{
		// update the current status
		currentState = newState;
		
		// in case of server disconnection
		if ( currentState == State.WAITING_FOR_SERVER )
		{
			// if a socket was open
			if ( socket != null )
			{
				// close the socket
				try 
				{
					socket.close();
				} 
				catch (IOException e) 
				{
					forwardAlert( "socket can not be closed as the link is already broken" );
				}
				
				// remove the socket dependency
				socket = null;
			}
			
			// and send a signal on server disconnection
			observer.serverDisconnection();
		}
		
		// send a signal on the status of the connection modification
		observer.connectionStatusChange( currentState );
	}
	
	// get the current state
	public State getCurrentState()
	{
		return currentState;
	}
	
	public PrintWriter getWriter()
	{
		return writer;
	}

	public void launchConnection(ConnectionInfo info) 
	{
		if ( socket == null )
		{
			try
			{
				socket = new Socket( info.getServer(), 
									 Integer.parseInt( info.getPort() ) );
				
				changeCurrentState( State.WAITING_FOR_LOGIN );
				forwardInfo( "Socket client accepted on " + socket.getLocalSocketAddress() + " waiting for server interaction" );
				
				writer = new PrintWriter( socket.getOutputStream() );
				
				// launch the connection thread
				Thread client = new Thread( new ClientToServerLoginConnection( socket, 
																	   		   this, 
																	   		   info ) );
				client.setName( "Connection" );
				client.start();
			} 
			catch (UnknownHostException e) 
			{
				forwardAlert( "No server found> " + e.getMessage() );
			} 
			catch (ConnectException e)
			{
				forwardAlert( "No server found> " + e.getMessage() );
			}
			catch (IOException e) 
			{
				forwardAlert( "No server found> " + e.getMessage() );
			}
		}
	}

	// call when the client want to close the connection
	public void closeConnection()
	{
		if ( writer != null )
		{
			// send the close message
			writer.println( MessageType.MessageSystem + " " + MessageType.MessageClose );
			writer.flush();
			
			// reset the parameters
			socket = null;
			writer = null;
		}
	}
	
	public void setLogin(String login) 
	{
		this.login = login;
	}

	public void forwardInfo(String message) 
	{
		observer.raiseInfo( message ); 
	}

	public void forwardAlert(String message) 
	{
		observer.raiseAlert( message ); 
	}

	public AbstractSocketListenerClientSide createSocketListener(Socket socket) throws IOException 
	{
		return observer.createSocketListener( socket );
	}

	public boolean sendMessageIfConnected(String message) 
	{
		if (  ( writer != null )
			&&( currentState == State.CONNECTED )  )
		{
			writer.println( message );
			writer.flush();
			
			return true;
		}
		return false;
	}

	public String getLogin() 
	{
		return login;
	}

	public void clientListChange(String[] newClients) 
	{
		clients.clear();
		for ( String client : newClients )
		{
			clients.add(client);
		}
	}

	public List< String > getClientList() 
	{
		return clients;
	}

	public void forwardGameMessage(String[] splitted) 
	{
		observer.manageGameMessage( splitted );
	}

	public AbstractGameClientFrame requireGame(String gameName) throws IOException 
	{
		return observer.requireGame(gameName);
	}
}
