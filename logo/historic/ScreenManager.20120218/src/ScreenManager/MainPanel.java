package ScreenManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import ScreenManager.Screen.AbstractScreen;

public class MainPanel extends JPanel
{ 
	private static final long serialVersionUID = -6623720668184719957L;
	Image image;
	Graphics buffer;

	AbstractScreen screen_ = null;
	
	public MainPanel() 
	{
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (screen_ != null) {
					screen_.mouseReleased(arg0);
				}
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (screen_ != null) {
					screen_.mousePressed(arg0);
				}
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
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				if (screen_ != null) {
					screen_.mouseMoved(arg0);
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (screen_ != null) {
					screen_.mouseDragged(arg0);
				}
			}
		});
	} 
	
	public void displayScreen(AbstractScreen abstractScreen) {
		screen_ = abstractScreen;
	}
	
	public void paintComponent(Graphics g) 
	{ 
		if (buffer == null 
			|| g.getClipBounds().width != image.getWidth(null) 
			|| g.getClipBounds().height != image.getHeight(null)) 
		{
			image = createImage(g.getClipBounds().width, g.getClipBounds().height);
			buffer = image.getGraphics();
		}
		
		super.paintComponent(g) ;
		drawBackground_(buffer);
		drawForeground_(buffer);
		g.drawImage(image,0,0,this);
	}

	private void drawBackground_(Graphics g) 
	{
		if (screen_ != null) {
			screen_.drawBackground(g);
		}
	}

	private void drawForeground_(Graphics g) 
	{
		if (screen_ != null) {
			screen_.drawForeground(g);
		}
	}
}
