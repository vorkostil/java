package game.tron.panel;

import graphic.GraphicalEnvironment;
import helper.DataRepository;

import java.awt.Dimension;
import java.awt.MediaTracker;

@SuppressWarnings("serial")
public class TronMiniMapPanel extends GraphicalEnvironment 
{
	public static final int frameSize = 64;
	public static final int gridPadding = 4;

	private static final String MINIMAP_GRAPHIC_CONFIGURATION = "minimap_graphic_configuration";
	private static final Dimension FRAME_DIMENSION = new Dimension( frameSize, frameSize );

	public TronMiniMapPanel( DataRepository repository,  
							 MediaTracker tracker,
							 int tempo) 
	{
		super(repository.getData( MINIMAP_GRAPHIC_CONFIGURATION ), tracker, tempo);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return FRAME_DIMENSION;
	}
}
