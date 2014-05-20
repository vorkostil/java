package client.visitor;

import java.awt.Graphics;

import client.object.GraphicalPlayer;
import client.object.GraphicalTrigger;
import client.object.GraphicalWall;

public class TextualVisitor extends AbstractVisitor {

	@Override
	public void visit(GraphicalWall wall, Graphics g, int x, int y, int width, int height) 
	{
	}

	@Override
	public void visit(GraphicalPlayer player, Graphics g, int x, int y, int width, int height) 
	{
	}

	@Override
	public void visit(GraphicalTrigger trigger, Graphics g, int x, int y, int width, int height) {
	}

}
