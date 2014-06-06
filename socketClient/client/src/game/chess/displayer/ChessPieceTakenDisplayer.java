package game.chess.displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;

import visitor.AbstractDisplayer;

public class ChessPieceTakenDisplayer extends AbstractDisplayer 
{
	private int deltaX;
	private int deltaY;
	
	private int factor = 32;
	
	public ChessPieceTakenDisplayer(int deltaX, int deltaY) 
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}


	@Override
	public void draw(GraphicalItem item, Graphics g, int x, int y, int width, int height) 
	{
		// draw image or place holder
		Image image = item.getImage(); 
		if (image != null) 
		{
			g.drawImage( image, factor + deltaX - x, factor + deltaY - y, null );
		}
	}

}
