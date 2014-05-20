package client.visitor;

import java.awt.Graphics;

import client.object.GraphicalPlayer;
import client.object.GraphicalTrigger;
import client.object.GraphicalWall;

public abstract class AbstractVisitor 
{
	public abstract void visit(GraphicalWall wall, Graphics g, int x, int y, int width, int height);
	public abstract void visit(GraphicalPlayer player, Graphics g, int x, int y, int width, int height);
	public abstract void visit(GraphicalTrigger trigger, Graphics g, int x, int y, int width, int height);
}
