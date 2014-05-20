package ScreenManager.ScreenAction;

import ScreenManager.ScreenManager;

public class ExitAction extends AbstractAction {

	public ExitAction(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void run() {
		System.exit(0);
	}
}
