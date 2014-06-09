package clientView.displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;

import displayer.AbstractDisplayer;

public class MainViewDisplayer extends AbstractDisplayer {

	public static final String NAME = "MainViewDisplayer";

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

	@Override
	public boolean contains(int x, int y) 
	{
		// has this displayer is the full screen
		return true;
	}

}
