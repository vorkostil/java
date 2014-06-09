package client.item;

import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.io.IOException;

import client.model.ChessPieceModel;

public class ChessPieceItem extends GraphicalItem 
{
	private ChessPieceModel model;
	
	public ChessPieceItem( ChessPieceModel model,
						   DataInformation dataInformation, 
						   MediaTracker mediaTracker,
						   int levelId ) throws IOException 
	{
		super(dataInformation, mediaTracker, levelId);
		this.model = model;
	}

	@Override
	public int getX() 
	{
		return model.getX();
	}

	@Override
	public int getY() 
	{
		return model.getY();
	}

	@Override
	public boolean isVisible() 
	{
		return true;
	}

	@Override
	public Rectangle getBoundingBox() 
	{
		return emptyBoundingBox;
	}

	/* return the current state of the object default is DEFAULT_STATE
	 * this state will be mainly used to retrieve the image to display
	 * */
	@Override
	public String getState()
	{
		return model.getState();
	}

	public ChessPieceModel getModel() 
	{
		return model;
	}
}
