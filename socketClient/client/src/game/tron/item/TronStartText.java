package game.tron.item;

import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;

import visitor.AbstractDisplayer;

public class TronStartText extends GraphicalItem 
{
	private boolean isVisible = false;
	
	public TronStartText( AbstractDisplayer displayer,
						  DataInformation dataInformation, 
						  MediaTracker mediaTracker,
						  int levelId ) throws IOException 
	{
		super(displayer, dataInformation, mediaTracker, levelId);
	}

	@Override
	public int getX() 
	{
		return 110;
	}

	@Override
	public int getY() 
	{
		return 64;
	}

	@Override
	public boolean isVisible() 
	{
		return this.isVisible;
	}

	@Override
	public Polygon getPolygonModel() 
	{
		return emptyPolygon;
	}

	@Override
	public boolean isPolygonPartInScreen(Rectangle rect)
	{
		// always need to draw it
		return true;
	}
	
	public void setVisibility( boolean visibleState )
	{
		if ( visibleState == true )
		{
			resetProcessTime();
		}
		
		this.isVisible = visibleState;
	}
}
