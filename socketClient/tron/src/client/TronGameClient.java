package client;

import game.AbstractGameClientFrame;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;
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

import common.MessageType;
import common.TronCommonInformation;

import displayer.AbstractDisplayer;


/*
 *	Client part: Mview + V (utilisation du Datarepository et config pour la definition)
 *		gestion du display des differents objets
 *		gestion de la presentation
 *		gestion des commandes via forward au Server
 * */
@SuppressWarnings("serial")
public class TronGameClient extends AbstractGameClientFrame 
{
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
				connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageChangeDirection + " " + connectionClient.getLogin() + " " + leftDirection );
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_RIGHT )
			{
				connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageChangeDirection + " " + connectionClient.getLogin() + " " + rightDirection );
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_UP )
			{
				connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageChangeDirection + " " + connectionClient.getLogin() + " " + upDirection );
			}
			else if ( arg0.getKeyCode() == KeyEvent.VK_DOWN )
			{
				connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessageChangeDirection + " " + connectionClient.getLogin() + " " + downDirection );
			}
		}
	};
	
	public TronGameClient() throws IOException 
	{
		super( TronCommonInformation.TRON_CONFIG_PATH );
		
		// characteristics of the frame
		this.setTitle( "Tron");
		this.setSize( TronMainPanel.frameWidth + 7, TronMainPanel.frameHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		this.addKeyListener( movementKeyListener );
	

		// get the game and player information
		DataInformation gameInformation = repository.getData( TronCommonInformation.GAME_CONFIGURATION );
		
		// store information about the game
		gridSize = gameInformation.getIntegerValue( TronCommonInformation.GRID_SIZE );
		leftDirection = gameInformation.getIntegerValue( TronCommonInformation.LEFT_VALUE );
		rightDirection = gameInformation.getIntegerValue( TronCommonInformation.RIGHT_VALUE );
		upDirection = gameInformation.getIntegerValue( TronCommonInformation.UP_VALUE );
		downDirection = gameInformation.getIntegerValue( TronCommonInformation.DOWN_VALUE );
		
		// create the player's model 
		bluePlayerModel = null;
		redPlayerModel = null;
		
		// create the main panel
		gamePanel = new TronMainPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		
		// create and associate the displayer
		gamePanel.addDisplayer( TronMainDisplayer.NAME, new TronMainDisplayer(0,64) );
		gamePanel.addDisplayer( TronMiniMapDisplayer.NAME, new TronMiniMapDisplayer(0,0) );
		gamePanel.addDisplayer( "BLUE" + TronPlayerPanelDisplayer.NAME, new TronPlayerPanelDisplayer(65,0) );
		gamePanel.addDisplayer( "RED" + TronPlayerPanelDisplayer.NAME, new TronPlayerPanelDisplayer(280,0) );
		
		// create the players and add them to their displayer / layer 
		gamePanel.createStartText(repository);
		gamePanel.computeDisplayableItems();
	
		// create the panel structure on screen
		this.setLayout( new BorderLayout() );
		this.getContentPane().add( gamePanel, BorderLayout.CENTER );

		// display itself
		this.setVisible( true );
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

	public void startSoon( String bluePlayer, String redPlayer ) 
	{
		// manage the players
		addPlayer( bluePlayer );
		addPlayer( redPlayer );

		// launch the game init
		gamePanel.setStartSoon();
	}

	public void addPlayer(String playerName) 
	{
		if ( bluePlayerModel == null )
		{
			try 
			{
				DataInformation bluePlayerInformation = repository.getData( TronCommonInformation.BLUE_PLAYER_CONFIGURATION );
				bluePlayerModel = new TronPlayerModel( this,
													   playerName,
													   bluePlayerInformation.getIntegerValue( TronCommonInformation.START_X ), 
													   bluePlayerInformation.getIntegerValue( TronCommonInformation.START_Y ),
													   bluePlayerInformation.getIntegerValue( TronCommonInformation.START_DIR ) );
				TronPlayerItem blueItem = new TronPlayerItem( bluePlayerModel, 
															  bluePlayerInformation, 
															  tracker, 
															  ImageLevel.ENVIRONMENT_IMAGE.index() );
				gamePanel.addItem( blueItem, 
								   TronMainDisplayer.NAME, 
								   AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW );  
				gamePanel.addItem( blueItem, 
						           TronMiniMapDisplayer.NAME, 
								   AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW );  
				gamePanel.addItem( blueItem, 
						           "BLUE" + TronPlayerPanelDisplayer.NAME, 
								   AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW );

				gamePanel.computeDisplayableItems();
				
				// ask for readiness
				if ( connectionClient.getLogin().compareTo( playerName ) == 0 )
				{
					if ( JOptionPane.showConfirmDialog( this, 
														"You play as blue\nare you ready ?", 
														"Game launch", 
														JOptionPane.YES_NO_OPTION, 
														JOptionPane.QUESTION_MESSAGE ) == JOptionPane.OK_OPTION)
					{
						readyToPlay();
					}
					else
					{
						closeGame();
					}
				}
			} 
			catch (IOException e) 
			{
				connectionClient.forwardAlert( "Something bad happens during blue player creation: " + e.getMessage() );
			}
		}
		else if ( redPlayerModel == null )
		{
			try 
			{
				DataInformation redPlayerInformation = repository.getData( TronCommonInformation.RED_PLAYER_CONFIGURATION );
				redPlayerModel = new TronPlayerModel( this,
													  playerName,
												   	  redPlayerInformation.getIntegerValue( TronCommonInformation.START_X ), 
												   	  redPlayerInformation.getIntegerValue( TronCommonInformation.START_Y ),
												   	  redPlayerInformation.getIntegerValue( TronCommonInformation.START_DIR ) );
				TronPlayerItem redItem = new TronPlayerItem( redPlayerModel, 
															 redPlayerInformation, 
															 tracker, 
															 ImageLevel.ENVIRONMENT_IMAGE.index() );
				gamePanel.addItem( redItem,
				   		   		   TronMainDisplayer.NAME,
				   		   		   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
				gamePanel.addItem( redItem,
								   TronMiniMapDisplayer.NAME,
						   		   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
				gamePanel.addItem( redItem,
								   "RED" + TronPlayerPanelDisplayer.NAME,
						   		   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
				
				gamePanel.computeDisplayableItems();
				
				// ask for readiness
				if ( connectionClient.getLogin().compareTo( playerName ) == 0 )
				{
					if ( JOptionPane.showConfirmDialog( this, 
														"You play as red\nare you ready ?", 
														"Game launch", 
														JOptionPane.YES_NO_OPTION, 
														JOptionPane.QUESTION_MESSAGE ) == JOptionPane.OK_OPTION)
					{
						readyToPlay();
					}
					else
					{
						closeGame();
					}
				}
			} 
			catch (IOException e) 
			{
				connectionClient.forwardAlert( "Something bad happens during blue player creation: " + e.getMessage() );
			}
		}
		else
		{
			connectionClient.forwardAlert( "Too many players added, player " + playerName + " is dropped." );
		}
	}

	@Override
	public void handleServerMessage(String message) 
	{
		String[] messageComponents = message.split( " " );
		String action = messageComponents[ 0 ];
		
		if ( action.compareTo( MessageType.MessageStart ) == 0 )
		{
			start();
		}
		else if ( action.compareTo( MessageType.MessageStartSoon ) == 0 )
		{
			startSoon( messageComponents[ 1 ], messageComponents[ 2 ] );
		}
		else if ( action.compareTo( MessageType.MessageEnd ) == 0 )
		{
			end( messageComponents[ 1 ] );
		}
		else if ( action.compareTo( MessageType.MessageUpdatePosition ) == 0 )
		{
			double bX 	= java.lang.Double.parseDouble( messageComponents[ 1 ] );
			double bY 	= java.lang.Double.parseDouble( messageComponents[ 2 ] );
			int bD 		= Integer.parseInt( messageComponents[ 3 ] );
			int bS 		= Integer.parseInt( messageComponents[ 4 ] );
			List< Point2D > bP = new ArrayList< Point2D >();  
			for ( int i = 0; i < bS; i++ )
			{
				bP.add( new Point2D.Double( java.lang.Double.parseDouble( messageComponents[ 5 + i * 2 ] ),
											java.lang.Double.parseDouble( messageComponents[ 6 + i * 2 ] ) ) );
			}
			
			double rX 	= java.lang.Double.parseDouble( messageComponents[ 5 + bS * 2 ] );
			double rY 	= java.lang.Double.parseDouble( messageComponents[ 6 + bS * 2 ] );
			int rD 		= Integer.parseInt( messageComponents[ 7 + bS * 2 ] );
			int rS 		= Integer.parseInt( messageComponents[ 8 + bS * 2 ] );
			List< Point2D > rP = new ArrayList< Point2D >();  
			for ( int i = 0; i < rS; i++ )
			{
				rP.add( new Point2D.Double( java.lang.Double.parseDouble( messageComponents[ 9 + bS * 2 + i * 2] ),
											java.lang.Double.parseDouble( messageComponents[ 10 + bS * 2 + i * 2] ) ) );
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
