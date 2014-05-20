package server.model.action;

import java.awt.geom.Point2D;

import server.model.object.IReactiveObject;

public class ActionMove implements IAction 
{
	private IReactiveObject source_ = null;
	private Point2D destination_ = null;
	
	public ActionMove(IReactiveObject source, Point2D destination) 
	{
		source_ = source;
		destination_ = destination;
	}

	public Point2D getDestination() {
		return destination_;
	}
	
	@Override
	public void run() 
	{
		source_.applyAction(this);
	}

}
