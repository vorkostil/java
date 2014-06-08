package client.displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.List;

import client.item.TronPlayerItem;
import client.model.TronPlayerModel;

import displayer.AbstractDisplayer;



public class TronMiniMapDisplayer extends AbstractDisplayer {

	public static final String NAME = "TronMiniMapDisplayer";

	private static final int gridPadding = 4;
	
	private int deltaX;
	private int deltaY;
	
	public TronMiniMapDisplayer(int deltaX, int deltaY) 
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	@Override
	public void render( Graphics g ) 
	{
		for ( GraphicalItem item : getDisplayableItems() )
		{
			if ( item instanceof TronPlayerItem )
			{
				drawPath( (TronPlayerItem) item, g);
			}
		}
	}
	
	private void drawPath( TronPlayerItem player, Graphics g) 
	{
		TronPlayerModel model = player.getModel();
		if (  ( model.getPath() != null )
			&&( model.getPath().size() > 0 )  )
		{
			List< Point2D > path = model.getPath();
			g.setColor( player.getColor() );
			for ( int i = 0; i < path.size() - 1; i++ )
			{
				g.drawLine( (int) (path.get( i ).getX() * gridPadding + 1 + deltaX ), 
							(int) (path.get( i ).getY() * gridPadding + 1 + deltaY ), 
							(int) (path.get( i + 1 ).getX() * gridPadding + 1 + deltaX ), 
							(int) (path.get( i + 1 ).getY() * gridPadding + 1 + deltaY ) );
			}
			
			g.drawLine( (int) (path.get( path.size() - 1 ).getX() * gridPadding + 1 + deltaX ), 
						(int) (path.get( path.size() - 1 ).getY() * gridPadding + 1 + deltaY ), 
						player.getModel().getGridX() * gridPadding + 1 + deltaX, 
						player.getModel().getGridY() * gridPadding + 1 + deltaY );
		}
	}

	@Override
	public boolean contains(int x, int y) 
	{
		// right now, no click should happen on this displayer
		return false;
	}
}
