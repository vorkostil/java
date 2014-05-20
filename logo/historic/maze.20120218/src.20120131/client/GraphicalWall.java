package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import server.model.Wall;

public class GraphicalWall 
{
	private Wall data_;
	
	public GraphicalWall(Wall wall) 
	{
		data_ = wall;
	}
	
	public void draw(Graphics g, int x, int y, int width, int height) 
	{
		if (data_.isVisible() && needDrawing(new Rectangle(x,y,width,height)))
		{
			Color oldColor = g.getColor();
			g.setColor(Color.RED);
			g.fillPolygon(data_.getModel());
			g.setColor(oldColor);
		}
	}
	
	private boolean needDrawing(Rectangle rect)
	{
		for (int i = 0; i < data_.getModel().npoints; ++i)
		{
			if (rect.contains(data_.getModel().xpoints[i], data_.getModel().ypoints[i]))
				return true;
		}
		return false;
	}
}
