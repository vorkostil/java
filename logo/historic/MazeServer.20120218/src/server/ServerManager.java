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
public class ServerManager extends AbstractServerManager
{
	private Environment environment_ = null;
	Timer timer_ = null;
	
    public class ServerManagerTask extends TimerTask 
    {
		@Override
		public void run() 
		{
			environment_.process(new Date().getTime());
		}
	}

	public ServerManager(Environment env) 
	{
		environment_ = env;
	}

	@Override
	public void run() 
	{
		if (timer_ == null)
			timer_ = new Timer("ServerManager");
    	timer_.schedule(new ServerManagerTask(), 100, 5);
    }

	@Override
	public void stop() {
		timer_.cancel();
		environment_.stop();
	}
}
