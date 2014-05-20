package server.model;

import java.awt.geom.Point2D;

public abstract class PhysicalObject 
{
	private boolean visibility_ = false;
	private boolean crossable_ = false;
	
	public PhysicalObject(boolean visible, boolean crossable)
	{
		visibility_ = visible;
		crossable_ = crossable;
	}
	
	public void setVisibility(boolean visible)
	{
		visibility_ = visible;
	}
	
	public boolean isVisible()
	{
		return visibility_;
	}
	
	public void setCrossable(boolean crossable)
	{
		crossable_ = crossable;
	}
	
	public boolean isCrossable()
	{
		return crossable_;
	}

	public abstract boolean contains(Point2D destination_); 
}
