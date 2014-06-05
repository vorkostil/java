package game.chess;

import game.AbstractGameFrame;
import game.chess.displayer.ChessMainDisplayer;
import game.chess.displayer.ChessPieceTakenDisplayer;
import game.chess.displayer.ChessPlayDisplayer;
import game.chess.item.CellItem;
import game.chess.item.ChessPieceItem;
import game.chess.item.PlayItem;
import game.chess.model.CellModel;
import game.chess.model.ChessPieceModel;
import game.chess.panel.ChessMainPanel;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;

import java.awt.BorderLayout;
import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.listener.ClosingMessageListener;
import visitor.AbstractDisplayer;

import common.MessageType;

@SuppressWarnings("serial")
public class ChessGameFrame extends AbstractGameFrame 
{
	private static final String WHITE = "white";
	private static final String BLACK = "black";
	private static final String CHESS_CONFIG_PATH = "resources/config/chess.cfg";
	private static final String[] strKind = { "peon", "tower", "horse", "bishop", "queen", "king" };

	private ChessMainPanel gamePanel = null;
	
	private AbstractDisplayer mainDisplayer = new ChessMainDisplayer( 95, 0 );
	private AbstractDisplayer whiteTakenDisplayer = new ChessPieceTakenDisplayer( 0, 0 );
	private AbstractDisplayer blackTakenDisplayer = new ChessPieceTakenDisplayer( 0, 255 );
	private ChessPlayDisplayer playDisplayer = new ChessPlayDisplayer( 607, 0 );
	
	private CellModel validCellModel = new CellModel( 0, 0);
	private CellModel invalidCellModel = new CellModel( 0, 0);
	private CellModel selectedCellModel = new CellModel( 0, 0);
	
	private boolean isPlayerToPlay;
	private boolean isWhite;
	private List< Point > movableCells = new ArrayList< Point >();
	
	//private List< GraphicalItem > chessPieces = new ArrayList< GraphicalItem >();
	
