package maze.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import maze.MazeGame;
import maze.MazeGraphicalConfiguration;

import ScreenManager.ScreenManager;
import ScreenManager.Screen.ScreenWithControl;
import ScreenManager.ScreenAction.AbstractAction;
import ScreenManager.ScreenAction.ChangeScreenAction;
import ScreenManager.ScreenControl.ControlButton;
import ScreenManager.ScreenControl.ControlCheckBoxButton;
import ScreenManager.ScreenControl.ControlStateButton;

public class ConfigScreen extends ScreenWithControl {

	public static final String NAME = "config";

	private ControlStateButton w640Button_ = new ControlStateButton("640", 100, 150, 231, 30, true,false);
	private ControlStateButton w800Button_ = new ControlStateButton("480", 400, 150, 231, 30, true,false);
	private ControlCheckBoxButton fullScreenButton_ = new ControlCheckBoxButton("full", 400, 250, 231, 30, true,false);
	private ControlButton okButton_ = new ControlButton("ok", 150, 350, 231, 30, true);
	private ControlButton backButton_ = new ControlButton("back", 400, 350, 231, 30, true);
	
	private MazeGame maze_ = null;
	private MazeGraphicalConfiguration temporaryGraphicalConfiguration_ = null;
	
	public ConfigScreen(ScreenManager screenManager, MazeGame maze) {
		super(screenManager, NAME, "pictures/configScreen" + maze.getGraphicalConfiguration().getWidth() + "x" + maze.getGraphicalConfiguration().getHeight() + ".jpg");
		maze_ = maze;
		temporaryGraphicalConfiguration_ = maze_.getGraphicalConfiguration().clone();
	}

	@Override
	public void init() {
		w640Button_.setLabel("640x480", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		w640Button_.setAction(new AbstractAction() {
			@Override
			public void run() {
				temporaryGraphicalConfiguration_.setWidth(640);
				temporaryGraphicalConfiguration_.setHeight(480);
				w800Button_.setChecked(false);
			}},KeyEvent.VK_6);
		w640Button_.setChecked(maze_.getGraphicalConfiguration().getWidth() == 640);
		w640Button_.init();
		addControl(w640Button_);

		w800Button_.setLabel("800x600", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		w800Button_.setAction(new AbstractAction() {
			@Override
			public void run() {
				temporaryGraphicalConfiguration_.setWidth(800);
				temporaryGraphicalConfiguration_.setHeight(600);
				w640Button_.setChecked(false);
			}},KeyEvent.VK_8);
		w800Button_.setChecked(maze_.getGraphicalConfiguration().getWidth() == 800);
		w800Button_.init();
		addControl(w800Button_);

		fullScreenButton_.setLabel("Fullscreen", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		fullScreenButton_.setChecked(maze_.getGraphicalConfiguration().isFullscreen());
		fullScreenButton_.init();
		addControl(fullScreenButton_);

		okButton_.setLabel("OK", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		okButton_.setAction(new AbstractAction() {
			@Override
			public void run() {
				temporaryGraphicalConfiguration_.setFullscreen(fullScreenButton_.isChecked());
				maze_.modifyGraphicalConfiguration(temporaryGraphicalConfiguration_);
				getScreenManager().setCurrentScreen(MainScreen.NAME);
			}},KeyEvent.VK_ENTER);
		okButton_.init();
		addControl(okButton_);

		backButton_.setLabel("Cancel", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		backButton_.setAction(new ChangeScreenAction(getScreenManager(),MainScreen.NAME),KeyEvent.VK_ESCAPE);
		backButton_.init();
		addControl(backButton_);
	}

	@Override
	public void changeScreenSize(int width, int height) {
		changeBackgroundImage("pictures/configScreen" + width + "x" + height + ".jpg");
	}
}
