package client;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import server.model.Wall;

public class GraphicalWall 
{
	private Wall data_;
	private List<AbstractVisitor> displayer_ = null;
	
	public GraphicalWall(Wall wall, List<AbstractVisitor> visitors) 
	{
		displayer_ = visitors;
		data_ = wall;
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
		for (int i = 0; i < data_.getModel().npoints - 1; ++i)
		{
			if (rect.intersectsLine(data_.getModel().xpoints[i], data_.getModel().ypoints[i],data_.getModel().xpoints[i+1], data_.getModel().ypoints[i+1]))
				return true;
		}
		return false;
	}

	public Wall getData() 
	{
		return data_;
	}
}