	public ChessGameFrame( PrintWriter writer, 
		  	  			   String gameID, 
		  	  			   String name, 
		  	  			   String blackPlayerName, 
		  	  			   String whitePlayerName ) throws IOException 
	{
		super(writer,gameID,name,CHESS_CONFIG_PATH);
		
		// characteristics of the frame
		this.setTitle( "Chess, playing as " + name );
		this.setSize( ChessMainPanel.frameWidth + 7, ChessMainPanel.frameHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		this.addWindowListener( new ClosingMessageListener( targetWriter, MessageType.MessageSystem + " " + MessageType.MessageGameClose + " " + gameId ) );
	
		// create the main panel
		gamePanel = new ChessMainPanel( this, repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );

		// create the cell's model 
		gamePanel.addItem( new CellItem( validCellModel,
						   				 mainDisplayer,
						   				 repository.getData( "valid_cell_configuration" ),
						   				 tracker,
						   				 ImageLevel.ENVIRONMENT_IMAGE.index() ),
						   GraphicalEnvironment.LAST_LAYER_LEVEL_TO_DRAW );
		gamePanel.addItem( new CellItem( invalidCellModel,
						   				 mainDisplayer,
						   				 repository.getData( "invalid_cell_configuration" ),
						   				 tracker,
						   				 ImageLevel.ENVIRONMENT_IMAGE.index() ),
						   GraphicalEnvironment.LAST_LAYER_LEVEL_TO_DRAW );
		gamePanel.addItem( new CellItem( selectedCellModel,
						   				 mainDisplayer,
						   				 repository.getData( "selected_cell_configuration" ),
						   				 tracker,
						   				 ImageLevel.ENVIRONMENT_IMAGE.index() ),
						   GraphicalEnvironment.LAST_LAYER_LEVEL_TO_DRAW );

		gamePanel.addItem( new PlayItem(playDisplayer,
						   				null,
						   				tracker,
						   				ImageLevel.ENVIRONMENT_IMAGE.index() ),
						   GraphicalEnvironment.LAST_LAYER_LEVEL_TO_DRAW );
						   
		validCellModel.setVisible(false);
		invalidCellModel.setVisible(false);
		selectedCellModel.setVisible(false);
		
		// attach the panel
		this.setLayout( new BorderLayout() );
		this.getContentPane().add( gamePanel, BorderLayout.CENTER );

		// display itself
		this.setVisible( true );
		
		playDisplayer.setBlackPlayer( blackPlayerName );
		playDisplayer.setWhitePlayer( whitePlayerName );
		
		String player = "You play as ";
		if ( getLogin().compareTo( blackPlayerName ) == 0 )
		{
			player += " BLACK\n";
			isWhite = false;
		}
		else
		{
			player += " WHITE\n";
			isWhite = true;
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
		if ( getLogin().compareTo( winner ) == 0 )
		{
			JOptionPane.showMessageDialog(this, "You WIN", "Game finish, " + winner + " wins", JOptionPane.INFORMATION_MESSAGE );
		}
		else
		{
			JOptionPane.showMessageDialog(this, "You LOOSE", "Game finish, " + winner + " wins", JOptionPane.INFORMATION_MESSAGE );
		}
	}

	@Override
	public void startSoon() 
	{
	}

	@Override
	public void forwardMessage(String message) 
	{
		String[] splitted = message.split(" ");
		String action = splitted[ 2 ];
		//String gameId = splitted[ 3 ];
		
		if ( action.compareTo( MessageType.MessageInitPieceInformation ) == 0 )
		{
			int kind = Integer.parseInt(splitted[4]);
			boolean isWhite = Boolean.parseBoolean(splitted[5]); 
			int x = Integer.parseInt(splitted[6]);
			int y = Integer.parseInt(splitted[7]);
			
			String color = BLACK;
			if ( isWhite == true )
			{
				color = WHITE;
			}
			
			try 
			{
				ChessPieceItem pieceItem = new ChessPieceItem( new ChessPieceModel(x,y),
															   mainDisplayer,
															   repository.getData( color + "_" + strKind[ kind ] + "_configuration" ),
															   tracker,
															   ImageLevel.ENVIRONMENT_IMAGE.index() );
				gamePanel.addItem( pieceItem,
								   GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else if ( action.compareTo( MessageType.MessagePlayerToPlay ) == 0 )
		{
			boolean isWhiteToPlay = Boolean.parseBoolean(splitted[4]);
			playDisplayer.isWhiteToPlay( isWhiteToPlay );
			
			this.isPlayerToPlay = ( isWhite == isWhiteToPlay );
		}
		else if ( action.compareTo( MessageType.MessagePlayerMovablePieces ) == 0 )
		{
			if ( isPlayerToPlay == true )
			{
				int size = Integer.parseInt(splitted[4]);
				movableCells.clear();
				for (int i = 0; i < size; i++ )
				{
					movableCells.add( new Point( Integer.parseInt(splitted[5 + i * 2]), Integer.parseInt(splitted[6 + i * 2]) ) );
				}
			}
		}
	}

	public void mouseClickOnCell( int x, int y )
	{
		if ( isPlayerToPlay == true )
		{
			if ( x >= 0 && y >= 0 && x < 8 && y < 8 )
			{
				// TODO manage already selected cell
				validCellModel.setCoord( x, y );
				invalidCellModel.setCoord( x, y );
				
				boolean found = false;
				for ( Point p : movableCells )
				{
					if ( p.x == x && p.y == y )
					{
						found = true;
						break;
					}
				}
				
				if ( found == true )
				{
					selectedCellModel.setVisible(true);
					
					validCellModel.setVisible(false);
					invalidCellModel.setVisible(false);
				}
				else
				{
					validCellModel.setVisible(false);
					invalidCellModel.setVisible(true);
				}
			}
		}
	}
	
	public void mouseMoveOnCell(int x, int y) 
	{
		if ( isPlayerToPlay == true )
		{
			if ( x >= 0 && y >= 0 && x < 8 && y < 8 )
			{
				validCellModel.setCoord( x, y );
				invalidCellModel.setCoord( x, y );
	
				boolean found = false;
				for ( Point p : movableCells )
				{
					if ( p.x == x && p.y == y )
					{
						found = true;
						break;
					}
				}
				
				if ( found == true )
				{
					validCellModel.setVisible(true);
					invalidCellModel.setVisible(false);
				}
				else
				{
					validCellModel.setVisible(false);
					invalidCellModel.setVisible(true);
				}
			}
			else
			{
				validCellModel.setVisible(false);
				invalidCellModel.setVisible(false);
			}
		}
	}
}
