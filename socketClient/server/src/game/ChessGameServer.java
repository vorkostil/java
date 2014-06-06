package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.ClientConnectionManager;

import common.MessageType;

public class ChessGameServer extends AbstractGame
{
	public static final String NAME = "Chess";

	private static final String CREATE_PIECE_INFORMATION_MESSAGE( String id, ChessPiece piece )
	{
		return MessageType.MessageSystem + " " + MessageType.MessageGameInitPieceInformation + " " + id + " " + piece.toString();
	}
	
	private static final String CREATE_UPDATE_PIECE_INFORMATION_MESSAGE( String id, ChessPiece piece )
	{
		return MessageType.MessageSystem + " " + MessageType.MessageGameUpdatePieceInformation + " " + id + " " + piece.getX() + " " + piece.getY();
	}
	
	private static final String CREATE_PLAYER_TURN_TO_PLAY(String id, boolean whiteToPlay ) 
	{
		return MessageType.MessageSystem + " " + MessageType.MessageGamePlayerToPlay + " " + id + " " + whiteToPlay;
	}
	
	private static final String CREATE_PLAYER_MOVABLE_PIECES( String id, List<ChessPiece> movable ) 
	{
		String msg = MessageType.MessageSystem + " " + MessageType.MessageGamePlayerMovablePieces + " " + id + " " + movable.size();
		for ( ChessPiece piece : movable )
		{
			msg += " " + piece.getX() + " " + piece.getY();
		}
		return msg;
	}

	private static final String CREATE_PLAYER_TARGET_CELLS( String id, List<Point> cells ) 
	{
		String msg = MessageType.MessageSystem + " " + MessageType.MessageGamePlayerTargetCells + " " + id + " " + cells.size();
		for ( Point cell : cells )
		{
			msg += " " + (int)cell.getX() + " " + (int)cell.getY();
		}
		return msg;
	}

	private String playerBlack;
	private String playerWhite;
	
	private boolean whiteToPlay = true;
	private ChessPiece board[] = new ChessPiece[64];
	private List< ChessPiece > whitePieceEated = new ArrayList< ChessPiece >();
	private List< ChessPiece > blackPieceEated = new ArrayList< ChessPiece >();
	
	private List< ChessPiece > whitePieceAlive = new ArrayList< ChessPiece >();
	private List< ChessPiece > blackPieceAlive = new ArrayList< ChessPiece >();

	private ChessPiece whiteKing;
	private ChessPiece blackKing;
	
	private List< Point > dangerousCells = new ArrayList< Point >();
	int lastMoveUnlockEnPassant = -1;
	
