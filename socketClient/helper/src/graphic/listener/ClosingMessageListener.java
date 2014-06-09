package graphic.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import network.client.ConnectionClient;

public class ClosingMessageListener implements WindowListener {

	private ConnectionClient connectionClient;
	private String message;

	public ClosingMessageListener( ConnectionClient connectionClient,
								   String message ) 
	{
		this.connectionClient = connectionClient;
		this.message = message; 
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		connectionClient.sendMessageIfConnected( message );
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

}
