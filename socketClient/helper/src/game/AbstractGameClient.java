package game;

import helper.DataRepository;

import java.awt.MediaTracker;

import javax.swing.JFrame;

import network.client.ConnectionClient;

@SuppressWarnings("serial")
abstract public class AbstractGameClient extends JFrame 
{
	protected ConnectionClient connectionClient;
	protected String gameId;
	
	protected DataRepository repository = new DataRepository();
	protected MediaTracker tracker = new MediaTracker( this );
	

	public AbstractGameClient(ConnectionClient cClient, String gameID, String configPath)
	{
		connectionClient  = cClient;
		gameId  = gameID;
		
		// load repository of information
		repository.addFromFile( configPath );
	}
	
	public String getId() 
	{
		return gameId;
	}
	
	abstract public void ready(String name); 

	abstract public void start();

	abstract public void end(String winner);

	abstract public void startSoon();

	abstract public void forwardMessage(String message); 
}
