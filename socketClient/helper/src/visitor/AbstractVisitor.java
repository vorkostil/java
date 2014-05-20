package visitor;

import java.awt.Graphics;

import graphic.GraphicalItem;

public abstract class AbstractVisitor 
{
	public abstract void visit(GraphicalItem object, Graphics g, int x, int y, int width, int height);
}
