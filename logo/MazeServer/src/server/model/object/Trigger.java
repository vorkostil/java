package server.model.object;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public abstract class Trigger extends PhysicalObject implements IReactiveObject {

	public enum Policy { pFOREVER, pONCE }

	private Polygon model_ = null;
	protected Policy policy_ = Policy.pFOREVER;
	
	public Trigger()
	{
	}
	
	public Trigger(String name, boolean visible, boolean crossable, boolean active) {
		super(name, visible, crossable, active);
	}

	public void setModel(Polygon model)
	{
		model_ = model;
	}
	
	@Override
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

	public void setPolicy(Policy policy) {
		policy_ = policy;
	}
	
	public void setPolicy(String policyString) {
		policy_ = Policy.valueOf(policyString);
	}
}
