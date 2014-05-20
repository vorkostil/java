package client;

import java.awt.Polygon;

import server.ServerManager;
import server.model.Environment;

public class MainMaze 
{
	public static void main(String[] args) 
	{
		Environment env = new Environment();
		Polygon wallUp = new Polygon();
		wallUp.addPoint(-10,-10);
		wallUp.addPoint(810,-10);
		wallUp.addPoint(810,10);
		wallUp.addPoint(-10,10);
		env.addWall(wallUp, true);

		Polygon wallLeft = new Polygon();
		wallLeft.addPoint(-10,-10);
		wallLeft.addPoint(10,-10);
		wallLeft.addPoint(10,610);
		wallLeft.addPoint(-10,610);
		env.addWall(wallLeft, true);
		
		Polygon wallDown = new Polygon();
		wallDown.addPoint(-10,610);
		wallDown.addPoint(810,610);
		wallDown.addPoint(810,590);
		wallDown.addPoint(-10,590);
		env.addWall(wallDown, true);
		
		Polygon wallRight = new Polygon();
		wallRight.addPoint(810,610);
		wallRight.addPoint(790,610);
		wallRight.addPoint(790,-10);
		wallRight.addPoint(810,-10);
		env.addWall(wallRight, true);
		
		env.addPlayer(200,300);
		
		// server manager initialization
		ServerManager manager = new ServerManager(env);
		manager.run();
		
		MainView view = new MainView("MAZE",env) ; 
		view.setVisible(true) ;
	}
}
