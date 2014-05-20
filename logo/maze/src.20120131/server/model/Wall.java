package server.model;

import java.awt.Polygon;
import java.io.Serializable;

public class Wall extends PhysicalObject implements Serializable
{
	private static final long serialVersionUID = -6694251534424825897L;
	private Polygon model_ = null;
	
	public Wall(Polygon model, boolean visible)
	{
		super(visible,false);
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
}
