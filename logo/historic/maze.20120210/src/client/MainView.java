package client;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import server.model.Environment;
import server.model.Player;

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

	private boolean drawMode_ = false;
	JRadioButtonMenuItem  itemModeDraw_ = new JRadioButtonMenuItem("Draw");
	JRadioButtonMenuItem  itemModeTest_ = new JRadioButtonMenuItem("Test");
	
	private List<Point2D> points_ = new ArrayList<Point2D>();
	List<AbstractVisitor> visitors_ = new ArrayList<AbstractVisitor>();
	
	public MainView(String name, Environment env) 
	{ 
		createView_(name,env);
		renderingThread.start();
	}

	private void createView_(String name, Environment env) 
	{
		setTitle(name); 
		setSize(817, 662); 

		createMenu_();
		createFrame_(env);
	}

	private void createMenu_() 
	{
		itemModeDraw_.setSelected(false);
		itemModeDraw_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawMode_ = true; 
			}
		});
		
		itemModeTest_.setSelected(true);
		itemModeTest_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawMode_ = false;
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(itemModeDraw_);
		group.add(itemModeTest_);
		
		JMenu menuMode = new JMenu("Mode");
		menuMode.add(itemModeDraw_);
		menuMode.add(itemModeTest_);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuMode);
		
		setJMenuBar(menuBar);
	}

	private void createFrame_(final Environment env) 
	{
		screen = new GameScreen();
		screen.setPreferredSize(new Dimension(600, 600));
		screen.setBorder(BorderFactory.createEtchedBorder());
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) 
			{
				if (!drawMode_)
				{
					if (arg0.getKeyCode() == KeyEvent.VK_UP)
					{
						env.getPlayer().stopMoving(Player.DIRECTION_UP);
					}
					else if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
					{
						env.getPlayer().stopMoving(Player.DIRECTION_DOWN);
					}
					else if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
					{
						env.getPlayer().stopMoving(Player.DIRECTION_LEFT);
					}
					else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
					{
						env.getPlayer().stopMoving(Player.DIRECTION_RIGHT);
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) 
			{
				if (!drawMode_)
				{
					if (arg0.getKeyCode() == KeyEvent.VK_UP)
					{
						env.getPlayer().startMoving(Player.DIRECTION_UP);
					}
					else if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
					{
						env.getPlayer().startMoving(Player.DIRECTION_DOWN);
					}
					else if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
					{
						env.getPlayer().startMoving(Player.DIRECTION_LEFT);
					}
					else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
					{
						env.getPlayer().startMoving(Player.DIRECTION_RIGHT);
					}
				}
			}
		});
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (drawMode_)
				{
					points_.add(new Point2D.Double(arg0.getX() - 7,arg0.getY() - 52));
					if (arg0.isControlDown())
					{
						Polygon p = new Polygon();
						for (Point2D point : points_)
						{
							p.addPoint((int)point.getX(), (int)point.getY());
						}
						env.addWall("wall", p, true);
						screen.manage(env,visitors_);
						points_.clear();
					}
				}
			}
		});
		getContentPane().add(screen);
		
		visitors_.add(new GraphicalVisitor());
		screen.manage(env,visitors_);
	}
}
