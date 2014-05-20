package server.model;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Wall extends PhysicalObject implements Serializable
{
	private static final long serialVersionUID = -6694251534424825897L;
	private Polygon model_ = null;
	
	public Wall(String name, Polygon model, boolean visible)
	{
		super(name,visible,false,true);
		setModel(model);
	}
	
	public void setModel(Polygon model)
	{
		model_ = model;
	}
	
	public Polygon getModel()
	{
		return model_;
	}

	@Override
	public boolean intersect(Rectangle bounds) 
	{
		return model_.intersects(bounds);
	}

	@Override
	public Rectangle getBounds(Point2D destination_) 
	{
		return model_.getBounds();
	}

	@Override
	public void moveTo(Point2D destination_) 
	{
	}

	@Override
	public void moveOn(PhysicalObject source_) {
	}
}
