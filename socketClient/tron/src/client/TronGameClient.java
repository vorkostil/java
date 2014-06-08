package client;

import game.AbstractGameClient;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;
import graphic.listener.ClosingMessageListener;
import helper.DataRepository.DataInformation;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import client.displayer.TronMainDisplayer;
import client.displayer.TronMiniMapDisplayer;
import client.displayer.TronPlayerPanelDisplayer;
import client.item.TronPlayerItem;
import client.model.TronPlayerModel;
import client.panel.TronMainPanel;

import network.client.ConnectionClient;

import common.MessageType;

import displayer.AbstractDisplayer;


/*
 *	Client part: Mview + V (utilisation du Datarepository et config pour la definition)
 *		gestion du display des differents objets
 *		gestion de la presentation
 *		gestion des commandes via forward au Server
 * */
@SuppressWarnings("serial")
public class TronGameClient extends AbstractGameClient 
{
	private static final String TRON_CONFIG_PATH = "../tron/resources/config/tron.cfg";
	
	private static final String BLUE_PLAYER_CONFIGURATION = "blue_player_configuration";
	private static final String RED_PLAYER_CONFIGURATION = "red_player_configuration";
	private static final String START_X = "start_x";
	private static final String START_Y = "start_y";
	private static final String START_DIR = "start_direction";

	private static final String GAME_CONFIGURATION = "tron_configuration";
	private static final String GRID_SIZE = "grid_size";
	public static final String LEFT_VALUE = "left_direction_value";
	public static final String RIGHT_VALUE = "right_direction_value";
	public static final String UP_VALUE = "up_direction_value";
	public static final String DOWN_VALUE = "down_direction_value";
	
	private TronMainPanel gamePanel = null;

	private TronPlayerModel bluePlayerModel = null;
	private TronPlayerModel redPlayerModel = null;

	private int gridSize;
	public int leftDirection;
	public int rightDirection;
	public int upDirection;
	public int downDirection;
	
