package server.model;

import java.awt.geom.Point2D;
import java.util.List;

public class IntentionMove implements IIntention 
{
	private PhysicalObject source_ = null;
	private Point2D destination_ = null;
	
	public IntentionMove(PhysicalObject source, Point2D destination)
	{
		source_ = source;
		destination_ = destination;
	}
	
	@Override
	public boolean isValid(Environment environment) 
	{
		List<PhysicalObject> objects = environment.getObjects(source_.getBounds(destination_));
		for (PhysicalObject object : objects)
		{
			if (!object.isCrossable())
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public IAction createAction() 
	{
		return new ActionMove(source_,destination_);
	}
}
