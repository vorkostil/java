package game.chess.displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;

import visitor.AbstractDisplayer;

public class ChessMainDisplayer extends AbstractDisplayer {

	@Override
	public void draw(GraphicalItem item, Graphics g, int x, int y, int width, int height) 
	{
		// draw image or place holder
		Image image = item.getImage(); 
		if (image != null) 
		{
			g.drawImage( image, item.getX() - x, item.getY() - y, null );
		}
	}

}
