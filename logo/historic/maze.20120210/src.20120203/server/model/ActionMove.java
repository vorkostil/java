package server.model;

import java.awt.geom.Point2D;

public class ActionMove implements IAction 
{
	private PhysicalObject source_ = null;
	private Point2D destination_ = null;
	
	public ActionMove(PhysicalObject source, Point2D destination) 
	{
		source_ = source;
		destination_ = destination;
	}

	@Override
	public void run() 
	{
		source_.moveTo(destination_);
	}

}
