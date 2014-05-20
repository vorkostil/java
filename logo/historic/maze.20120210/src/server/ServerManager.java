package server;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import server.model.Environment;

/*
 * The purpose of this class is: 
 *     	- to manage the model
 *		- to receive action from the controller
 *		- to manage the time
 **/
public class ServerManager implements Runnable
{
	private Environment env_ = null;
	
    public class ServerManagerTask extends TimerTask 
    {
		@Override
		public void run() 
		{
			env_.process(new Date().getTime());
		}
	}

	public ServerManager(Environment env) 
	{
		env_ = env;
	}

	@Override
	public void run() 
	{
    	Timer timer = new Timer("ServerManager");
    	timer.schedule(new ServerManagerTask(), 100, 5);
    }
}
