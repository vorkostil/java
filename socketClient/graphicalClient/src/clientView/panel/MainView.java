package clientView.panel;

import graphic.GraphicalListeneredEnvironment;
import helper.DataRepository;

import java.awt.MediaTracker;

@SuppressWarnings("serial")
public class MainView extends GraphicalListeneredEnvironment {

	private static final String GRAPHIC_CONFIGURATION = "main_view_configuration";
	
	public MainView(DataRepository repository, MediaTracker tracker, int tempo) 
	{
		super(repository.getData( GRAPHIC_CONFIGURATION ), tracker, tempo);
	}
}
