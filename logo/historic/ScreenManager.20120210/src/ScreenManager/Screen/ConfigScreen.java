package ScreenManager.Screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import ScreenManager.ScreenManager;
import ScreenManager.ScreenAction.ChangeScreenAction;
import ScreenManager.ScreenControl.ScreenControlButton;

public class ConfigScreen extends ScreenWithControl {

	public static final String NAME = "config";

	private ScreenControlButton backButton_ = new ScreenControlButton("back", 150, 250, 231, 30, true);

	public ConfigScreen(ScreenManager screenManager) {
		super(screenManager, NAME, "pictures/configScreen.jpg");
	}

	@Override
	public void init() {
		backButton_.setLabel("Back", new Font("Lucida Console", Font.BOLD, 12), Color.CYAN, 50, 20);
		backButton_.setAction(new ChangeScreenAction(getScreenManager_(),MainScreen.NAME));
		backButton_.setKey(KeyEvent.VK_B);
		addControl(backButton_);
	}
}
