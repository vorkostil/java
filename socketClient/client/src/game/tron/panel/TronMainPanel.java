package game.tron.panel;

import game.tron.displayer.TronMainDisplayer;
import game.tron.item.TronStartText;
import graphic.GraphicalEnvironment;
import helper.DataRepository;

import java.awt.MediaTracker;
import java.io.IOException;

@SuppressWarnings("serial")
public class TronMainPanel extends GraphicalEnvironment 
{
	private static final String GRAPHIC_CONFIGURATION = "graphical_configuration";
	private static final String START_TEXT_CONFIGURATION = "start_configuration";
	
	public static final int gridPadding = 32;
	public static final int framePadding = 7;
	
	public static final int frameWidth = 496;
	public static final int frameHeight = 560;

	private TronStartText startText = null;
	
	public TronMainPanel( DataRepository repository, MediaTracker tracker, int tempo ) throws IOException
	{
		super( repository.getData( GRAPHIC_CONFIGURATION ), tracker,  tempo );
	}
	
	public void createStartText( DataRepository repository ) throws IOException
	{
		startText = new TronStartText( repository.getData( START_TEXT_CONFIGURATION ), 
									   getTracker(), 
									   ImageLevel.ENVIRONMENT_IMAGE.index() );
		
		addItem(startText, TronMainDisplayer.NAME, LAST_LAYER_LEVEL_TO_DRAW );
	}
	
	public void setStartSoon() 
	{
		startText.setVisibility( true );
		computeDisplayableItems();
	}

	public void setStart() 
	{
		startText.setVisibility( false );
		computeDisplayableItems();
	}
}
