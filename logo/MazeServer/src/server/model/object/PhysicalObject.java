package server.model.object;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public abstract class PhysicalObject 
{
	public enum Attributes { aACTIVE, aVISIBLE, aCROSSABLE }
	
	private boolean active_ = false;
	private boolean visibility_ = false;
	private boolean crossable_ = false;
	protected String name_ = null;
	
	public PhysicalObject()
	{
	}
	
	public PhysicalObject(String name, boolean visible, boolean crossable, boolean active)
	{
		name_ = name;
		visibility_ = visible;
		crossable_ = crossable;
		active_ = active;
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
	
	public boolean isActive()
	{
		return active_;
	}

	public void setActive(boolean active)
	{
		active_ = active;
	}
	
	public boolean isCrossable()
	{
		return crossable_;
	}

	public String toString()
	{
		return name_;
	}

	public String getName()
	{
		return name_;
	}

	public abstract boolean intersect(Rectangle boundingBox);
	public abstract Rectangle getBounds(Point2D destination_);
	public abstract Polygon getModel();
	
}
