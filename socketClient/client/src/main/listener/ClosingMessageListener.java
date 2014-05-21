package main.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintWriter;

public class ClosingMessageListener implements WindowListener {

	private PrintWriter writer = null;
	private String message = null;
	
	public ClosingMessageListener(PrintWriter writer, String message) 
	{
		this.writer = writer;
		this.message = message;
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
		if  ( writer != null )
		{
			writer.println( message );
			writer.flush();
		}
	}
	
	@Override
	public void windowClosed(WindowEvent arg0) {
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

}
