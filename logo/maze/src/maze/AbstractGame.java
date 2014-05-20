package maze;

import server.AbstractServerManager;

public abstract class AbstractGame {

	AbstractServerManager manager_ = null;

	public AbstractGame() {
		super();
	}

	public abstract void init();

	public abstract void setup();

	public abstract void tearDown();
}