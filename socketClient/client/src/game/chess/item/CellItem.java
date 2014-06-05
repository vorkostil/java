package game.chess.item;

import game.chess.model.CellModel;
import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;

import visitor.AbstractDisplayer;

public class CellItem extends GraphicalItem 
{
	private CellModel model;
	
	public CellItem( CellModel model,
					 AbstractDisplayer displayer,
					 DataInformation dataInformation, 
					 MediaTracker mediaTracker,
					 int levelId ) throws IOException 
	{
		super(displayer, dataInformation, mediaTracker, levelId);
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
	public Polygon getPolygonModel() 
	{
		return emptyPolygon;
	}


	@Override
	public boolean isPolygonPartInScreen(Rectangle rect)
	{
		return true;
	}
}
