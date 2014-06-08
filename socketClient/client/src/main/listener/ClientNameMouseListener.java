package main.listener;

import game.ChessGameServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import server.TronGameServer;

import main.GraphicalClient;

public class ClientNameMouseListener implements MouseListener 
{
	/**
	 * 
	 */
	JPopupMenu menu = new JPopupMenu();
	String name = null;
	
	GraphicalClient gClient = null;
	public ClientNameMouseListener( GraphicalClient graphicalClient )
	{
		gClient = graphicalClient;
		JMenuItem chat = new JMenuItem( "Chat"); 
		JMenuItem tron = new JMenuItem( "Tron"); 
		JMenuItem chess = new JMenuItem( "Chess"); 
		
		chat.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gClient.addDirectCommunication( name );
			}
		});
		
		tron.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gClient.askForGameTo( name, TronGameServer.NAME );
			}
		});
		
		chess.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gClient.askForGameTo( name, ChessGameServer.NAME );
			}
		});
		
		menu.add( chat );
		menu.add( tron );
		menu.add( chess );
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if ( arg0.isPopupTrigger() == true )
		{
		     int index = gClient.getClientName().locationToIndex( arg0.getPoint());
		     name = gClient.getClientName().getModel().getElementAt(index);
		     gClient.getClientName().ensureIndexIsVisible(index);
		     if ( name.compareTo( gClient.getLogin() ) != 0 )
		     {
		    	 menu.show( gClient.getClientScrollPane(), arg0.getX(), arg0.getY() );
		     }				
		}
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if ( arg0.isPopupTrigger() == true )
		{
		     int index = gClient.getClientName().locationToIndex( arg0.getPoint());
		     name = gClient.getClientName().getModel().getElementAt(index);
		     gClient.getClientName().ensureIndexIsVisible(index);
		     if ( name.compareTo( gClient.getLogin() ) != 0 )
		     {
		    	 menu.show( gClient.getClientScrollPane(), arg0.getX(), arg0.getY() );
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