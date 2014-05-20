package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GraphicalVisitor extends AbstractVisitor 
{
	@Override
	public void visit(GraphicalWall wall, Graphics g, int x, int y, int width,int height) 
	{
		if (wall.getData().isVisible() && wall.needDrawing(new Rectangle(x,y,width,height)))
		{
			Color oldColor = g.getColor();
			g.setColor(Color.RED);
			g.fillPolygon(wall.getData().getModel());
			g.setColor(oldColor);
		}
	}

	@Override
	public void visit(GraphicalPlayer player, Graphics g, int x, int y,	int width, int height) 
	{
		if (player.needDrawing(new Rectangle(x,y,width,height)))
		{
			Color oldColor = g.getColor();
			g.setColor(Color.BLUE);
			g.drawRect(player.getData().x() - 16, player.getData().y() - 16, 32, 32);
			g.setColor(oldColor);
		}
	}

	@Override
	public void visit(GraphicalTrigger trigger, Graphics g, int x, int y, int width, int height) {
		if (trigger.getData().isVisible() && trigger.needDrawing(new Rectangle(x,y,width,height)))
		{
			Color oldColor = g.getColor();
			g.setColor(Color.GREEN);
			g.fillPolygon(trigger.getData().getModel());
			g.setColor(oldColor);
		}
	}
}
