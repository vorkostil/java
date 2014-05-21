package game.tron.model;

import game.TronGame;
import graphic.GraphicalItem;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TronPlayerModel 
{
	private static final String UP_STATE = "UP";
	private static final String DOWN_STATE = "DOWN";
	private static final String LEFT_STATE = "LEFT";
	private static final String RIGHT_STATE = "RIGHT";
	
	private double x = -1;
	private double y = -1;
	private List< Point2D > path = null;
	private int direction = TronGame.NO_DIRECTION;
	private String state = GraphicalItem.DEFAULT_STATE;
	private String name = null;
	
	public TronPlayerModel( String name, 
							int blueStartX, 
							int blueStartY,
							int blueStartDirection ) 
	{
		this.name = name;
		x = blueStartX;
		y = blueStartY;
		direction = blueStartDirection;
		path = new ArrayList< Point2D >();
		computeState();
	}

	private void computeState() 
	{
		if ( direction == TronGame.UP )
		{
			state = UP_STATE;
		}
		else if ( direction == TronGame.DOWN )
		{
			state = DOWN_STATE;
		}
		else if ( direction == TronGame.LEFT )
		{
			state = LEFT_STATE;
		}
		else if ( direction == TronGame.RIGHT )
		{
			state = RIGHT_STATE;
		}
		else
		{
			state = GraphicalItem.DEFAULT_STATE;
		}
	}

	public double getX() 
	{
		return x;
	}
	
	public double getY() 
	{
		return y;
	}
	
	public int getGridX()
	{
		return (int) Math.floor( x );
	}
	
	public int getGridY()
	{
		return (int) Math.floor( y );
	}
	
	public List< Point2D > getPath()
	{
		return path;
	}
	
	public void changePosition( double x, 
							    double y ) 
	{
		this.x = x;
		this.y = y;
	}

	public void changePath( List< Point2D > path ) 
	{
		this.path = path;
	}

	public void changeDirection( int newDirection )
	{
		this.direction = newDirection;
		computeState();
	}
	
	public String getState()
	{
		return state;
	}
	
	public String getName()
	{
		return name;
	}

	public boolean isCurrentlyVertical() 
	{
		return (  ( direction == TronGame.UP )
				||( direction == TronGame.DOWN )  );
	}

}
