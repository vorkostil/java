package client;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import server.model.Environment;

/*
 * The purpose of this class is to manage the main view of the game, it's the graphical displaying thread
 **/
public class MainView  extends JFrame 
{
	private static final long serialVersionUID = 8916775585248449318L;

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
	GameScreen screen = null;
	
	public MainView(String name, Environment env) 
	{ 
		createView_(name,env);
		renderingThread.start();
	}

	private void createView_(String name, Environment env) 
	{
		setTitle(name); 
		setSize(800, 600); 

		createMenu_();
		createFrame_(env);
	}

	private void createMenu_() 
	{
	}

	private void createFrame_(final Environment env) 
	{
		screen = new GameScreen();
		screen.setPreferredSize(new Dimension(600, 600));
		screen.setBorder(BorderFactory.createEtchedBorder());
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) 
			{
				if (arg0.getKeyCode() == KeyEvent.VK_UP)
				{
					env.getPlayer().stopMovingUp();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
				{
					env.getPlayer().stopMovingDown();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
				{
					env.getPlayer().stopMovingLeft();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					env.getPlayer().stopMovingRight();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) 
			{
				if (arg0.getKeyCode() == KeyEvent.VK_UP)
				{
					env.getPlayer().startMovingUp();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
				{
					env.getPlayer().startMovingDown();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
				{
					env.getPlayer().startMovingLeft();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					env.getPlayer().startMovingRight();
				}
			}
		});
		
		getContentPane().add(screen);
		
		screen.manage(env);
	}
}
