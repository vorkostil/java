package server;

import game.AbstractGameProvider;
import game.GameProvider;
import helper.DataRepository;
import helper.DataRepository.DataInformation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import common.MessageType;
import common.TronCommonInformation;

/*
 * 	Server part: Mfonctionel + C
 *		gestion du mouvement
 *		gestion de la colision
 *		gestion des regles
 *		appel des IA si besion
 */
public class TronGameServer extends AbstractGameProvider
{
	// number of cell each second
	private static double speed = 1.5;
	private static int tempo = 25; 
	
	// need to be close to 0.5 pixel depending from the tempo not from the padding
	// 0.03 s tempo, speed = 1.5 --> 0.045 / tempo
	private static double EPSILON = (2.2 * speed * tempo) / 1000;
	
	// game information
	private int gridSize;
	private int leftDirection;
	private int rightDirection;
	private int upDirection;
	private int downDirection;
	
	// player information
	private String bluePlayer = null;
	private String redPlayer = null;
	
	private double blueX;
	private double blueY;
	private double redX;
	private double redY;
	
	private List< Point2D > pathBlue = new ArrayList< Point2D >();
	private List< Point2D > pathRed = new ArrayList< Point2D >();
	
	// direction: 0 --> left, 1 --> right, 2 --> up, 3 --> down
	private int blueDirection;
	private int redDirection;
	
	private int blueTargetDirection;
	private int redTargetDirection;
	
	class MainTask extends TimerTask
	{
        private long startTime = 0;
 
        @Override
        public void run() {
            long currentTime = System.nanoTime();
            long elaspedTime = currentTime  - startTime; // calcul du temps écoulé
            startTime = currentTime ; // on réinitialise le compteur
 
            computeMovement( TimeUnit.MILLISECONDS.convert(elaspedTime, TimeUnit.NANOSECONDS) );
        }
        
        public void init()
        {
        	startTime = System.nanoTime();
        }
	}
	
	MainTask mainTask = new MainTask();
	Timer mainTimer = new Timer();
	private boolean blueIsReady = false;
	private boolean redIsReady = false;
	
	public TronGameServer( String gameId, 
						   GameProvider gameProvider ) 
	{
		super( gameId, gameProvider );

		// get the repository
		DataRepository repository = new DataRepository();
		repository.addFromFile( TronCommonInformation.TRON_CONFIG_PATH );
		
		// get the game and player shared information
		DataInformation gameInformation = repository.getData( TronCommonInformation.GAME_CONFIGURATION );
		DataInformation bluePlayerInformation = repository.getData( TronCommonInformation.BLUE_PLAYER_CONFIGURATION );
		DataInformation redPlayerInformation = repository.getData( TronCommonInformation.RED_PLAYER_CONFIGURATION );

		// player's information
		blueX = bluePlayerInformation.getIntegerValue( TronCommonInformation.START_X );
		blueY = bluePlayerInformation.getIntegerValue( TronCommonInformation.START_Y );
		redX = redPlayerInformation.getIntegerValue( TronCommonInformation.START_X );
		redY = redPlayerInformation.getIntegerValue( TronCommonInformation.START_Y );
		blueDirection = bluePlayerInformation.getIntegerValue( TronCommonInformation.START_DIR );
		redDirection = redPlayerInformation.getIntegerValue( TronCommonInformation.START_DIR );
		blueTargetDirection = blueDirection;
		redTargetDirection = redDirection;

		// game's information
		gridSize = gameInformation.getIntegerValue( TronCommonInformation.GRID_SIZE );
		leftDirection = gameInformation.getIntegerValue( TronCommonInformation.LEFT_VALUE );
		rightDirection = gameInformation.getIntegerValue( TronCommonInformation.RIGHT_VALUE );
		upDirection = gameInformation.getIntegerValue( TronCommonInformation.UP_VALUE );
		downDirection = gameInformation.getIntegerValue( TronCommonInformation.DOWN_VALUE );
		
		// create the player
		bluePlayer = null;
		redPlayer = null;
		
		// and the player's path
		pathBlue.add( new Point2D.Double( blueX, blueY ) );
		pathRed.add( new Point2D.Double( redX, redY ) );
	}

