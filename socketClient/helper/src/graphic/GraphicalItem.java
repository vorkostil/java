package graphic;

import helper.DataRepository.DataInformation;

import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/* Abstract class for item animated or not which need to be displayed on the layer of a graphical environment
 * 
 * */
public abstract class GraphicalItem 
{
	public static final String EMPTY_STRING = "";
	
	private static final String SEPARATOR = "_";
	
	private static final String STATES = "states";
	private static final String STATE = "state";
	private static final String COLOR = "color";
	private static final String FRAME = "frame";
	private static final String CYCLIC = "cyclic";
	
	private static final String IMAGE_PATH = "image_path";
	private static final String IMAGE_PATH_FRAME = IMAGE_PATH + SEPARATOR + FRAME;
	private static final String NUMBER_FRAME = "number_of_frames";
	private static final String DELAY_FRAME = "delay_between_frames";
			
	private static final String FRAME_SIZE_GETTER( String state ) 			 { return STATE + SEPARATOR + state + SEPARATOR + NUMBER_FRAME; }
	private static final String FRAME_PATH_GETTER( String state, int frame ) { return STATE + SEPARATOR + state + SEPARATOR + IMAGE_PATH_FRAME + SEPARATOR + ( frame + 1 ); }
	private static final String FRAME_DELAY_GETTER( String state ) 			 { return STATE + SEPARATOR + state + SEPARATOR + DELAY_FRAME; }
	private static final String FRAME_CYCLIC_GETTER( String state ) 		 { return STATE + SEPARATOR + state + SEPARATOR + CYCLIC; }
	private static final String IMAGE_PATH_GETTER( String state ) 			 { return STATE + SEPARATOR + state + SEPARATOR + IMAGE_PATH; }
	
	public static final String DEFAULT_STATE = "DEFAULT";
	
	protected static final Rectangle emptyBoundingBox = new Rectangle();
	
	private Color color_ = Color.BLACK;
	private Map< String, List< Image > > images_ = new HashMap< String, List< Image > >();
	private Map< String, Integer> delays_ = new HashMap< String, Integer >();
	private Map< String, Boolean> cyclic_ = new HashMap< String, Boolean >();
	protected String currentState_ = DEFAULT_STATE;
	private int currentFrame_ = 0;
	private int delay_ = 0;
	private long currentTime_ = 0;
	private boolean hasImage = false;
	
	private int imageWidth = -1;
	private int imageHeight = -1;
	
	/* Sample of configuration file associated
	 * 
	 * 
	 * */
	public GraphicalItem( DataInformation dataInformation, 
						  MediaTracker mediaTracker,
						  int levelId ) 
	{
		if ( dataInformation == null )
		{
			color_ = Color.BLACK;
		}
		else
		{
			color_ = Color.decode( dataInformation.getStringValue( COLOR ) );
			
			// check for multi-state object
			//-----------------------------
			if ( dataInformation.contains( STATES ) == true ) 
			{
				hasImage = true;
				String states[] = dataInformation.getStringValue( STATES ).split( DataInformation.LIST_SEPARATOR );
				for (String state : states) 
				{
					// check for multi-frame state
					//----------------------------
					images_.put( state, new ArrayList< Image >() );
					if ( dataInformation.contains( FRAME_SIZE_GETTER( state ) ) == true ) 
					{
						int frameSize = dataInformation.getIntegerValue( FRAME_SIZE_GETTER( state ) );
						for ( int i = 0; i < frameSize;++i) 
						{
							try
							{
								Image image = ImageIO.read( new File( dataInformation.getStringValue( FRAME_PATH_GETTER( state, i ) ) ) );
								if ( image != null )
								{
									imageWidth = image.getWidth( null );
									imageHeight = image.getHeight( null );
									
									mediaTracker.addImage( image, levelId );
									images_.get( state ).add( image );
								}
							}
							catch (IOException e)
							{
								System.err.println("Uanble to read the image '" + dataInformation.getStringValue( FRAME_PATH_GETTER( state, i ) ) + "': " + e.getMessage() );
								hasImage = false;
							}
						}
						// awful method to store the delay between frame
						// TODO rework this part to store the delay and cycle within the frames of state
						//------------------------------------------------------------------------------
						delays_.put( state, dataInformation.getIntegerValue(FRAME_DELAY_GETTER( state ) ) );
						if ( dataInformation.contains( FRAME_CYCLIC_GETTER( state ) ) == true )
						{
							cyclic_.put( state, dataInformation.getBooleanValue( FRAME_CYCLIC_GETTER( state ) ) );
						}
						else
						{
							cyclic_.put( state, true );
						}
							
					}
					else 
					{ 
						try
						{
							Image image = ImageIO.read( new File( dataInformation.getStringValue( IMAGE_PATH_GETTER( state ) ) ) );
							if ( image != null )
							{
								imageWidth = image.getWidth( null );
								imageHeight = image.getHeight( null );
								
								mediaTracker.addImage( image, levelId );
								images_.get(state).add(image);
							}
						}
						catch (IOException e)
						{
							System.err.println("Uanble to read the image '" + dataInformation.getStringValue( IMAGE_PATH_GETTER( state ) ) + "': " + e.getMessage() );
							hasImage = false;
						}
					}
				}
			}
			// Check if there is at least one image
			//-------------------------------------
			else if ( dataInformation.contains( IMAGE_PATH ) == true ) 
			{
				try
				{
					hasImage = true;
					Image image = ImageIO.read( new File( dataInformation.getStringValue( IMAGE_PATH ) ) );
					if (image != null)
					{
						imageWidth = image.getWidth( null );
						imageHeight = image.getHeight( null );
						
						mediaTracker.addImage( image, levelId );
						images_.put( DEFAULT_STATE, new ArrayList< Image >() );
						images_.get( DEFAULT_STATE ).add( image );
					}
				}
				catch (IOException e)
				{
					hasImage = false;
					System.err.println("Uanble to read the image '" + dataInformation.getStringValue( IMAGE_PATH ) + "': " + e.getMessage() );
				}
			}
		}
	}
	
