package displayer;

import graphic.GraphicalItem;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDisplayer 
{
	// first, consider 5 levels of layer
	public final static int FIRST_LAYER_LEVEL_TO_DRAW = 0;
	public final static int LAST_LAYER_LEVEL_TO_DRAW = 4;
	
	// the list of gItem to display from First o Last layer
	List< List< GraphicalItem > > graphicalItemLayers = new ArrayList< List< GraphicalItem > >();
	List< GraphicalItem > displayableItems = new ArrayList< GraphicalItem >();
	
	protected AbstractDisplayer()
	{
		for ( int level = FIRST_LAYER_LEVEL_TO_DRAW;
				  level < LAST_LAYER_LEVEL_TO_DRAW + 1; 
				  level++ )
		{
			graphicalItemLayers.add( new ArrayList< GraphicalItem >() );			
		}
	}
	
	public synchronized boolean addItem( GraphicalItem item, int layerLevel )
	{
		if (  ( layerLevel >= FIRST_LAYER_LEVEL_TO_DRAW )
			&&( layerLevel <= LAST_LAYER_LEVEL_TO_DRAW )  )
		{
			graphicalItemLayers.get( layerLevel ).add( item );
			return true;
		}
		return false;
	}

	protected boolean isLayerNeedDrawing( int layerLevel )
	{
		return (  ( layerLevel >= FIRST_LAYER_LEVEL_TO_DRAW )
				&&( layerLevel <= LAST_LAYER_LEVEL_TO_DRAW )
				&&( graphicalItemLayers.get( layerLevel ).size() > 0 )  );
		
	}

	public synchronized void computeDisplayableItems()
	{
		displayableItems.clear();
		for ( int layerLevel = FIRST_LAYER_LEVEL_TO_DRAW; layerLevel <= LAST_LAYER_LEVEL_TO_DRAW; layerLevel++ )
		{
			if ( isLayerNeedDrawing(layerLevel) == true )
			{
				for ( GraphicalItem item : this.getLayer(layerLevel) )
				{
					if ( item.isVisible() == true )
					{
						displayableItems.add(item);
					}
				}
			}
		}
	}
	
	public synchronized List< GraphicalItem > getDisplayableItems()
	{
		return displayableItems;
	}
	
	// assume that the layer level is valid
	protected List< GraphicalItem > getLayer( int layerLevel )
	{
		return graphicalItemLayers.get( layerLevel );
	}
	
	public boolean removeItem(GraphicalItem item, int layerLevel) 
	{
		if (  ( layerLevel >= FIRST_LAYER_LEVEL_TO_DRAW )
			&&( layerLevel <= LAST_LAYER_LEVEL_TO_DRAW )  )
		{
			graphicalItemLayers.get( layerLevel ).remove( item );
			return true;
		}
		return false;
	}
	
	abstract public boolean contains(int x, int y);
	
	abstract public void render(Graphics g);
}
