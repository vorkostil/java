package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import main.ClientConnectionManager;

import common.MessageType;

/*
 * 	Server part: Mfonctionel + C
 *		gestion du mouvement
 *		gestion de la colision
 *		gestion des regles
 *		appel des IA si besion
 */
public class TronGame {
	
	public static final int NO_DIRECTION = 0;
	public static final int LEFT = -2; 
	public static final int RIGHT = 2; 
	public static final int UP = -1; 
	public static final int DOWN = 1; 
	public static final int gridSize = 16;
	
	public static final int BLUE_START_X = 3;
	public static final int BLUE_START_Y = 2;
	public static final int RED_START_X = 12;
	public static final int RED_START_Y = 13;
	public static final int BLUE_START_DIRECTION = RIGHT;
	public static final int RED_START_DIRECTION = LEFT;
	
	// number of cell each second
	private static double speed = 1.5;
	private static int tempo = 25; 
	
	// need to be close to 0.5 pixel depending from the tempo not from the padding
	// 0.03 s tempo, speed = 1.5 --> 0.045 / tempo
	private static double EPSILON = (2.2 * speed * tempo) / 1000;
	
	private ClientConnectionManager manager = null;

	private String id = null;
	
	private String bluePlayer = null;
	private String redPlayer = null;
	
	private double blueX = BLUE_START_X;
	private double blueY = BLUE_START_Y;
	private double redX = RED_START_X;
	private double redY = RED_START_Y;
	
	private List< Point2D > pathBlue = new ArrayList< Point2D >();
	private List< Point2D > pathRed = new ArrayList< Point2D >();
	
	// direction: 0 --> left, 1 --> right, 2 --> up, 3 --> down
	private int blueDirection = BLUE_START_DIRECTION;
	private int redDirection = RED_START_DIRECTION;
	
	private int blueTargetDirection = BLUE_START_DIRECTION;
	private int redTargetDirection = RED_START_DIRECTION;
	
	private boolean isBlueReady = false;
	private boolean isRedReady = false;
	
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
	
	public TronGame( String player1, String player2, String gameId, ClientConnectionManager connectionManager ) 
	{
		manager = connectionManager;
		id = gameId;
		
		bluePlayer = player1;
		redPlayer = player2;
		
		pathBlue.add( new Point2D.Double( blueX, blueY ) );
		pathRed.add( new Point2D.Double( redX, redY ) );
		
		manager.forwardToClient( bluePlayer, MessageType.MessageSystem + " " + MessageType.MessageGameOpen + " " + id + " " + bluePlayer + " " + redPlayer);
		manager.forwardToClient( redPlayer , MessageType.MessageSystem + " " + MessageType.MessageGameOpen + " " + id + " " + bluePlayer + " " + redPlayer);
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
		if ( blueDirection == LEFT )
		{
			blueX -= speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( blueDirection == RIGHT )
		{
			blueX += speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( blueDirection == UP )
		{
			blueY -= speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( blueDirection == DOWN )
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
		if ( redDirection == LEFT )
		{
			redX -= speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( redDirection == RIGHT )
		{
			redX += speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( redDirection == UP )
		{
			redY -= speed * elaspedTimeInMilliSecond / 1000;
		}
		else if ( redDirection == DOWN )
		{
			redY += speed * elaspedTimeInMilliSecond / 1000;
		}
		
		// forward the new position to the client
		manager.forwardToClient( bluePlayer, MessageType.MessageSystem + " " + MessageType.MessageGameUpdatePosition + " " + id + " " + createBlueInfo() + " " + createRedInfo() );
		manager.forwardToClient( redPlayer, MessageType.MessageSystem + " " + MessageType.MessageGameUpdatePosition + " " + id + " " + createBlueInfo() + " " + createRedInfo() );
		
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
			System.out.println( "Game " + id + " end, winner is " + winner );
			manager.forwardToClient( bluePlayer, MessageType.MessageSystem + " " + MessageType.MessageGameEnd + " " + id + " " + winner );
			manager.forwardToClient( redPlayer, MessageType.MessageSystem + " " + MessageType.MessageGameEnd + " " + id + " " + winner );
			manager.closeGame( id );
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

	public void setReady(String player) 
	{
		if ( player.compareTo( bluePlayer ) == 0 )
		{
			isBlueReady = true;
		}
		else if ( player.compareTo( redPlayer ) == 0 )
		{
			isRedReady = true;
		}
		
		manager.forwardToClient( bluePlayer, MessageType.MessageSystem + " " + MessageType.MessageGameReady + " " + id + " " + player );
		manager.forwardToClient( redPlayer , MessageType.MessageSystem + " " + MessageType.MessageGameReady + " " + id + " " + player );
		
		if (  ( isBlueReady == true )
			&&( isRedReady == true )  )
		{
			manager.forwardToClient( bluePlayer, MessageType.MessageSystem + " " + MessageType.MessageGameStartSoon + " " + id );
			manager.forwardToClient( redPlayer , MessageType.MessageSystem + " " + MessageType.MessageGameStartSoon + " " + id );
			
			javax.swing.Timer timer = new javax.swing.Timer( 3000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					manager.forwardToClient( bluePlayer, MessageType.MessageSystem + " " + MessageType.MessageGameStart + " " + id );
					manager.forwardToClient( redPlayer , MessageType.MessageSystem + " " + MessageType.MessageGameStart + " " + id );
					
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
	}

	public void changePlayerDirection(String player, String dir) 
	{
		if ( player.compareTo(bluePlayer) == 0 )
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

	public boolean containsPlayer(String login) 
	{
		return (  ( login.compareTo( redPlayer ) == 0)
				||( login.compareTo( bluePlayer ) == 0)  );
	}

	public String getId() 
	{
		return id;
	}
}
