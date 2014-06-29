package graphic.model;

import helper.DataRepository.DataInformation;

public abstract class AbstractButtonModel extends VisibilityModel
{
	public static final String UP = "UP"; 
	public static final String DOWN = "DOWN"; 
	public static final String ON = "ON";
	
	private int x;
	private int y;
	private String state = UP;
	
	public AbstractButtonModel(DataInformation data) 
	{
		super( data.getBooleanValue( "visible" ) );
		
		x = data.getIntegerValue( "x" );
		y = data.getIntegerValue( "y" );
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public String getState()
	{
		return state;
	}

	// behavior when mouse left the item zone
	public void mouseLeftItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp )
	{
		changeState( AbstractButtonModel.UP );
	}
	
	// behavior when mouse enter the item zone
	public void mouseEnterItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		if ( leftButtonIsUp == true )
		{
			changeState( AbstractButtonModel.ON );
		}
		else
		{
			changeState( AbstractButtonModel.DOWN );
		}
	}
	
	// behavior when mouse pressed on the item zone
	public void mousePressedItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		if ( leftButtonIsUp == false )
		{
			changeState( AbstractButtonModel.DOWN );
		}
	}
	
	// behavior when mouse release on the item zone
	public void mouseReleasedItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
		changeState( AbstractButtonModel.ON );
	}
	
	// behavior on activation
	public void activate( boolean leftButtonIsUp, boolean rightButtonIsUp )
	{
		changeState( AbstractButtonModel.UP );
		
		// if it's a left click and it is visible, do it
		if (  ( leftButtonIsUp == false )
			&&( isVisible() == true )  )
		{
			callAction();
		}
	}
	
	protected void changeState( String newState )
	{
		// by default, just change the state
		state = newState;
	}
	
	// action call when the mouseReleased is call on the button
	// call after the changeState occurs
	public abstract void callAction();
}
