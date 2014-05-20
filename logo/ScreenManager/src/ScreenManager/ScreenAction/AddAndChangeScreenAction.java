package ScreenManager.ScreenAction;

import ScreenManager.ScreenManager;

public class AddAndChangeScreenAction extends AbstractAction {

	private String newScreen_ = null;
	protected ScreenManager screenManager_ = null;

	public AddAndChangeScreenAction(ScreenManager screenManager, String screen) {
		super();
		screenManager_  = screenManager; 
		newScreen_ = screen;
	}

	@Override
	public void run() {
		screenManager_.addCurrentScreen(newScreen_);
	}

}
