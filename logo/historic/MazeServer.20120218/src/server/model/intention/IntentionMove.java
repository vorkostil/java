package server.model.intention;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import server.model.Environment;
import server.model.action.ActionMove;
import server.model.action.ActionMoveOn;
import server.model.action.IAction;
import server.model.object.IReactiveObject;
import server.model.object.PhysicalObject;

public class IntentionMove implements IIntention 
{
	private PhysicalObject source_ = null;
	private Point2D destination_ = null;
	private List<PhysicalObject> intersecteds_ = new ArrayList<PhysicalObject>();
	
	public IntentionMove(PhysicalObject source, Point2D destination)
	{
		source_ = source;
		destination_ = destination;
	}
	
	@Override
	public boolean isValid(Environment environment) 
	{
		intersecteds_ = environment.getIntersectedObjects(source_.getBounds(destination_));
		for (PhysicalObject object : intersecteds_)
		{
			if (!object.isCrossable() && object.isActive())
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public List<IAction> createActions()
	{
		List<IAction> actions = new ArrayList<IAction>();
		actions.add(new ActionMove((IReactiveObject)source_,destination_));
		for (PhysicalObject object : intersecteds_)
		{
			if (object instanceof IReactiveObject)
				actions.add(new ActionMoveOn((IReactiveObject)object,source_));
		}
		return actions;
	}
}
