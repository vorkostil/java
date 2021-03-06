package maze.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import maze.MazeGame;
import ScreenManager.ScreenManager;
import ScreenManager.Screen.ScreenWithControl;
import ScreenManager.ScreenAction.ChangeScreenAction;
import ScreenManager.ScreenControl.ControlButton;

public class EndScreen extends ScreenWithControl {

	public static final String NAME = "end_game";
	
	private ControlButton okButton_ = new ControlButton("ok", 150, 250, 231, 30, true);

	private MazeGame maze_ = null;

	public EndScreen(ScreenManager screenManager, MazeGame maze) {
		super(screenManager, NAME, "pictures/endGameScreen" + maze.getGraphicalConfiguration().getWidth() + "x" + maze.getGraphicalConfiguration().getHeight() + ".jpg");
		maze_ = maze;
	}

	@Override
	public void init() {
		okButton_.setLabel("OK", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		okButton_.setAction(new ChangeScreenAction(getScreenManager(),ScoreScreen.NAME));
		okButton_.setKey(KeyEvent.VK_ENTER);
		okButton_.init();
		addControl(okButton_);
	}

	@Override
	public void changeScreenSize(int width, int height) {
		changeBackgroundImage("pictures/endGameScreen" + width + "x" + height + ".jpg");
	}
}
