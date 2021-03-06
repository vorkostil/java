package client;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import server.model.Environment;
import server.model.Player;
import server.model.Wall;

public class GraphicalEnvironment {

	List<GraphicalWall> gWalls_ = new ArrayList<GraphicalWall>();
	GraphicalPlayer gPlayer_ = null;
	
	public GraphicalEnvironment(Environment env) 
	{
		for(Object object : env.getObjects())
		{
			if (object instanceof Wall)
				gWalls_.add(new GraphicalWall((Wall) object));
			else if (object instanceof Player)
				gPlayer_ = new GraphicalPlayer((Player) object);
		}
	}

	public void draw(Graphics g, int x, int y, int width, int height) 
	{
		for (GraphicalWall wall : gWalls_)
		{
			wall.draw(g,x,y,width,height);
		}
		gPlayer_.draw(g,x,y,width,height);
	}
}
