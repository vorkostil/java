package game.tron.item;

import game.tron.model.TronPlayerModel;
import game.tron.panel.TronMainPanel;
import graphic.GraphicalItem;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;

import visitor.AbstractDisplayer;

public class TronPlayerItem extends GraphicalItem 
{
	private TronPlayerModel model = null;
	private int deltaImage = 0;
	
	public TronPlayerItem( TronPlayerModel model,
					   	   AbstractDisplayer displayer,
					   	   DataInformation dataInformation, 
					   	   MediaTracker mediaTracker,
					   	   int levelId ) throws IOException 
	{
		super( displayer, dataInformation, mediaTracker, levelId );
		
		this.model = model;
		this.currentState_ = model.getState();
		
		if ( getImage() != null )
		{
			deltaImage = getImage().getWidth( null ) / 2;
		}
	}

	@Override
	public int getX() 
	{
		return (int) (model.getX() * TronMainPanel.gridPadding ) + TronMainPanel.framePadding - deltaImage;
	}

	@Override
	public int getY() 
	{
		return (int) (model.getY() * TronMainPanel.gridPadding ) + TronMainPanel.framePadding - deltaImage;
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
		// always need to draw it
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

	public TronPlayerModel getModel() 
	{
		return model;
	}

	public int getDeltaImage() 
	{
		return deltaImage;
	}
}