	public ChessGameServer( String playerWhiteName, 
							String playerBlackName, 
							String gameId, 
							ClientConnectionManager connectionManager ) 
	{
		super( gameId, connectionManager );
		
		System.out.println( "create new chess game, white is " + playerWhiteName + " - black is " + playerBlackName );
		
		// create environment
		addPlayer( playerBlack = playerBlackName );
		addPlayer( playerWhite = playerWhiteName );

		// board 0 = A1, board 63 = G8, A1 is SW
		whitePieceAlive.add( board[ 0 ] = new ChessPiece( true, ChessPiece.TOWER_ID, 0, 0 ) );
		whitePieceAlive.add( board[ 1 ] = new ChessPiece( true, ChessPiece.HORSE_ID, 1, 0 ) );
		whitePieceAlive.add( board[ 2 ] = new ChessPiece( true, ChessPiece.BISHOP_ID, 2, 0 ) );
		whitePieceAlive.add( board[ 3 ] = new ChessPiece( true, ChessPiece.QUEEN_ID, 3, 0 ) );
		whitePieceAlive.add( whiteKing = board[ 4 ] = new ChessPiece( true, ChessPiece.KING_ID, 4, 0 ) );
		whitePieceAlive.add( board[ 5 ] = new ChessPiece( true, ChessPiece.BISHOP_ID, 5, 0 ) );
		whitePieceAlive.add( board[ 6 ] = new ChessPiece( true, ChessPiece.HORSE_ID, 6, 0 ) );
		whitePieceAlive.add( board[ 7 ] = new ChessPiece( true, ChessPiece.TOWER_ID, 7, 0 ) );
		whitePieceAlive.add( board[ 8 ] = new ChessPiece( true, ChessPiece.PEON_ID, 0, 1 ) );
		whitePieceAlive.add( board[ 9 ] = new ChessPiece( true, ChessPiece.PEON_ID, 1, 1 ) );
		whitePieceAlive.add( board[ 10 ] = new ChessPiece( true, ChessPiece.PEON_ID, 2, 1 ) );
		whitePieceAlive.add( board[ 11 ] = new ChessPiece( true, ChessPiece.PEON_ID, 3, 1 ) );
		whitePieceAlive.add( board[ 12 ] = new ChessPiece( true, ChessPiece.PEON_ID, 4, 1 ) );
		whitePieceAlive.add( board[ 13 ] = new ChessPiece( true, ChessPiece.PEON_ID, 5, 1 ) );
		whitePieceAlive.add( board[ 14 ] = new ChessPiece( true, ChessPiece.PEON_ID, 6, 1 ) );
		whitePieceAlive.add( board[ 15 ] = new ChessPiece( true, ChessPiece.PEON_ID, 7, 1 ) );
		
		blackPieceAlive.add( board[ 48 ] = new ChessPiece( false, ChessPiece.PEON_ID, 0, 6 ) );
		blackPieceAlive.add( board[ 49 ] = new ChessPiece( false, ChessPiece.PEON_ID, 1, 6 ) );
		blackPieceAlive.add( board[ 50 ] = new ChessPiece( false, ChessPiece.PEON_ID, 2, 6 ) );
		blackPieceAlive.add( board[ 51 ] = new ChessPiece( false, ChessPiece.PEON_ID, 3, 6 ) );
		blackPieceAlive.add( board[ 52 ] = new ChessPiece( false, ChessPiece.PEON_ID, 4, 6 ) );
		blackPieceAlive.add( board[ 53 ] = new ChessPiece( false, ChessPiece.PEON_ID, 5, 6 ) );
		blackPieceAlive.add( board[ 54 ] = new ChessPiece( false, ChessPiece.PEON_ID, 6, 6 ) );
		blackPieceAlive.add( board[ 55 ] = new ChessPiece( false, ChessPiece.PEON_ID, 7, 6 ) );
		blackPieceAlive.add( board[ 56 ] = new ChessPiece( false, ChessPiece.TOWER_ID, 0, 7 ) );
		blackPieceAlive.add( board[ 57 ] = new ChessPiece( false, ChessPiece.HORSE_ID, 1, 7 ) );
		blackPieceAlive.add( board[ 58 ] = new ChessPiece( false, ChessPiece.BISHOP_ID, 2, 7 ) );
		blackPieceAlive.add( board[ 59 ] = new ChessPiece( false, ChessPiece.QUEEN_ID, 3, 7 ) );
		blackPieceAlive.add( blackKing = board[ 60 ] = new ChessPiece( false, ChessPiece.KING_ID, 4, 7 ) );
		blackPieceAlive.add( board[ 61 ] = new ChessPiece( false, ChessPiece.BISHOP_ID, 5, 7 ) );
		blackPieceAlive.add( board[ 62 ] = new ChessPiece( false, ChessPiece.HORSE_ID, 6, 7 ) );
		blackPieceAlive.add( board[ 63 ] = new ChessPiece( false, ChessPiece.TOWER_ID, 7, 7 ) );
		
		// send the open message
		sendOpenMessageToPlayers();
	}
	
