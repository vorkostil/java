package graphic;

import helper.DataRepository.DataInformation;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import displayer.AbstractDisplayer;


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
	public int viewMaxWidth = 0;
	public int viewMaxHeight = 0;

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
					 System.err.println("Exception catch in the rendering thread of GraphicalEnvironment: " + e);
				 } 
			 }
		 }
	 }
	 
	RenderingThread renderingThread = new RenderingThread();
	
	// first, consider 5 levels of layer
	public final static int FIRST_LAYER_LEVEL_TO_DRAW = 0;
	public final static int LAST_LAYER_LEVEL_TO_DRAW = 4;

	// the image to display in the background as first image
	// assuming that his image is not modifiable
	// for a moving background, create a specific displayer
	Image backgroundImage = null;
	
	// the list of displayer, each will be called when rendering the scene
	// no overlapping displayer or beware the side effects
	protected Map< String, AbstractDisplayer> displayers = new HashMap< String, AbstractDisplayer >();
	
	// the list of gItems available
	protected List< GraphicalItem > graphicalItems = new ArrayList< GraphicalItem >();

	// default ctor, prepare the background and start the displaying
	public GraphicalEnvironment( DataInformation datas, MediaTracker tracker, int tempo ) 
	{
		this.tempo = tempo;
		this.tracker = tracker;
		
		String backgroundFileName = datas.getStringValue( BACKGROUND_IMAGE_PATH );
		try 
		{
			File imageFile = new File( backgroundFileName );
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
		catch (IOException | InterruptedException e) 
		{
			System.err.println( "Could not find the image: " + backgroundFileName + "> " + e.getMessage() );
		} 
	}

	public void addDisplayer(String name, AbstractDisplayer displayer) 
	{
		if ( displayers.containsKey( name ) == false )
		{
			displayers.put(name, displayer);
		}
	}
	
	public void computeDisplayableItems() 
	{
		for ( AbstractDisplayer displayer : displayers.values() )
		{
			displayer.computeDisplayableItems();
		}
	}
	
	// return true if the item has been added to a displayer
	public synchronized boolean addItem( GraphicalItem item, String layerName, int layerLevel )
	{
		graphicalItems.add( item );
		return addItemInDisplayer( item, layerName, layerLevel );
	}
	
	private boolean addItemInDisplayer(GraphicalItem item, String layerName, int layerLevel) 
	{
		if ( displayers.containsKey(layerName) == true)
		{
			return displayers.get(layerName).addItem(item, layerLevel);
		}
		return false;
	}

	private boolean removeItemInDisplayer(GraphicalItem item, String layerName, int layerLevel) 
	{
		if ( displayers.containsKey(layerName) == true)
		{
			return displayers.get(layerName).removeItem(item, layerLevel);
		}
		return false;
	}

	public synchronized boolean moveItemDisplayer( GraphicalItem item, String layerNameFrom, int layerLevelFrom, String layerNameTo, int layerLevelTo )
	{
		boolean success = true;
		if (  ( displayers.containsKey(layerNameFrom) == true)
			&&( displayers.containsKey(layerNameTo) == true)  )
		{
			success &= removeItemInDisplayer(item, layerNameFrom, layerLevelFrom);
			success &= addItemInDisplayer(item, layerNameTo, layerLevelTo);
		}
			
		return success;
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

	protected void drawBackGround( Graphics g )
	{
		if ( backgroundImage != null )
		{
			g.drawImage( backgroundImage, 0, 0, viewMaxWidth, viewMaxHeight, 0, 0, viewMaxWidth, viewMaxHeight, null);
		}
	}

	protected void drawDisplayer( Graphics g )
	{
		for ( AbstractDisplayer displayer : displayers.values() )
		{
			displayer.render(g);
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
		drawDisplayer( buffer );
		
		// double buffering display
		g.drawImage( image, 0, 0, this);
		
	}  
}
