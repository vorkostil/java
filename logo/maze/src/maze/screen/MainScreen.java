package maze.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import ScreenManager.ScreenManager;
import ScreenManager.Screen.ScreenWithControl;
import ScreenManager.ScreenAction.ChangeScreenAction;
import ScreenManager.ScreenAction.ExitAction;
import ScreenManager.ScreenControl.ControlButton;

import maze.MazeGame;

public class MainScreen extends ScreenWithControl {

	public static final String NAME = "main";

	private ControlButton launchButton_ = null;
	private ControlButton configButton_ = null;
	private ControlButton scoreButton_ = null;
	private ControlButton exitButton_ = null;

	private MazeGame maze_ = null;
	
	public MainScreen(ScreenManager screenManager, MazeGame maze) {
		super(screenManager,NAME, "pictures/mainScreen" + maze.getGraphicalConfiguration().getWidth() + "x" + maze.getGraphicalConfiguration().getHeight() + ".jpg");
		
		int width = maze.getGraphicalConfiguration().getWidth();
		int height = maze.getGraphicalConfiguration().getHeight();
		launchButton_ = new ControlButton("launch", width - 231 - 20 - 16, height - 30 - 170 - 38, 231, 30, true);
		configButton_ = new ControlButton("config", width - 231 - 20 - 16, height - 30 - 120 - 38, 231, 30, true);
		scoreButton_ = new ControlButton("score", width - 231 - 20 - 16, height - 30 - 70 - 38, 231, 30, true);
		exitButton_ = new ControlButton("exit", width - 231 - 20 - 16, height - 30 - 20 - 38, 231, 30, true);
		maze_ = maze;
	}

	@Override
	public void init() {
		super.init();
		
		launchButton_.setLabel("Launch", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		launchButton_.setAction(new ChangeScreenAction(getScreenManager(),LaunchScreen.NAME));
		launchButton_.setKey(KeyEvent.VK_L);
		launchButton_.init();
		addControl(launchButton_);

		configButton_.setLabel("Config", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		configButton_.setAction(new ChangeScreenAction(getScreenManager(),ConfigScreen.NAME));
		configButton_.setKey(KeyEvent.VK_C);
		configButton_.init();
		addControl(configButton_);

		scoreButton_.setLabel("Score", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		scoreButton_.setAction(new ChangeScreenAction(getScreenManager(),ScoreScreen.NAME));
		scoreButton_.setKey(KeyEvent.VK_S);
		scoreButton_.init();
		addControl(scoreButton_);

		exitButton_.setLabel("Exit", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		exitButton_.setAction(new ExitAction());
		exitButton_.setKey(KeyEvent.VK_ESCAPE);
		exitButton_.init();
		addControl(exitButton_);
	}

	@Override
	public void changeScreenSize(int width, int height) {
		changeBackgroundImage("pictures/mainScreen" + width + "x" + height + ".jpg");

		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int x = (dim.width - backgroundImage_.getWidth(null))/2;
		int y = (dim.height - backgroundImage_.getHeight(null))/2;

		// Move the window
		setPosition(x, y);
	}
}