	public void movePiece( int x0, int y0, int x1, int y1 )
	{
		String player = playerBlack;
		if ( whiteToPlay == true )
		{
			player = playerWhite;
		}
		
		if ( isMoveValid( x0, y0, x1, y1 ) == false )
		{
			connectionManager.forwardToClient( player, MessageType.MessageSystem + " " + MessageType.MessageGameMoveInvalid + " " + id);
		}
		else
		{
			// get the piece before movement and prepare the message to send to the clients
			ChessPiece movedPiece = pieceAt( x0, y0 );
			String messageMove = CREATE_UPDATE_PIECE_INFORMATION_MESSAGE( id, movedPiece );
			
			// apply the movement and get the eated piece if any
			ChessPiece eatedPiece = move( x0, y0, x1, y1 );
			if ( eatedPiece != null )
			{
				// prepare the message for the eated piece to send to the clients
				String messageEated = CREATE_UPDATE_PIECE_INFORMATION_MESSAGE( id, eatedPiece );
				
				// add the piece to the eated repository
				if ( whiteToPlay == true )
				{
					blackPieceEated.add( eatedPiece );
					blackPieceAlive.remove( eatedPiece );
				}
				else
				{
					whitePieceEated.add( eatedPiece );
					whitePieceAlive.remove( eatedPiece );
				}
				
				// modify the piece status
				eatedPiece.modifyStatus( -1, -1, false );

				// send the eated piece message
				forwardMessageToAllPlayer( messageEated + " " + eatedPiece.getX() + " " + eatedPiece.getY() + " " + eatedPiece.isAlive() );
			}
			
			// send the moved piece message to clients
			forwardMessageToAllPlayer( messageMove + " " + movedPiece.getX() + " " + movedPiece.getY() + " " + movedPiece.isAlive() );
			
			// check if the game is finish
			String winner = null;
			if ( whiteToPlay == true )
			{
				if ( chessMatOccurs( blackKing ) == true )
				{
					winner = playerWhite;
				}
			}
			else
			{
				if ( chessMatOccurs( whiteKing ) == true )
				{
					winner = playerBlack;
				}
			}
			
			if ( winner != null )
			{
				System.out.println( "Game " + id + " end, winner is " + winner );
				forwardMessageToAllPlayer( MessageType.MessageSystem + " " + MessageType.MessageGameEnd + " " + id + " " + winner );
				connectionManager.closeGame( id );
			}
			else
			{
				// reset the information
				dangerousCells.clear();
				
				// send the next player turn
				whiteToPlay = !whiteToPlay;
				forwardMessageToAllPlayer( CREATE_PLAYER_TURN_TO_PLAY( id, whiteToPlay ) );
				
				// send the list of movable piece
				sendMovablePiece();
			}
		}
	}

	private void sendMovablePiece() 
	{
		// check for all the piece of the player, if path is greater than 1, add it to the list of movable piece
		List< ChessPiece > movable = new ArrayList< ChessPiece >();
		if ( whiteToPlay == true )
		{
			// check for alive white pieces
			for ( ChessPiece piece : whitePieceAlive )
			{
				// and add the cells to the dangerous cells for cache purpose
				List< Point > cells = piece.availableMove( this );
				if ( cells.size() > 0 )
				{
					movable.add( piece );
					dangerousCells.addAll( cells );
				}
			}
			
			// and then, send the movable pieces to the player
			connectionManager.forwardToClient( playerWhite, CREATE_PLAYER_MOVABLE_PIECES( id, movable ) );
		}
		else
		{
			// check for alive white pieces
			for ( ChessPiece piece : blackPieceAlive )
			{
				// and add the cells to the dangerous cells for cache purpose
				List< Point > cells = piece.availableMove( this );
				if ( cells.size() > 0 )
				{
					movable.add( piece );
					dangerousCells.addAll( cells );
				}
			}
			
			// and then, send the movable pieces to the player
			connectionManager.forwardToClient( playerBlack, CREATE_PLAYER_MOVABLE_PIECES( id, movable ) );
		}
	}

