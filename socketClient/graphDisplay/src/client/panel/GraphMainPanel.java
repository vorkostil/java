package client.panel;

import graphic.GraphicalEnvironment;
import helper.DataRepository;

import java.awt.MediaTracker;

@SuppressWarnings("serial")
public class GraphMainPanel extends GraphicalEnvironment 
{

	private static final String GRAPHIC_CONFIGURATION = "graphical_configuration";

	public GraphMainPanel(DataRepository repository, MediaTracker tracker, int tempo) 
	{
		super(repository.getData( GRAPHIC_CONFIGURATION ), tracker, tempo);
	}

}
