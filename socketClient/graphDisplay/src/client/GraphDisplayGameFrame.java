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
import client.model.AStarButtonModel;
import client.model.GraphCellModel;
import client.model.MessageButtonModel;
import client.model.SetKindButtonModel;
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
	private static final String WATER_BUTTON_CONFIG = "water_button_configuration";
	private static final String ROAD_BUTTON_CONFIG = "road_button_configuration";
	private static final String FOREST_BUTTON_CONFIG = "forest_button_configuration";
	private static final String HILL_BUTTON_CONFIG = "mountain_button_configuration";
	private static final String CELL_BUTTON_CONFIG = "cell_configuration";
	private static final String DFS_BUTTON_CONFIG = "DFS_button_configuration";
	private static final String BFS_BUTTON_CONFIG = "BFS_button_configuration";
	private static final String DIJ_BUTTON_CONFIG = "DIJ_button_configuration";
	private static final String ASTAR_BUTTON_CONFIG = "Astar_button_configuration";
	private static final String ASTARM_BUTTON_CONFIG = "Astar_Manhattan_button_configuration";
	private static final String ASTARME_BUTTON_CONFIG = "Astar_Manhattan_epsilon_button_configuration";
	private static final String RESET_BUTTON_CONFIG = "reset_button_configuration";
	private static final String CLEAR_BUTTON_CONFIG = "clear_button_configuration";

	// the cell constant 
	public static final String GRASS = "GRASS";
	private static final String BLOCK = "BLOCK";
	private static final String START = "START";
	private static final String EXIT = "EXIT";
	private static final String MOUNTAIN = "MOUNTAIN";
	private static final String ROAD = "ROAD";
	private static final String WATER = "WATER";
	private static final String FOREST = "FOREST";
	private static final String VISITED = "VISITED";
	private static final String PATH = "PATH";
	
	// the network constant
	private static final String CHANGE_CELL_STATE = "CHANGE_CELL_STATE";
	private static final String CELL_UPDATED = "CELL_UPDATED";
	private static final String COMPUTE_RESULT = "COMPUTE_RESULT";
	
	// The member
	private GraphMainDisplayer mainDisplayer = new GraphMainDisplayer();
	private GraphMainPanel mainPanel;
	private String currentStateForCell = GRASS;
	
	// the static element
	private GraphicalButtonItem setStartButton;
	private GraphicalButtonItem setExitButton;
	private GraphicalButtonItem setBlockButton;
	private GraphicalButtonItem setWaterButton;
	private GraphicalButtonItem setRoadButton;
	private GraphicalButtonItem setForestButton;
	private GraphicalButtonItem setHillButton;
	private GraphicalButtonItem dfsButton;
	private GraphicalButtonItem bfsButton;
	private GraphicalButtonItem dijButton;
	private GraphicalButtonItem astarButton;
	private GraphicalButtonItem astarMButton;
	private GraphicalButtonItem astarMEButton;
	private GraphicalButtonItem resetButton;
	private GraphicalButtonItem clearButton;
	
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
		
		// and initialize the entry at 0,0
		((GraphCellItem)mainPanel.getItemAt(0, 0)).getModel().setState( START );

		// display itself
		this.setVisible( true );
	}

	private void addStaticElement() throws IOException 
	{
		// create a button group
		List< AbstractStateButtonModel > buttons = new ArrayList< AbstractStateButtonModel >();
		
		// create a start button
		setStartButton = new GraphicalButtonItem( new SetKindButtonModel( this,
																		  repository.getData( START_BUTTON_CONFIG ) ),
												  repository.getData( START_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setStartButton.getModel() );
		mainPanel.addItem( setStartButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );

		// create an exit button
		setExitButton = new GraphicalButtonItem( new SetKindButtonModel( this,
																		 repository.getData( EXIT_BUTTON_CONFIG ) ),
												 repository.getData( EXIT_BUTTON_CONFIG ), 
												 tracker, 
												 ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setExitButton.getModel() );
		mainPanel.addItem( setExitButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
	
		// create a block button
		setBlockButton = new GraphicalButtonItem( new SetKindButtonModel( this,
																		   repository.getData( BLOCK_BUTTON_CONFIG ) ),
												  repository.getData( BLOCK_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setBlockButton.getModel() );
		mainPanel.addItem( setBlockButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a water button
		setWaterButton = new GraphicalButtonItem( new SetKindButtonModel( this,
																		  repository.getData( WATER_BUTTON_CONFIG ) ),
												  repository.getData( WATER_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setWaterButton.getModel() );
		mainPanel.addItem( setWaterButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a road button
		setRoadButton = new GraphicalButtonItem( new SetKindButtonModel( this,
																		 repository.getData( ROAD_BUTTON_CONFIG ) ),
												  repository.getData( ROAD_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setRoadButton.getModel() );
		mainPanel.addItem( setRoadButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a forest button
		setForestButton = new GraphicalButtonItem( new SetKindButtonModel( this,
																		   repository.getData( FOREST_BUTTON_CONFIG ) ),
												  repository.getData( FOREST_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setForestButton.getModel() );
		mainPanel.addItem( setForestButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a hill button
		setHillButton = new GraphicalButtonItem( new SetKindButtonModel( this,
																		 repository.getData( HILL_BUTTON_CONFIG ) ),
												  repository.getData( HILL_BUTTON_CONFIG ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() );
		buttons.add( (AbstractStateButtonModel) setHillButton.getModel() );
		mainPanel.addItem( setHillButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a DFS button
		dfsButton = new GraphicalButtonItem( new MessageButtonModel( this,
																 	 repository.getData( DFS_BUTTON_CONFIG ) ),
											 repository.getData( DFS_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( dfsButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a BFS button
		bfsButton = new GraphicalButtonItem( new MessageButtonModel( this,
																 	 repository.getData( BFS_BUTTON_CONFIG ) ),
											 repository.getData( BFS_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( bfsButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a Dij button
		dijButton = new GraphicalButtonItem( new MessageButtonModel( this,
																 	 repository.getData( DIJ_BUTTON_CONFIG ) ),
											 repository.getData( DIJ_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( dijButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a A* button
		astarButton = new GraphicalButtonItem( new AStarButtonModel( this,
																 	 repository.getData( ASTAR_BUTTON_CONFIG ) ),
											 repository.getData( ASTAR_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( astarButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a A* manhattan button
		astarMButton = new GraphicalButtonItem( new AStarButtonModel( this,
																      repository.getData( ASTARM_BUTTON_CONFIG ) ),
											 repository.getData( ASTARM_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( astarMButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a  A* manhattatn epslion button
		astarMEButton = new GraphicalButtonItem( new AStarButtonModel( this,
																 	   repository.getData( ASTARME_BUTTON_CONFIG ) ),
											 repository.getData( ASTARME_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( astarMEButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a  reset button
		resetButton = new GraphicalButtonItem( new MessageButtonModel( this,
																 	   repository.getData( RESET_BUTTON_CONFIG ) ),
											 repository.getData( RESET_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( resetButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a  clear button
		clearButton = new GraphicalButtonItem( new MessageButtonModel( this,
																 	   repository.getData( CLEAR_BUTTON_CONFIG ) ),
											 repository.getData( CLEAR_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( clearButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// give the list to the model for the group
		((AbstractStateButtonModel)setStartButton.getModel()).setButtonList( buttons );
		((AbstractStateButtonModel)setExitButton.getModel()).setButtonList( buttons );
		((AbstractStateButtonModel)setBlockButton.getModel()).setButtonList( buttons );
		((AbstractStateButtonModel)setWaterButton.getModel()).setButtonList( buttons );
		((AbstractStateButtonModel)setRoadButton.getModel()).setButtonList( buttons );
		((AbstractStateButtonModel)setForestButton.getModel()).setButtonList( buttons );
		((AbstractStateButtonModel)setHillButton.getModel()).setButtonList( buttons );
		
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
		else if ( messageComponents[ 0 ].compareTo( COMPUTE_RESULT ) == 0 )
		{
			int visited = 0;
			int path = 0;
			for ( int y = 0; y < 32; y++ )
			{
				for ( int x = 0; x < 32; x++ )
				{
					GraphCellItem item = (GraphCellItem) mainPanel.getItemAt( x * 16, y * 16 );
					char currentChar = messageComponents[ 2 ].charAt( y * 32 + x ); 
					switch( currentChar )
					{
					case '0':
						item.getModel().setState(GRASS);
						break;
					case 'S':
						item.getModel().setState(START);
						break;
					case 'E':
						item.getModel().setState(EXIT);
						break;
					case 'P':
						item.getModel().setState(PATH);
						path++;
						break;
					case 'V':
						item.getModel().setState(VISITED);
						visited++;
						break;
					case 'X':
						item.getModel().setState(BLOCK);
						break;
					case 'M':
						item.getModel().setState(MOUNTAIN);
						break;
					case 'R':
						item.getModel().setState(ROAD);
						break;
					case 'W':
						item.getModel().setState(WATER);
						break;
					case 'F':
						item.getModel().setState(FOREST);
						break;
					}
				}
			}
			setTitle( "GraphDisplay, path found = " + messageComponents[ 1 ] + " - V: " + ( visited + path) + " - P: " +path );
		}
	}

	public void requestChangeState(GraphCellModel model) 
	{
		// ask the server to change the state
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + CHANGE_CELL_STATE + " " + model.getX() + " " + model.getY() + " " + currentStateForCell );
	}

	public void setCurrentStateForCell( String state ) 
	{
		currentStateForCell = state;
	}

	public void sendMessage( String message ) 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + message );
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

}
