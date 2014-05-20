package ScreenManager.ScreenAction;

import ScreenManager.ScreenManager;

public abstract class AbstractAction {

	protected ScreenManager screenManager_ = null;

	public AbstractAction(ScreenManager manager) {
		screenManager_  = manager; 
	}

	public abstract void run();

}