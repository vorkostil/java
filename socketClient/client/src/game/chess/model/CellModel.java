package game.chess.model;

public class CellModel 
{
	private boolean isVisible = false;
	private int x;
	private int y;
	
	public CellModel( int x, int y )
	{
		setCoord(x, y);
	}

	public int getX() 
	{
		return x;
	}

	public int getY() 
	{
		return y;
	}

	public void setVisible( boolean value ) 
	{
		isVisible = value;
	}

	public boolean isVisible() 
	{
		return isVisible;
	}

	public void setCoord(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
	
}
