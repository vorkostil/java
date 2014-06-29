package client;

import game.AbstractGameClientFrame;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;

import java.awt.BorderLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import client.displayer.ChessMainDisplayer;
import client.displayer.ChessPieceTakenDisplayer;
import client.displayer.ChessPlayDisplayer;
import client.item.CellItem;
import client.item.ChessPieceItem;
import client.model.CellModel;
import client.model.ChessPieceModel;
import client.panel.ChessMainPanel;

import common.ChessCommonInformation;
import common.MessageType;

import displayer.AbstractDisplayer;

@SuppressWarnings("serial")
public class ChessGameFrame extends AbstractGameClientFrame 
{
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
	private List< Point > targetCells = new ArrayList< Point >();
	private Point cellSelected = null;

	private boolean gameIsStarted = false;
	
	public ChessGameFrame() throws IOException 
	{
		super( ChessCommonInformation.CHESS_CONFIG_PATH );
		
		// characteristics of the frame
		this.setTitle( ChessCommonInformation.GAME_SHORT_NAME );
		this.setSize( ChessMainPanel.frameWidth + 7, ChessMainPanel.frameHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
	
		// create the main panel
		gamePanel = new ChessMainPanel( this, repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		gamePanel.addDisplayer( ChessMainDisplayer.NAME, mainDisplayer );
		gamePanel.addDisplayer( "WHITE" + ChessPieceTakenDisplayer.NAME, whiteTakenDisplayer );
		gamePanel.addDisplayer( "BLACK" + ChessPieceTakenDisplayer.NAME, blackTakenDisplayer );
		gamePanel.addDisplayer( ChessPlayDisplayer.NAME, playDisplayer );
		
		// create the cell's model 
		gamePanel.addItem( new CellItem( validCellModel,
						   				 repository.getData( "valid_cell_configuration" ),
						   				 tracker,
						   				 ImageLevel.ENVIRONMENT_IMAGE.index() ),
						   ChessMainDisplayer.NAME,
						   GraphicalEnvironment.LAST_LAYER_LEVEL_TO_DRAW );
		gamePanel.addItem( new CellItem( invalidCellModel,
						   				 repository.getData( "invalid_cell_configuration" ),
						   				 tracker,
						   				 ImageLevel.ENVIRONMENT_IMAGE.index() ),
						   ChessMainDisplayer.NAME,
						   GraphicalEnvironment.LAST_LAYER_LEVEL_TO_DRAW );
		gamePanel.addItem( new CellItem( selectedCellModel,
						   				 repository.getData( "selected_cell_configuration" ),
						   				 tracker,
						   				 ImageLevel.ENVIRONMENT_IMAGE.index() ),
						   ChessMainDisplayer.NAME,
						   GraphicalEnvironment.LAST_LAYER_LEVEL_TO_DRAW );
						   
		validCellModel.setVisible(false);
		invalidCellModel.setVisible(false);
		selectedCellModel.setVisible(false);
		
		gamePanel.computeDisplayableItems();
		
		// attach the panel
		this.setLayout( new BorderLayout() );
		this.getContentPane().add( gamePanel, BorderLayout.CENTER );

		// create the piece
		try 
		{
//			whitePieceAlive.add( board[ 0 ] = new ChessPiece( true, ChessPiece.TOWER_ID, 0, 0 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 0, 0),
												   repository.getData( "white_tower_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
			
//			whitePieceAlive.add( board[ 1 ] = new ChessPiece( true, ChessPiece.HORSE_ID, 1, 0 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 1, 0),
												   repository.getData( "white_horse_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
			
//			whitePieceAlive.add( board[ 2 ] = new ChessPiece( true, ChessPiece.BISHOP_ID, 2, 0 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 2, 0),
												   repository.getData( "white_bishop_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
			
//			whitePieceAlive.add( board[ 3 ] = new ChessPiece( true, ChessPiece.QUEEN_ID, 3, 0 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 3, 0),
												   repository.getData( "white_queen_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );
							
//			whitePieceAlive.add( whiteKing = board[ 4 ] = new ChessPiece( true, ChessPiece.KING_ID, 4, 0 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 4, 0),
												   repository.getData( "white_king_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 5 ] = new ChessPiece( true, ChessPiece.BISHOP_ID, 5, 0 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 5, 0),
												   repository.getData( "white_bishop_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 6 ] = new ChessPiece( true, ChessPiece.HORSE_ID, 6, 0 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 6, 0),
												   repository.getData( "white_horse_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 7 ] = new ChessPiece( true, ChessPiece.TOWER_ID, 7, 0 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 7, 0),
												   repository.getData( "white_tower_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 8 ] = new ChessPiece( true, ChessPiece.PEON_ID, 0, 1 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 0, 1),
												   repository.getData( "white_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 9 ] = new ChessPiece( true, ChessPiece.PEON_ID, 1, 1 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 1, 1),
												   repository.getData( "white_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 10 ] = new ChessPiece( true, ChessPiece.PEON_ID, 2, 1 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 2, 1),
												   repository.getData( "white_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 11 ] = new ChessPiece( true, ChessPiece.PEON_ID, 3, 1 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 3, 1),
												   repository.getData( "white_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 12 ] = new ChessPiece( true, ChessPiece.PEON_ID, 4, 1 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 4, 1),
												   repository.getData( "white_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 13 ] = new ChessPiece( true, ChessPiece.PEON_ID, 5, 1 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 5, 1),
												   repository.getData( "white_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 14 ] = new ChessPiece( true, ChessPiece.PEON_ID, 6, 1 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 6, 1),
												   repository.getData( "white_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			whitePieceAlive.add( board[ 15 ] = new ChessPiece( true, ChessPiece.PEON_ID, 7, 1 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 7, 1),
												   repository.getData( "white_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 48 ] = new ChessPiece( false, ChessPiece.PEON_ID, 0, 6 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 0, 6),
												   repository.getData( "black_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 49 ] = new ChessPiece( false, ChessPiece.PEON_ID, 1, 6 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 1, 6),
												   repository.getData( "black_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 50 ] = new ChessPiece( false, ChessPiece.PEON_ID, 2, 6 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 2, 6),
												   repository.getData( "black_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 51 ] = new ChessPiece( false, ChessPiece.PEON_ID, 3, 6 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 3, 6),
												   repository.getData( "black_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 52 ] = new ChessPiece( false, ChessPiece.PEON_ID, 4, 6 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 4, 6),
												   repository.getData( "black_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 53 ] = new ChessPiece( false, ChessPiece.PEON_ID, 5, 6 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 5, 6),
												   repository.getData( "black_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 54 ] = new ChessPiece( false, ChessPiece.PEON_ID, 6, 6 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 6, 6),
												   repository.getData( "black_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 55 ] = new ChessPiece( false, ChessPiece.PEON_ID, 7, 6 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 7, 6),
												   repository.getData( "black_peon_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 56 ] = new ChessPiece( false, ChessPiece.TOWER_ID, 0, 7 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 0, 7),
												   repository.getData( "black_tower_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 57 ] = new ChessPiece( false, ChessPiece.HORSE_ID, 1, 7 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 1, 7),
												   repository.getData( "black_horse_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 58 ] = new ChessPiece( false, ChessPiece.BISHOP_ID, 2, 7 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 2, 7),
												   repository.getData( "black_bishop_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 59 ] = new ChessPiece( false, ChessPiece.QUEEN_ID, 3, 7 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 3, 7),
												   repository.getData( "black_queen_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( blackKing = board[ 60 ] = new ChessPiece( false, ChessPiece.KING_ID, 4, 7 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 4, 7),
												   repository.getData( "black_king_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 61 ] = new ChessPiece( false, ChessPiece.BISHOP_ID, 5, 7 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 5, 7),
												   repository.getData( "black_bishop_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 62 ] = new ChessPiece( false, ChessPiece.HORSE_ID, 6, 7 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 6, 7),
												   repository.getData( "black_horse_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

//			blackPieceAlive.add( board[ 63 ] = new ChessPiece( false, ChessPiece.TOWER_ID, 7, 7 ) );
			gamePanel.addItem( new ChessPieceItem( new ChessPieceModel( 7, 7),
												   repository.getData( "black_tower_configuration" ),
												   tracker,
												   ImageLevel.ENVIRONMENT_IMAGE.index() ),
							ChessMainDisplayer.NAME,
							GraphicalEnvironment.FIRST_LAYER_LEVEL_TO_DRAW );

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		// display itself
		this.setVisible( true );
	}

	public void start(String whitePlayer, String blackPlayer) 
	{
		// manage the players
		addPlayer( blackPlayer );
		addPlayer( whitePlayer );
		
		// and set white as beginner
		playDisplayer.isWhiteToPlay( true );
		this.isPlayerToPlay = ( isWhite == true );
		
		// and set the start flag
		this.gameIsStarted  = true;
	}

	public void end(String winner) 
	{
		if ( connectionClient.getLogin().compareTo( winner ) == 0 )
		{
			JOptionPane.showMessageDialog(this, "You WIN", "Game finish, " + winner + " wins", JOptionPane.INFORMATION_MESSAGE );
		}
		else
		{
			JOptionPane.showMessageDialog(this, "You LOOSE", "Game finish, " + winner + " wins", JOptionPane.INFORMATION_MESSAGE );
		}
	}

	@Override
	public void handleServerMessage(String message ) 
	{
		String[] messageComponents = message.split( " " );
		String action = messageComponents[ 0 ];
		
		if ( action.compareTo( MessageType.MessageStart ) == 0 )
		{
			start( messageComponents[ 1 ], messageComponents[ 2 ] );
		}
		else if ( action.compareTo( MessageType.MessageEnd ) == 0 )
		{
			end( messageComponents[ 1 ] );
		}
		else if ( action.compareTo( MessageType.MessagePlayerToPlay ) == 0 )
		{
			boolean isWhiteToPlay = Boolean.parseBoolean( messageComponents[ 1 ] );
			playDisplayer.isWhiteToPlay( isWhiteToPlay );
			
			this.isPlayerToPlay = ( isWhite == isWhiteToPlay );
		}
		else if ( action.compareTo( MessageType.MessagePlayerMovablePieces ) == 0 )
		{
			if (  ( isPlayerToPlay == true )
				||( gameIsStarted == false )  )
			{
				int size = Integer.parseInt(messageComponents[ 1 ]);
				movableCells.clear();
				for (int i = 0; i < size; i++ )
				{
					movableCells.add( new Point( Integer.parseInt(messageComponents[2 + i * 2]), Integer.parseInt(messageComponents[3 + i * 2]) ) );
				}
			}
		}
		else if ( action.compareTo( MessageType.MessagePlayerTargetCells ) == 0 )
		{
			if ( isPlayerToPlay == true )
			{
				int size = Integer.parseInt(messageComponents[ 1 ]);
				targetCells.clear();
				for (int i = 0; i < size; i++ )
				{
					targetCells.add( new Point( Integer.parseInt(messageComponents[2 + i * 2]), Integer.parseInt(messageComponents[3 + i * 2]) ) );
				}
			}
		}
		else if ( action.compareTo( MessageType.MessageUpdatePieceInformation ) == 0 )
		{
			ChessPieceItem piece = gamePanel.findChessPieceAt( Integer.parseInt(messageComponents[ 1 ]), Integer.parseInt(messageComponents[ 2 ]) );
			if ( piece != null )
			{
				boolean isAlive = Boolean.parseBoolean(messageComponents[5]);
				piece.getModel().update( Integer.parseInt(messageComponents[ 3 ]), Integer.parseInt(messageComponents[ 4 ]), isAlive );
				
				if ( isAlive == false )
				{
					mainDisplayer.removeItem(piece, AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW);
					if ( isPlayerToPlay == isWhite )
					{
						blackTakenDisplayer.addItem(piece, AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW);
					}
					else
					{
						whiteTakenDisplayer.addItem(piece, AbstractDisplayer.FIRST_LAYER_LEVEL_TO_DRAW);
					}
				}
			}
		}
		gamePanel.computeDisplayableItems();
	}

	public void mouseClickOnCell( int x, int y )
	{
		if (  ( isPlayerToPlay == true )
			&&( gameIsStarted == true )  )
		{
			if ( x >= 0 && y >= 0 && x < 8 && y < 8 )
			{
				if ( cellSelected != null )
				{
					if ( cellSelected.x == x && cellSelected.y == y )
					{
						// unselect
						cellSelected = null;
						validCellModel.setCoord( x, y );
						validCellModel.setVisible(false);
						
						invalidCellModel.setVisible(false);
						selectedCellModel.setVisible(false);
						
					}
					else
					{
						boolean found = false;
						for ( Point p : targetCells )
						{
							if ( p.x == x && p.y == y )
							{
								found = true;
								break;
							}
						}
						
						if ( found == true )
						{
							// a valid move, send it to the server
							connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessagePlayerMovePiece + " " + cellSelected.x + " " + cellSelected.y + " " + x + " " + y );
							
							// reset information
							validCellModel.setVisible(false);
							invalidCellModel.setVisible(false);
							selectedCellModel.setVisible(false);
							cellSelected = null;
						}
					}
				}
				else
				{
					// check if the current cell is selectable
					boolean found = false;
					for ( Point p : movableCells )
					{
						if ( p.x == x && p.y == y )
						{
							found = true;
							break;
						}
					}
					
					// if it is, select it
					if ( found == true )
					{
						cellSelected  = new Point( x, y );
						
						// select the cell
						selectedCellModel.setCoord( x, y );
						selectedCellModel.setVisible(true);

						validCellModel.setVisible(false);
						invalidCellModel.setVisible(false);
						
						// ask for target cells to the server
						connectionClient.sendMessageIfConnected( MessageType.MessageGame + " " + gameId + " " + MessageType.MessagePlayerAskTargetCells + " " + x + " " + y );
					}
				}
			}
		}
		gamePanel.computeDisplayableItems();
	}
	
	public void mouseMoveOnCell(int x, int y) 
	{
		if (  ( isPlayerToPlay == true )
			&&( gameIsStarted == true )  )
		{
			if ( x >= 0 && y >= 0 && x < 8 && y < 8 )
			{
				validCellModel.setCoord( x, y );
				invalidCellModel.setCoord( x, y );
	
				boolean found = false;
				if ( cellSelected != null )
				{
					for ( Point p : targetCells )
					{
						if ( p.x == x && p.y == y )
						{
							found = true;
							break;
						}
					}
				}
				else
				{
					for ( Point p : movableCells )
					{
						if ( p.x == x && p.y == y )
						{
							found = true;
							break;
						}
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
		gamePanel.computeDisplayableItems();
	}

	public void addPlayer(String playerName) 
	{
		if ( playDisplayer.isBlackPlayerNameInitialized() == false )
		{
			playDisplayer.setBlackPlayer( playerName );
			if ( connectionClient.getLogin().compareTo( playerName ) == 0 )
			{
				isWhite = false;
				this.setTitle( ChessCommonInformation.GAME_SHORT_NAME + " (" + connectionClient.getLogin() + ") playing BLACK" );
			}
		}
		else
		{
			playDisplayer.setWhitePlayer( playerName );
			if ( connectionClient.getLogin().compareTo( playerName ) == 0 )
			{
				isWhite = true;
				this.setTitle( ChessCommonInformation.GAME_SHORT_NAME + " (" + connectionClient.getLogin() + ") playing WHITE" );
			}
		}
	}
}
