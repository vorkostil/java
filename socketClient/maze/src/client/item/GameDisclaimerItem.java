package client.item;

import java.awt.MediaTracker;
import java.awt.Rectangle;


import graphic.GraphicalItem;
import graphic.model.VisibilityModel;
import helper.DataRepository.DataInformation;

public class GameDisclaimerItem extends GraphicalItem 
{
	VisibilityModel model;
	
	public GameDisclaimerItem( VisibilityModel model,
							   DataInformation dataInformation,
							   MediaTracker mediaTracker, 
							   int levelId ) 
	{
		super(dataInformation, mediaTracker, levelId);
		this.model = model;
	}

	@Override
	public int getX() 
	{
		return 0;
	}

	@Override
	public int getY() 
	{
		return 0;
	}

	@Override
	public boolean isVisible() 
	{
		return model.isVisible();
	}

	@Override
	public Rectangle getBoundingBox() 
	{
		return emptyBoundingBox;
	}

	public VisibilityModel getModel()
	{
		return model;
	}
}
