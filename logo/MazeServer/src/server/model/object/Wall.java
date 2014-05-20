package server.model.object;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Wall extends PhysicalObject implements Serializable
{
	private static final long serialVersionUID = -6694251534424825897L;
	private Polygon model_ = null;
	
	public Wall(String line)
	{
		String values[] = line.split(";");
		name_ = values[0];
		setVisibility(Boolean.parseBoolean(values[1]));
		setCrossable(false);
		setActive(true);
		
		model_ = new Polygon();
		for (int i = 0; i < Integer.parseInt(values[2]); ++i)
			model_.addPoint(Integer.parseInt(values[i+3].split(",")[0]),Integer.parseInt(values[i+3].split(",")[1]));
	}
	
	public Wall(String name, Polygon model, boolean visible)
	{
		super(name,visible,false,true);
		setModel(model);
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
	public boolean intersect(Rectangle bounds) 
	{
		return model_.intersects(bounds);
	}

	@Override
	public Rectangle getBounds(Point2D destination_) 
	{
		return model_.getBounds();
	}
}