	private ChessPiece move(int x0, int y0, int x1, int y1) 
	{
		ChessPiece from = pieceAt( x0, y0 );
		ChessPiece to = pieceAt( x1, y1 );
		
		// check for specific eat behavior if not already eated
		if (  ( to == null )
			&&( from.getKind() == ChessPiece.PEON_ID )  )
		{
			if ( lastMoveUnlockEnPassant == x1 )
			{
				if (  ( from.isWhite() == true )
					&&( y0 == 5 )  )
				{
					to = pieceAt( x1, 5 );
				}
				else if (  ( from.isWhite() == false )
						 &&( y0 == 4 )  )
				{
					to = pieceAt( x1, 4 );
				}
			}
		}

		// if not piece eated, check for roque move
		if (  ( to == null )
			&&( from.getKind() == ChessPiece.KING_ID )  )
		{
			// is it a white king on it's base line
			if (  ( from.isWhite() == true )
				&&( y0 == 0 )  )
			{
				ChessPiece tower;
				
				// check if it move from 2 to the W
				if (  ( x0 == 4 )
					&&( x1 == 2 )
					&&( ( tower = pieceAt( 0, 0) ) != null )  )
				{
					// move the W tower to its new position
					String message = CREATE_UPDATE_PIECE_INFORMATION_MESSAGE( id, tower );
					
					// update the board
					setPieceAt( tower.getX(), tower.getY(), null );
					
					// move the piece
					tower.modifyStatus( 3, 0, true );
					
					// update the board
					setPieceAt( tower.getX(), tower.getY(), tower );
					
					// send the tower message
					forwardMessageToAllPlayer( message + " " + tower.getX() + " " + tower.getY() + " " + tower.isAlive());
				}
				// check if it move from 2 to the E
				else if (  ( x0 == 4 )
						 &&( x1 == 6 )
						 &&( ( tower = pieceAt( 7, 0) ) != null )  )
				{
					// move the W tower to its new position
					String message = CREATE_UPDATE_PIECE_INFORMATION_MESSAGE( id, tower );
					
					// update the board
					setPieceAt( tower.getX(), tower.getY(), null );
					
					// move the piece
					tower.modifyStatus( 5, 0, true );

					// update the board
					setPieceAt( tower.getX(), tower.getY(), tower );
					
					// send the tower message
					forwardMessageToAllPlayer( message + " " + tower.getX() + " " + tower.getY() + " " + tower.isAlive() );
				}
			}
			// or a black king on it's base line
			else if (  ( from.isWhite() == false )
					 &&( y0 == 7 )  )
			{
				ChessPiece tower;
				
				// check if it move from 2 to the W
				if (  ( x0 == 4 )
					&&( x1 == 2 )
					&&( ( tower = pieceAt( 0, 7) ) != null )  )
				{
					// move the W tower to its new position
					String message = CREATE_UPDATE_PIECE_INFORMATION_MESSAGE( id, tower );
					
					// update the board
					setPieceAt( tower.getX(), tower.getY(), null );
					
					// move the piece
					tower.modifyStatus( 3, 7, true );

					// update the board
					setPieceAt( tower.getX(), tower.getY(), tower );
					
					// send the eated piece message
					forwardMessageToAllPlayer( message + " " + tower.getX() + " " + tower.getY() + " " + tower.isAlive() );
				}
				// check if it move from 2 to the E
				else if (  ( x0 == 4 )
						 &&( x1 == 6 )
						 &&( ( tower = pieceAt( 7, 7) ) != null )  )
				{
					// move the W tower to its new position
					String message = CREATE_UPDATE_PIECE_INFORMATION_MESSAGE( id, tower );
					
					// update the board
					setPieceAt( tower.getX(), tower.getY(), null );
					
					// move the piece
					tower.modifyStatus( 5, 7, true );

					// update the board
					setPieceAt( tower.getX(), tower.getY(), tower );
					
					// send the eated piece message
					forwardMessageToAllPlayer( message + " " + tower.getX() + " " + tower.getY() + " " + tower.isAlive() );
				}
			}
		}
		
		// check for the en passant status
		if (  ( to == null )
			&&( from.getKind() == ChessPiece.PEON_ID )
			&&(  (  ( from.isWhite() == true )
				  &&( y0 == 1 )
				  &&( y1 == 3 )  )
			   ||(  ( from.isWhite() == false )
				  &&( y0 == 6 )
				  &&( y1 == 4 )  )  )  )
		{
			lastMoveUnlockEnPassant = x0;
		}
		else
		{
			lastMoveUnlockEnPassant = -1;
		}
		
		// update the board
		setPieceAt( from.getX(), from.getY(), null );
		
		// move the piece
		from.modifyStatus( x1, y1, true );
		
		// update the board
		setPieceAt( from.getX(), from.getY(), from );
		
		return to;
	}

	// check if there a piece at position x0,y0 and if x1,y1 is in the list of acceptable move
	private boolean isMoveValid(int x0, int y0, int x1, int y1) 
	{
		ChessPiece piece = pieceAt( x0, y0 );
		if ( piece == null )
		{
			return false;
		}
		
		List< Point > cells = piece.availableMove( this );
		for ( Point cell : cells )
		{
			if (  ( x1 == cell.getX() )
				&&( y1 == cell.getY() )  )
			{
				return true;
			}
		}
		
		return false;
	}

	// return the piece at position x,y if return null if none or out of bounds 
	public ChessPiece pieceAt( int x, int y ) 
	{
		if (  isCellInBoard( x, y ) == false  )
		{
			return null;
		}
		return board[ y * 8 + x ];
	}
	
