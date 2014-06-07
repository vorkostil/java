package game.tron.displayer;

import game.tron.item.TronPlayerItem;
import game.tron.model.TronPlayerModel;
import game.tron.panel.TronMainPanel;
import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.List;

import displayer.AbstractDisplayer;


public class TronMainDisplayer extends AbstractDisplayer {

	public final static String NAME = "TronMainDisplayer";
	
	private int deltaX;
	private int deltaY;
	
	public TronMainDisplayer(int deltaX, int deltaY) 
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	@Override
	public synchronized void render( Graphics g ) 
	{
		for ( GraphicalItem item : getDisplayableItems() )
		{
			// draw image or place holder
			Image image = item.getImage(); 
			if (image != null) 
			{
				g.drawImage( image, item.getX() + deltaX , item.getY() + deltaY, null );
			}

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
				boolean isVertical = path.get( i ).getX() == path.get( i + 1 ).getX();
				if ( isVertical )
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (path.get( i ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + d + deltaX, 
									(int) (path.get( i ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + deltaY, 
									(int) (path.get( i + 1 ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding + d + deltaX), 
									(int) (path.get( i + 1 ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding + deltaY) );
					}
				}
				else
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (path.get( i ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + deltaX, 
									(int) (path.get( i ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + d + deltaY, 
									(int) (path.get( i + 1 ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + deltaX, 
									(int) (path.get( i + 1 ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding + d + deltaY) );
					}
				}
			}
			
			if ( model.isCurrentlyVertical() == true )
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (path.get( path.size() - 1 ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + d + deltaX, 
								(int) (path.get( path.size() - 1 ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding + deltaY), 
								player.getX() + player.getDeltaImage() + d + deltaX, 
								player.getY() + player.getDeltaImage() + deltaY );
				}
			}
			else
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (path.get( path.size() - 1 ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + deltaX, 
								(int) (path.get( path.size() - 1 ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + d + deltaY, 
								player.getX() + player.getDeltaImage() + deltaX, 
								player.getY() + player.getDeltaImage() + d + deltaY );
				}
			}
		}
	}
}
