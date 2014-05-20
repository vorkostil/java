package client;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import client.object.GraphicalPlayer;
import client.object.GraphicalTrigger;
import client.object.GraphicalWall;
import client.visitor.AbstractVisitor;

import server.model.Environment;
import server.model.object.Player;
import server.model.object.Trigger;
import server.model.object.TriggerModifyPhysicalAttributes;
import server.model.object.Wall;

public class GraphicalEnvironment {

	List<GraphicalTrigger> gTriggers_ = new ArrayList<GraphicalTrigger>();
	List<GraphicalWall> gWalls_ = new ArrayList<GraphicalWall>();
	GraphicalPlayer gPlayer_ = null;
	
	public GraphicalEnvironment(Environment env, List<AbstractVisitor> visitors) 
	{
		for(Object object : env.getObjects())
		{
			if (object instanceof Wall)
				gWalls_.add(new GraphicalWall((Wall) object, visitors));
			else if (object instanceof Player)
				gPlayer_ = new GraphicalPlayer((Player) object, visitors);
			else if (object instanceof TriggerModifyPhysicalAttributes)
				gTriggers_.add(new GraphicalTrigger((Trigger) object, visitors));
		}
	}

	public void draw(Graphics g, int x, int y, int width, int height) 
	{
		for (GraphicalWall wall : gWalls_)
		{
			wall.draw(g,x,y,width,height);
		}
		for (GraphicalTrigger trigger : gTriggers_)
		{
			trigger.draw(g,x,y,width,height);
		}
		gPlayer_.draw(g,x,y,width,height);
	}
}
