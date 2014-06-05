package game.chess.item;

import java.awt.MediaTracker;
import java.awt.Polygon;
import java.io.IOException;

import visitor.AbstractDisplayer;

import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

/*
 * fucking awful, just here to have a displayer active
 * TODO remove it and find a way to use a displayer without any item
 * TODO keep it but find something useful to do with it
 **/
public class PlayItem extends GraphicalItem 
{

	public PlayItem(AbstractDisplayer displayer,
					DataInformation dataInformation, 
					MediaTracker mediaTracker,
					int levelId) throws IOException 
	{
		super(displayer, dataInformation, mediaTracker, levelId);
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
		return true;
	}

	@Override
	public Polygon getPolygonModel() 
	{
		return emptyPolygon;
	}

}
