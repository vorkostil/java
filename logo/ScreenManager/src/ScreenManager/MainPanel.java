package ScreenManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ScreenManager.Screen.AbstractScreen;

public class MainPanel extends JPanel
{ 
	private static final long serialVersionUID = -6623720668184719957L;
	Image image;
	Graphics buffer;

	List<AbstractScreen> screens_ = new ArrayList<AbstractScreen>();
	
	public MainPanel() 
	{
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				for (AbstractScreen screen : screens_) {
					if (screen.mouseReleased(arg0)) {
						break;
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				for (AbstractScreen screen : screens_) {
					if (screen.mousePressed(arg0)) {
						break;
					}
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
				for (AbstractScreen screen : screens_) {
					if (screen.mouseMoved(arg0)) {
						break;
					}
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				for (AbstractScreen screen : screens_) {
					if (screen.mouseDragged(arg0)) {
						break;
					}
				}
			}
		});
	} 
	
	public void displayScreens(List<AbstractScreen> abstractScreens) {
		screens_.clear();
		for (AbstractScreen screen : abstractScreens)
			screens_.add(screen);
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
		for (int i = screens_.size(); i > 0; i--) {
			screens_.get(i - 1).drawBackground(buffer);
			screens_.get(i - 1).drawForeground(buffer);
			
		}
		
		// double buffering disaply
		g.drawImage(image,0,0,this);
	}
}
