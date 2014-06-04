package game.chess;

import game.AbstractGameFrame;
import game.chess.panel.ChessMainPanel;
import game.tron.displayer.TronMainDisplayer;
import graphic.GraphicalEnvironment;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import main.listener.ClosingMessageListener;

import common.MessageType;

@SuppressWarnings("serial")
public class ChessGameFrame extends AbstractGameFrame 
{
	private static final String CHESS_CONFIG_PATH = "resources/config/chess.cfg";

	private ChessMainPanel gamePanel = null;

	// TODO create baseGameFrame to share with Tron
	public ChessGameFrame( PrintWriter writer, 
		  	  			   String gameID, 
		  	  			   String name, 
		  	  			   String blackPlayerName, 
		  	  			   String whitePlayerName ) throws IOException 
	{
		super(writer,gameID,name,CHESS_CONFIG_PATH);
		
		// characteristics of the frame
		this.setTitle( "Chess");
		this.setSize( ChessMainPanel.frameWidth + 7, ChessMainPanel.frameHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		this.addWindowListener( new ClosingMessageListener( targetWriter, MessageType.MessageSystem + " " + MessageType.MessageGameClose + " " + gameId ) );
	
		// create the player's model 
		
		// create the main panel
		gamePanel = new ChessMainPanel( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );

		TronMainDisplayer mainPanelDisplayer = new TronMainDisplayer();
		
		// add the gItem to the panel
//		gamePanel.addItem( new TronPlayerItem( bluePlayerModel, 
//											   mainPanelDisplayer, 
//											   repository.getData( BLUE_PLAYER_GRAPHICAL_CONFIGURATION ), 
//											   tracker, 
//											   ImageLevel.ENVIRONMENT_IMAGE.index() ), 
//											   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
		
		this.setLayout( new BorderLayout() );
		this.getContentPane().add( gamePanel, BorderLayout.CENTER );

		// display itself
		this.setVisible( true );
		
		
		String player = "You play as ";
		if ( getLogin().compareTo( blackPlayerName ) == 0 )
		{
			player += " BLACK\n";
		}
		else
		{
			player += " WHITE\n";
		}
		
		// ask for ready
		if ( JOptionPane.showConfirmDialog( null, 
											player + "are you ready to play ?", 
											"Game launch", 
											JOptionPane.YES_NO_OPTION, 
											JOptionPane.QUESTION_MESSAGE ) == JOptionPane.OK_OPTION)
		{
			targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameReady + " " + gameId + " " + getLogin() );
		}
		else
		{
			targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageGameClose );
		}
		targetWriter.flush();
	}

	@Override
	public void ready(String name) 
	{
	}

	@Override
	public void start() 
	{
	}

	@Override
	public void end(String winner) 
	{
	}

	@Override
	public void startSoon() 
	{
	}

	@Override
	public void forwardMessage(String message) 
	{
	}
}
