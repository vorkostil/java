package client.model;

import client.GraphDisplayGameFrame;

public class GraphCellModel 
{
	private GraphDisplayGameFrame mainFrame;
	private int x;
	private int y;
	private String state = GraphDisplayGameFrame.EMPTY;
	
	public GraphCellModel( GraphDisplayGameFrame graphDisplayGameFrame,
				      	   int x,
				           int y ) 
	{
		this.mainFrame = graphDisplayGameFrame;
		this.x = x;
		this.y = y;
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

	public void requestToChangeState() 
	{
		mainFrame.requestChangeState( this );
	}

	public void setState(String newState) 
	{
		this.state = newState;
	}

}
