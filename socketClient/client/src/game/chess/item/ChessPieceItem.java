package game.chess.item;

import game.chess.model.ChessPieceModel;
import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;

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
	public Polygon getPolygonModel() 
	{
		return emptyPolygon;
	}


	@Override
	public boolean isPolygonPartInScreen(Rectangle rect)
	{
		return true;
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
