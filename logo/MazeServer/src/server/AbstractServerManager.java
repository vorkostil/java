package server;

public abstract class AbstractServerManager  implements Runnable {

	public AbstractServerManager() {
		super();
	}

	public abstract void run();

	public abstract void stop();

}