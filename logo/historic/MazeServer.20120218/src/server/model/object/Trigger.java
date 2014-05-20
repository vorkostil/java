package server.model.object;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public abstract class Trigger extends PhysicalObject implements IReactiveObject {

	private Polygon model_ = null;
	
	public Trigger(String name, boolean visible, boolean crossable, boolean active) {
		super(name, visible, crossable, active);
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
	public boolean intersect(Rectangle bounds) {
		return model_.intersects(bounds);
	}

	@Override
	public Rectangle getBounds(Point2D destination_) {
		return model_.getBounds();
	}
}
