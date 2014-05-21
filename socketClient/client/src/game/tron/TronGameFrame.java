package game.tron;

import game.TronGame;
import game.tron.displayer.TronMainDisplayer;
import game.tron.displayer.TronMiniMapDisplayer;
import game.tron.displayer.TronPlayerPanelDisplayer;
import game.tron.item.TronPlayerItem;
import game.tron.model.TronPlayerModel;
import game.tron.panel.TronMainPanel;
import game.tron.panel.TronMiniMapPanel;
import game.tron.panel.TronPlayerPanel;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;
import helper.DataRepository;

import java.awt.BorderLayout;
import java.awt.MediaTracker;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import main.listener.ClosingMessageListener;

import common.MessageType;


/*
 *	Client part: Mview + V (utilisation du Datarepository et config pour la definition)
 *		gestion du display des differents objets
 *		gestion de la presentation
 *		gestion des commandes via forward au Server
 * */
@SuppressWarnings("serial")
public class TronGameFrame extends JFrame {

	private static final String TRON_CONFIG_PATH = "resources/config/tron.cfg";
	private static final String BLUE_PLAYER_GRAPHICAL_CONFIGURATION = "blue_player_configuration";
	private static final String RED_PLAYER_GRAPHICAL_CONFIGURATION = "red_player_configuration";
	private static final String BLUE_PLAYER_MINIMAP_CONFIGURATION = "blue_player_minimap_configuration";
	private static final String RED_PLAYER_MINIMAP_CONFIGURATION = "red_player_minimap_configuration";
	
	private PrintWriter targetWriter = null;
	private String gameId = null;
	private TronMainPanel gamePanel = null;
	
	private String login = null;
	private DataRepository repository = new DataRepository();

	private TronPlayerModel bluePlayerModel = null;
	private TronPlayerModel redPlayerModel = null;
	
	private MediaTracker tracker = new MediaTracker( this );
	
