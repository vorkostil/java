package frame;

import game.TronGame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	private static Color redColor = new Color( 255, 0, 0 );
	private static Color blueColor = new Color( 0, 0, 255 );
	private static Color redInsideColor = new Color( 192, 96, 96 );
	private static Color blueInsideColor = new Color( 96, 96, 192);
	
	private static int gridPadding = 32;
	private static int gridWidth = (TronGame.gridSize - 1) * gridPadding; // we move on the grid, not on the cell
	private static int framePadding = 7;
	
	private static int playerSize = 10;
	private static int paddingPlayer = framePadding - 5;
	
	public static int frameSize = gridWidth + framePadding * 2;

	private String backgroundFileName = "resources/pictures/background-495.jpg";
	private BufferedImage backgroundImage = null;
	
	private double blueX = 3;
	private double blueY = 2;
	private double redX = 12;
	private double redY = 13;
	
	MediaTracker tracker = new MediaTracker( new Canvas() );
	private int viewMaxWidth = 0;
	private int viewMaxHeight = 0;

	Image image;
	Graphics buffer;

	class RenderingThread extends Thread {
		 public void run() {
			 while(true) {
				 try {
					 repaint(); 
					 sleep( 16 );
				 } 
				 catch ( Exception e ) {
					 System.out.println("Exception catch in the rendering thread: " + e);
				 } 
			 }
		 }
	 }
	 
	RenderingThread renderingThread = new RenderingThread();
	private List<Point2D> bluePath = null;
	private List<Point2D> redPath = null;
	
	public enum ImageLevel {
		DEFAULT_SCREEN (0),
		PREVIEW_IMAGE(1),
		ENVIRONMENT_IMAGE(2),
		QUESTION_IMAGE(3);
		
		ImageLevel(int index) { index_ = index; }
		
		private int index_;
		
		public int index() {
			return index_;
		}
	}
	
	public GamePanel()
	{
		try 
		{
			backgroundImage = ImageIO.read( new File( backgroundFileName ) );
			if (backgroundImage != null)
				tracker.addImage(backgroundImage, ImageLevel.ENVIRONMENT_IMAGE.index());
			tracker.waitForID(ImageLevel.ENVIRONMENT_IMAGE.index());
			
			viewMaxWidth  = backgroundImage.getWidth(null);
			viewMaxHeight = backgroundImage.getHeight(null);
			
			// and start the rendering thread
			renderingThread.start();
		} 
		catch (IOException e) 
		{
			System.out.println( "Could not find the image: " + backgroundFileName );
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		// create double buffering components
		if (buffer == null 
			|| g.getClipBounds().width != image.getWidth(null) 
			|| g.getClipBounds().height != image.getHeight(null)) 
		{
			image = createImage(g.getClipBounds().width, g.getClipBounds().height);
			buffer = image.getGraphics();
		}
		
		// father call
		super.paintComponent(g) ;
		
		// backward drawing of the screens as the oldest is the first screen
		drawBackGround( buffer );
		drawForeGround( buffer );
		
		// double buffering display
		g.drawImage(image,0,0,this);
		
	}  

	private void drawForeGround(Graphics g) 
	{
		// draw the paths
		if ( redPath != null )
		{
			g.setColor( redColor );
			for ( int i = 0; i < redPath.size() - 1; i++ )
			{
				boolean isVertical = redPath.get( i ).getX() == redPath.get( i + 1 ).getX();
				if ( isVertical )
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (redPath.get( i ).getX() * gridPadding + framePadding) + d, 
									(int) (redPath.get( i ).getY() * gridPadding + framePadding), 
									(int) (redPath.get( i + 1 ).getX() * gridPadding + framePadding + d), 
									(int) (redPath.get( i + 1 ).getY() * gridPadding + framePadding) );
					}
				}
				else
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (redPath.get( i ).getX() * gridPadding + framePadding), 
									(int) (redPath.get( i ).getY() * gridPadding + framePadding) + d, 
									(int) (redPath.get( i + 1 ).getX() * gridPadding + framePadding), 
									(int) (redPath.get( i + 1 ).getY() * gridPadding + framePadding + d) );
					}
				}
			}
			boolean isVertical = redPath.get( redPath.size() - 1 ).getX() == redX;
			if ( isVertical )
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (redPath.get( redPath.size() - 1 ).getX() * gridPadding + framePadding) + d, 
								(int) (redPath.get( redPath.size() - 1 ).getY() * gridPadding + framePadding), 
								(int) (redX * gridPadding + framePadding) + d, 
								(int) (redY * gridPadding + framePadding) );
				}
			}
			else
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (redPath.get( redPath.size() - 1 ).getX() * gridPadding + framePadding), 
								(int) (redPath.get( redPath.size() - 1 ).getY() * gridPadding + framePadding) + d, 
								(int) (redX * gridPadding + framePadding), 
								(int) (redY * gridPadding + framePadding) + d );
				}
			}
		}
		
		if ( bluePath != null )
		{
			g.setColor( blueColor );
			for ( int i = 0; i < bluePath.size() - 1; i++ )
			{
				boolean isVertical = bluePath.get( i ).getX() == bluePath.get( i + 1 ).getX();
				if ( isVertical )
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (bluePath.get( i ).getX() * gridPadding + framePadding) + d, 
									(int) (bluePath.get( i ).getY() * gridPadding + framePadding), 
									(int) (bluePath.get( i + 1 ).getX() * gridPadding + framePadding) + d, 
									(int) (bluePath.get( i + 1 ).getY() * gridPadding + framePadding) );
					}
				}
				else
				{
					for ( int d = -1; d < 2; d++ )
					{
						g.drawLine( (int) (bluePath.get( i ).getX() * gridPadding + framePadding), 
									(int) (bluePath.get( i ).getY() * gridPadding + framePadding) + d, 
									(int) (bluePath.get( i + 1 ).getX() * gridPadding + framePadding), 
									(int) (bluePath.get( i + 1 ).getY() * gridPadding + framePadding) + d );
					}
				}
			}
			boolean isVertical = bluePath.get( bluePath.size() - 1 ).getX() == blueX;
			if ( isVertical )
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (bluePath.get( bluePath.size() - 1 ).getX() * gridPadding + framePadding) + d, 
								(int) (bluePath.get( bluePath.size() - 1 ).getY() * gridPadding + framePadding), 
								(int) (blueX * gridPadding + framePadding) + d, 
								(int) (blueY * gridPadding + framePadding) );
				}
			}
			else
			{
				for ( int d = -1; d < 2; d++ )
				{
					g.drawLine( (int) (bluePath.get( bluePath.size() - 1 ).getX() * gridPadding + framePadding), 
								(int) (bluePath.get( bluePath.size() - 1 ).getY() * gridPadding + framePadding) + d, 
								(int) (blueX * gridPadding + framePadding), 
								(int) (blueY * gridPadding + framePadding) + d );
				}
			}
		}
		
		// draw the player
		g.setColor( redInsideColor );
		g.fillOval( (int) (redX * gridPadding) + paddingPlayer, (int) (redY * gridPadding) + paddingPlayer, playerSize, playerSize );
		g.setColor( redColor );
		g.drawOval( (int) (redX * gridPadding) + paddingPlayer, (int) (redY * gridPadding) + paddingPlayer, playerSize, playerSize );

		g.setColor( blueInsideColor );
		g.fillOval( (int) (blueX * gridPadding) + paddingPlayer, (int) (blueY * gridPadding) + paddingPlayer, playerSize, playerSize );
		g.setColor( blueColor );
		g.drawOval( (int) (blueX * gridPadding) + paddingPlayer, (int) (blueY * gridPadding) + paddingPlayer, playerSize, playerSize );
	}

	private void drawBackGround( Graphics g )
	{
		if ( backgroundImage != null )
			g.drawImage( backgroundImage, 0, 0, viewMaxWidth, viewMaxHeight, 0, 0, viewMaxWidth, viewMaxHeight, null);
	}
	
	public void changeBluePlayerCoordinate( double x, double y )
	{
		blueX = Math.min( TronGame.gridSize - 1, Math.max( 0.0, x ) );
		blueY = Math.min( TronGame.gridSize - 1, Math.max( 0.0, y ) );;
	}

	public void changeRedPlayerCoordinate(double x, double y) 
	{
		redX = Math.min( TronGame.gridSize - 1, Math.max( 0.0, x ) );
		redY = Math.min( TronGame.gridSize - 1, Math.max( 0.0, y ) );;
	}

	public void updateBluePlayerPath(List<Point2D> bp) 
	{
		bluePath = bp;
	}

	public void updateRedPlayerPath(List<Point2D> rp) 
	{
		redPath = rp;
	}
}
