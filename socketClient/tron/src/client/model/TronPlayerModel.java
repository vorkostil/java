package client.model;

import graphic.GraphicalItem;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import client.TronGameClient;


public class TronPlayerModel 
{
	private static final String UP_STATE = "UP";
	private static final String DOWN_STATE = "DOWN";
	private static final String LEFT_STATE = "LEFT";
	private static final String RIGHT_STATE = "RIGHT";
	
	private TronGameClient root;
	private String name;
	private double x;
	private double y;
	private int direction;
	private List< Point2D > path = new ArrayList< Point2D >();
	private String state = GraphicalItem.DEFAULT_STATE;
	
	public TronPlayerModel( TronGameClient root,
							String name, 
							int startX, 
							int startY,
							int startDirection ) 
	{
		this.root = root;
		this.name = name;
		x = startX;
		y = startY;
		direction = startDirection;
		
		computeState();
	}

	private void computeState() 
	{
		if ( direction == root.upDirection )
		{
			state = UP_STATE;
		}
		else if ( direction == root.downDirection )
		{
			state = DOWN_STATE;
		}
		else if ( direction == root.leftDirection )
		{
			state = LEFT_STATE;
		}
		else if ( direction == root.rightDirection )
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
		return (  ( direction == root.upDirection )
				||( direction == root.downDirection )  );
	}

}
