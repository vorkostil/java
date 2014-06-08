package game.chess.item;

import game.chess.model.CellModel;
import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.io.IOException;

public class CellItem extends GraphicalItem 
{
	private CellModel model;
	
	public CellItem( CellModel model,
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
		return model.isVisible();
	}

	@Override
	public Rectangle getBoundingBox() 
	{
		return emptyBoundingBox;
	}
}
