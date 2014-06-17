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
import client.model.BfsButtonModel;
import client.model.DfsButtonModel;
import client.model.DijButtonModel;
import client.model.GraphCellModel;
import client.model.ResetButtonModel;
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
	private static final String DFS_BUTTON_CONFIG = "DFS_button_configuration";
	private static final String BFS_BUTTON_CONFIG = "BFS_button_configuration";
	private static final String DIJ_BUTTON_CONFIG = "DIJ_button_configuration";
	private static final String ASTAR_BUTTON_CONFIG = "Astar_button_configuration";
	private static final String ASTARM_BUTTON_CONFIG = "Astar_Manhattan_button_configuration";
	private static final String ASTARME_BUTTON_CONFIG = "Astar_Manhattan_epsilon_button_configuration";
	private static final String RESET_BUTTON_CONFIG = "reset_button_configuration";

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
	private static final String COMPUTE_RESULT = "COMPUTE_RESULT";
	
	// The member
	private GraphMainDisplayer mainDisplayer = new GraphMainDisplayer();
	private GraphMainPanel mainPanel;
	private String currentStateForCell = EMPTY;
	
	// the static element
	private GraphicalButtonItem setStartButton;
	private GraphicalButtonItem setExitButton;
	private GraphicalButtonItem setBlockButton;
	private GraphicalButtonItem dfsButton;
	private GraphicalButtonItem bfsButton;
	private GraphicalButtonItem dijButton;
	private GraphicalButtonItem astarButton;
	private GraphicalButtonItem astarMButton;
	private GraphicalButtonItem astarMEButton;
	private GraphicalButtonItem resetButton;
	
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
		
		// create a DFS button
		dfsButton = new GraphicalButtonItem( new DfsButtonModel( this,
																 repository.getData( DFS_BUTTON_CONFIG ) ),
											 repository.getData( DFS_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( dfsButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a BFS button
		bfsButton = new GraphicalButtonItem( new BfsButtonModel( this,
																 repository.getData( BFS_BUTTON_CONFIG ) ),
											 repository.getData( BFS_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( bfsButton,
						   GraphMainDisplayer.NAME,
						   AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a Dij button
		dijButton = new GraphicalButtonItem( new DijButtonModel( this,
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
		resetButton = new GraphicalButtonItem( new ResetButtonModel( this,
																 	 repository.getData( RESET_BUTTON_CONFIG ) ),
											 repository.getData( RESET_BUTTON_CONFIG ), 
											 tracker, 
											 ImageLevel.ENVIRONMENT_IMAGE.index() );
		mainPanel.addItem( resetButton,
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
						item.getModel().setState(EMPTY);
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
		currentStateForCell  = state;
	}

	public void callForDfs() 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageComputeDfs );
	}

	public void callForBfs() 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageComputeBfs );
	}

	public void callForDij() 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageComputeDij );
	}

	public void callForAstar(String heuristic) 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageComputeAStar + " " + heuristic );
	}

	public void callForReset() 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageResetPath );
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
