package ScreenManager.ScreenAction;

import ScreenManager.ScreenManager;

public class ChangeScreenAction extends AbstractAction {

	private String newScreen_ = null;
	protected ScreenManager screenManager_ = null;

	public ChangeScreenAction(ScreenManager screenManager, String screen) {
		super();
		screenManager_  = screenManager; 
		newScreen_ = screen;
	}

	@Override
	public void run() {
		screenManager_.setCurrentScreen(newScreen_);
	}

}
