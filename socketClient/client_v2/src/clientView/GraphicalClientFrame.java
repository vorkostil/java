package clientView;

import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;
import graphic.item.GraphicalButtonItem;
import graphic.model.AbstractButtonModel;
import helper.DataRepository;

import java.awt.MediaTracker;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import network.client.AbstractSocketListenerClientSide;
import network.client.ConnectionClient;
import network.client.ConnectionInfo;
import network.client.ConnectionObserver;
import clientView.displayer.MainViewDisplayer;
import clientView.model.ConnectButtonModel;
import clientView.model.DisconnectButtonModel;
import clientView.model.QuitButtonModel;
import clientView.model.TronButtonModel;
import clientView.panel.MainView;

import common.MessageType;

import displayer.AbstractDisplayer;

@SuppressWarnings("serial")
public class GraphicalClientFrame extends JFrame implements ConnectionObserver
{
	// the constant
	private static final String CONFIG_PATH = "resources/config/gClient.cfg";
	private static final String CONNECT_BUTTON = "connect_button_configuration";
	private static final String DISCONNECT_BUTTON = "disconnect_button_configuration";
	private static final String QUIT_BUTTON = "quit_button_configuration";
	private static final String TRON_BUTTON = "tron_button_configuration";;
	
	// the mandatory information
	protected DataRepository repository = new DataRepository();
	protected MediaTracker tracker = new MediaTracker( this );
	
	// the displayer information
	private MainViewDisplayer mainDisplayer;
	private MainView displayPanel;
	
	// graphical element
	ConnectButtonModel connectButtonModel;
	DisconnectButtonModel disconnectButtonModel;
	TronButtonModel tronButtonModel;

	// Network relevant information
	ConnectionClient connectionClient;
	
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
		connectButtonModel = new ConnectButtonModel( this,
													 repository.getData( CONNECT_BUTTON ) );
		displayPanel.addItem( new GraphicalButtonItem( connectButtonModel,
				 									   repository.getData( CONNECT_BUTTON ), 
				 									   tracker, 
				 									   ImageLevel.ENVIRONMENT_IMAGE.index() ),
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a disconnect button
		disconnectButtonModel = new DisconnectButtonModel( this,
													       repository.getData( DISCONNECT_BUTTON ) );
		displayPanel.addItem( new GraphicalButtonItem( disconnectButtonModel,
				 									   repository.getData( DISCONNECT_BUTTON ), 
				 									   tracker, 
				 									   ImageLevel.ENVIRONMENT_IMAGE.index() ),
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		
		// create a tron button
		tronButtonModel = new TronButtonModel( this,
											   repository.getData( TRON_BUTTON ) );
		displayPanel.addItem( new GraphicalButtonItem( tronButtonModel,
				 									   repository.getData( TRON_BUTTON ), 
				 									   tracker, 
				 									   ImageLevel.ENVIRONMENT_IMAGE.index() ),
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
		tronButtonModel.hide();
		
		// create a quit button
		AbstractButtonModel quitButtonModel = new QuitButtonModel( repository.getData( QUIT_BUTTON ) );
		displayPanel.addItem( new GraphicalButtonItem( quitButtonModel,
				 									   repository.getData( QUIT_BUTTON ), 
				 									   tracker, 
				 									   ImageLevel.ENVIRONMENT_IMAGE.index() ),
				 			  MainViewDisplayer.NAME,
							  AbstractDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
	}
	
	public void launchConnection( ConnectionInfo info ) throws UnknownHostException, IOException, InterruptedException 
	{
		connectionClient = new ConnectionClient( this );
		connectionClient.launchConnection( info );
	}

	public void closeConnection() 
	{
		if ( connectionClient != null )
		{
			connectionClient.closeConnection();
		}
	}
	
	@Override
	public void raiseAlert(String message) 
	{
	}

	@Override
	public void raiseInfo(String message) 
	{
	}

	@Override
	public void serverDisconnection() 
	{
		connectButtonModel.show();
		disconnectButtonModel.hide();
		tronButtonModel.hide();
		
		displayPanel.computeDisplayableItems();
	}

	@Override
	public void connectionStatusChange( network.client.ConnectionClient.State currentState ) 
	{
		if ( currentState == network.client.ConnectionClient.State.CONNECTED )
		{
			connectButtonModel.hide();
			disconnectButtonModel.show();
			tronButtonModel.show();
			
			displayPanel.computeDisplayableItems();
		}
	}

	@Override
	public AbstractSocketListenerClientSide createSocketListener(Socket socket) throws IOException 
	{
		return new MinimalSocketListener( socket, connectionClient );
	}

	// ask a game to the server, giving the opponent and the game name
	public void askForGameTo( String opponentName, 
							  String gameName) 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameAsked + " " + opponentName + " " + gameName );
	}
}
