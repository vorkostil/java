package client;

import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class PaintScreen extends JPanel implements Observer
{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4113957359020117906L;
	TurtleGraphicDisplayer turtle_ = null;

	public PaintScreen(TurtleGraphicDisplayer turtleGraphicDisplayer) 
	{
		turtle_ = turtleGraphicDisplayer;
		turtle_.addObserver(this);
	} 
	
	public void paintComponent(Graphics g) 
	{ 
		super.paintComponent(g) ;
		if (turtle_ != null)
			turtle_.draw(g);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		paintComponent(getGraphics());
	}
}
