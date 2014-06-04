package game.chess.panel;

import graphic.GraphicalEnvironment;
import helper.DataRepository;

import java.awt.MediaTracker;
import java.io.IOException;

@SuppressWarnings("serial")
public class ChessMainPanel extends GraphicalEnvironment 
{
	private static final String GRAPHIC_CONFIGURATION = "graphical_configuration";
	public static final int frameWidth = 702;
	public static final int frameHeight = 512;
	
	public ChessMainPanel( DataRepository repository, MediaTracker tracker, int tempo ) throws IOException
	{
		super( repository.getData( GRAPHIC_CONFIGURATION ), tracker,  tempo );
	}
	
	public void setStartSoon() 
	{
	}

	public void setStart() 
	{
	}
}
