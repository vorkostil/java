package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import maze.MazeGame;
import ScreenManager.ScreenManager;
import ScreenManager.Screen.ScreenWithControl;
import ScreenManager.ScreenAction.ChangeScreenAction;
import ScreenManager.ScreenControl.ControlButton;

public class ConfigScreen extends ScreenWithControl {

	public static final String NAME = "config";

	private ControlButton backButton_ = new ControlButton("back", 150, 250, 231, 30, true);
	
	private MazeGame maze_ = null;
	
	public ConfigScreen(ScreenManager screenManager, MazeGame maze) {
		super(screenManager, NAME, "pictures/configScreen.jpg");
		maze_ = maze;
	}

	@Override
	public void init() {
		backButton_.setLabel("Back", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		backButton_.setAction(new ChangeScreenAction(getScreenManager_(),MainScreen.NAME),KeyEvent.VK_B);
		backButton_.init();
		addControl(backButton_);
	}
}
