package maze.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import maze.MazeGame;
import ScreenManager.ScreenManager;
import ScreenManager.Screen.ScreenWithControl;
import ScreenManager.ScreenAction.ChangeScreenAction;
import ScreenManager.ScreenControl.ControlButton;

public class LaunchScreen extends ScreenWithControl {

	public static final String NAME = "launch_game";

	private ControlButton gameButton_ = new ControlButton("game", 150, 150, 231, 30, true);
	private ControlButton editButton_ = new ControlButton("edit", 150, 200, 231, 30, true);
	private ControlButton backButton_ = new ControlButton("back", 150, 250, 231, 30, true);
	
	private MazeGame maze_ = null;

	public LaunchScreen(ScreenManager screenManager, MazeGame maze) {
		super(screenManager, NAME, "pictures/launchScreen" + maze.getGraphicalConfiguration().getWidth() + "x" + maze.getGraphicalConfiguration().getHeight() + ".jpg");
		maze_ = maze;
	}

	@Override
	public void init() {
		gameButton_.setLabel("Game", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		gameButton_.setAction(new ChangeScreenAction(getScreenManager(),MazeScreen.NAME));
		gameButton_.setKey(KeyEvent.VK_B);
		gameButton_.init();
		addControl(gameButton_);

		editButton_.setLabel("Editor", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		editButton_.setAction(new ChangeScreenAction(getScreenManager(),EditorScreen.NAME));
		editButton_.setKey(KeyEvent.VK_E);
		editButton_.init();
		addControl(editButton_);

		backButton_.setLabel("Back", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		backButton_.setAction(new ChangeScreenAction(getScreenManager(),MainScreen.NAME));
		backButton_.setKey(KeyEvent.VK_B);
		backButton_.init();
		addControl(backButton_);
	}

	@Override
	public void changeScreenSize(int width, int height) {
		changeBackgroundImage("pictures/launchScreen" + width + "x" + height + ".jpg");
	}
}
