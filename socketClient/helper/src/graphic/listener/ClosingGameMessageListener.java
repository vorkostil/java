package graphic.listener;

import game.AbstractGameClientFrame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ClosingGameMessageListener implements WindowListener {

	private AbstractGameClientFrame gameClient;
	
	public ClosingGameMessageListener(AbstractGameClientFrame gameClient) 
	{
		this.gameClient = gameClient;
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
	
	@Override
	public void windowIconified(WindowEvent arg0) {
	}
	
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}
	
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		gameClient.closeGame();
	}
	
	@Override
	public void windowClosed(WindowEvent arg0) {
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

}