	private KeyListener movementKeyListener = new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyPressed(KeyEvent arg0) 
		{
			if ( arg0.getKeyCode() == KeyEvent.VK_LEFT )
			{
				targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + login + " " + TronGame.LEFT );
				targetWriter.flush();
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_RIGHT )
			{
				targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + login + " " + TronGame.RIGHT );
				targetWriter.flush();
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_UP )
			{
				targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + login + " " + TronGame.UP );
				targetWriter.flush();
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_DOWN )
			{
				targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + login + " " + TronGame.DOWN );
				targetWriter.flush();
			}
		}
	};
	
	public TronGameFrame( PrintWriter writer, 
					  	  String gameID, 
					  	  String name, 
					  	  String bluePlayerName, 
					  	  String redPlayerName ) throws IOException 
	{
		targetWriter  = writer;
		gameId  = gameID;
		login = name;

		// load repository of information
		repository.addFromFile( TRON_CONFIG_PATH );
		
		// characteristics of the frame
		this.setTitle( "Tron");
		this.setSize( TronMainPanel.frameSize + 7, TronMainPanel.frameSize + 29 + TronMiniMapPanel.frameSize); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		this.addWindowListener( new ClosingMessageListener( targetWriter, MessageType.MessageSystem + " " + MessageType.MessageGameClose + " " + gameId ) );
		this.addKeyListener( movementKeyListener );
	
		// create the player's model 
		bluePlayerModel = new TronPlayerModel( bluePlayerName,
											   TronGame.BLUE_START_X, 
			 	  							   TronGame.BLUE_START_Y,
			 	  							   TronGame.BLUE_START_DIRECTION );
		redPlayerModel = new TronPlayerModel( redPlayerName,
											  TronGame.RED_START_X, 
											  TronGame.RED_START_Y,
											  TronGame.RED_START_DIRECTION );
		
		// create the main panel
		gamePanel = new TronMainPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		gamePanel.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );

		TronMainDisplayer mainPanelDisplayer = new TronMainDisplayer();
		
		// add the gItem to the panel
		gamePanel.addItem( new TronPlayerItem( bluePlayerModel, 
											   mainPanelDisplayer, 
											   repository.getData( BLUE_PLAYER_GRAPHICAL_CONFIGURATION ), 
											   tracker, 
											   ImageLevel.ENVIRONMENT_IMAGE.index() ), 
											   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		gamePanel.addItem( new TronPlayerItem( redPlayerModel, 
											   mainPanelDisplayer, 
											   repository.getData( RED_PLAYER_GRAPHICAL_CONFIGURATION ), 
											   tracker, 
											   ImageLevel.ENVIRONMENT_IMAGE.index() ), 
											   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		
		// create the minimap panel
		TronMiniMapPanel miniMapPanel = new TronMiniMapPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		miniMapPanel.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		
		// create the visitor of the miniPanel
		TronMiniMapDisplayer minimapDisplayer = new TronMiniMapDisplayer();
		
		// add the gItem to the panel
		miniMapPanel.addItem( new TronPlayerItem( bluePlayerModel, 
												  minimapDisplayer, 
												  repository.getData( BLUE_PLAYER_MINIMAP_CONFIGURATION ), 
												  tracker,
												  ImageLevel.ENVIRONMENT_IMAGE.index() ), 
												  GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		miniMapPanel.addItem( new TronPlayerItem( redPlayerModel, 
												  minimapDisplayer, 
												  repository.getData( RED_PLAYER_MINIMAP_CONFIGURATION ), 
												  tracker, 
												  ImageLevel.ENVIRONMENT_IMAGE.index() ), 
												  GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		
		// create the visitor of the miniPanel
		TronPlayerPanelDisplayer playerDisplayer = new TronPlayerPanelDisplayer();
		
		// create the blue panel
		TronPlayerPanel bluePlayerPanel = new TronPlayerPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		bluePlayerPanel.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		
		// add the gItem to the panel
		bluePlayerPanel.addItem( new TronPlayerItem( bluePlayerModel, 
													 playerDisplayer, 
													 repository.getData( BLUE_PLAYER_GRAPHICAL_CONFIGURATION ), 
													 tracker,
													 ImageLevel.ENVIRONMENT_IMAGE.index() ), 
													 GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		// create the blue panel
		TronPlayerPanel redPlayerPanel = new TronPlayerPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		redPlayerPanel.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		
		redPlayerPanel.addItem( new TronPlayerItem( redPlayerModel, 
													playerDisplayer, 
													repository.getData( RED_PLAYER_GRAPHICAL_CONFIGURATION ), 
													tracker, 
													ImageLevel.ENVIRONMENT_IMAGE.index() ), 
													GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

		// create the panel structure on screen
		Box infoPanel = Box.createHorizontalBox();
		infoPanel.add( miniMapPanel );
		infoPanel.add( bluePlayerPanel );
		infoPanel.add( redPlayerPanel );
		
		this.setLayout( new BorderLayout() );
		this.getContentPane().add( gamePanel, BorderLayout.CENTER );
		this.getContentPane().add( infoPanel, BorderLayout.NORTH );

		// display itself
		this.setVisible( true );
		
		
		String player = "You play as ";
		if ( login.compareTo( bluePlayerName ) == 0 )
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
			targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameReady + " " + gameId + " " + login );
		}
		else
		{
			targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameClose );
		}
		targetWriter.flush();
	}

	public void ready(String name) 
	{
		// manage the player being ready
	}

	public void start() 
	{
		gamePanel.setStart();
	}

	public String getId() 
	{
		return gameId;
	}

	public void end(String winner) 
	{
		if ( login.compareTo( winner ) == 0 )
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
			bluePlayerModel.changePosition( Math.min( TronGame.gridSize - 1, Math.max( 0.0, bX ) ), 
											Math.min( TronGame.gridSize - 1, Math.max( 0.0, bY ) ) );
			bluePlayerModel.changeDirection( bD );
			bluePlayerModel.changePath( bP );
			
			redPlayerModel.changePosition( Math.min( TronGame.gridSize - 1, Math.max( 0.0, rX ) ), 
				  	   					   Math.min( TronGame.gridSize - 1, Math.max( 0.0, rY ) ) );
			redPlayerModel.changeDirection( rD );
			redPlayerModel.changePath( rP );
		}
	}
}
