package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.JPanel;

import server.model.Environment;

public class GameScreen extends JPanel
{ 
	private static final long serialVersionUID = -6623720668184719957L;
	Image image;
	Graphics buffer;

	GraphicalEnvironment gEnv_ = null;
	
	public GameScreen() 
	{
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
		drawBackground_(buffer,g.getClipBounds().width,g.getClipBounds().height);
		drawForeground_(buffer,g.getClipBounds().width,g.getClipBounds().height);
		g.drawImage(image,0,0,this);
	}

	private void drawBackground_(Graphics g, int width, int height) 
	{
		// reset background
		Color oldColor = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(oldColor);
		
		// draw environment
		if (gEnv_ != null)
		{
			gEnv_.draw(g,0,0,width,height);
		}
	}

	private void drawForeground_(Graphics g, int width, int height) 
	{
	}

	public void manage(Environment env, List<AbstractVisitor> visitors) 
	{
		gEnv_ = new GraphicalEnvironment(env,visitors);
	}
}
