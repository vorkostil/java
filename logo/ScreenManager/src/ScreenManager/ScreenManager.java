package ScreenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import ScreenManager.Screen.AbstractScreen;

public class ScreenManager extends Observable {

	private Map<String, AbstractScreen> screens_ = new HashMap<String, AbstractScreen>();
	private boolean fullscreen_ = false;

	private List<AbstractScreen> displayScreens_ = new ArrayList<AbstractScreen>();

	public void addScreen(AbstractScreen screen) {
		screens_.put(screen.getName(), screen);
	}

	public void setCurrentScreen(String screenName) {
		AbstractScreen newScreen = screens_.get(screenName);
		if (newScreen != null) {
			for (AbstractScreen screen : displayScreens_) {
				screen.tearDown();
			}
			displayScreens_.clear();

			newScreen.setup();
			displayScreens_.add(newScreen);

			setChanged();
			notifyObservers();
		}
	}

	public boolean init(String currentScreenName) {
		for (AbstractScreen screen : screens_.values()) {
			screen.init();
		}
		AbstractScreen newScreen = screens_.get(currentScreenName);
		if (newScreen != null) {
			newScreen.setup();
			displayScreens_.add(newScreen);
		}
		return newScreen != null;
	}

	public List<AbstractScreen> getCurrentScreens() {
		return displayScreens_;
	}

	public void addCurrentScreen(String name) {
		if (displayScreens_.size() == 0) {
			setCurrentScreen(name);
		} else {
			AbstractScreen newScreen = screens_.get(name);
			if (newScreen != null) {
				newScreen.setup();
				displayScreens_.add(0, newScreen);

				setChanged();
				notifyObservers();
			}
		}
	}

	public void changeScreenSize(int width, int height) {
		for (AbstractScreen screen : screens_.values()) {
			screen.changeScreenSize(width, height);
		}
	}

	public void setFullscreen(boolean fullscreen) {
		fullscreen_ = fullscreen;
	}

	public boolean isFullscreen() {
		return fullscreen_;
	}

	public AbstractScreen getCurrentRootScreen() {
		if (displayScreens_.size() > 0)
			return displayScreens_.get(displayScreens_.size() - 1);
		return null;
	}

	public void removeCurrentScreen() {
		if (displayScreens_.size() > 1) {
			displayScreens_.get(0).tearDown();
			displayScreens_.remove(0);

			setChanged();
			notifyObservers();
		}
	}
}
