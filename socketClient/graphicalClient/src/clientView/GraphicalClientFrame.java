package clientView;

import frame.ConsoleFrame;
import game.AbstractGameClientFrame;
import game.ConsumerGameManager;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;
import graphic.item.GraphicalButtonItem;
import helper.DataRepository;

import java.awt.MediaTracker;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import network.client.AbstractSocketListenerClientSide;
import network.client.ConnectionInfo;
import network.client.ConsumerConnectionClient;
import network.client.GameConsumerObserver;
import network.client.MinimalSocketListener;
import client.ChessGameFrame;
import client.GraphDisplayGameFrame;
import client.TronGameClient;
import clientView.displayer.MainViewDisplayer;
import clientView.model.ChessButtonModel;
import clientView.model.ConnectButtonModel;
import clientView.model.ConsoleButtonModel;
import clientView.model.DisconnectButtonModel;
import clientView.model.GraphButtonModel;
import clientView.model.QuitButtonModel;
import clientView.model.TronButtonModel;
import clientView.panel.MainView;

import common.ChatCommonInformation;
import common.ChessCommonInformation;
import common.GraphCommonInformation;
import common.MessageType;
import common.TronCommonInformation;

import displayer.AbstractDisplayer;

@SuppressWarnings("serial")
public class GraphicalClientFrame extends JFrame implements GameConsumerObserver
{
	// the constant
	
	// configuration
	private static final String CONFIG_PATH = "resources/config/gClient.cfg";
	private static final String CONSOLE_BUTTON = "console_button_configuration";
	private static final String CONNECT_BUTTON = "connect_button_configuration";
	private static final String DISCONNECT_BUTTON = "disconnect_button_configuration";
	private static final String QUIT_BUTTON = "quit_button_configuration";
	private static final String TRON_BUTTON = "tron_button_configuration";
	private static final String CHESS_BUTTON = "chess_button_configuration";
	private static final String GRAPH_BUTTON = "graph_display_button_configuration";

	// the mandatory information
	protected DataRepository repository = new DataRepository();
	protected MediaTracker tracker = new MediaTracker( this );
	
	// the displayer information
	private MainViewDisplayer mainDisplayer;
	private MainView displayPanel;
	
	// graphical element
	private GraphicalButtonItem consoleButton;
	private GraphicalButtonItem connectButton;
	private GraphicalButtonItem disconnectButton;
	private GraphicalButtonItem tronButton;
	private GraphicalButtonItem chessButton;
	private GraphicalButtonItem graphButton;
	
	// Network relevant information
	private ConsumerConnectionClient connectionClient;

	// game relevant information
	private ConsumerGameManager gameManager;

	private ConsoleFrame consoleFrame = new ConsoleFrame();
	
