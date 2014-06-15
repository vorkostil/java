package client.item;

import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Rectangle;

import client.model.GraphCellModel;

public class GraphCellItem extends GraphicalItem 
{
	GraphCellModel model;
	private Rectangle boundingBox;
	
	public GraphCellItem( GraphCellModel model, 
					 	  DataInformation dataInformation, 
					 	  MediaTracker mediaTracker,
					 	  int levelId ) 
	{
		super( dataInformation, mediaTracker, levelId );
		this.model = model;
		this.boundingBox = new Rectangle( model.getX() * 16, 
										  model.getY() * 16,
										  16,
										  16 );
	}

	@Override
	public int getX() 
	{
		return model.getX() * 16;
	}

	@Override
	public int getY() 
	{
		return model.getY() * 16;
	}

	@Override
	public boolean isVisible() 
	{
		return true;
	}

	@Override
	public Rectangle getBoundingBox() 
	{
		return boundingBox;
	}

	/* return the current state of the object default is DEFAULT_STATE
	 * this state will be mainly used to retrieve the image to display
	 * */
	@Override
	public String getState()
	{
		return model.getState();
	}
	
	// behavior when activation of the item is asked
	@Override
	public void activate(boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		model.requestToChangeState();
	}

	public GraphCellModel getModel() 
	{
		return model;
	}
}
