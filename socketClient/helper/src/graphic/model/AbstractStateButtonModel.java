package graphic.model;

import helper.DataRepository.DataInformation;

public abstract class AbstractStateButtonModel  extends AbstractButtonModel
{
	public static final String UP = "UP"; 
	public static final String DOWN = "DOWN"; 
	public static final String ON_UP = "ON_UP";
	public static final String ON_DOWN = "ON_DOWN";
	
	protected boolean isUp = true;
	
	public AbstractStateButtonModel(DataInformation data) 
	{
		super( data );
	}

	// behavior when mouse left the item zone
	@Override
	public void mouseLeftItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp )
	{
		if ( isUp == true )
		{
			changeState( AbstractButtonModel.UP );
		}
		else
		{
			changeState( AbstractButtonModel.DOWN );
		}
	}
	
	// behavior when mouse enter the item zone
	@Override
	public void mouseEnterItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		if ( isUp == true )
		{
			changeState( AbstractStateButtonModel.ON_UP );
		}
		else
		{
			changeState( AbstractStateButtonModel.ON_DOWN );
		}
	}
	
	// behavior when mouse pressed on the item zone
	@Override
	public void mousePressedItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
	}
	
	// behavior when mouse release on the item zone
	@Override
	public void mouseReleasedItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
	}
	
	// behavior on activation
	@Override
	public void activate( boolean leftButtonIsUp, boolean rightButtonIsUp )
	{
		// compute the new state
		if( isUp == true )
		{
			changeState( AbstractStateButtonModel.ON_DOWN );
		}
		else
		{
			changeState( AbstractStateButtonModel.ON_UP );
		}
		
		// if it's a left click and it is visible, do it
		if (  ( leftButtonIsUp == false )
			&&( isVisible() == true )  )
		{
			callAction();
		}
		
		// change the internal state
		isUp = !isUp;
	}
}
