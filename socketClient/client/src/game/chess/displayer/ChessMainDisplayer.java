package game.chess.displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;

import visitor.AbstractDisplayer;

public class ChessMainDisplayer extends AbstractDisplayer {

	private int factor = 64;
	
	private int deltaX;
	private int deltaY;
	
	public ChessMainDisplayer(int deltaX, int deltaY) 
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}


	@Override
	public void draw(GraphicalItem item, Graphics g, int x, int y, int width, int height) 
	{
		if ( item.isVisible() == true )
		{
			// draw image or place holder
			Image image = item.getImage(); 
			if (image != null) 
			{
				g.drawImage( image, item.getX() * factor + deltaX - x , 448 - (item.getY() * factor + deltaY - y), null );
			}
		}
	}

}
