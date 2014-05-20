package ScreenManager.ScreenAction;


public class ExitAction extends AbstractAction {

	public ExitAction() {
		super();
	}

	@Override
	public void run() {
		System.exit(0);
	}
}