	private KeyListener movementKeyListener = new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent arg0) {
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {
		}
		
		@Override
		public void keyPressed(KeyEvent arg0) 
		{
			if ( arg0.getKeyCode() == KeyEvent.VK_LEFT )
			{
				connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + connectionClient.getLogin() + " " + leftDirection );
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_RIGHT )
			{
				connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + connectionClient.getLogin() + " " + rightDirection );
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_UP )
			{
				connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + connectionClient.getLogin() + " " + upDirection );
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_DOWN )
			{
				connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + connectionClient.getLogin() + " " + downDirection );
			}
		}
	};
	
	public TronGameClient( ConnectionClient connectionClient, 
						   String gameId,
						   String bluePlayerName, 
						   String redPlayerName) throws IOException 
	{
		super( connectionClient, gameId, TRON_CONFIG_PATH );
		
		// characteristics of the frame
		this.setTitle( "Tron");
		this.setSize( TronMainPanel.frameWidth + 7, TronMainPanel.frameHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		this.addWindowListener( new ClosingMessageListener( connectionClient, MessageType.MessageSystem + " " + MessageType.MessageGameClose + " " + gameId ) );
		this.addKeyListener( movementKeyListener );
	

		// get the game and player information
		DataInformation gameInformation = repository.getData( GAME_CONFIGURATION );
		DataInformation bluePlayerInformation = repository.getData( BLUE_PLAYER_CONFIGURATION );
		DataInformation redPlayerInformation = repository.getData( RED_PLAYER_CONFIGURATION );
		
		// store information about the game
		gridSize = gameInformation.getIntegerValue( GRID_SIZE );
		leftDirection = gameInformation.getIntegerValue( LEFT_VALUE );
		rightDirection = gameInformation.getIntegerValue( RIGHT_VALUE );
		upDirection = gameInformation.getIntegerValue( UP_VALUE );
		downDirection = gameInformation.getIntegerValue( DOWN_VALUE );
		
		// create the player's model 
		bluePlayerModel = new TronPlayerModel( this,
											   bluePlayerName,
											   bluePlayerInformation.getIntegerValue( START_X ), 
											   bluePlayerInformation.getIntegerValue( START_Y ),
											   bluePlayerInformation.getIntegerValue( START_DIR ) );
		redPlayerModel = new TronPlayerModel( this,
											  redPlayerName,
										   	  redPlayerInformation.getIntegerValue( START_X ), 
										   	  redPlayerInformation.getIntegerValue( START_Y ),
										   	  redPlayerInformation.getIntegerValue( START_DIR ) );
		
		// create the main panel
		gamePanel = new TronMainPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		
		// create and associate the displayer
		gamePanel.addDisplayer( TronMainDisplayer.NAME, new TronMainDisplayer(0,64) );
		gamePanel.addDisplayer( TronMiniMapDisplayer.NAME, new TronMiniMapDisplayer(0,0) );
		gamePanel.addDisplayer( "BLUE" + TronPlayerPanelDisplayer.NAME, new TronPlayerPanelDisplayer(65,0) );
		gamePanel.addDisplayer( "RED" + TronPlayerPanelDisplayer.NAME, new TronPlayerPanelDisplayer(280,0) );
		
		// create the players and add them to their displayer / layer 
		gamePanel.createStartText(repository);
		TronPlayerItem blueItem = new TronPlayerItem( bluePlayerModel, 
													  bluePlayerInformation, 
													  tracker, 
													  ImageLevel.ENVIRONMENT_IMAGE.index() );
		TronPlayerItem redItem = new TronPlayerItem( redPlayerModel, 
													 redPlayerInformation, 
													 tracker, 
													 ImageLevel.ENVIRONMENT_IMAGE.index() );
		
		gamePanel.addItem( blueItem, 
						   TronMainDisplayer.NAME, 
						   AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW );  
		gamePanel.addItem( redItem,
				   		   TronMainDisplayer.NAME,
				   		   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		gamePanel.addItem( blueItem, 
				           TronMiniMapDisplayer.NAME, 
						   AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW );  
		gamePanel.addItem( redItem,
						   TronMiniMapDisplayer.NAME,
				   		   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		gamePanel.addItem( blueItem, 
				           "BLUE" + TronPlayerPanelDisplayer.NAME, 
						   AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW );  
		gamePanel.addItem( redItem,
						   "RED" + TronPlayerPanelDisplayer.NAME,
				   		   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		
		gamePanel.computeDisplayableItems();
	
		// create the panel structure on screen
		this.setLayout( new BorderLayout() );
		this.getContentPane().add( gamePanel, BorderLayout.CENTER );

		// display itself
		this.setVisible( true );
		
		
		String player = "You play as ";
		if ( connectionClient.getLogin().compareTo( bluePlayerName ) == 0 )
		{
			player += " BLUE\n";
		}
		else
		{
			player += " RED\n";
		}
		
		// ask for ready
		if ( JOptionPane.showConfirmDialog( null, 
											player + "are you ready to play ?", 
											"Game launch", 
											JOptionPane.YES_NO_OPTION, 
											JOptionPane.QUESTION_MESSAGE ) == JOptionPane.OK_OPTION)
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameReady + " " + gameId + " " + connectionClient.getLogin() );
		}
		else
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameClose );
		}
	}

	public void ready(String name) 
	{
		// manage the player being ready
	}

	public void start() 
	{
		gamePanel.setStart();
	}

	public void end(String winner) 
	{
		if ( connectionClient.getLogin().compareTo( winner ) == 0 )
		{
			JOptionPane.showMessageDialog(this, "You WIN", "Game finish, " + winner + " wins", JOptionPane.INFORMATION_MESSAGE );
		}
		else
		{
			JOptionPane.showMessageDialog(this, "You LOOSE", "Game finish, " + winner + " wins", JOptionPane.INFORMATION_MESSAGE );
		}
	}

	public void startSoon() 
	{
		gamePanel.setStartSoon();
	}

	public void forwardMessage(String line) 
	{
		String[] splitted = line.split(" ");
		String action = splitted[ 2 ];
		
		if ( action.compareTo( MessageType.MessageUpdatePosition ) == 0 )
		{
			double bX 	= java.lang.Double.parseDouble( splitted[ 4 ] );
			double bY 	= java.lang.Double.parseDouble( splitted[ 5 ] );
			int bD 		= Integer.parseInt( splitted[ 6 ] );
			int bS 		= Integer.parseInt( splitted[ 7 ] );
			List< Point2D > bP = new ArrayList< Point2D >();  
			for ( int i = 0; i < bS; i++ )
			{
				bP.add( new Point2D.Double( java.lang.Double.parseDouble( splitted[ 8 + i * 2 ] ),
											java.lang.Double.parseDouble( splitted[ 9 + i * 2 ] ) ) );
			}
			
			double rX 	= java.lang.Double.parseDouble( splitted[ 8 + bS * 2 ] );
			double rY 	= java.lang.Double.parseDouble( splitted[ 9 + bS * 2 ] );
			int rD 		= Integer.parseInt( splitted[ 10 + bS * 2 ] );
			int rS 		= Integer.parseInt( splitted[ 11 + bS * 2 ] );
			List< Point2D > rP = new ArrayList< Point2D >();  
			for ( int i = 0; i < rS; i++ )
			{
				rP.add( new Point2D.Double( java.lang.Double.parseDouble( splitted[ 12 + bS * 2 + i * 2] ),
											java.lang.Double.parseDouble( splitted[ 13 + bS * 2 + i * 2] ) ) );
			}

			// forward player position update
			bluePlayerModel.changePosition( Math.min( gridSize - 1, Math.max( 0.0, bX ) ), 
											Math.min( gridSize - 1, Math.max( 0.0, bY ) ) );
			bluePlayerModel.changeDirection( bD );
			bluePlayerModel.changePath( bP );
			
			redPlayerModel.changePosition( Math.min( gridSize - 1, Math.max( 0.0, rX ) ), 
				  	   					   Math.min( gridSize - 1, Math.max( 0.0, rY ) ) );
			redPlayerModel.changeDirection( rD );
			redPlayerModel.changePath( rP );
		}
	}
}
