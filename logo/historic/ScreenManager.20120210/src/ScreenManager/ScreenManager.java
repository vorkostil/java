package ScreenManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import ScreenManager.Screen.AbstractScreen;


public class ScreenManager extends Observable {

	private Map<String,AbstractScreen> screens_ = new HashMap<String,AbstractScreen>();
	private String currentScreenName_ = null;
	private AbstractScreen currentScreen_ = null;
	
	public void addScreen(AbstractScreen screen) {
		screens_ .put(screen.getName(),screen);
	}

	public void setCurrentScreen(String screenName) {
		AbstractScreen newScreen = screens_.get(screenName); 
		if (newScreen != null) {
			if (currentScreen_ != null)
				currentScreen_.tearDown();
			currentScreenName_ = screenName;
			currentScreen_ = newScreen;
			currentScreen_.setup();
			
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
			currentScreenName_ = currentScreenName;
			currentScreen_ = newScreen;
			currentScreen_.setup();
		}
		return currentScreenName_ != null && currentScreen_ != null;
	}
	
	public AbstractScreen getCurrentScreen() {
		return currentScreen_;
	}
}
