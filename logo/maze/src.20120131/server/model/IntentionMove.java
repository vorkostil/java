package server.model;

import java.awt.geom.Point2D;

public class IntentionMove implements IIntention 
{
	private Point2D destination_ = null;
	
	public IntentionMove(Point2D destination)
	{
		destination_ = destination;
	}
	
	public Point2D getDestination()
	{
		return destination_;
	}

	@Override
	public boolean isValid(Environment environment) 
	{
		return environment.isCrossable(destination_);
	}

	@Override
	public IAction createAction() 
	{
		return null;
	}
}
