package game.chess.displayer;

import java.awt.Color;
import java.awt.Graphics;

import displayer.AbstractDisplayer;


public class ChessPlayDisplayer extends AbstractDisplayer {

	public static final String NAME = "ChessPlayDisplayer";
	
	private int deltaX;
	private int deltaY;
	private boolean isWhiteToPlay = true;
	private String blackPlayerName = null;
	private String whitePlayerName = null;
	
	public ChessPlayDisplayer(int deltaX, int deltaY) 
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}


	@Override
	public synchronized void render(Graphics g) 
	{
		g.setColor( Color.BLACK );
		if ( isWhiteToPlay == true )
		{
			if ( whitePlayerName != null )
			{
				g.drawString(whitePlayerName, deltaX + 16, deltaY + 32 );
			}
		}
		else
		{
			if ( blackPlayerName != null )
			{
				g.drawString(blackPlayerName, deltaX + 16, deltaY + 32 );
			}
		}
	}


	public void isWhiteToPlay(boolean isWhiteToPlay) 
	{
		this.isWhiteToPlay  = isWhiteToPlay;
	}


	public void setBlackPlayer(String blackPlayerName) 
	{
		this.blackPlayerName = blackPlayerName;
	}


	public void setWhitePlayer(String whitePlayerName) 
	{
		this.whitePlayerName = whitePlayerName;
	}

	@Override
	public boolean contains(int x, int y) 
	{
		// has this displayer will never trigger mouse action
		return true;
	}

}