	public int getImageWidth()
	{
		return imageWidth;
	}
	
	public int getImageHeight()
	{
		return imageHeight;
	}
	
	public Color getColor() 
	{
		return color_;
	}

	public void process( long currentTimeInMilli ) 
	{
		long delay = 0;
		if ( currentTime_ != 0 )
		{
			delay = currentTimeInMilli - currentTime_ ;
		}
		
		if ( hasImage == true ) 
		{
			// check the change of state
			//--------------------------
			String state = getState();
			if ( state.compareTo( currentState_ ) != 0) 
			{
				currentState_ = state;
				currentFrame_ = 0;
				delay_ = 0;
			}
			// or change of frame
			//-------------------
			else if ( images_.get( currentState_ ).size() > 1 ) 
			{
				delay_ += delay;
				if ( delay_ > delays_.get( currentState_ ) ) 
				{
					++currentFrame_;
					delay_ = 0;
				}
				
				// roll on the first image of the animation if needed
				// or stay on the last image if not cyclic
				//---------------------------------------------------
				if ( currentFrame_ >= images_.get(currentState_).size() )
				{
					if ( cyclic_.get( currentState_ ) == true )
					{
						currentFrame_ = 0;
					}
					else
					{
						currentFrame_ = images_.get(currentState_).size() - 1;
					}
				}
			}
		}
		
		currentTime_ = currentTimeInMilli;
	}
	
	public void resetProcessTime()
	{
		currentTime_ = 0;
	}
	
	public synchronized Image getImage() 
	{
		if ( hasImage == true ) 
		{
			return images_.get( currentState_ ).get( currentFrame_ );
		}
		return null;
	}

	public boolean contains(int x, int y) 
	{
		return getBoundingBox().contains(x,y);
	}
	
	/* return a text to display default return EMPTY_STRING
	 **/ 
	public String getText()
	{
		return EMPTY_STRING;
	}
	
	/* return the current state of the object default is DEFAULT_STATE
	 * this state will be mainly used to retrieve the image to display
	 * */
	public String getState()
	{
		return DEFAULT_STATE;
	}
	
	// behavior when mouse left the item zone
	public void mouseLeftItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
	}
	
	// behavior when mouse enter the item zone
	public void mouseEnterItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
	}
	
	// behavior when mouse pressed on the item zone
	public void mousePressedItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
	}
	
	// behavior when mouse release on the item zone
	public void mouseReleasedItem(int x, int y, boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
	}
	
	// behavior when activation of the item is asked
	public void activate(boolean leftButtonIsUp, boolean rightButtonIsUp ) 
	{
	}
	
	/* return the X coordinate of the item used to draw it on the screen
	 * this value is relative to the environment, not the screen  
	 **/ 
	abstract public int getX(); 
	
	/* return the Y coordinate of the item used to draw it on the screen
	 * this value is relative to the environment, not the screen  
	 **/ 
	abstract public int getY(); 

	/*return if the item is visible from a functional point of view, not a display's one
	 **/ 
	abstract public boolean isVisible(); 

	/* return the AABB of the item
	 * SHALL NOT return NULL
	 * */
	abstract public Rectangle getBoundingBox();
}
