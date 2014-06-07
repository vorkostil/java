package game.tron;

import game.AbstractGameFrame;
import game.TronGameServer;
import game.tron.displayer.TronMainDisplayer;
import game.tron.displayer.TronMiniMapDisplayer;
import game.tron.displayer.TronPlayerPanelDisplayer;
import game.tron.item.TronPlayerItem;
import game.tron.model.TronPlayerModel;
import game.tron.panel.TronMainPanel;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.listener.ClosingMessageListener;

import common.MessageType;
import displayer.AbstractDisplayer;


/*
 *	Client part: Mview + V (utilisation du Datarepository et config pour la definition)
 *		gestion du display des differents objets
 *		gestion de la presentation
 *		gestion des commandes via forward au Server
 * */
@SuppressWarnings("serial")
public class TronGameFrame extends AbstractGameFrame 
{
	private static final String TRON_CONFIG_PATH = "resources/config/tron.cfg";
	private static final String BLUE_PLAYER_GRAPHICAL_CONFIGURATION = "blue_player_configuration";
	private static final String RED_PLAYER_GRAPHICAL_CONFIGURATION = "red_player_configuration";
	
	private TronMainPanel gamePanel = null;

	private TronPlayerModel bluePlayerModel = null;
	private TronPlayerModel redPlayerModel = null;
	
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
				targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + getLogin() + " " + TronGameServer.LEFT );
				targetWriter.flush();
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_RIGHT )
			{
				targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + getLogin() + " " + TronGameServer.RIGHT );
				targetWriter.flush();
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_UP )
			{
				targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + getLogin() + " " + TronGameServer.UP );
				targetWriter.flush();
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_DOWN )
			{
				targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + getLogin() + " " + TronGameServer.DOWN );
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
		super( writer, gameID, name, TRON_CONFIG_PATH );
		
		// characteristics of the frame
		this.setTitle( "Tron");
		this.setSize( TronMainPanel.frameWidth + 7, TronMainPanel.frameHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		this.addWindowListener( new ClosingMessageListener( targetWriter, MessageType.MessageSystem + " " + MessageType.MessageGameClose + " " + gameId ) );
		this.addKeyListener( movementKeyListener );
	
		// create the player's model 
		bluePlayerModel = new TronPlayerModel( bluePlayerName,
											   TronGameServer.BLUE_START_X, 
			 	  							   TronGameServer.BLUE_START_Y,
			 	  							   TronGameServer.BLUE_START_DIRECTION );
		redPlayerModel = new TronPlayerModel( redPlayerName,
											  TronGameServer.RED_START_X, 
											  TronGameServer.RED_START_Y,
											  TronGameServer.RED_START_DIRECTION );
		
		// create the main panel
		gamePanel = new TronMainPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		
		// create and associate the displayer
		gamePanel.addLayer( TronMainDisplayer.NAME, new TronMainDisplayer(0,64) );
		gamePanel.addLayer( TronMiniMapDisplayer.NAME, new TronMiniMapDisplayer(0,0) );
		gamePanel.addLayer( "BLUE" + TronPlayerPanelDisplayer.NAME, new TronPlayerPanelDisplayer(65,0) );
		gamePanel.addLayer( "RED" + TronPlayerPanelDisplayer.NAME, new TronPlayerPanelDisplayer(280,0) );
		
		// create the players and add them to their displayer / layer 
		gamePanel.createStartText(repository);
		TronPlayerItem blueItem = new TronPlayerItem( bluePlayerModel, 
													  repository.getData( BLUE_PLAYER_GRAPHICAL_CONFIGURATION ), 
													  tracker, 
													  ImageLevel.ENVIRONMENT_IMAGE.index() );
		TronPlayerItem redItem = new TronPlayerItem( redPlayerModel, 
													 repository.getData( RED_PLAYER_GRAPHICAL_CONFIGURATION ), 
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
		if ( getLogin().compareTo( bluePlayerName ) == 0 )
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
			targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameReady + " " + gameId + " " + getLogin() );
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

	public void end(String winner) 
	{
		if ( getLogin().compareTo( winner ) == 0 )
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
			bluePlayerModel.changePosition( Math.min( TronGameServer.gridSize - 1, Math.max( 0.0, bX ) ), 
											Math.min( TronGameServer.gridSize - 1, Math.max( 0.0, bY ) ) );
			bluePlayerModel.changeDirection( bD );
			bluePlayerModel.changePath( bP );
			
			redPlayerModel.changePosition( Math.min( TronGameServer.gridSize - 1, Math.max( 0.0, rX ) ), 
				  	   					   Math.min( TronGameServer.gridSize - 1, Math.max( 0.0, rY ) ) );
			redPlayerModel.changeDirection( rD );
			redPlayerModel.changePath( rP );
		}
	}
}
