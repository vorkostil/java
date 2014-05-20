package frame;

import game.TronGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.GraphicalClient;

import common.MessageType;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	private PrintWriter targetWriter = null;
	private String gameId = null;
	private GraphicalClient father = null;;
	private GamePanel gamePanel = new GamePanel();
	private String login = null;
	private boolean isBlue = false;
	
	public GameFrame( GraphicalClient client, PrintWriter writer, String gameID, String name, String blue, String red ) 
	{
		father = client;
		targetWriter  = writer;
		gameId  = gameID;
		login = name;
		
		if ( login.compareTo( blue ) == 0 )
		{
			isBlue = true;
		}
		
		// characteristics of the frame
		this.setTitle( "Tron");
		this.setSize( GamePanel.frameSize + 7, GamePanel.frameSize + 29); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		
		this.addWindowListener( new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				if  ( targetWriter != null )
				{
					father.closeSpecificCommunication( gameId, false );
					targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameClose + " " + gameId );
					targetWriter.flush();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		createElement();
	}
	
	private void createElement()
	{
		// create the reference panel
		addKeyListener( new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) 
			{
				if ( arg0.getKeyCode() == KeyEvent.VK_LEFT )
				{
					targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + login + " " + TronGame.LEFT );
					targetWriter.flush();
				}
				else if ( arg0.getKeyCode() == KeyEvent.VK_RIGHT )
				{
					targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + login + " " + TronGame.RIGHT );
					targetWriter.flush();
				}
				else if ( arg0.getKeyCode() == KeyEvent.VK_UP )
				{
					targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + login + " " + TronGame.UP );
					targetWriter.flush();
				}
				else if ( arg0.getKeyCode() == KeyEvent.VK_DOWN )
				{
					targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameChangeDirection + " " + gameId + " " + login + " " + TronGame.DOWN );
					targetWriter.flush();
				}
			}
		});

		// associate the panel to the frame
		this.setContentPane(gamePanel);

		// display itself
		this.setVisible(true);
		
		// ask for ready
		String player = "You play as ";
		if ( isBlue == true )
		{
			player += " BLUE\n";
		}
		else
		{
			player += " RED\n";
		}
		int response = JOptionPane.showConfirmDialog(null, player + "are you ready to play ?", "Game launch", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if ( response == JOptionPane.OK_OPTION)
		{
			targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameReady + " " + gameId + " " + login );
			targetWriter.flush();
		}
		else
		{
			targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameClose );
			targetWriter.flush();
		}
	}

	public void ready(String name) 
	{
		// receive the ready flag from a player (could be himself)
	}

	public void start() 
	{
		// receive the start flag, the game begin
	}

	public void updateInformation( double blueX, double blueY, List<Point2D> bp, double redX, double redY, List<Point2D> rp ) 
	{
		// receive player position update
		gamePanel.changeBluePlayerCoordinate( blueX, blueY );
		gamePanel.changeRedPlayerCoordinate( redX, redY );
		gamePanel.updateBluePlayerPath( bp );
		gamePanel.updateRedPlayerPath( rp );
	}

	public String getId() 
	{
		return gameId;
	}

	public void end(String winner) 
	{
		if ( login.compareTo( winner ) == 0 )
		{
			JOptionPane.showMessageDialog(this, "You WIN", "Game finish, " + winner + " wins", JOptionPane.INFORMATION_MESSAGE );
		}
		else
		{
			JOptionPane.showMessageDialog(this, "You LOOSE", "Game finish, " + winner + " wins", JOptionPane.INFORMATION_MESSAGE );
		}
	}
}
