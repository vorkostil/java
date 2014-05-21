package visitor;

import java.awt.Graphics;

import graphic.GraphicalItem;

public abstract class AbstractDisplayer 
{
	public abstract void draw( GraphicalItem object, Graphics g, int x, int y, int width, int height );
}
