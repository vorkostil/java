package server;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ServerManager implements Runnable{

	private Turtle turtle_ = null;
	
    public class ServerManagerTask extends TimerTask {
		@Override
		public void run() {
			turtle_.process(new Date().getTime());
		}
	}

	@Override
	public void run() {
    	Timer timer = new Timer("ServerManager");
    	timer.schedule(new ServerManagerTask(), 100, 16);
    }
	
	public ServerManager(Turtle turtle) {
		turtle_ = turtle;
	}
}
