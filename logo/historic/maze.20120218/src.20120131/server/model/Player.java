package server.model;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player extends PhysicalObject implements Serializable, ICognitiveObject, IReactiveObject
{
	private static final long serialVersionUID = 5090708816115468683L;

	private enum State {WAITING, MOVING }

	private static final int DIRECTION_UP = 90;
	private static final int DIRECTION_DOWN = 270;
	private static final int DIRECTION_LEFT = 180;
	private static final int DIRECTION_RIGHT = 0;
	
	private int x_;
	private int y_;
	private State status_ = State.WAITING;
	private int direction_;
	private long previousTimestamp_ = 0; 
	private double speed_ = 0.1;
	
	public Player(int x, int y)
	{
		super(true,true);
		x_ = x;
		y_ = y;
		direction_ = 0;
	}
	
	public int x()
	{
		return x_;
	}
	
	public int y()
	{
		return y_;
	}

	public State getStatus()
	{
		return status_;
	}
	
	public void startMovingUp() 
	{
		status_ = State.MOVING;
		direction_ = DIRECTION_UP;
	}

	public void stopMovingUp() 
	{
		if (direction_ == DIRECTION_UP)
		{
			status_ = State.WAITING;
		}
	}

	public void startMovingDown() 
	{
		status_ = State.MOVING;
		direction_ = DIRECTION_DOWN;
	}

	public void stopMovingDown() 
	{
		if (direction_ == DIRECTION_DOWN)
		{
			status_ = State.WAITING;
		}
	}

	public void startMovingLeft() 
	{
		status_ = State.MOVING;
		direction_ = DIRECTION_LEFT;
	}

	public void stopMovingLeft() 
	{
		if (direction_ == DIRECTION_LEFT)
		{
			status_ = State.WAITING;
		}
	}

	public void startMovingRight() 
	{
		status_ = State.MOVING;
		direction_ = DIRECTION_RIGHT;
	}

	public void stopMovingRight() 
	{
		if (direction_ == DIRECTION_RIGHT)
		{
			status_ = State.WAITING;
		}
	}

	@Override
	public boolean applyAction(IAction action) 
	{
		return true;
	}

	@Override
	public List<IIntention> process(long time) 
	{
		List<IIntention> result = new ArrayList<IIntention>();
		long delay = previousTimestamp_ != 0 ? time - previousTimestamp_ : 0;
		previousTimestamp_ = time;
		
		if (status_ == State.MOVING)
		{
			double distance = delay * speed_;
			result.add(new IntentionMove(new Point2D.Double(x_ + Math.cos(Math.toRadians(direction_)) * distance,y_ + Math.sin(Math.toRadians(direction_)) * distance)));
		}
		
		return result;
	}

	@Override
	public boolean contains(Point2D destination_) 
	{
		return false;
	}
}
