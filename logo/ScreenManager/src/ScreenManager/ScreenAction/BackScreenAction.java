package ScreenManager.ScreenAction;

import ScreenManager.ScreenManager;

public class BackScreenAction extends AbstractAction {

	protected ScreenManager screenManager_ = null;

	public BackScreenAction(ScreenManager screenManager) {
		super();
		screenManager_  = screenManager; 
	}

	@Override
	public void run() {
		screenManager_.removeCurrentScreen();
	}

}
