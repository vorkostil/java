package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class PaintScreen extends JPanel
{ 
	Image image;
	Graphics buffer;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4113957359020117906L;
	TurtleGraphicDisplayer turtle_ = null;

	public PaintScreen(TurtleGraphicDisplayer turtleGraphicDisplayer) 
	{
		turtle_ = turtleGraphicDisplayer;
	} 
	
	public void paintComponent(Graphics g) 
	{ 
		if (buffer == null || 
			g.getClipBounds().width != image.getWidth(null) || 
			g.getClipBounds().height != image.getHeight(null)) {
			image = createImage(g.getClipBounds().width, g.getClipBounds().height);
			buffer = image.getGraphics();
		}
		
		super.paintComponent(g) ;
		drawBackground_(buffer,g.getClipBounds().width,g.getClipBounds().height);
		if (turtle_ != null) {
			turtle_.draw(buffer);
		}
		g.drawImage(image,0,0,this);
	}

	private void drawBackground_(Graphics g, int width, int height) {
		Color oldColor = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(oldColor);
	}
}
