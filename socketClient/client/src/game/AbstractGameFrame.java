package game;

import helper.DataRepository;

import java.awt.MediaTracker;
import java.io.PrintWriter;

import javax.swing.JFrame;

@SuppressWarnings("serial")
abstract public class AbstractGameFrame extends JFrame 
{
	private String login = null;
	
	protected PrintWriter targetWriter = null;
	protected String gameId = null;
	protected DataRepository repository = new DataRepository();
	protected MediaTracker tracker = new MediaTracker( this );
	

	public AbstractGameFrame(PrintWriter writer, String gameID, String name, String configPath)
	{
		targetWriter  = writer;
		gameId  = gameID;
		login = name;
		
		// load repository of information
		repository.addFromFile( configPath );
	}
	
	public String getLogin()
	{
		return login;
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
