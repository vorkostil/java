package graphic;

import graphic.listener.MainViewMouseListener;
import graphic.listener.MainViewMouseMotionListener;
import helper.DataRepository.DataInformation;

import java.awt.MediaTracker;
import java.util.ArrayList;
import java.util.List;

import displayer.AbstractDisplayer;

@SuppressWarnings("serial")
public class GraphicalListeneredEnvironment extends GraphicalEnvironment {

	private GraphicalItem currentItemBelowMouse = null;
	private GraphicalItem pressedItem = null;
	private boolean leftButtonIsUp = true;
	private boolean rightButtonIsUp = true;
	
	public GraphicalListeneredEnvironment( DataInformation datas,
										   MediaTracker tracker, 
										   int tempo ) 
	{
		super(datas, tracker, tempo);
		
		this.addMouseMotionListener( new MainViewMouseMotionListener( this ) );
		this.addMouseListener( new MainViewMouseListener( this ) );
	}

	public GraphicalItem getItemAt(int x, int y) 
	{
		// find the displayer
		AbstractDisplayer displayer = getDisplayerAt(x,y);
		if ( displayer != null )
		{
			// find the items 
			List< GraphicalItem > items = new ArrayList< GraphicalItem >();
			for ( GraphicalItem item : displayer.getDisplayableItems() )
			{
				// store them from back to front
				if ( item.contains(x,y) == true )
				{
					items.add(0,item);
				}
			}
			
			// return the first one, it means the one on the upper layer
			if ( items.size() > 0 )
			{
				return items.get( 0 );
			}
		}
		return null;
	}

	private AbstractDisplayer getDisplayerAt(int x, int y) 
	{
		for ( AbstractDisplayer displayer : displayers.values() )
		{
			if ( displayer.contains(x,y) == true )
			{
				return displayer;
			}
		}
		return null;
	}
	
	public GraphicalItem getCurrentItemBelowMouse() 
	{
		return currentItemBelowMouse;
	}

	public void setCurrentItemBelowMouse(GraphicalItem currentItemBelowMouse) 
	{
		this.currentItemBelowMouse = currentItemBelowMouse;
	}

	public boolean isLeftButtonIsUp() 
	{
		return leftButtonIsUp;
	}

	public void setLeftButtonIsUp(boolean leftButtonIsUp) 
	{
		this.leftButtonIsUp = leftButtonIsUp;
	}

	public boolean isRightButtonIsUp() 
	{
		return rightButtonIsUp;
	}

	public void setRightButtonIsUp(boolean rightButtonIsUp) 
	{
		this.rightButtonIsUp = rightButtonIsUp;
	}

	public GraphicalItem getPressedItem() 
	{
		return pressedItem;
	}

	public void setPressedItem(GraphicalItem pressedItem) 
	{
		this.pressedItem = pressedItem;
	}
}
