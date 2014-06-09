package client.panel;

import graphic.GraphicalEnvironment;
import graphic.GraphicalItem;
import helper.DataRepository;

import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import client.ChessGameFrame;
import client.item.ChessPieceItem;

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
		
		addMouseListener( new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if ( arg0.getX() >= 0 )
				{
					mainFrame.mouseClickOnCell( (arg0.getX() - 95) / 64, 7 - arg0.getY() / 64 );
				}
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
	}
	
	public void setStartSoon() 
	{
	}

	public void setStart() 
	{
	}

	public ChessPieceItem findChessPieceAt(int x, int y) 
	{
		for ( GraphicalItem gItem : graphicalItems )
		{
			if ( gItem instanceof ChessPieceItem )
			{
				ChessPieceItem item = (ChessPieceItem)gItem; 
				if ( item.getX() == x && item.getY() == y )
				{
					return item;
				}
			}
		}
		return null;
	}
}
