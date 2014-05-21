package game.tron.displayer;

import game.tron.item.TronPlayerItem;
import graphic.GraphicalItem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import visitor.AbstractDisplayer;

public class TronPlayerPanelDisplayer extends AbstractDisplayer {

	private static final int FONT_SIZE = 10;

	@Override
	public void draw(GraphicalItem item, Graphics g, int x, int y,int width, int height) 
	{
		Image image = item.getImage(); 
		if (image != null) 
		{
			g.drawImage( image, 18, 24, null );
		}
		
		TronPlayerItem player = (TronPlayerItem) item;
		
		Color oldColor = g.getColor();
		g.setColor( item.getColor() );

		g.drawString( player.getModel().getName(), 48, 26 + FONT_SIZE );
		g.drawString( String.valueOf( player.getModel().getGridX() ), 190, 16 + FONT_SIZE );
		g.drawString( String.valueOf( player.getModel().getGridY() ), 190, 40 + FONT_SIZE );
		
		
		g.setColor( oldColor );
	}

}
