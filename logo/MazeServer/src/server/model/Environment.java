package server.model;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import server.model.action.IAction;
import server.model.intention.IIntention;
import server.model.object.PhysicalObject;
import server.model.object.Player;
import server.model.object.Wall;

public class Environment implements Serializable
{
	private static final long serialVersionUID = 4493142088921428652L;
	private Player player_ = null;
	private List<PhysicalObject> physicalObjects_ = new ArrayList<PhysicalObject>();
	
	public void addWall(String name, Polygon model, boolean visible) 
	{
		physicalObjects_.add(new Wall(name,model,visible));
	}

	public void addPhysicalObject(PhysicalObject object) 
	{
		physicalObjects_.add(object);
	}
	
	public void addPlayer(int x, int y, int w, int h, String imagePath) 
	{
		player_ = new Player("Player1",x,y,w,h,imagePath);
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
				actions.addAll(intention.createActions());
			}
		}
		
		for (IAction action : actions)
		{
			action.run();
		}
	}

	public List<PhysicalObject> getIntersectedObjects(Rectangle boundingBox) 
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

	public void stop() {
		player_.stop();
	}

	public PhysicalObject getPhysicalObject(String objectName) {
		for (PhysicalObject object : physicalObjects_)
			if (object.getName().compareTo(objectName) == 0)
				return object;
		return null;
	}
}
