package server.model;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Environment implements Serializable
{
	private static final long serialVersionUID = 4493142088921428652L;
	private Player player_ = null;
	private List<PhysicalObject> physicalObjects_ = new ArrayList<PhysicalObject>();
	
	public void addWall(Polygon model, boolean visible) 
	{
		physicalObjects_.add(new Wall("Wall1",model,visible));
	}
	
	public void addPlayer(int x, int y) 
	{
		player_ = new Player("Player1",x,y);
		physicalObjects_.add(player_);
	}

	public void process(long time) 
	{
		List<IIntention> intentions = player_.process(time);
		List<IAction> actions = new ArrayList<IAction>();
		for (IIntention intention : intentions)
		{
			if (intention.isValid(this))
			{
				actions.add(intention.createAction());
			}
		}
		
		for (IAction action : actions)
		{
			action.run();
		}
	}

	public List<PhysicalObject> getObjects(Rectangle boundingBox) 
	{
		List<PhysicalObject> objects = new ArrayList<PhysicalObject>();
		for (PhysicalObject object : physicalObjects_)
		{
			if (object.intersect(boundingBox))
			{
				objects.add(object);
			}
		}
		return objects;
	}

	public List<PhysicalObject> getObjects() 
	{
		return physicalObjects_;
	}

	public Player getPlayer() 
	{
		return player_;
	}
}
