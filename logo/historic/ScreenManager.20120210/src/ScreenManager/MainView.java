package ScreenManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

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
					 //long begin = new Date().getTime();
					 repaint(); 
					 //long end = new Date().getTime();
					 //System.out.println("time: " + (end - begin) + " ms");
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
				if (screenManager_.getCurrentScreen() != null) {
					screenManager_.getCurrentScreen().keyReleased(arg0);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (screenManager_.getCurrentScreen() != null) {
					screenManager_.getCurrentScreen().keyPressed(arg0);
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
		screen.displayScreen(screenManager_.getCurrentScreen());
		setSize(screenManager_.getCurrentScreen().getWidth(),screenManager_.getCurrentScreen().getHeight());
		getContentPane().add(screen);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		screen.displayScreen(screenManager_.getCurrentScreen());
		setSize(screenManager_.getCurrentScreen().getWidth(),screenManager_.getCurrentScreen().getHeight());
	}
}
