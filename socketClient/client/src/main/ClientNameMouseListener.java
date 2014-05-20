package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class ClientNameMouseListener implements MouseListener 
{
	/**
	 * 
	 */
	JPopupMenu menu = new JPopupMenu();
	String name = null;
	
	GraphicalClient gClient = null;
	ClientNameMouseListener( GraphicalClient graphicalClient, GraphicalClient src )
	{
		gClient = src;
		JMenuItem chat = new JMenuItem( "Chat"); 
		JMenuItem game = new JMenuItem( "Game"); 
		
		chat.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gClient.addDirectCommunication( name );
			}
		});
		game.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gClient.askForGameTo( name );
			}
		});
		
		menu.add( chat );
		menu.add( game );
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if ( arg0.isPopupTrigger() == true )
		{
		     int index = gClient.clientName.locationToIndex( arg0.getPoint());
		     name = gClient.clientName.getModel().getElementAt(index);
		     gClient.clientName.ensureIndexIsVisible(index);
		     if ( name.compareTo( gClient.login ) != 0 )
		     {
		    	 menu.show( gClient.clientScrollPane, arg0.getX(), arg0.getY() );
		     }				
		}
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if ( arg0.isPopupTrigger() == true )
		{
		     int index = gClient.clientName.locationToIndex( arg0.getPoint());
		     name = gClient.clientName.getModel().getElementAt(index);
		     gClient.clientName.ensureIndexIsVisible(index);
		     if ( name.compareTo( gClient.login ) != 0 )
		     {
		    	 menu.show( gClient.clientScrollPane, arg0.getX(), arg0.getY() );
		     }				
		}
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
	}
}