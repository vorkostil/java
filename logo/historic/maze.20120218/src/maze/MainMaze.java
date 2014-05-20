package maze;

import screen.ConfigScreen;
import screen.LaunchScreen;
import screen.MainScreen;
import screen.MazeScreen;
import screen.SplashScreen;
import ScreenManager.MainView;
import ScreenManager.ScreenManager;

public class MainMaze 
{
	public static void main(String[] args) 
	{
		MazeGame maze = new MazeGame();
		
		ScreenManager screenManager = new ScreenManager();
		screenManager.addScreen(new SplashScreen(screenManager,200,200));
		screenManager.addScreen(new MainScreen(screenManager));
		screenManager.addScreen(new ConfigScreen(screenManager,maze));
		screenManager.addScreen(new LaunchScreen(screenManager,maze));
		screenManager.addScreen(new MazeScreen(screenManager,maze));
		screenManager.init(SplashScreen.NAME);
		
		MainView view = new MainView(screenManager); 
		view.setVisible(true);
		
	}
}
