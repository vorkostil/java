package client.item;

import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.io.IOException;

public class TronStartText extends GraphicalItem 
{
	private boolean isVisible = false;
	
	public TronStartText( DataInformation dataInformation, 
						  MediaTracker mediaTracker,
						  int levelId ) throws IOException 
	{
		super(dataInformation, mediaTracker, levelId);
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
	public Rectangle getBoundingBox() 
	{
		return emptyBoundingBox;
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
