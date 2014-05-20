package client.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import server.model.object.PhysicalObject;
import client.visitor.AbstractVisitor;

public class GraphicalItem 
{
	private PhysicalObject data_;
	private List<AbstractVisitor> displayer_ = null;
	private Color color_ = Color.BLACK;
	private boolean intersectOnly_ = false;
	
	public GraphicalItem(PhysicalObject data, List<AbstractVisitor> visitors, boolean intersectOnly, Color color) 
	{
		displayer_ = visitors;
		data_ = data;
		color_ = color;
		intersectOnly_ = intersectOnly;
	}
	
	public void draw(Graphics g, int x, int y, int width, int height) 
	{
		for (AbstractVisitor visitor : displayer_)
		{
			visitor.visit(this,g,x,y,width,height);
		}
	}
	
	public boolean needDrawing(Rectangle rect)
	{
		if (intersectOnly_)
		{
			for (int i = 0; i < data_.getModel().npoints - 1; ++i)
			{
				if (rect.intersectsLine(data_.getModel().xpoints[i], data_.getModel().ypoints[i],data_.getModel().xpoints[i+1], data_.getModel().ypoints[i+1]))
					return true;
			}
		}
		else
		{
			for (int i = 0; i < data_.getModel().npoints; ++i)
			{
				if (rect.contains(data_.getModel().xpoints[i], data_.getModel().ypoints[i]))
					return true;
			}
		}
		return false;
	}

	public PhysicalObject getData() 
	{
		return data_;
	}

	public Color getColor() {
		return color_;
	}
}
