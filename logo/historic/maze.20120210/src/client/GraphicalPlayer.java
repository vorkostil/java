package client;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import server.model.Player;

public class GraphicalPlayer 
{
	private Player data_ = null;
	private List<AbstractVisitor> displayer_ = null;

	public GraphicalPlayer(Player player, List<AbstractVisitor> visitors) 
	{
		displayer_ = visitors;
		data_ = player;
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
		return rect.contains(data_.x(), data_.y());
	}

	public Player getData() 
	{
		return data_;
	}
}