	// change the board contents at position x,y 
	public void setPieceAt( int x, int y, ChessPiece piece) 
	{
		if (  isCellInBoard( x, y ) == true )
		{
			board[ y * 8 + x ] = piece;
		}
	}
	
	// check if white or black are in chess mat
	private boolean chessMatOccurs( ChessPiece kingToCheck ) 
	{
		for ( int dx = -1; dx < 2; dx++ )
		{
			for ( int dy = -1; dy < 2; dy ++ )
			{
				if (  ( isCellInBoard( kingToCheck.getX() + dx, kingToCheck.getY() + dy )  == true )
					&&( cellIsNotDangerous( kingToCheck.isWhite(), kingToCheck.getX() + dx, kingToCheck.getY() + dy ) == true )  )
				{
					return false;
				}
			}
		}
		return true;
	}

	// return true if the coordinates are in the board
	private boolean isCellInBoard(int x, int y) 
	{
		return (  ( x >= 0 )
				&&( x < 8 )
				&&( y >= 0 )
				&&( y < 8 )  );
	}

	// return true if the cell is not under attack of an adversary piece
	public boolean cellIsNotDangerous( boolean isWhite, int x, int y) 
	{
		// let's do an awful job by asking the path of all the piece alive of the adversary and checking the cell against it
		// but at least keep a cache to do it only once
		if ( dangerousCells.size() == 0 )
		{
			// look for the black piece path
			if ( isWhite == true )
			{
				// get the path of all black pieces
				for ( ChessPiece piece : blackPieceAlive )
				{
					dangerousCells.addAll( piece.availableMove( this ) );
				}
			}
			else
			{
				// get the path of all white pieces
				for ( ChessPiece piece : whitePieceAlive )
				{
					dangerousCells.addAll( piece.availableMove( this ) );
				}
			}
		}
		
		// and check for the threat
		for ( Point cell : dangerousCells )
		{
			if (  ( cell.getX() == x )
				&&( cell.getY() == y )  )
			{
				return false;
			}
		}
		
		return true;
	}

	public int getLastMoveUnlockEnPassant() 
	{
		return lastMoveUnlockEnPassant;
	}

	@Override
	protected void callGameStart() 
	{
		sendGameStartMessage();
		for ( ChessPiece piece : this.whitePieceAlive )
		{
			forwardMessageToAllPlayer( CREATE_PIECE_INFORMATION_MESSAGE(id, piece));
		}
		for ( ChessPiece piece : this.blackPieceAlive )
		{
			forwardMessageToAllPlayer( CREATE_PIECE_INFORMATION_MESSAGE(id, piece));
		}
		forwardMessageToAllPlayer(CREATE_PLAYER_TURN_TO_PLAY(id, whiteToPlay));
		sendMovablePiece();
	}

	@Override
	public void stop() 
	{
	}

	@Override
	public void manageSpecificMessage(String command) 
	{
		String[] splitted = command.split( " " );
		String action = splitted[2]; 
//		String gameId = splitted[3]; 
		
		if ( action.compareTo( MessageType.MessagePlayerAskTargetCells ) == 0 )
		{
			manageAskTargetCells( Integer.parseInt( splitted[ 4 ] ), Integer.parseInt( splitted[ 5 ] ) ); 
		}
		else if ( action.compareTo( MessageType.MessagePlayerMovePiece ) == 0 )
		{
			movePiece( Integer.parseInt( splitted[ 4 ] ), Integer.parseInt( splitted[ 5 ] ), Integer.parseInt( splitted[ 6 ] ), Integer.parseInt( splitted[ 7 ] ) );
		}
	}

	private void manageAskTargetCells(int x, int y) 
	{
		ChessPiece piece = pieceAt( x, y );
		if ( piece != null )
		{
			List< Point > cells = piece.availableMove( this );
			
			if ( whiteToPlay == true )
			{
				// send the movable pieces to the player
				connectionManager.forwardToClient( playerWhite, CREATE_PLAYER_TARGET_CELLS( id, cells ) );
			}
			else
			{
				// send the movable pieces to the player
				connectionManager.forwardToClient( playerBlack, CREATE_PLAYER_TARGET_CELLS( id, cells ) );
			}
		}
	}

	@Override
	protected String getName() 
	{
		return NAME;
	}

}
