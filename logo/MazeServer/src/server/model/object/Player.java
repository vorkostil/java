package server.model.object;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import server.model.action.ActionMove;
import server.model.action.IAction;
import server.model.intention.IIntention;
import server.model.intention.IntentionMove;

public class Player extends PhysicalObject implements Serializable, ICognitiveObject, IReactiveObject
{
	private static final long serialVersionUID = 5090708816115468683L;

	private enum State {WAITING, MOVING }
	
	public static final double DIRECTION_UP = Math.toRadians(270);
	public static final double DIRECTION_DOWN = Math.toRadians(90);
	public static final double DIRECTION_LEFT = Math.toRadians(180);
	public static final double DIRECTION_RIGHT = Math.toRadians(0);
	private static final double NO_DIRECTION = -1;
	
	private int initialX_;
	private int initialY_;
	private int x_;
	private int y_;
	private State status_ = State.WAITING;
	private double currentDirection_ = NO_DIRECTION;
	private double previousDirection_ = NO_DIRECTION;
	private long previousTimestamp_ = 0; 
	private double speed_ = 0.25;
	
	private String imagePath_ = null;
	private Polygon model_ = null;
	private int width_;
	private int height_;
	
	public Player(String name, int x, int y, int width, int height, String imagePath)
	{
		super(name, true,true, true);
		initialX_ = x_ = x + width / 2;
		initialY_ = y_ = y + height / 2;
	
		model_ = new Polygon();
		model_.addPoint(0, 0);
		model_.addPoint(width - 1, 0);
		model_.addPoint(width - 1, height - 1);
		model_.addPoint(0, height - 1);
		
		width_ = width;
		height_ = height;
		
		imagePath_ = imagePath;
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
	public List<IIntention> process(long time) 
	{
		List<IIntention> result = new ArrayList<IIntention>();
		long delay = previousTimestamp_ != 0 ? time - previousTimestamp_ : 0;
		previousTimestamp_ = time;
		
		if (status_ == State.MOVING && currentDirection_ != NO_DIRECTION)
		{
			double distance = delay * speed_;
//			System.out.println("distance: " + distance);
			result.add(new IntentionMove(this,new Point2D.Double(x_ + Math.cos(currentDirection_) * distance,y_ + Math.sin(currentDirection_) * distance)));
		}
		
		return result;
	}

	@Override
	public boolean intersect(Rectangle boundingBox) 
	{
		return getBounds(new Point2D.Double(x_, y_)).intersects(boundingBox);
	}

	@Override
	public Rectangle getBounds(Point2D destination_) 
	{
		if (destination_ == null)
			return new Rectangle(x_ - width_ / 2, y_ - height_ / 2, width_, height_);
		return new Rectangle((int)destination_.getX() - 16, (int)destination_.getY() - 16, 32, 32);
	}

	@Override
	public boolean applyAction(IAction action) 
	{
		if (action instanceof ActionMove)
		{
			moveTo_(((ActionMove)action).getDestination());
			return true;
		}
		return false;
	}
	
	private void moveTo_(Point2D destination_) 
	{
		x_ = (int)destination_.getX();
		y_ = (int)destination_.getY();
	}

	public void stop() {
		previousTimestamp_ = 0;
		status_ = State.WAITING;
		currentDirection_ = NO_DIRECTION;
		previousDirection_ = NO_DIRECTION;
		x_ = initialX_;
		y_ = initialY_;
	}

	@Override
	public Polygon getModel() {
		Polygon temp = new Polygon(model_.xpoints,model_.ypoints,model_.npoints);
		temp.translate(x_, y_);
		return temp;
	}

	public String getImagePath() {
		return imagePath_;
	}
}