	public GraphicalClientFrame() throws IOException
	{
		// load repository of information
		repository.addFromFile( CONFIG_PATH );
		
		// characteristics of the frame
		this.setTitle( "Graphical Client" );
	
		// create the main panel
		mainDisplayer = new MainViewDisplayer();
		displayPanel = new MainView( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		displayPanel.addDisplayer( MainViewDisplayer.NAME, mainDisplayer );

		// add the panel to the view
		this.setContentPane( displayPanel );
		
		// change the size of the frame to match the display
		this.setSize( displayPanel.viewMaxWidth + 7, displayPanel.viewMaxHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		
		// add the element in the view
		addStaticElement();
		
		// initialize the state of the visible items
		displayPanel.computeDisplayableItems();
	}

	private void addStaticElement() throws IOException 
	{
		// create a connect button
		connectButton = new GraphicalButtonItem( new ConnectButtonModel( this,
																		 repository.getData( CONNECT_BUTTON ) ),
												 repository.getData( CONNECT_BUTTON ), 
												 tracker, 
												 ImageLevel.ENVIRONMENT_IMAGE.index() );
		displayPanel.addItem( connectButton,
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a console button
		consoleButton = new GraphicalButtonItem( new ConsoleButtonModel( this,
																		 repository.getData( CONSOLE_BUTTON ) ),
												 repository.getData( CONSOLE_BUTTON ), 
												 tracker, 
												 ImageLevel.ENVIRONMENT_IMAGE.index() );
		displayPanel.addItem( consoleButton,
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a disconnect button
		disconnectButton = new GraphicalButtonItem( new DisconnectButtonModel( this,
																	 	 	   repository.getData( DISCONNECT_BUTTON ) ),
													repository.getData( DISCONNECT_BUTTON ), 
													tracker, 
													ImageLevel.ENVIRONMENT_IMAGE.index() );
		displayPanel.addItem( disconnectButton,
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a tron button
		tronButton = new GraphicalButtonItem( new TronButtonModel( this,
												 				   repository.getData( TRON_BUTTON ) ),
											  repository.getData( TRON_BUTTON ), 
											  tracker, 
											  ImageLevel.ENVIRONMENT_IMAGE.index() );
		displayPanel.addItem( tronButton,
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		tronButton.getModel().hide();
		
		// create a chess button
		chessButton = new GraphicalButtonItem( new ChessButtonModel( this,
												 				     repository.getData( CHESS_BUTTON ) ),
											  repository.getData( CHESS_BUTTON ), 
											  tracker, 
											  ImageLevel.ENVIRONMENT_IMAGE.index() );
		displayPanel.addItem( chessButton,
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		chessButton.getModel().hide();
		
		// create a graph button
		graphButton = new GraphicalButtonItem( new GraphButtonModel( this,
												 				     repository.getData( GRAPH_BUTTON ) ),
											  repository.getData( GRAPH_BUTTON ), 
											  tracker, 
											  ImageLevel.ENVIRONMENT_IMAGE.index() );
		displayPanel.addItem( graphButton,
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		graphButton.getModel().hide();
		
		// create a quit button
		displayPanel.addItem( new GraphicalButtonItem( new QuitButtonModel( repository.getData( QUIT_BUTTON ) ),
				 									   repository.getData( QUIT_BUTTON ), 
				 									   tracker, 
				 									   ImageLevel.ENVIRONMENT_IMAGE.index() ),
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
	}
	
	public void launchConnection( ConnectionInfo info ) throws UnknownHostException, IOException, InterruptedException 
	{
		connectionClient = new ConsumerConnectionClient( this );
		connectionClient.launchConnection( info, false );
		
		gameManager = new ConsumerGameManager( connectionClient );
	}

	public void closeConnection() 
	{
		if ( gameManager != null )
		{
			// remove the gameManager
			try 
			{
				gameManager.closeAllGames();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			gameManager = null;
		}
		
		if ( connectionClient != null )
		{
			// close the connection
			connectionClient.closeConnection();
			connectionClient = null;
		}
	}
	
	@Override
	public void raiseAlert(String message) 
	{
		consoleFrame.displayAlert(message);
	}

	@Override
	public void raiseInfo(String message) 
	{
		consoleFrame.displayInfo(message);
	}

	@Override
	public void serverDisconnection() 
	{
		connectButton.getModel().show();
		disconnectButton.getModel().hide();
		tronButton.getModel().hide();
		chessButton.getModel().hide();
		graphButton.getModel().hide();
		
		displayPanel.computeDisplayableItems();
	}

	@Override
	public void connectionStatusChange( network.client.ConnectionClient.State currentState ) 
	{
		if ( currentState == network.client.ConnectionClient.State.CONNECTED )
		{
			connectButton.getModel().hide();
			disconnectButton.getModel().show();
			tronButton.getModel().show();
			chessButton.getModel().show();
			graphButton.getModel().show();
			
			displayPanel.computeDisplayableItems();
		}
	}

	@Override
	public AbstractSocketListenerClientSide createSocketListener(Socket socket) throws IOException 
	{
		return new MinimalSocketListener( socket, connectionClient );
	}
	
	// display the dialog with the list of opponent
	public String displayOpponentChoiceDialog(String gameName, boolean canPlayAgainstIA) 
	{
		// prepare the list of opponent
		List< String > listOpponent = new ArrayList< String >();
		if ( canPlayAgainstIA == true )
		{
			listOpponent.add( "IA" );
		}
		for ( String client : connectionClient.getClientList() )
		{
			if ( client.compareTo( connectionClient.getLogin() ) != 0 )
			{
				listOpponent.add( client );
			}
		}

		if ( listOpponent.size() > 0 )
		{
			// display the choice dialog 
			return (String) JOptionPane.showInputDialog( null, 
														 "Choose an opponent> ",
														 "Playing " + gameName,
														 JOptionPane.QUESTION_MESSAGE,
														 null,
														 listOpponent.toArray(),
														 listOpponent.get(0) );
		}
		
		connectionClient.forwardInfo( "No player can play against you at " + gameName );
		return null;
	}

	public void showConsoleFrame() 
	{
		consoleFrame .setVisible( true );
	}

	public void hideConsoleFrame() 
	{
		consoleFrame.setVisible( false );
	}

	@Override
	public void handleMessage(String message) 
	{
		gameManager.handleGameMessage(message);
	}

	@Override
	public AbstractGameClientFrame requireGame(String gameName) throws IOException 
	{
		if ( gameName.compareTo( TronCommonInformation.GAME_NAME ) == 0 )
		{
			return new TronGameClient();
		}
		else if ( gameName.compareTo( ChessCommonInformation.GAME_NAME ) == 0 )
		{
			return new ChessGameFrame();
		}
		else if ( gameName.compareTo( GraphCommonInformation.GAME_NAME ) == 0 )
		{
			return new GraphDisplayGameFrame();
		}
		return null;
	}

	public DataRepository getRepository() 
	{
		return repository;
	}

	@Override
	public void onLoginAccepted() 
	{
		// send the registration
		connectionClient.sendMessageIfConnected( MessageType.MessageSystemRegister + " " + MessageType.RegistrationAsConsumer + " " + 
											     ChatCommonInformation.GAME_NAME + " " + 
											     TronCommonInformation.GAME_NAME + " " +
											     GraphCommonInformation.GAME_NAME + " " +
											     ChessCommonInformation.GAME_NAME );
	}


//	// ask a game to the server, giving the opponent and the game name
//	public void askForGameTo( String opponentName, 
//							  String gameName) 
//	{
//		connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameAsked + " " + opponentName + " " + gameName );
//	}
//
//	// ask a solo game to the server (no opponent)
//	public void askForSoloGame(String gameName) 
//	{
//		connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameAsked + " " + MessageType.MessageGameSoloGameOpponent + " " + gameName );
//	}
	
	// request a game given its name
	public void requestGame( String gameName ) 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageSystemRequestGame + " " + gameName );
	}
}
