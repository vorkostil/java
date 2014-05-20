package client.visitor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import client.object.GraphicalItem;
import client.object.GraphicalItemWithPicture;
import client.object.GraphicalPlayer;

public class GraphicalVisitor extends AbstractVisitor 
{
	private Polygon copyAndTranslate_(Polygon model, int x, int y) {
		Polygon result = new Polygon(model.xpoints,model.ypoints,model.npoints);
		result.translate(-x, -y);
		return result;
	}

	@Override
	public void visit(GraphicalItem item, Graphics g, int x, int y, int width,int height) 
	{
		if (item.getData().isVisible() && item.needDrawing(new Rectangle(x,y,width,height)))
		{
			Color oldColor = g.getColor();
			g.setColor(item.getColor());
			g.fillPolygon(copyAndTranslate_(item.getData().getModel(),x,y));
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
			g.drawRect(player.getData().x() - 16 - x, player.getData().y() - 16 - y, 32, 32);
			g.setColor(oldColor);
		}
	}
	
	@Override
	public void visit(GraphicalItemWithPicture item, Graphics g, int x, int y,	int width, int height) {
		if (item.isVisible() && item.needDrawing(new Rectangle(x,y,width,height)))
		{
			g.drawImage(item.getImage(),item.getX() - x,item.getY() - y,null);
		}
	}
}
