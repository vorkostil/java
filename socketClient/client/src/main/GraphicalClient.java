package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import server.TronGameServer;

import client.TronGameClient;

import main.listener.ClientNameMouseListener;
import network.client.AbstractSocketListenerClientSide;
import network.client.ConnectionClient;
import network.client.ConnectionInfo;
import network.client.ConnectionObserver;

import common.ConnectionDialog;
import common.MessageType;

import frame.PeerToPeerCommunicationFrame;
import game.AbstractGameClient;
import game.ChessGameServer;
import game.chess.ChessGameFrame;

@SuppressWarnings("serial")
public class GraphicalClient extends JFrame implements ConnectionObserver
{
	public static final Font errorFont = new Font( "Default", Font.BOLD, 12);
	public static final Font normalFont = new Font( "Default", Font.PLAIN, 12);
	public static final Font serverFont = new Font( "Default", Font.ITALIC, 12);
	
	public static final Color errorColor = Color.RED;
	public static final Color normalColor = Color.BLACK;
	public static final Color serverColor = Color.BLUE;
	
	
	JTextField textEdition = new JTextField();
	JTextPane chatArea = new JTextPane();
	JList< String > clientName = new JList<String>();
	JScrollPane clientScrollPane = new JScrollPane( clientName );
	JMenuBar menuBar = new JMenuBar();
	JMenuItem menuItemConnect = new JMenuItem( "Connect" );
	JMenuItem menuItemDisconnect = new JMenuItem( "Disconnect" );
	
	HashMap< String, PeerToPeerCommunicationFrame > directCommunications = new HashMap< String, PeerToPeerCommunicationFrame >();
	HashMap< String, AbstractGameClient > games = new HashMap< String, AbstractGameClient >();

	// Network relevant information
	ConnectionClient connectionClient;
	
