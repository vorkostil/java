package game.tron.displayer;

import game.tron.item.TronPlayerItem;
import graphic.GraphicalItem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import displayer.AbstractDisplayer;


public class TronPlayerPanelDisplayer extends AbstractDisplayer {

	private static final int FONT_SIZE = 10;
	public static final String NAME = "TronPlayerPanelDisplayer";
	
	private int deltaX;
	private int deltaY;
	
	public TronPlayerPanelDisplayer(int deltaX, int deltaY) 
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	@Override
	public void render( Graphics g ) 
	{
		for ( GraphicalItem item : getDisplayableItems() )
		{
			// draw image or place holder
			Image image = item.getImage(); 
			if (image != null) 
			{
				g.drawImage( image, 18 + deltaX, 24 + deltaY, null );
			}

			if ( item instanceof TronPlayerItem )
			{
				TronPlayerItem player = (TronPlayerItem) item;
				
				Color oldColor = g.getColor();
				g.setColor( item.getColor() );

				g.drawString( player.getModel().getName(), 48 + deltaX, 26 + FONT_SIZE + deltaY );
				g.drawString( String.valueOf( player.getModel().getGridX() ), 190 + deltaX, 16 + FONT_SIZE + deltaY );
				g.drawString( String.valueOf( player.getModel().getGridY() ), 190 + deltaX, 40 + FONT_SIZE + deltaY );
				
				
				g.setColor( oldColor );
			}
		}
	}
}
