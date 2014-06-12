package server;

import game.AbstractGameServer;
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

import network.ConnectionServer;

import common.MessageType;

/*
 * 	Server part: Mfonctionel + C
 *		gestion du mouvement
 *		gestion de la colision
 *		gestion des regles
 *		appel des IA si besion
 */
public class TronGameServer extends AbstractGameServer
{
	public static final String NAME = "Tron";
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
	
	public TronGameServer( String player1, 
						   String player2, 
						   String gameId, 
						   ConnectionServer connectionServer ) 
	{
		super( gameId, connectionServer );

		// get the repository
		DataRepository repository = new DataRepository();
		repository.addFromFile( TRON_CONFIG_PATH );
		
		// get the game and player shared information
		DataInformation gameInformation = repository.getData( GAME_CONFIGURATION );
		DataInformation bluePlayerInformation = repository.getData( BLUE_PLAYER_CONFIGURATION );
		DataInformation redPlayerInformation = repository.getData( RED_PLAYER_CONFIGURATION );

		// player's information
		blueX = bluePlayerInformation.getIntegerValue( START_X );
		blueY = bluePlayerInformation.getIntegerValue( START_Y );
		redX = redPlayerInformation.getIntegerValue( START_X );
		redY = redPlayerInformation.getIntegerValue( START_Y );
		blueDirection = bluePlayerInformation.getIntegerValue( START_DIR );
		redDirection = redPlayerInformation.getIntegerValue( START_DIR );
		blueTargetDirection = blueDirection;
		redTargetDirection = redDirection;

		// game's information
		gridSize = gameInformation.getIntegerValue( GRID_SIZE );
		leftDirection = gameInformation.getIntegerValue( LEFT_VALUE );
		rightDirection = gameInformation.getIntegerValue( RIGHT_VALUE );
		upDirection = gameInformation.getIntegerValue( UP_VALUE );
		downDirection = gameInformation.getIntegerValue( DOWN_VALUE );
		
		// create the player
		addPlayer( bluePlayer = player1 );
		addPlayer( redPlayer = player2 );
		
		// and the player's path
		pathBlue.add( new Point2D.Double( blueX, blueY ) );
		pathRed.add( new Point2D.Double( redX, redY ) );
		
		// and open the game
		sendOpenMessageToPlayers();
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
		forwardMessageToAllPlayer( MessageType.MessageSystem + " " + MessageType.MessageGameUpdatePosition + " " + id + " " + createBlueInfo() + " " + createRedInfo() );

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
			sendGameEndMessage(winner);
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

	@Override
	public void stop()
	{
		mainTimer.cancel();
		mainTimer = null;
	}

	@Override
	protected void callGameStart() 
	{
		javax.swing.Timer timer = new javax.swing.Timer( 3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendGameStartMessage();
				
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
	public void manageSpecificMessage(String command) 
	{
		String[] splitted = command.split( " " );
		String action = splitted[2]; 
		
		if ( action.compareTo( MessageType.MessageChangeDirection ) == 0 )
		{
			changePlayerDirection( splitted[ 4 ], splitted[ 5 ] );
		}
	}

	@Override
	protected String getName() 
	{
		return NAME;
	}
}
