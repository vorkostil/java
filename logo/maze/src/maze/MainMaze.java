package maze;

import maze.screen.ConfigScreen;
import maze.screen.EndScreen;
import maze.screen.LaunchScreen;
import maze.screen.MainScreen;
import maze.screen.MazeScreen;
import maze.screen.PauseScreen;
import maze.screen.ScoreScreen;
import maze.screen.SplashScreen;
import ScreenManager.MainView;
import ScreenManager.ScreenManager;

public class MainMaze 
{
	public static void main(String[] args) 
	{
		MazeGame maze = new MazeGame();
		maze.initConfiguration();
		
		ScreenManager screenManager = new ScreenManager();
		screenManager.addScreen(new SplashScreen(screenManager));
		screenManager.addScreen(new MainScreen(screenManager,maze));
		screenManager.addScreen(new ConfigScreen(screenManager,maze));
		screenManager.addScreen(new LaunchScreen(screenManager,maze));
		screenManager.addScreen(new MazeScreen(screenManager,maze));
		screenManager.addScreen(new EndScreen(screenManager,maze));
		screenManager.addScreen(new ScoreScreen(screenManager,maze));
		screenManager.addScreen(new PauseScreen(screenManager,maze));
		
		screenManager.init(MazeScreen.NAME);
		
		maze.setScreenManager(screenManager);
		
		MainView view = new MainView(screenManager); 
		view.setVisible(true);
		
	}
}
