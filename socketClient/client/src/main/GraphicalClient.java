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
import java.io.PrintWriter;
import java.net.ConnectException;
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

import main.listener.ClientNameMouseListener;

import common.MessageType;

import frame.ConnectionDialog;
import frame.ConnectionInfo;
import frame.PeerToPeerCommunicationFrame;
import game.tron.TronGameFrame;

@SuppressWarnings("serial")
public class GraphicalClient extends JFrame 
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
	HashMap< String, TronGameFrame > games = new HashMap< String, TronGameFrame >();
	
	public enum State { WAITING_FOR_SERVER, WAITING_FOR_LOGIN, CONNECTED, DURING_LOGIN };
				 
	boolean isConnected = false;
	State currentState = State.WAITING_FOR_SERVER;

	Socket socket = null;
	PrintWriter writer = null;
	ConnectionInfo info = null;
	String login = null;
	
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
					
					if (  ( writer != null )
						&&( currentState == State.CONNECTED )  )
					{
						writer.println( message );
						writer.flush();
					}
					else
					{
						appendToChatArea( "|local|" + message + "\n", normalFont, normalColor );
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
				info = dialog.showConnectionDialog();
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
				if ( writer != null )
				{
					for ( String name : directCommunications.keySet() )
					{
						writer.println( MessageType.MessageSystem + " " + MessageType.MessageCommunicationSpecific + " " + MessageType.MessageClose + " " + name );
						writer.flush();
					}
					writer.println( MessageType.MessageSystem + " " + MessageType.MessageClose );
					writer.flush();
					
					socket = null;
					writer = null;
				}
			}
		});
		menuItemQuit.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible( false );
				System.exit( 0 );
			}
		});

		menuItemConnect.setEnabled( currentState == State.WAITING_FOR_SERVER );
		menuItemDisconnect.setEnabled( ( currentState != State.WAITING_FOR_SERVER ) );
		
		menuConnection.add( menuItemConnect );
		menuConnection.add( menuItemDisconnect );
		menuConnection.add( menuItemQuit );
		
		menuBar.add( menuConnection );
	}

	public void changeCurrentState( State newState )
	{
		currentState = newState;
		if ( currentState == State.WAITING_FOR_SERVER )
		{
			if ( socket != null )
			{
				try {
					socket.close();
				} catch (IOException e) {
					appendToChatArea( "socket can not be closed as the link is already broken", errorFont, errorColor);
				}
				socket = null;
			}
			writer = null;
			updateContactList( new String[] {} );
			closeAllGames();
		}
		menuItemConnect.setEnabled( currentState == State.WAITING_FOR_SERVER );
		menuItemDisconnect.setEnabled( ( currentState != State.WAITING_FOR_SERVER ) );
	}
	
	public State getCurrentState()
	{
		return currentState;
	}
	
	public void launchConnection( ConnectionInfo info ) throws UnknownHostException, IOException, InterruptedException 
	{
		if ( socket == null )
		{
			try
			{
				socket = new Socket( info.getServer(), 
									 Integer.parseInt( info.getPort() ) );
				
				changeCurrentState( State.WAITING_FOR_LOGIN );
				appendToChatArea( "Socket client accepted on " + socket.getLocalSocketAddress() + " waiting for server interaction\n", serverFont, serverColor );
				
				writer = new PrintWriter( socket.getOutputStream() );
				
				// launch the connection thread
				Thread client = new Thread( new ServerLoginConnection( socket, 
																	   this, 
																	   info ) );
				client.setName( "Connection" );
				client.start();
			}
			catch (NumberFormatException e)
			{
				appendToChatArea( "Invalid server information\n", errorFont, errorColor );
			}
			catch (ConnectException e)
			{
				appendToChatArea( "No server found\n", errorFont, errorColor );
			}
		}
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
			chatArea.getDocument().insertString( chatArea.getDocument().getLength(), msg, aset );
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

	public void setLogin(String login) 
	{
		this.login  = login;
		this.setTitle( "Chat as " + login );
	}

	public void addDirectCommunication(String name) 
	{
		if ( directCommunications.containsKey( name ) == false )
		{
			directCommunications.put( name, new PeerToPeerCommunicationFrame( this, login, name, writer ) );
			writer.println( MessageType.MessageSystem + " " + MessageType.MessageCommunicationSpecificOpen + " " + name + " " + login );
			writer.flush();
		}
	}

	public void closeSpecificCommunication( String name, boolean destroy) 
	{
		if ( directCommunications.containsKey( name ) == true )
		{
			appendToChatArea( "Direct communication close with " + name + "\n", serverFont, serverColor);
			if ( destroy == true )
				((PeerToPeerCommunicationFrame) directCommunications.get( name )).dispose();
			directCommunications.remove( name );
		}
	}

	public void forwardSpecificCommunication(String name, String msg) 
	{
		if ( directCommunications.containsKey( name ) == true )
		{
			((PeerToPeerCommunicationFrame) directCommunications.get( name )).appendToChatArea( name + "> " + msg + "\n", normalFont, normalColor);
		}
	}

	public void openSpecificCommunication(String name) 
	{
		if ( directCommunications.containsKey( name ) == false )
		{
			appendToChatArea( "Direct communication open with " + name + "\n", serverFont, serverColor);
			directCommunications.put( name, new PeerToPeerCommunicationFrame( this, login, name, writer ) );
		}
	}

	public void closeAllGames() 
	{
		for ( TronGameFrame game : games.values() )
		{
			appendToChatArea( "Game close with id: " + game.getId() + "\n", serverFont, serverColor);
			game.dispose();
			games.remove( game.getId() );
		}
	}

	public void closeGame( String gameId, boolean destroy) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			appendToChatArea( "Game close with id: " + gameId + "\n", serverFont, serverColor);
			if ( destroy == true )
				((TronGameFrame) games.get( gameId )).dispose();
			games.remove( gameId );
		}
	}

	public void openGame( String gameId, String playerBlue, String playerRed ) 
	{
		if ( games.containsKey( gameId ) == false )
		{
			appendToChatArea( "Game open with id: " + gameId + "\n", serverFont, serverColor);
			try 
			{
				games.put( gameId, new TronGameFrame( writer, gameId, login, playerBlue, playerRed ) );
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
			((TronGameFrame) games.get( gameId )).ready( name );
		}
	}

	public void startGame( String gameId ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			appendToChatArea( "Game " + gameId + " will start\n", serverFont, serverColor);
			((TronGameFrame) games.get( gameId )).start();
		}
	}

	public void startGameSoon(String gameId) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			appendToChatArea( "Game " + gameId + " will start soon\n", serverFont, serverColor);
			((TronGameFrame) games.get( gameId )).startSoon();
		}
	}

	public void askForGameTo(String name) 
	{
		writer.println( MessageType.MessageSystem + " " + MessageType.MessageGameAsked + " " + name );
		writer.flush();
	}

	public void askForGameFrom(String name) 
	{
		int response = JOptionPane.showConfirmDialog(null, "Would you play with " + name + " ?", "Game launch", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if ( response == JOptionPane.OK_OPTION)
		{
			writer.println( MessageType.MessageSystem + " " + MessageType.MessageGameAccepted + " " + name );
			writer.flush();
		}
		else
		{
			writer.println( MessageType.MessageSystem + " " + MessageType.MessageGameRefused + " " + name );
			writer.flush();
		}
	}
	
	public void endGame( String gameId, String winner ) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			((TronGameFrame) games.get( gameId )).end( winner );
			closeGame(gameId, true);
		}
	}

	public void forwardGameMessage(String gameId, String line) 
	{
		if ( games.containsKey( gameId ) == true )
		{
			((TronGameFrame) games.get( gameId )).forwardMessage( line );
		}
	}

	public JList< String > getClientName() 
	{
		return clientName;
	}

	public String getLogin() 
	{
		return login;
	}

	public JScrollPane getClientScrollPane() 
	{
		return clientScrollPane;
	}
}
