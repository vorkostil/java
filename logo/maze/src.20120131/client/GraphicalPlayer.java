package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import server.model.Player;

public class GraphicalPlayer 
{
	Player data_ = null;
	
	public GraphicalPlayer(Player player) 
	{
		data_ = player;
	}

	public void draw(Graphics g, int x, int y, int width, int height) 
	{
		if (needDrawing(new Rectangle(x,y,width,height)))
		{
			Color oldColor = g.getColor();
			g.setColor(Color.BLUE);
			g.drawRect(data_.x() - 16, data_.y() - 16, 32, 32);
			g.setColor(oldColor);
		}
	}
	
	private boolean needDrawing(Rectangle rect)
	{
		return rect.contains(data_.x(), data_.y());
	}
}