	protected void computeMovement( long elaspedTimeInMilliSecond ) 
	{
		// BLUE player 
		// check if there is a need to change direction
		if (  ( blueTargetDirection != blueDirection )
			&&( blueTargetDirection != -blueDirection )  )
		{
			// check if on a cell's grid to change direction
			double blueXint = Math.rint( blueX );
			double blueYint = Math.rint( blueY );
			if (  ( Math.abs( blueXint - blueX ) < EPSILON )
				&&( Math.abs( blueYint - blueY ) < EPSILON )  )
			{
				blueDirection = blueTargetDirection;
				
				// map to the grid
				blueX = blueXint;
				blueY = blueYint;
				
				pathBlue.add( new Point2D.Double( blueX, blueY ) );
			}
		}
		// compute new position
		if ( blueDirection == leftDirection )
		{
			blueX -= speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( blueDirection == rightDirection )
		{
			blueX += speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( blueDirection == upDirection )
		{
			blueY -= speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( blueDirection == downDirection )
		{
			blueY += speed * elaspedTimeInMilliSecond / 1000;
		}
		
		// RED player 
		// check if there is a need to change direction
		if (  ( redTargetDirection != redDirection )
			&&( redTargetDirection != -redDirection )  )
		{
			// check if on a cell's grid to change direction
			double redXint = Math.rint( redX );
			double redYint = Math.rint( redY );
			if (  ( Math.abs( redXint - redX ) < EPSILON )
				&&( Math.abs( redYint - redY ) < EPSILON )  )
			{
				redDirection = redTargetDirection;
				
				// map to the grid
				redX = redXint;
				redY = redYint;
				
				pathRed.add( new Point2D.Double( redX, redY ) );
			}
		}
		// compute new position
		if ( redDirection == leftDirection )
		{
			redX -= speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( redDirection == rightDirection )
		{
			redX += speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( redDirection == upDirection )
		{
			redY -= speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( redDirection == downDirection )
		{
			redY += speed * elaspedTimeInMilliSecond / 1000;
		}
		
		// forward the new position to the client
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + id + " " + MessageType.MessageUpdatePosition + " " + createBlueInfo() + " " + createRedInfo() );

		// check for the and of the game
		checkEndGame();
	}

	private String createBlueInfo() 
	{
		String result = new String();
		result += blueX + " " + blueY + " " + blueDirection + " " + pathBlue.size();
		for ( Point2D point : pathBlue )
		{
			result += " " + point.getX() + " " + point.getY();
		}
		return result;
	}

	private String createRedInfo() 
	{
		String result = new String();
		result += redX + " " + redY + " " + redDirection + " " + pathRed.size();
		for ( Point2D point : pathRed )
		{
			result += " " + point.getX() + " " + point.getY();
		}
		return result;
	}

	public void checkEndGame()
	{
		String winner = "";
		// check if blue player loose from grid
		if (  ( blueX < -EPSILON )
			||( blueX > gridSize - 1 + EPSILON ) 
			||( blueY < -EPSILON)
			||( blueY > gridSize - 1 + EPSILON )  )
		{
			winner = redPlayer;
		}
		
		// check if red player loose from grid
		if (  ( redX < -EPSILON )
			||( redX > gridSize - 1 + EPSILON ) 
			||( redY < -EPSILON )
			||( redY > gridSize - 1 + EPSILON )  )
		{
			winner = bluePlayer;
		}

		// check if blue player loose from path
		boolean touched = false;
		if ( pathBlue.size() > 1 )
		{		
			// can it it's own path on the if there is at least one turn
			for ( int i = 0; i < pathBlue.size() - 1; ++i )
			{
				if ( computeTouched( pathBlue.get( i ), pathBlue.get( i + 1 ), new Point2D.Double( blueX, blueY ) ) == true )
				{
					winner = redPlayer;
					touched = true;
					break;
				}
			}
		}
		if ( touched == false )
		{
			for ( int i = 0; i < pathRed.size() - 1; ++i )
			{
				if ( computeTouched( pathRed.get( i ), pathRed.get( i + 1 ), new Point2D.Double( blueX, blueY ) ) == true )
				{
					winner = redPlayer;
					touched = true;
					break;
				}
			}
			if ( touched == false )
			{
				if ( computeTouched( pathRed.get( pathRed.size() - 1 ), new Point2D.Double( redX, redY ), new Point2D.Double( blueX, blueY ) ) == true )
				{
					winner = redPlayer;
					touched = true;
				}
			}
		}

		// check if red player loose from path
		if (  ( pathRed.size() > 1 )
			&&( touched == false )  )
		{		
			// can it it's own path on the if there is at least one turn
			for ( int i = 0; i < pathRed.size() - 1; ++i )
			{
				if ( computeTouched( pathRed.get( i ), pathRed.get( i + 1 ), new Point2D.Double( redX, redY ) ) == true )
				{
					winner = bluePlayer;
					touched = true;
					break;
				}
			}
		}
		if ( touched == false )
		{
			for ( int i = 0; i < pathBlue.size() - 1; ++i )
			{
				if ( computeTouched( pathBlue.get( i ), pathBlue.get( i + 1 ), new Point2D.Double( redX, redY ) ) == true )
				{
					winner = bluePlayer;
					touched = true;
					break;
				}
			}
			if ( touched == false )
			{
				if ( computeTouched( pathBlue.get( pathBlue.size() - 1 ), new Point2D.Double( blueX, blueY ), new Point2D.Double( redX, redY ) ) == true )
				{
					winner = bluePlayer;
					touched = true;
				}
			}
		}
		
		// send the end game signal
		if ( winner.length() > 0 )
		{
			stop();
			connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + id + " " + MessageType.MessageEnd + " " + winner );
			gameProvider.closeGame( id );
		}
	}
	
	// return true if p.x == p1.X && ( p1.y < p.y < p2.y ) || p.y == p1.y && ( p1.x < p.x < p2.x ) 
	private boolean computeTouched(Point2D p1, Point2D p2, Point2D p) 
	{
		// touched on X
		if (  ( p1.getX() == p2.getX() )
			&&( Math.abs( p1.getX() - p.getX() ) < EPSILON )  )
		{
			double minY = Math.min( p1.getY(), p2.getY() );
			double maxY = Math.max( p1.getY(), p2.getY() );
			if (  ( p.getY() > minY )
				&&( p.getY() < maxY )  )
			{
				return true;
			}
		}
		// touched on Y
		else if (  ( p1.getY() == p2.getY() )
				 &&( Math.abs( p1.getY() - p.getY() ) < EPSILON )  )
		{
			double minX = Math.min( p1.getX(), p2.getX() );
			double maxX = Math.max( p1.getX(), p2.getX() );
			if (  ( p.getX() > minX )
				&&( p.getX() < maxX )  )
			{
				return true;
			}
		}
		return false;
	}

	public void changePlayerDirection(String player, String dir) 
	{
		if ( player.compareTo( bluePlayer ) == 0 )
		{
			blueTargetDirection = Integer.parseInt( dir );
		}
		else
		{
			redTargetDirection = Integer.parseInt( dir );
		}
	}

	public void stop()
	{
		mainTimer.cancel();
		mainTimer = null;
	}

	protected void callGameStart() 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + id + " " + MessageType.MessageStartSoon + " " + bluePlayer + " " + redPlayer );
		
		javax.swing.Timer timer = new javax.swing.Timer( 3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + id + " " + MessageType.MessageStart );
				
				if (  ( mainTimer != null )
					&&( mainTask != null )  )
				{
					mainTask.init();
					mainTimer.schedule( mainTask, tempo, tempo);
				}
			}
		});
		timer.setRepeats( false );
		timer.start();
	}

	@Override
	public void handleMessage( String action, String remain ) 
	{
		if ( action.compareTo( MessageType.MessageReady ) == 0 )
		{
			playerIsReady( remain );
		}
		else if ( action.compareTo( MessageType.MessageChangeDirection ) == 0 )
		{
			String[] parts = remain.split( " " );
			changePlayerDirection( parts[ 0 ], parts[ 1 ] );
		}
	}

	private void playerIsReady(String playerName) 
	{
		if ( playerName.compareTo(bluePlayer) == 0)
		{
			blueIsReady = true;
		}
		else if (playerName.compareTo(redPlayer) == 0 )
		{
			redIsReady = true;
		}
		
		if (  ( blueIsReady == true )
			&&( redIsReady == true ) )
		{
			// send the game start message
			callGameStart();
		}
	}

	@Override
	public void playerJoinGame(String playerName) 
	{
		if ( bluePlayer == null )
		{
			// set the player as blue
			bluePlayer = playerName;
			
			// and send the join acceptation message
			connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + id + " " + MessageType.MessagePlayerJoinAccepted + " " + playerName + " blue" );
		}
		else if ( redPlayer == null )
		{
			// set the player as red
			redPlayer = playerName;
			
			// and send the join acceptation message
			connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + id + " " + MessageType.MessagePlayerJoinAccepted + " " + playerName + " red" );
		}
		
		// send the player list to everyone
		String message = new String( MessageType.MessageGame + " " + id + " " + MessageType.MessagePlayerListUpdate );
		if ( bluePlayer != null )
		{
			message += " " + bluePlayer + " blue";
		}
		if ( redPlayer != null )
		{
			message += " " + redPlayer + " red";
		}
		connectionClient.sendMessageIfConnected( message );
	}

	@Override
	public void playerLeaveGame(String playerName) 
	{
		stop();
		if ( playerName.compareTo( bluePlayer ) == 0 )
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + id + " " + MessageType.MessageEnd + " " + redPlayer );
		}
		else
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + id + " " + MessageType.MessageEnd + " " + bluePlayer );
		}
		gameProvider.closeGame( id );
	}
}
