package client.model;

public class ChessPieceModel 
{
	private static final String DEFAULT = "DEFAULT";
	private static final String TAKEN = "TAKEN";
	
	private boolean taken = false;
	private int x;
	private int y;
	
	public ChessPieceModel( int x, int y )
	{
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
		if ( taken == false )
		{
			return DEFAULT;
		}
		return TAKEN;
	}

	public void update(int x, int y, boolean alive) 
	{
		this.x = x;
		this.y = y;
		this.taken = !alive;
	}
	
}
