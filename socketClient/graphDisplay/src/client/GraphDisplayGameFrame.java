package client;

import game.AbstractGameClientFrame;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;
import graphic.item.GraphicalButtonItem;
import graphic.model.AbstractStateButtonModel;
import helper.DataRepository.DataInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.displayer.GraphMainDisplayer;
import client.item.GraphCellItem;
import client.model.GraphCellModel;
import client.model.SetBlockButtonModel;
import client.model.SetExitButtonModel;
import client.model.SetStartButtonModel;
import client.panel.GraphMainPanel;

import common.MessageType;

import displayer.AbstractDisplayer;

@SuppressWarnings("serial")
public class GraphDisplayGameFrame extends AbstractGameClientFrame 
{
	// The constant
	private static final String CONFIG_PATH = "../graphDisplay/resources/config/graph_display.cfg";
	private static final String START_BUTTON_CONFIG = "start_button_configuration";
	private static final String EXIT_BUTTON_CONFIG = "exit_button_configuration";
	private static final String BLOCK_BUTTON_CONFIG = "block_button_configuration";
	private static final String CELL_BUTTON_CONFIG = "cell_configuration";

	// the cell constant 
	public static final String EMPTY = "EMPTY";
	public static final String BLOCK = "BLOCK";
	public static final String START = "START";
	public static final String EXIT = "EXIT";
	public static final String VISITED = "VISITED";
	public static final String PATH = "PATH";
	
	// the network constant
	private static final String CHANGE_CELL_STATE = "CHANGE_CELL_STATE";
	private static final String CELL_UPDATED = "CELL_UPDATED";
	
	// The member
	private GraphMainDisplayer mainDisplayer = new GraphMainDisplayer();
	private GraphMainPanel mainPanel;
	
	// the static element
	private GraphicalButtonItem setStartButton;
	private GraphicalButtonItem setExitButton;
	private GraphicalButtonItem setBlockButton;
	private String currentStateForCell = EMPTY;
	
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
		this.setSize( mainPanel.viewMaxWidth + 6, mainPanel.viewMaxHeight + 28 ); // add the border to the size and the padding
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
		setStartButton = new GraphicalButtonItem( new SetStartButtonModel( this,
																		   repository.getData( START_BUTTON_CONFIG ) ),
												  repository.getData( START_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setStartButton.getModel() );
		mainPanel.addItem( setStartButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );

		// create an exit button
		setExitButton = new GraphicalButtonItem( new SetExitButtonModel( this,
																		 repository.getData( EXIT_BUTTON_CONFIG ) ),
												 repository.getData( EXIT_BUTTON_CONFIG ), 
												 tracker, 
												 ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setExitButton.getModel() );
		mainPanel.addItem( setExitButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
	
		// create a block button
		setBlockButton = new GraphicalButtonItem( new SetBlockButtonModel( this,
																		   repository.getData( BLOCK_BUTTON_CONFIG ) ),
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
		
		// create the 32 x 32 cells
		DataInformation dataCell = repository.getData( CELL_BUTTON_CONFIG );
		for ( int y = 0; y < 32; y++ )
		{
			for ( int x = 0; x < 32; x++ )
			{
				mainPanel.addItem( new GraphCellItem( new GraphCellModel( this, 
															    x,
															    y ),
												 dataCell,
												 tracker,
												 ImageLevel.ENVIRONMENT_IMAGE.index() ),
								   GraphMainDisplayer.NAME,
								   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
			}
		}
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
		if ( messageComponents[ 0 ].compareTo( CELL_UPDATED ) == 0 )
		{
			GraphCellItem item = (GraphCellItem) mainPanel.getItemAt( Integer.parseInt( messageComponents[ 1 ] ) * 16, 
								 									  Integer.parseInt( messageComponents[ 2 ] ) * 16 );
			if ( item != null )
			{
				item.getModel().setState( messageComponents[ 3 ] );
			}
		}
	}

	public void requestChangeState(GraphCellModel model) 
	{
		// ask the server to change the state
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + CHANGE_CELL_STATE + " " + model.getX() + " " + model.getY() + " " + currentStateForCell );
	}

	public void setCurrentStateForCell( String state ) 
	{
		currentStateForCell  = state;
	}

}
