package client.displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;

import displayer.AbstractDisplayer;

public class GraphMainDisplayer extends AbstractDisplayer 
{
	public static final String NAME = "GraphMainDisplayer";

	@Override
	public boolean contains(int x, int y) 
	{
		// as this displayer is the only one and is on all the frame ...
		return true;
	}

	@Override
	public void render(Graphics g) 
	{
		for ( GraphicalItem item : getDisplayableItems() )
		{
			Image image = item.getImage(); 
			if (image != null) 
			{
				g.drawImage( image, item.getX(), item.getY(), null );
			}
		}
	}

}
