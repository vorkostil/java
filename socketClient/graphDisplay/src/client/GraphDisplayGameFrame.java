package client;

import game.AbstractGameClientFrame;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;
import graphic.item.GraphicalButtonItem;
import graphic.model.AbstractStateButtonModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.displayer.GraphMainDisplayer;
import client.model.SetBlockButtonModel;
import client.model.SetExitButtonModel;
import client.model.SetStartButtonModel;
import client.panel.GraphMainPanel;
import displayer.AbstractDisplayer;

@SuppressWarnings("serial")
public class GraphDisplayGameFrame extends AbstractGameClientFrame 
{
	// The constant
	private static final String CONFIG_PATH = "../graphDisplay/resources/config/graph_display.cfg";
	private static final String START_BUTTON_CONFIG = "start_button_configuration";
	private static final String EXIT_BUTTON_CONFIG = "exit_button_configuration";
	private static final String BLOCK_BUTTON_CONFIG = "block_button_configuration";

	// The member
	private GraphMainDisplayer mainDisplayer = new GraphMainDisplayer();
	private GraphMainPanel mainPanel;
	
	// the static element
	private GraphicalButtonItem setStartButton;
	private GraphicalButtonItem setExitButton;
	private GraphicalButtonItem setBlockButton;
	
	public GraphDisplayGameFrame() throws IOException 
	{
		super( CONFIG_PATH );
		
		// characteristics of the frame
		this.setTitle( "Graph Display" );
	
		// create the main panel
		mainDisplayer = new GraphMainDisplayer();
		mainPanel = new GraphMainPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		mainPanel.addDisplayer( GraphMainDisplayer.NAME, mainDisplayer );

		// add the panel to the view
		this.setContentPane( mainPanel );
		
		// change the size of the frame to match the display
		this.setSize( mainPanel.viewMaxWidth + 7, mainPanel.viewMaxHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		
		// add the element in the view
		addStaticElement();
		
		// initialize the state of the visible items
		mainPanel.computeDisplayableItems();

		// display itself
		this.setVisible( true );
	}

	private void addStaticElement() throws IOException 
	{
		// create a button group
		List< AbstractStateButtonModel > buttons = new ArrayList< AbstractStateButtonModel >();
		
		// create a start button
		setStartButton = new GraphicalButtonItem( new SetStartButtonModel( repository.getData( START_BUTTON_CONFIG ) ),
												  repository.getData( START_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setStartButton.getModel() );
		mainPanel.addItem( setStartButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );

		// create an exit button
		setExitButton = new GraphicalButtonItem( new SetExitButtonModel( repository.getData( EXIT_BUTTON_CONFIG ) ),
												 repository.getData( EXIT_BUTTON_CONFIG ), 
												 tracker, 
												 ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setExitButton.getModel() );
		mainPanel.addItem( setExitButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
	
		// create a block button
		setBlockButton = new GraphicalButtonItem( new SetBlockButtonModel( repository.getData( BLOCK_BUTTON_CONFIG ) ),
												  repository.getData( BLOCK_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setBlockButton.getModel() );
		mainPanel.addItem( setBlockButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// give the list to the model
		((AbstractStateButtonModel)setStartButton.getModel()).setButtonList( buttons );
		((AbstractStateButtonModel)setExitButton.getModel()).setButtonList( buttons );
		((AbstractStateButtonModel)setBlockButton.getModel()).setButtonList( buttons );
	}

	@Override
	public void addPlayer(String playerName) 
	{
	}

	@Override
	public void ready(String playerName) 
	{
	}

	@Override
	public void start() 
	{
	}

	@Override
	public void startSoon() 
	{
	}

	@Override
	public void end(String winner) 
	{
	}

	@Override
	public void handleServerMessage(String[] messageComponents) 
	{
	}

}
