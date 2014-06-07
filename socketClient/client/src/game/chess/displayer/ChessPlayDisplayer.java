package game.chess.displayer;

import graphic.GraphicalItem;

import java.awt.Color;
import java.awt.Graphics;

import visitor.AbstractDisplayer;

public class ChessPlayDisplayer extends AbstractDisplayer {

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
	public void draw(GraphicalItem item, Graphics g, int x, int y, int width, int height) 
	{
//		// draw image or place holder
//		Image image = item.getImage(); 
//		if (image != null) 
//		{
//			g.drawImage( image, item.getX() - x, item.getY() - y, null );
//		}

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

}