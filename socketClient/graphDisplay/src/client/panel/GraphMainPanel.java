package client.panel;

import graphic.GraphicalListeneredEnvironment;
import helper.DataRepository;

import java.awt.MediaTracker;

@SuppressWarnings("serial")
public class GraphMainPanel extends GraphicalListeneredEnvironment 
{
	private static final String GRAPHIC_CONFIGURATION = "graphical_configuration";

	public GraphMainPanel(DataRepository repository, MediaTracker tracker, int tempo) 
	{
		super(repository.getData( GRAPHIC_CONFIGURATION ), tracker, tempo);
	}

}
