package game.tron.panel;

import java.awt.Dimension;
import java.awt.MediaTracker;

import graphic.GraphicalEnvironment;
import helper.DataRepository;

@SuppressWarnings("serial")
public class TronPlayerPanel extends GraphicalEnvironment 
{
	public static final int frameSizeWidth = 212;
	public static final int frameSizeHeight = 64;
	public static final int gridPadding = 4;

	private static final String PLAYER_GRAPHIC_CONFIGURATION = "player_graphic_configuration";
	private static final Dimension FRAME_DIMENSION = new Dimension( frameSizeWidth, frameSizeHeight );

	public TronPlayerPanel( DataRepository repository,  
							MediaTracker tracker,
							int tempo ) 
	{
		super(repository.getData( PLAYER_GRAPHIC_CONFIGURATION ), tracker, tempo);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return FRAME_DIMENSION;
	}
}
