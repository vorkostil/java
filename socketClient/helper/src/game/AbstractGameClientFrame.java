package game;

import graphic.listener.ClosingGameMessageListener;
import helper.DataRepository;

import java.awt.MediaTracker;

import javax.swing.JFrame;

import common.MessageType;

import network.client.ConnectionClient;

@SuppressWarnings("serial")
abstract public class AbstractGameClientFrame extends JFrame 
{
	protected ConnectionClient connectionClient;
	protected String gameId;
	
	protected DataRepository repository = new DataRepository();
	protected MediaTracker tracker = new MediaTracker( this );
	
	// default ctor
	public AbstractGameClientFrame( String configPath )
	{
		connectionClient  = null;
		gameId  = null;
		
		// load repository of information
		if ( configPath != null )
		{
			repository.addFromFile( configPath );
		}
	}
	
	// check if the game is correctly defined
	public boolean isGameValid()
	{
		return (  ( connectionClient != null )
				&&( gameId != null )  );
	}
	
	// get the game id for communication purpose
	public String getId() 
	{
		return gameId;
	}
	
	// initialize the id of the game (from the game manager)
	public void setId( String gameId )
	{
		this.gameId = gameId;
	}
	
	// initialize the connection client (from the game manager)
	public void setConnectionClient(ConnectionClient connectionClient)
	{
		this.connectionClient = connectionClient;
		this.addWindowListener( new ClosingGameMessageListener( this ) );
	}
	
	// send the close message to the server
	public void closeGame()
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageSystemLeaveGame + " " + gameId );
		disposeView();
	}
	
	// send the ready message to the server
	public void readyToPlay()
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageReady + " " + connectionClient.getLogin() );
	}
	
	// dispose the view
	public void disposeView()
	{
		this.dispose();
	}
	
	// the server send a message relative to the game modification
	abstract public void handleServerMessage(String remain);

}
