package game.tron.displayer;

import game.tron.item.TronPlayerItem;
import game.tron.model.TronPlayerModel;
import game.tron.panel.TronMiniMapPanel;
import graphic.GraphicalItem;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.List;

import visitor.AbstractDisplayer;

public class TronMiniMapDisplayer extends AbstractDisplayer {

	@Override
	public void draw(GraphicalItem item, Graphics g, int x, int y, int width, int height) 
	{
		drawPath( (TronPlayerItem) item, g);
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
				g.drawLine( (int) (path.get( i ).getX() * TronMiniMapPanel.gridPadding + 1 ), 
							(int) (path.get( i ).getY() * TronMiniMapPanel.gridPadding + 1 ), 
							(int) (path.get( i + 1 ).getX() * TronMiniMapPanel.gridPadding + 1 ), 
							(int) (path.get( i + 1 ).getY() * TronMiniMapPanel.gridPadding + 1 ) );
			}
			
			g.drawLine( (int) (path.get( path.size() - 1 ).getX() * TronMiniMapPanel.gridPadding + 1 ), 
						(int) (path.get( path.size() - 1 ).getY() * TronMiniMapPanel.gridPadding + 1 ), 
						player.getModel().getGridX() * TronMiniMapPanel.gridPadding + 1, 
						player.getModel().getGridY() * TronMiniMapPanel.gridPadding + 1 );
		}
	}

}
