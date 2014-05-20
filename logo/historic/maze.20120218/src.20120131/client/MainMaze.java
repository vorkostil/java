package client;

import java.awt.Polygon;

import server.ServerManager;
import server.model.Environment;

public class MainMaze 
{
	public static void main(String[] args) 
	{
		Environment env = new Environment();
		Polygon wall = new Polygon();
		wall.addPoint(0,0);
		wall.addPoint(100,0);
		wall.addPoint(100,100);
		wall.addPoint(0,100);
		env.addWall(wall, true);
		
		env.addPlayer(200,300);
		
		// server manager initialization
		ServerManager manager = new ServerManager(env);
		manager.run();
		
		MainView view = new MainView("MAZE",env) ; 
		view.setVisible(true) ;
	}
}
