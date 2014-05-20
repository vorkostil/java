package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import ScreenManager.ScreenManager;
import ScreenManager.Screen.ScreenWithControl;
import ScreenManager.ScreenAction.ChangeScreenAction;
import ScreenManager.ScreenAction.ExitAction;
import ScreenManager.ScreenControl.ControlButton;

public class MainScreen extends ScreenWithControl {

	public static final String NAME = "main";

	private ControlButton launchButton_ = null;
	private ControlButton configButton_ = null;
	private ControlButton exitButton_ = null;

	public MainScreen(ScreenManager screenManager) {
		super(screenManager,NAME, "pictures/mainScreen.jpg");
		launchButton_ = new ControlButton("launch", getWidth() - 231 - 20 - 16, getHeight() - 30 - 120 - 38, 231, 30, true);
		configButton_ = new ControlButton("config", getWidth() - 231 - 20 - 16, getHeight() - 30 - 70 - 38, 231, 30, true);
		exitButton_ = new ControlButton("exit", getWidth() - 231 - 20 - 16, getHeight() - 30 - 20 - 38, 231, 30, true);
	}

	@Override
	public void init() {
		super.init();
		
		launchButton_.setLabel("Launch", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		launchButton_.setAction(new ChangeScreenAction(getScreenManager_(),LaunchScreen.NAME));
		launchButton_.setKey(KeyEvent.VK_L);
		launchButton_.init();
		addControl(launchButton_);

		configButton_.setLabel("Config", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		configButton_.setAction(new ChangeScreenAction(getScreenManager_(),ConfigScreen.NAME));
		configButton_.setKey(KeyEvent.VK_C);
		configButton_.init();
		addControl(configButton_);

		exitButton_.setLabel("Exit", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		exitButton_.setAction(new ExitAction(getScreenManager_()));
		exitButton_.setKey(KeyEvent.VK_ESCAPE);
		exitButton_.init();
		addControl(exitButton_);
	}
}
