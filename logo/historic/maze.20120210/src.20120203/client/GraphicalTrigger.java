package client;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import server.model.Trigger;

public class GraphicalTrigger
{
	private Trigger data_ = null;
	private List<AbstractVisitor> displayer_ = null;

	public GraphicalTrigger(Trigger trigger, List<AbstractVisitor> visitors) 
	{
		displayer_ = visitors;
		data_ = trigger;
	}

	public void draw(Graphics g, int x, int y, int width, int height) 
	{
		for (AbstractVisitor visitor : displayer_)
		{
			visitor.visit(this, g, x, y, width, height);
		}
	}
	
	public boolean needDrawing(Rectangle rect)
	{
		for (int i = 0; i < data_.getModel().npoints; ++i)
		{
			if (rect.contains(data_.getModel().xpoints[i], data_.getModel().ypoints[i]))
				return true;
		}
		return false;
	}

	public Trigger getData() 
	{
		return data_;
	}
}
