package frame.tron;

import game.TronGame;
import graphic.GraphicalEnvironment;
import helper.DataRepository;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import visitor.AbstractVisitor;
import visitor.GraphicalVisitor;

@SuppressWarnings("serial")
public class TronGraphicalPanel extends GraphicalEnvironment 
{
	private static final String GRAPHIC_CONFIGURATION = "graphical_configuration";
	private static final String BLUE_PLAYER_CONFIGURATION = "blue_player_configuration";
	private static final String RED_PLAYER_CONFIGURATION = "red_player_configuration";
	
	public static int gridPadding = 32;
	private static int gridWidth = (TronGame.gridSize - 1) * gridPadding; // we move on the grid, not on the cell
	public static int framePadding = 7;
	
	public static int frameSize = gridWidth + framePadding * 2;

	private TronPlayer bluePlayer = null;
	private TronPlayer redPlayer = null;
	
	public TronGraphicalPanel( DataRepository repository ) throws IOException
	{
		super( repository.getData( GRAPHIC_CONFIGURATION ) );
		
		List< AbstractVisitor > visitors = new ArrayList< AbstractVisitor >();
		visitors.add( new GraphicalVisitor() );
		bluePlayer = new TronPlayer( visitors, repository.getData( BLUE_PLAYER_CONFIGURATION ), getTracker(), ImageLevel.ENVIRONMENT_IMAGE.index(), 3, 2 );
		redPlayer = new TronPlayer( visitors, repository.getData( RED_PLAYER_CONFIGURATION ), getTracker(), ImageLevel.ENVIRONMENT_IMAGE.index(), 12, 13 );
		
		addItem( bluePlayer, FIRST_LAYER_LEVEL_TO_DRAW );
		addItem( redPlayer, FIRST_LAYER_LEVEL_TO_DRAW );
	}
	
	@Override
	protected void drawForeground(Graphics g) 
	{
		// draw the paths
		redPlayer.drawPath( g );
		bluePlayer.drawPath( g );
	}

	public void changeBluePlayerCoordinate( double x, double y )
	{
		bluePlayer.changePosition( Math.min( TronGame.gridSize - 1, Math.max( 0.0, x ) ), 
								   Math.min( TronGame.gridSize - 1, Math.max( 0.0, y ) ) );
	}

	public void changeRedPlayerCoordinate(double x, double y) 
	{
		redPlayer.changePosition( Math.min( TronGame.gridSize - 1, Math.max( 0.0, x ) ), 
				   				  Math.min( TronGame.gridSize - 1, Math.max( 0.0, y ) ) );
	}

	public void updateBluePlayerPath(List<Point2D> bp) 
	{
		bluePlayer.changePath( bp );
	}

	public void updateRedPlayerPath(List<Point2D> rp) 
	{
		redPlayer.changePath( rp );
	}
}
