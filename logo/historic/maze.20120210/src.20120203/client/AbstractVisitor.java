package client;

import java.awt.Graphics;

public abstract class AbstractVisitor 
{
	public abstract void visit(GraphicalWall wall, Graphics g, int x, int y, int width, int height);
	public abstract void visit(GraphicalPlayer player, Graphics g, int x, int y, int width, int height);
	public abstract void visit(GraphicalTrigger trigger, Graphics g, int x, int y, int width, int height);
}
