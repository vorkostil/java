package ScreenManager;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import ScreenManager.Screen.AbstractScreen;

public class MainView  extends JFrame implements Observer
{
	class RenderingThread extends Thread 
	{
		 public void run()
		 {
			 while(true)
			 {
				 try 
				 {
					 repaint(); 
					 sleep( 10 );
				 } 
				 catch ( Exception e ) 
				 {
				 } 
			 }
		 }
	 }
	 
	RenderingThread renderingThread = new RenderingThread();
	private static final long serialVersionUID = 7816573456108846570L;
	
	private MainPanel screen = null;
	private ScreenManager screenManager_ = null;
	private boolean fullscreen_ = false;

	public MainView(ScreenManager screenManager) 
	{ 
		screenManager_ = screenManager;
		screenManager_.addObserver(this);
		createView_();
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				for (AbstractScreen screen : screenManager_.getCurrentScreens()) {
					if (screen.keyReleased(arg0)) {
						break;
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				for (AbstractScreen screen : screenManager_.getCurrentScreens()) {
					if (screen.keyPressed(arg0)) {
						break;
					}
				}
			}
		});
		renderingThread.start();
	}

	private void createView_() 
	{
		createFrame_();
	}

	private void createFrame_() 
	{
		screen = new MainPanel();
		changeScreen_();
		getContentPane().add(screen);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (screenManager_.isFullscreen() != fullscreen_ ) {
			fullscreen_ = screenManager_.isFullscreen();
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			if (fullscreen_) {
				gs.setFullScreenWindow(this);
				this.validate();
			}
			else {
			    gs.setFullScreenWindow(null);
			}
		}
		
		changeScreen_();
	}
	
	protected void changeScreen_() {
		screen.displayScreens(screenManager_.getCurrentScreens());
		if (screenManager_.getCurrentRootScreen() != null) {
			setSize(screenManager_.getCurrentRootScreen().getWidth(),screenManager_.getCurrentRootScreen().getHeight());
			setLocation(screenManager_.getCurrentRootScreen().getX(), screenManager_.getCurrentRootScreen().getY());
		}
	}
}