	public GraphicalClient()
	{
		// characteristics of the frame
		this.setTitle("Chat");
		this.setSize(640, 480);
		this.setLocationRelativeTo(null);               
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setJMenuBar( menuBar );
		createMenuBar();
		
		// associate a BorderLayout (simplest one)
		this.setLayout( new BorderLayout() );
		
		textEdition.setMinimumSize( new Dimension( 480, 32 ) );
		textEdition.addKeyListener( new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				if ( arg0.getKeyCode() == KeyEvent.VK_ENTER )
				{
					String message = textEdition.getText();
					textEdition.setText( "" );
					
					if ( connectionClient.sendMessageIfConnected(message) == false )
					{
						appendToChatArea( "|local|" + message, normalFont, normalColor );
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		
		textEdition.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		
		clientScrollPane.setPreferredSize( new Dimension( 128, 320 ) );
		clientScrollPane.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		
		clientName.setPrototypeCellValue( "Abcdefgh Ijk" );
		clientName.setBackground( new Color( 232, 232, 196 ) );
		clientName.addMouseListener( new ClientNameMouseListener( this ) );
		chatArea.setEditable( false );
		chatArea.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		chatArea.setBackground( new Color( 232, 232, 196 ) );
		
		this.getContentPane().add( textEdition, BorderLayout.SOUTH );
		this.getContentPane().add( new JScrollPane(  chatArea ), BorderLayout.CENTER );
		this.getContentPane().add( clientScrollPane, BorderLayout.EAST );
		
		// display itself
		this.setVisible(true);
	}

	private void createMenuBar() 
	{
		JMenu menuConnection = new JMenu( "Connection" );
		JMenuItem menuItemQuit = new JMenuItem( "Quit" );
		
		menuItemConnect.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConnectionDialog dialog = new ConnectionDialog(null, "Connection", true);
				ConnectionInfo info = dialog.showConnectionDialog();
				if (  ( info != null )
					&&( info.isValid() == true )  )
				{
					try 
					{
						launchConnection( info );
					} 
					catch (UnknownHostException e1) 
					{
						e1.printStackTrace();
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					} 
					catch (InterruptedException e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		});
		menuItemDisconnect.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				closeConnection();
			}
		});
		menuItemQuit.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
				System.exit( 0 );
			}
		});

		menuItemConnect.setEnabled( true );
		menuItemDisconnect.setEnabled( false );
		
		menuConnection.add( menuItemConnect );
		menuConnection.add( menuItemDisconnect );
		menuConnection.add( menuItemQuit );
		
		menuBar.add( menuConnection );
	}

	protected void closeConnection() 
	{
		if ( connectionClient != null )
		{
			for ( String name : directCommunications.keySet() )
			{
				connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageCommunicationSpecific + " " + MessageType.MessageClose + " " + name );
			}
			connectionClient.closeConnection();
		}
	}

	public void launchConnection( ConnectionInfo info ) throws UnknownHostException, IOException, InterruptedException 
	{
		connectionClient = new ConnectionClient( this );
		connectionClient.launchConnection( info );
	}

    public void appendToChatArea(String msg, Font f, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, f.getFamily() );
        aset = sc.addAttribute(aset, StyleConstants.FontSize, f.getSize() );
        aset = sc.addAttribute(aset, StyleConstants.FontConstants.Bold, f.isBold() );
        aset = sc.addAttribute(aset, StyleConstants.FontConstants.Italic, f.isItalic() );
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        try 
        {
			chatArea.getDocument().insertString( chatArea.getDocument().getLength(), msg + "\n", aset );
			chatArea.setCaretPosition( chatArea.getDocument().getLength() );
		} 
        catch (BadLocationException e) 
        {
			e.printStackTrace();
		}
    }

	public void updateContactList(String[] names) 
	{
		clientName.setListData( names );
	}

	public void addDirectCommunication(String name) 
	{
		if ( directCommunications.containsKey( name ) == false )
		{
			directCommunications.put( name, new PeerToPeerCommunicationFrame( this, connectionClient, name ) );
		}
	}

	public void closeSpecificCommunication( String name, boolean destroy) 
	{
		if ( directCommunications.containsKey( name ) == true )
		{
			appendToChatArea( "Direct communication close with " + name, serverFont, serverColor);
			if ( destroy == true )
				((PeerToPeerCommunicationFrame) directCommunications.get( name )).dispose();
			directCommunications.remove( name );
		}
	}

	public void forwardSpecificCommunication(String name, String msg) 
	{
		if ( directCommunications.containsKey( name ) == true )
		{
			((PeerToPeerCommunicationFrame) directCommunications.get( name )).appendToChatArea( name + "> " + msg, normalFont, normalColor);
		}
	}

	public void openSpecificCommunication(String name) 
	{
		if ( directCommunications.containsKey( name ) == false )
		{
			appendToChatArea( "Direct communication open with " + name, serverFont, serverColor);
			directCommunications.put( name, new PeerToPeerCommunicationFrame( this, connectionClient, name ) );
		}
	}

	public void closeAllGames() 
	{
		for ( AbstractGameClient game : games.values() )
		{
			appendToChatArea( "Game close with id: " + game.getId(), serverFont, serverColor);
			game.dispose();
			games.remove( game.getId() );
		}
	}

	public void closeGame( String gameId, boolean destroy) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			appendToChatArea( "Game close with id: " + gameId, serverFont, serverColor);
			if ( destroy == true )
				games.get( gameId ).dispose();
			games.remove( gameId );
		}
	}

	public void openGame( String gameId, String gameName, String playerBlue, String playerRed ) 
	{
		if ( games.containsKey( gameId ) == false )
		{
			appendToChatArea( "Game " + gameName + " open with id: " + gameId, serverFont, serverColor);
			try 
			{
				if ( gameName.compareTo( TronGameServer.NAME ) == 0 )
				{
					games.put( gameId, new TronGameClient( connectionClient, gameId, playerBlue, playerRed ) );
				}
				else if ( gameName.compareTo( ChessGameServer.NAME ) == 0 )
				{
					games.put( gameId, new ChessGameFrame( connectionClient, gameId, playerBlue, playerRed ) );
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	public void readyGame( String gameId, String name ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			appendToChatArea( "Game " + gameId + ", player " + name + " is ready\n", serverFont, serverColor);
			games.get( gameId ).ready( name );
		}
	}

	public void startGame( String gameId ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			appendToChatArea( "Game " + gameId + " will start\n", serverFont, serverColor);
			games.get( gameId ).start();
		}
	}

	public void startGameSoon(String gameId) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			appendToChatArea( "Game " + gameId + " will start soon\n", serverFont, serverColor);
			games.get( gameId ).startSoon();
		}
	}

	public void askForGameTo( String opponentName, String gameName) 
	{
		connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameAsked + " " + opponentName + " " + gameName );
	}

	public void askForGameFrom( String opponentName, String gameName) 
	{
		int response = JOptionPane.showConfirmDialog(null, "Would you play " + gameName + " with " + opponentName + " ?", "Game launch", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if ( response == JOptionPane.OK_OPTION)
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameAccepted + " " + opponentName + " " + gameName );
		}
		else
		{
			connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageGameRefused + " " + opponentName );
		}
	}
	
	public void endGame( String gameId, String winner ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			games.get( gameId ).end( winner );
			closeGame(gameId, true);
		}
	}

	public void forwardGameMessage(String gameId, String line) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			games.get( gameId ).forwardMessage( line );
		}
	}

	public JList< String > getClientName() 
	{
		return clientName;
	}

	public JScrollPane getClientScrollPane() 
	{
		return clientScrollPane;
	}

	@Override
	public void raiseAlert(String message) 
	{
		appendToChatArea(message, errorFont, errorColor);
	}

	@Override
	public void raiseInfo(String message) 
	{
		appendToChatArea(message, serverFont, serverColor);
	}

	@Override
	public void serverDisconnection() 
	{
		updateContactList( new String[] {} );
		closeAllGames();
	}

	@Override
	public void connectionStatusChange( network.client.ConnectionClient.State currentState ) 
	{
		menuItemConnect.setEnabled( currentState == network.client.ConnectionClient.State.WAITING_FOR_SERVER );
		menuItemDisconnect.setEnabled( ( currentState != network.client.ConnectionClient.State.WAITING_FOR_SERVER ) );
	}

	@Override
	public AbstractSocketListenerClientSide createSocketListener(Socket socket) throws IOException 
	{
		return new SocketListenerClientSide(this, socket, connectionClient);
	}

	public String getLogin() 
	{
		if ( connectionClient != null )
		{
			return connectionClient.getLogin();
		}
		return null;
	}
}
