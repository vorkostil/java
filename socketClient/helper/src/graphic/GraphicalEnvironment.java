package graphic;

import helper.DataRepository.DataInformation;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/* This class described an environment to display, it means a list of graphical element
 * those element can be mobile or not
 * those element can be animated or not
 * all of this depends from the final item description
 * */
@SuppressWarnings("serial")
public abstract class GraphicalEnvironment extends JPanel
{
	public final static int TEMPO_60_HZ = 16;
	public final static int TEMPO_100_HZ = 10;
	public final static int TEMPO_25_HZ = 40;
	
	public final static String BACKGROUND_IMAGE_PATH = "background_image_path";
	public enum ImageLevel 
	{
		DEFAULT_SCREEN (0),
		PREVIEW_IMAGE(1),
		ENVIRONMENT_IMAGE(2),
		QUESTION_IMAGE(3);
		
		ImageLevel(int index) 
		{ 
			this.index = index;
		}
		
		private int index;
		
		public int index() 
		{
			return this.index;
		}
	}
	
	// used for double buffering drawing
	Image image = null;
	Graphics buffer = null;

	private MediaTracker tracker = null;
	private int viewMaxWidth = 0;
	private int viewMaxHeight = 0;

	// used for the rendering of the environment
	private int tempo = -1; // 60Hz
	class RenderingThread extends Thread 
	{
		 public void run() 
		 {
			 while(true) 
			 {
				 try 
				 {
					 repaint(); 
					 sleep( tempo );
				 } 
				 catch ( Exception e ) 
				 {
					 System.out.println("Exception catch in the rendering thread of GraphicalEnvironment: " + e);
				 } 
			 }
		 }
	 }
	 
	RenderingThread renderingThread = new RenderingThread();
	
	// first, consider 5 levels of layer
	public final static int FIRST_LAYER_LEVEL_TO_DRAW = 0;
	public final static int LAST_LAYER_LEVEL_TO_DRAW = 4;
	
	Image backgroundImage = null;
	List< List< GraphicalItem > > graphicalItemLayers = new ArrayList< List< GraphicalItem > >();
	List< GraphicalItem > graphicalItems = new ArrayList< GraphicalItem >();

	public GraphicalEnvironment( DataInformation datas, MediaTracker tracker, int tempo ) 
	{
		this.tempo = tempo;
		this.tracker = tracker;
		
		for ( int level = FIRST_LAYER_LEVEL_TO_DRAW;
			  level < LAST_LAYER_LEVEL_TO_DRAW + 1; 
			  level++ )
		{
			graphicalItemLayers.add( new ArrayList< GraphicalItem >() );			
		}
		
		String backgroundFileName = datas.getStringValue( BACKGROUND_IMAGE_PATH );
		try 
		{
			File imageFile = new File( backgroundFileName );
			System.out.println(imageFile.getAbsolutePath());
			backgroundImage = ImageIO.read( imageFile );
			if ( backgroundImage != null )
			{
				tracker.addImage(backgroundImage, ImageLevel.ENVIRONMENT_IMAGE.index());
				tracker.waitForID(ImageLevel.ENVIRONMENT_IMAGE.index());
			
				viewMaxWidth  = backgroundImage.getWidth(null);
				viewMaxHeight = backgroundImage.getHeight(null);
			}
			
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

	public synchronized boolean addItem( GraphicalItem item, int layerLevel )
	{
		if (  ( layerLevel >= FIRST_LAYER_LEVEL_TO_DRAW )
			&&( layerLevel < LAST_LAYER_LEVEL_TO_DRAW + 1 )  )
		{
			graphicalItemLayers.get( layerLevel ).add( item );
			graphicalItems.add( item );
		}
		return false;
	}
	
	public MediaTracker getTracker()
	{
		return tracker;
	}
	
	private void processVisibleElement() 
	{
		// process event for animated item
		long time = System.currentTimeMillis();
		for ( GraphicalItem item : graphicalItems ) 
		{
			if ( item.isVisible() == true )
			{
				item.process(time);
			}
		}
	}

	protected synchronized void drawLayers(Graphics g, int x, int y, int width, int height) 
	{
		// draw the images
		for ( int level = FIRST_LAYER_LEVEL_TO_DRAW;
				  level < LAST_LAYER_LEVEL_TO_DRAW + 1; 
				  level++ )
		{
			for ( GraphicalItem item : graphicalItemLayers.get( level ) ) 
			{
				item.draw( g, x, y, width, height );
			}
		}
	}

	protected void drawBackGround( Graphics g )
	{
		if ( backgroundImage != null )
		{
			g.drawImage( backgroundImage, 0, 0, viewMaxWidth, viewMaxHeight, 0, 0, viewMaxWidth, viewMaxHeight, null);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		// create double buffering components
		if (  ( buffer == null )
			||( g.getClipBounds().width != image.getWidth( null ) ) 
			||( g.getClipBounds().height != image.getHeight( null ) )  ) 
		{
			image = createImage( g.getClipBounds().width, g.getClipBounds().height );
			buffer = image.getGraphics();
		}
		
		// father call
		super.paintComponent(g) ;
		
		// backward drawing of the components, from brackground to foreground
		processVisibleElement();
		drawBackGround( buffer );
		drawLayers( buffer, 0, 0, viewMaxWidth, viewMaxHeight );
		
		// double buffering display
		g.drawImage( image, 0, 0, this);
		
	}  
}
