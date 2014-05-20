package server.model;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player extends PhysicalObject implements Serializable, ICognitiveObject, IReactiveObject
{
	private static final long serialVersionUID = 5090708816115468683L;

	private enum State {WAITING, MOVING }
	
	public static final double DIRECTION_UP = Math.toRadians(270);
	public static final double DIRECTION_DOWN = Math.toRadians(90);
	public static final double DIRECTION_LEFT = Math.toRadians(180);
	public static final double DIRECTION_RIGHT = Math.toRadians(0);
	private static final double NO_DIRECTION = -1;
	
	private int x_;
	private int y_;
	private State status_ = State.WAITING;
	private double currentDirection_ = NO_DIRECTION;
	private double previousDirection_ = NO_DIRECTION;
	private long previousTimestamp_ = 0; 
	private double speed_ = 0.1;
	
	public Player(String name, int x, int y)
	{
		super(name, true,true);
		x_ = x;
		y_ = y;
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
	
	public void startMoving(double direction)
	{
		if (currentDirection_ != direction)
		{
			previousDirection_ = currentDirection_;
			currentDirection_ = direction;
			status_ = State.MOVING;
		}
	}
	
	public void stopMoving(double direction)
	{
		if (currentDirection_ == direction)
		{
			currentDirection_ = previousDirection_;
			previousDirection_ = NO_DIRECTION;
		}
		else if (previousDirection_ == direction)
		{
			previousDirection_ = NO_DIRECTION;
		}
		
		if (currentDirection_ == NO_DIRECTION)
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
		
		if (status_ == State.MOVING && currentDirection_ != NO_DIRECTION)
		{
			double distance = delay * speed_;
			result.add(new IntentionMove(this,new Point2D.Double(x_ + Math.cos(currentDirection_) * distance,y_ + Math.sin(currentDirection_) * distance)));
		}
		
		return result;
	}

	@Override
	public boolean intersect(Rectangle boundingBox) 
	{
		return false;
	}

	@Override
	public Rectangle getBounds(Point2D destination_) 
	{
		return new Rectangle((int)destination_.getX() - 16, (int)destination_.getY() - 16, 32, 32);
	}

	@Override
	public void moveTo(Point2D destination_) 
	{
		x_ = (int)destination_.getX();
		y_ = (int)destination_.getY();
	}
}
