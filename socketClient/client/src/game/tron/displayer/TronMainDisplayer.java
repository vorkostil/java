package game.tron.displayer;

import game.tron.item.TronPlayerItem;
import game.tron.model.TronPlayerModel;
import game.tron.panel.TronMainPanel;
import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.List;

import visitor.AbstractDisplayer;

public class TronMainDisplayer extends AbstractDisplayer {

	@Override
	public void draw(GraphicalItem item, Graphics g, int x, int y, int width,int height) 
	{
		if ( item.isVisible() == true )
		{
			// draw image or place holder
			Image image = item.getImage(); 
			if (image != null) 
			{
				g.drawImage( image, item.getX() - x, item.getY() - y, null );
			}
			
			drawPath( (TronPlayerItem) item, g);
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
						g.drawLine( (int) (path.get( i ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + d, 
									(int) (path.get( i ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding), 
									(int) (path.get( i + 1 ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding + d), 
									(int) (path.get( i + 1 ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding) );
					}
				}
				else
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (path.get( i ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding), 
									(int) (path.get( i ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + d, 
									(int) (path.get( i + 1 ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding), 
									(int) (path.get( i + 1 ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding + d) );
					}
				}
			}
			
			if ( model.isCurrentlyVertical() == true )
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (path.get( path.size() - 1 ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + d, 
								(int) (path.get( path.size() - 1 ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding), 
								player.getX() + player.getDeltaImage() + d, 
								player.getY() + player.getDeltaImage() );
				}
			}
			else
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (path.get( path.size() - 1 ).getX() * TronMainPanel.gridPadding + TronMainPanel.framePadding), 
								(int) (path.get( path.size() - 1 ).getY() * TronMainPanel.gridPadding + TronMainPanel.framePadding) + d, 
								player.getX() + player.getDeltaImage(), 
								player.getY() + player.getDeltaImage() + d );
				}
			}
		}
	}
}
