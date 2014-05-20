package server.model;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

public abstract class PhysicalObject 
{
	private boolean visibility_ = false;
	private boolean crossable_ = false;
	private String name_ = null;
	
	public PhysicalObject(String name, boolean visible, boolean crossable)
	{
		name_ = name;
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

	public abstract void moveTo(Point2D destination_); 
	public abstract boolean intersect(Rectangle boundingBox);
	public abstract Rectangle getBounds(Point2D destination_);
	
	public String toString()
	{
		return name_;
	}
}
