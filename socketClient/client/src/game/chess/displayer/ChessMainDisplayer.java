package game.chess.displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;

import displayer.AbstractDisplayer;


public class ChessMainDisplayer extends AbstractDisplayer {

	public static final String NAME = "ChessMainDisplayer";

	private int factor = 64;
	
	private int deltaX;
	private int deltaY;
	
	public ChessMainDisplayer(int deltaX, int deltaY) 
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}


	@Override
	public synchronized void render( Graphics g ) 
	{
		for ( GraphicalItem item : getDisplayableItems() )
		{
			Image image = item.getImage(); 
			if (image != null) 
			{
				g.drawImage( image, item.getX() * factor + deltaX, 448 - (item.getY() * factor + deltaY), null );
			}
		}
	}
}
