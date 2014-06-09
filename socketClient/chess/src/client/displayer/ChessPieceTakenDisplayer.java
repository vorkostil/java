package client.displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;

import displayer.AbstractDisplayer;


public class ChessPieceTakenDisplayer extends AbstractDisplayer 
{
	public static final String NAME = "ChessPieceTakenDisplayer";
	private int deltaX;
	private int deltaY;
	
	private int factor = 32;
	
	public ChessPieceTakenDisplayer(int deltaX, int deltaY) 
	{
		super();
		
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}


	@Override
	public synchronized void render( Graphics g ) 
	{
		int x = 0;
		int y = 0;
		for ( GraphicalItem item : getDisplayableItems() )
		{
			Image image = item.getImage(); 
			if (image != null) 
			{
				g.drawImage( image, ( x * factor ) + deltaX + 16, ( y * factor ) + deltaY, null );
				if ( y < 7 )
				{
					y++;
				}
				else
				{
					y = 0;
					x++;
				}
			}
		}
	}

	@Override
	public boolean contains(int x, int y) 
	{
		// has this displayer will never trigger mouse action
		return true;
	}

}
