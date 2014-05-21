package visitor;

import graphic.GraphicalItem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;

public class DefaultGraphicalDisplayer extends AbstractDisplayer 
{
	private Polygon copyAndTranslate_(Polygon model, int x, int y) 
	{
		Polygon result = new Polygon( model.xpoints, model.ypoints, model.npoints );
		result.translate(-x, -y);
		return result;
	}

	@Override
	public void draw(GraphicalItem item, Graphics g, int x, int y, int width,int height) 
	{
		if ( item.isPolygonPartInScreen( new Rectangle( x, y, width, height ) ) ) 
		{
			if ( item.isVisible() == true )
			{
				// draw image or place holder
				Image image = item.getImage(); 
				if (image != null) 
				{
					g.drawImage( image, item.getX() - x, item.getY() - y, null );
				}
				else 
				{
					Color oldColor = g.getColor();
					g.setColor( item.getColor() );
					g.fillPolygon( copyAndTranslate_( item.getPolygonModel(), x, y ) );
					g.setColor( oldColor );
				}
			}
			
			// draw text if needed even if the item is not visible
			String str = item.getText();
			if ( str.length() > 0 ) 
			{
				Color oldColor = g.getColor();
				g.setColor( Color.BLACK );
				g.drawString( str, item.getX() - x, item.getY() - y + 36 );
				g.setColor( oldColor );
			}
		}
	}
}
