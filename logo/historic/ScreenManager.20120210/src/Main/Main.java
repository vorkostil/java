package Main;

import ScreenManager.MainView;
import ScreenManager.ScreenManager;
import ScreenManager.Screen.ConfigScreen;
import ScreenManager.Screen.LaunchScreen;
import ScreenManager.Screen.MainScreen;
import ScreenManager.Screen.SplashScreen;

public class Main 
{
	public static void main(String[] args) 
	{
		ScreenManager screenManager = new ScreenManager();
		screenManager.addScreen(new SplashScreen(screenManager));
		screenManager.addScreen(new MainScreen(screenManager));
		screenManager.addScreen(new ConfigScreen(screenManager));
		screenManager.addScreen(new LaunchScreen(screenManager));
		screenManager.init(SplashScreen.NAME);
		
		MainView view = new MainView(screenManager); 
		view.setVisible(true);
	}
}
