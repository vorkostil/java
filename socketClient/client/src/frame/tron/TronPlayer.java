package frame.tron;

import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

import visitor.AbstractVisitor;

public class TronPlayer extends GraphicalItem 
{
	private double x;
	private double y;
	private List<Point2D> path = null;

	private int deltaImage = 0;
	
	public TronPlayer( List<AbstractVisitor> visitors,
					   DataInformation dataInformation, 
					   MediaTracker mediaTracker,
					   int levelId,
					   double x,
					   double y ) throws IOException 
	{
		super( visitors, dataInformation, mediaTracker, levelId );
		changePosition( x, y );
		
		if ( getImage() != null )
		{
			deltaImage = getImage().getWidth( null ) / 2;
		}
	}

	@Override
	public int getX() 
	{
		return (int) (x * TronGraphicalPanel.gridPadding ) + TronGraphicalPanel.framePadding - deltaImage;
	}

	@Override
	public int getY() 
	{
		return (int) (y * TronGraphicalPanel.gridPadding ) + TronGraphicalPanel.framePadding - deltaImage;
	}

	@Override
	public boolean isVisible() 
	{
		return true;
	}

	@Override
	public Polygon getModel() 
	{
		return new Polygon();
	}

	@Override
	public boolean needDrawing(Rectangle rect)
	{
		// always need to draw it
		return true;
	}
	
	public double getRealX()
	{
		return x;
	}

	public double getRealY()
	{
		return y;
	}

	public void changePosition( double x, double y ) 
	{
		this.x = x;
		this.y = y;
	}

	public void changePath( List< Point2D > path ) 
	{
		this.path = path;
	}

	public void drawPath(Graphics g) 
	{
		if ( path != null )
		{
			g.setColor( getColor() );
			for ( int i = 0; i < path.size() - 1; i++ )
			{
				boolean isVertical = path.get( i ).getX() == path.get( i + 1 ).getX();
				if ( isVertical )
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (path.get( i ).getX() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding) + d, 
									(int) (path.get( i ).getY() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding), 
									(int) (path.get( i + 1 ).getX() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding + d), 
									(int) (path.get( i + 1 ).getY() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding) );
					}
				}
				else
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (path.get( i ).getX() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding), 
									(int) (path.get( i ).getY() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding) + d, 
									(int) (path.get( i + 1 ).getX() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding), 
									(int) (path.get( i + 1 ).getY() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding + d) );
					}
				}
			}
			
			boolean isVertical = path.get( path.size() - 1 ).getX() == getRealX();
			if ( isVertical )
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (path.get( path.size() - 1 ).getX() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding) + d, 
								(int) (path.get( path.size() - 1 ).getY() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding), 
								getX() + deltaImage + d, 
								getY() + deltaImage );
				}
			}
			else
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (path.get( path.size() - 1 ).getX() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding), 
								(int) (path.get( path.size() - 1 ).getY() * TronGraphicalPanel.gridPadding + TronGraphicalPanel.framePadding) + d, 
								getX() + deltaImage, 
								getY() + deltaImage + d );
				}
			}
		}
	}

	public List<Point2D> getPath() 
	{
		return path;
	}
}
