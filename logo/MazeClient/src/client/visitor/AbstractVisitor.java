package client.visitor;

import java.awt.Graphics;

import client.object.GraphicalItem;
import client.object.GraphicalItemWithPicture;
import client.object.GraphicalPlayer;

public abstract class AbstractVisitor 
{
	public abstract void visit(GraphicalItem object, Graphics g, int x, int y, int width, int height);
	public abstract void visit(GraphicalItemWithPicture item, Graphics g, int x, int y, int width, int height);
	public abstract void visit(GraphicalPlayer player, Graphics g, int x, int y, int width, int height);
}
