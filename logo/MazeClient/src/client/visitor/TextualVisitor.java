package client.visitor;

import java.awt.Graphics;

import client.object.GraphicalItem;
import client.object.GraphicalItemWithPicture;
import client.object.GraphicalPlayer;

public class TextualVisitor extends AbstractVisitor {

	@Override
	public void visit(GraphicalItem item, Graphics g, int x, int y, int width, int height) 
	{
	}

	@Override
	public void visit(GraphicalPlayer player, Graphics g, int x, int y, int width, int height) 
	{
	}

	public void visit(GraphicalItemWithPicture item, Graphics g, int x, int y, int width, int height) {
	}

}
