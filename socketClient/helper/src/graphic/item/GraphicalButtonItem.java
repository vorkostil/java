package graphic.item;

import graphic.GraphicalItem;
import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.io.IOException;

public class GraphicalButtonItem extends GraphicalItem {

	private AbstractButtonModel model;
	private Rectangle boundingBox;
	
	public GraphicalButtonItem( AbstractButtonModel buttonModel, 
								DataInformation 	dataInformation,
								MediaTracker 		mediaTracker, 
								int 				levelId ) throws IOException 
	{
		super(dataInformation, mediaTracker, levelId);
		model = buttonModel;
		boundingBox = null;
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
		// if the AABB has not been initialized, do it
		// this kind of item did not move so create a static rectangle
		if ( boundingBox == null )
		{
			boundingBox = new Rectangle(getX(), getY(), getImageWidth(), getImageHeight() );
		}
		
		return boundingBox;
	}

	/* return the current state of the object default is DEFAULT_STATE
	 * this state will be mainly used to retrieve the image to display
	 * */
	public String getState()
	{
		return model.getState();
	}
	
	// behavior when mouse left the item zone
	public void mouseLeftItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		model.mouseLeftItem(x, y, leftButtonIsUp, rightButtonIsUp);
	}
	
	// behavior when mouse enter the item zone
	public void mouseEnterItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		model.mouseEnterItem(x, y, leftButtonIsUp, rightButtonIsUp);
	}
	
	// behavior when mouse pressed on the item zone
	public void mousePressedItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		model.mousePressedItem(x, y, leftButtonIsUp, rightButtonIsUp);
	}
	
	// behavior when mouse release on the item zone
	public void mouseReleasedItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		model.mouseReleasedItem(x, y, leftButtonIsUp, rightButtonIsUp);
	}
	
	// behavior when activation of the item is asked
	public void activate(boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		model.activate(leftButtonIsUp, rightButtonIsUp);
	}
	
	
}
