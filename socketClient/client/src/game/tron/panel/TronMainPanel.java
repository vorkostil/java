package game.tron.panel;

import game.TronGame;
import game.tron.item.TronStartText;
import graphic.GraphicalEnvironment;
import helper.DataRepository;

import java.awt.MediaTracker;
import java.io.IOException;

import visitor.DefaultGraphicalDisplayer;

@SuppressWarnings("serial")
public class TronMainPanel extends GraphicalEnvironment 
{
	private static final String GRAPHIC_CONFIGURATION = "graphical_configuration";
	private static final String START_TEXT_CONFIGURATION = "start_configuration";
	
	public static int gridPadding = 32;
	private static int gridWidth = (TronGame.gridSize - 1) * gridPadding; // we move on the grid, not on the cell
	public static int framePadding = 7;
	
	public static int frameSize = gridWidth + framePadding * 2;

	private TronStartText startText = null;
	
	public TronMainPanel( DataRepository repository, MediaTracker tracker, int tempo ) throws IOException
	{
		super( repository.getData( GRAPHIC_CONFIGURATION ), tracker,  tempo );
		
		startText = new TronStartText( new DefaultGraphicalDisplayer(), 
									   repository.getData( START_TEXT_CONFIGURATION ), 
									   tracker, 
									   ImageLevel.ENVIRONMENT_IMAGE.index() );
		
		addItem(startText, LAST_LAYER_LEVEL_TO_DRAW );
	}
	
	public void setStartSoon() 
	{
		startText.setVisibility( true );
	}

	public void setStart() 
	{
		startText.setVisibility( false );
	}
}
