package game.chess.panel;

import game.chess.ChessGameFrame;
import graphic.GraphicalEnvironment;
import helper.DataRepository;

import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

@SuppressWarnings("serial")
public class ChessMainPanel extends GraphicalEnvironment 
{
	private static final String GRAPHIC_CONFIGURATION = "graphical_configuration";
	public static final int frameWidth = 702;
	public static final int frameHeight = 512;
	private ChessGameFrame mainFrame;
	
	public ChessMainPanel( ChessGameFrame main, DataRepository repository, MediaTracker tracker, int tempo ) throws IOException
	{
		super( repository.getData( GRAPHIC_CONFIGURATION ), tracker,  tempo );
		this.mainFrame = main;
		
		addMouseMotionListener( new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				if ( arg0.getX() < 0 )
				{
					mainFrame.mouseMoveOnCell( -1, -1 );
				}
				else
				{
					mainFrame.mouseMoveOnCell( (arg0.getX() - 95) / 64, 7 - arg0.getY() / 64 );
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
			}
		});
	}
	
	public void setStartSoon() 
	{
	}

	public void setStart() 
	{
	}
}
