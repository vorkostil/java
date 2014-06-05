package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class ChessPiece
{
	/**
	 * 
	 */
	private ChessGameServer chessGameServer;
	public static final int PEON_ID = 0;
	public static final int TOWER_ID = 1;
	public static final int HORSE_ID = 2;
	public static final int BISHOP_ID = 3;
	public static final int QUEEN_ID = 4;
	public static final int KING_ID = 5;
	 
	private int kind;
	private int x;
	private int y;
	private boolean alive = true;
	private boolean isWhite;
	private boolean hasMoved = false;
	
	public ChessPiece( boolean isWhite, int kind, int x, int y ) 
	{
		this.isWhite = isWhite;
		this.kind = kind;
		this.x = x;
		this.y = y;
	}
	
	// modify the internal status of the piece, either the coordinate or the alive status too
	public void modifyStatus( int newX, int newY, boolean isAlive )
	{
		this.x = newX;
		this.y = newY;
		this.alive = isAlive;
		this.hasMoved = true;
	}
	
	// translate the piece information to string for the clients
	public String toString()
	{
		return kind + " " + isWhite + " " + x + " " + y + " " + alive; 
	}

	// return the list of the cells available
	public List<Point> availableMove( ChessGameServer chess ) 
	{
		if ( kind == TOWER_ID )
		{
			return availableMoveForTower( chess );
		}
		else if ( kind == HORSE_ID )
		{
			return availableMoveForHorse( chess );
		}
		else if ( kind == BISHOP_ID )
		{
			return availableMoveForBishop( chess );
		}
		else if ( kind == QUEEN_ID )
		{
			return availableMoveForQueen( chess );
		}
		else if ( kind == KING_ID )
		{
			return availableMoveForKing( chess );
		}
		
		return availableMoveForPeon( chess );
	}

	// beware table is 0,0 on south west corner, not on north west corner
	// white is moving from 0 to 7
	// black is moving from 7 to 0
	private List<Point> availableMoveForPeon( ChessGameServer chess )
	{
		List< Point > result = new ArrayList< Point >();
		
		ChessPiece chessPiece = null;
		if ( isWhite == true )
		{
			// standard move
			if (  ( y + 1 < 8 )
				&&( chess.pieceAt( x, y + 1 ) == null )  )
			{
				result.add( new Point( x, y + 1 ) );

				// double move only if move of one cell is available
				if (  ( y == 1 )
					&&( chess.pieceAt( x, 3 ) == null )  )
				{
					result.add( new Point( x, 3 ) );
				}
			}
			// prise standard
			if (  ( y + 1 < 8 )
				&&( x - 1 >= 0 )
				&&( ( chessPiece = chess.pieceAt( x - 1, y + 1 ) ) != null ) 
				&&( isWhite != chessPiece.isWhite )  )
			{
				result.add( new Point( x - 1, y + 1 ) );
			}
			if (  ( y + 1 < 8 )
				&&( x + 1 < 8 )
				&&( ( chessPiece = chess.pieceAt( x + 1, y + 1 ) ) != null )
				&&( isWhite != chessPiece.isWhite )  )
			{
				result.add( new Point( x + 1, y + 1 ) );
			}
			// prise en passant
			if (  ( y == 5 )
				&&( x - 1 >= 0 )
				&&( chess.getLastMoveUnlockEnPassant() == x - 1 )  )
			{
				result.add( new Point( x - 1, y + 1 ) );
			}
			if (  ( y == 5 )
				&&( x + 1 < 8 )
				&&( chess.getLastMoveUnlockEnPassant() == x + 1 )  )
			{
				result.add( new Point( x + 1, y + 1 ) );
			}
		}
		else
		{
			// standard move
			if (  ( y - 1 >= 0 )
				&&( chess.pieceAt( x, y - 1 ) == null )  )
			{
				result.add( new Point( x, y - 1 ) );

				// double move only if move of one cell is available
				if (  ( y == 6 )
					&&( chess.pieceAt( x, 4 ) == null )  )
				{
					result.add( new Point( x, 4 ) );
				}
			}
			// prise standard
			if (  ( y - 1 >= 0 )
				&&( x - 1 >= 0 )
				&&( ( chessPiece = chess.pieceAt( x - 1, y - 1 ) ) != null )
				&&( isWhite != chessPiece.isWhite )  )
			{
				result.add( new Point( x - 1, y - 1 ) );
			}
			if (  ( y + 1 >= 0 )
				&&( x + 1 < 8 )
				&&( ( chessPiece = chess.pieceAt( x + 1, y - 1 ) ) != null )
				&&( isWhite != chessPiece.isWhite )  )
			{
				result.add( new Point( x + 1, y - 1 ) );
			}
			// prise en passant
			if (  ( y == 4 )
				&&( x - 1 >= 0 )
				&&( this.chessGameServer.lastMoveUnlockEnPassant == x - 1 )  )
			{
				result.add( new Point( x - 1, y - 1 ) );
			}
			if (  ( y == 4 )
				&&( x + 1 < 8 )
				&&( this.chessGameServer.lastMoveUnlockEnPassant == x + 1 )  )
			{
				result.add( new Point( x + 1, y - 1 ) );
			}
		}
		
		return result;
	}

	private List<Point> availableMoveForHorse( ChessGameServer chess )
	{
		List< Point > result = new ArrayList< Point >();
		
		ChessPiece chessPiece = null;
		// compute the 8 position
		// NE
		if (  ( x + 1 < 8 )
			&&( y + 2 < 8 )
			&&(  ( ( chessPiece = chess.pieceAt( x + 1, y + 2 ) ) == null )
			   ||( isWhite != chessPiece.isWhite )  )  )
		{
			result.add( new Point( x + 1, y + 2 ) );
		}
		// EN
		if (  ( x + 2 < 8 )
			&&( y + 1 < 8 )
			&&(  ( ( chessPiece = chess.pieceAt( x + 2, y + 1 ) ) == null )
			   ||( isWhite != chessPiece.isWhite )  )  )
		{
			result.add( new Point( x + 2, y + 1 ) );
		}
		// ES
		if (  ( x + 2 < 8 )
			&&( y - 1 >= 0 )
			&&(  ( ( chessPiece = chess.pieceAt( x + 2, y - 1 ) ) == null )
			   ||( isWhite != chessPiece.isWhite )  )  )
		{
			result.add( new Point( x + 2, y - 1 ) );
		}
		// SE
		if (  ( x + 1 < 8 )
			&&( y - 2 >= 0 )
			&&(  ( ( chessPiece = chess.pieceAt( x + 1, y - 2 ) ) == null )
			   ||( isWhite != chessPiece.isWhite )  )  )
		{
			result.add( new Point( x + 1, y - 2 ) );
		}
		// SW
		if (  ( x - 1 >= 0 )
			&&( y - 2 >= 0 )
			&&(  ( ( chessPiece = chess.pieceAt( x - 1, y - 2 ) ) == null )
			   ||( isWhite != chessPiece.isWhite )  )  )
		{
			result.add( new Point( x - 1, y - 2 ) );
		}
		// WS
		if (  ( x - 2 >= 0 )
			&&( y - 1 >= 0 )
			&&(  ( ( chessPiece = chess.pieceAt( x - 2, y - 1 ) ) == null )
			   ||( isWhite != chessPiece.isWhite )  )  )
		{
			result.add( new Point( x - 2, y - 1 ) );
		}
		// WN
		if (  ( x - 2 >= 0 )
			&&( y + 1 < 8 )
			&&(  ( ( chessPiece = chess.pieceAt( x - 2, y + 1 ) ) == null )
			   ||( isWhite != chessPiece.isWhite )  )  )
		{
			result.add( new Point( x - 2, y + 1 ) );
		}
		// NW
		if (  ( x - 1 >= 0 )
			&&( y + 2 < 8 )
			&&(  ( ( chessPiece = chess.pieceAt( x - 1, y + 2 ) ) == null )
		   	   ||( isWhite!= chessPiece.isWhite )  )  )
		{
			result.add( new Point( x - 1, y + 2 ) );
		}
		
		return result;
	}

	private List<Point> availableMoveForTower( ChessGameServer chess ) 
	{
		List< Point > result = new ArrayList< Point >();
		
		ChessPiece currentPiece = null;

		// compute east cells
		int d = 1;
		while (  ( x + d < 8 )
			   &&( ( currentPiece = chess.pieceAt( x + d, y ) ) == null )  )
		{
			result.add( new Point( x + d, y ) );
			d++;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x + d, y ) );
		}
		
		// compute west cells
		d = -1;
		while (  ( x + d > 0 )
			   &&( ( currentPiece = chess.pieceAt( x + d, y ) ) == null )  )
		{
			result.add( new Point( x + d, y ) );
			d--;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x + d, y ) );
		}
		
		// compute north cells
		d = 1;
		while (  ( y + d < 8 )
			   &&( ( currentPiece = chess.pieceAt( x, y + d ) ) == null )  )
		{
			result.add( new Point( x, y + d ) );
			d++;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x, y +d ) );
		}
		
		// compute south cells
		d = -1;
		while (  ( y + d > 0 )
			   &&( ( currentPiece = chess.pieceAt( x, y + d ) ) == null )  )
		{
			result.add( new Point( x, y + d ) );
			d--;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x, y + d ) );
		}
		
		return result;
	}
	
	private List<Point> availableMoveForBishop( ChessGameServer chess ) 
	{
		List< Point > result = new ArrayList< Point >();
		
		ChessPiece currentPiece = null;

		// compute north east cells
		int d = 1;
		while (  ( x + d < 8 )
			   &&( y + d < 8 )
			   &&( ( currentPiece = chess.pieceAt( x + d, y + d ) ) == null )  )
		{
			result.add( new Point( x + d, y + d ) );
			d++;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x + d, y + d ) );
		}
		
		// compute south west cells
		d = -1;
		while (  ( x + d > 0 )
			   &&( y + d > 0 )
			   &&( ( currentPiece = chess.pieceAt( x + d, y +d ) ) == null )  )
		{
			result.add( new Point( x + d, y + d ) );
			d--;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x + d, y + d ) );
		}
		
		// compute north west cells
		d = 1;
		while (  ( x - d > 0 )
			   &&( y + d < 8 )
			   &&( ( currentPiece = chess.pieceAt( x - d, y + d ) ) == null )  )
		{
			result.add( new Point( x - d, y + d ) );
			d++;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x - d, y +d ) );
		}
		
		// compute south est cells
		d = -1;
		while (  ( x - d < 8 )
			   &&( y + d > 0 )
			   &&( ( currentPiece = chess.pieceAt( x - d, y + d ) ) == null )  )
		{
			result.add( new Point( x - d, y + d ) );
			d--;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x - d, y + d ) );
		}
		
		return result;
	}
	
	private List<Point> availableMoveForQueen( ChessGameServer chess ) 
	{
		List< Point > result = new ArrayList< Point >();
		
		ChessPiece currentPiece = null;

		// compute north east cells
		int d = 1;
		while (  ( x + d < 8 )
			   &&( y + d < 8 )
			   &&( ( currentPiece = chess.pieceAt( x + d, y + d ) ) == null )  )
		{
			result.add( new Point( x + d, y + d ) );
			d++;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x + d, y + d ) );
		}
		
		// compute south west cells
		d = -1;
		while (  ( x + d > 0 )
			   &&( y + d > 0 )
			   &&( ( currentPiece = chess.pieceAt( x + d, y +d ) ) == null )  )
		{
			result.add( new Point( x + d, y + d ) );
			d--;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x + d, y + d ) );
		}
		
		// compute north west cells
		d = 1;
		while (  ( x - d > 0 )
			   &&( y + d < 8 )
			   &&( ( currentPiece = chess.pieceAt( x - d, y + d ) ) == null )  )
		{
			result.add( new Point( x - d, y + d ) );
			d++;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x - d, y +d ) );
		}
		
		// compute south est cells
		d = -1;
		while (  ( x - d < 8 )
			   &&( y + d > 0 )
			   &&( ( currentPiece = chess.pieceAt( x - d, y + d ) ) == null )  )
		{
			result.add( new Point( x - d, y + d ) );
			d--;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x - d, y + d ) );
		}
		
		// compute north east cells
		d = 1;
		while (  ( x + d < 8 )
			   &&( y + d < 8 )
			   &&( ( currentPiece = chess.pieceAt( x + d, y + d ) ) == null )  )
		{
			result.add( new Point( x + d, y + d ) );
			d++;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x + d, y + d ) );
		}
		
		// compute south west cells
		d = -1;
		while (  ( x + d > 0 )
			   &&( y + d > 0 )
			   &&( ( currentPiece = chess.pieceAt( x + d, y +d ) ) == null )  )
		{
			result.add( new Point( x + d, y + d ) );
			d--;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x + d, y + d ) );
		}
		
		// compute north west cells
		d = 1;
		while (  ( x - d > 0 )
			   &&( y + d < 8 )
			   &&( ( currentPiece = chess.pieceAt( x - d, y + d ) ) == null )  )
		{
			result.add( new Point( x - d, y + d ) );
			d++;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x - d, y +d ) );
		}
		
		// compute south est cells
		d = -1;
		while (  ( x - d < 8 )
			   &&( y + d > 0 )
			   &&( ( currentPiece = chess.pieceAt( x - d, y + d ) ) == null )  )
		{
			result.add( new Point( x - d, y + d ) );
			d--;
		}
		// including a cell containing an adversary piece
		if (  ( currentPiece != null )
			&&( isWhite != currentPiece.isWhite )  )
		{
			result.add( new Point( x - d, y + d ) );
		}

		return result;
	}

	private List<Point> availableMoveForKing( ChessGameServer chess ) 
	{
		List< Point > result = new ArrayList< Point >();
		
		ChessPiece currentPiece = null;
		
		// 8 positions around
		for ( int dx = -1; dx < 2; dx++ )
		{
			for ( int dy = -1; dy < 2; dy++ )
			{
				if (  ( dx != 0 )
					||( dy != 0 )  )
				{
					if (  ( x + dx >= 0)
						&&( x + dx < 8)
						&&( y + dy >= 0)
						&&( y + dy < 8)  )
					{
						if (  (  ( ( currentPiece = chess.pieceAt(x + dx, y + dy) ) == null )
							   ||( isWhite != currentPiece.isWhite )  )
							&&( chess.cellIsNotDangerous( isWhite, x + dx, y + dy ) == true )  )
						{
							result.add( new Point( x + dx, y + dy ) );
						}
					}
				}
			}
		}
		
		// roque is available
		if ( isWhite == true )
		{
			// never been moved
			if ( hasMoved == false )
			{
				// tower on W never moved
				ChessPiece tower = null;
				if (  ( ( tower = chess.pieceAt( 0, 0) ) != null )
					&&( tower.hasMoved == false )  )
				{
					// no pieces on the path and the cells start, walk, end are note dangerous
					if (  ( chess.pieceAt( 1, 0) == null )
						&&( chess.pieceAt( 2, 0) == null )
						&&( chess.pieceAt( 3, 0) == null )
						&&( chess.cellIsNotDangerous( isWhite, 2, 0) == true)
						&&( chess.cellIsNotDangerous( isWhite, 3, 0) == true)
						&&( chess.cellIsNotDangerous( isWhite, 4, 0) == true)  )
					{
						result.add( new Point( 2, 0 ) );
					}
				}
				
				// check on E tower
				if (  ( ( tower = chess.pieceAt( 7, 0) ) != null )
						&&( tower.hasMoved == false )  )
				{
					// no pieces on the path and the cells start, walk, end are note dangerous
					if (  ( chess.pieceAt( 5, 0) == null )
						&&( chess.pieceAt( 6, 0) == null )
						&&( chess.cellIsNotDangerous( isWhite, 4, 0) == true)
						&&( chess.cellIsNotDangerous( isWhite, 5, 0) == true)
						&&( chess.cellIsNotDangerous( isWhite, 6, 0) == true)  )
					{
						result.add( new Point( 6, 0 ) );
					}
				}
			}
		}
		else
		{
			// never been moved
			if ( hasMoved == false )
			{
				// tower on W never moved
				ChessPiece tower = null;
				if (  ( ( tower = chess.pieceAt( 0, 7) ) != null )
					&&( tower.hasMoved == false )  )
				{
					// no pieces on the path and the cells start, walk, end are note dangerous
					if (  ( chess.pieceAt( 1, 7) == null )
						&&( chess.pieceAt( 2, 7) == null )
						&&( chess.pieceAt( 3, 7) == null )
						&&( chess.cellIsNotDangerous( isWhite, 2, 7) == true)
						&&( chess.cellIsNotDangerous( isWhite, 3, 7) == true)
						&&( chess.cellIsNotDangerous( isWhite, 4, 7) == true)  )
					{
						result.add( new Point( 2, 7 ) );
					}
				}
				
				// check on E tower
				if (  ( ( tower = chess.pieceAt( 7, 7) ) != null )
						&&( tower.hasMoved == false )  )
				{
					// no pieces on the path and the cells start, walk, end are note dangerous
					if (  ( chess.pieceAt( 5, 7) == null )
						&&( chess.pieceAt( 6, 7) == null )
						&&( chess.cellIsNotDangerous( isWhite, 4, 7) == true)
						&&( chess.cellIsNotDangerous( isWhite, 5, 7) == true)
						&&( chess.cellIsNotDangerous( isWhite, 6, 7) == true)  )
					{
						result.add( new Point( 6, 7 ) );
					}
				}
			}
		}
		
		return result;
	}

	public boolean isWhite() 
	{
		return isWhite;
	}

	public int getX() 
	{
		return x;
	}

	public int getY() 
	{
		return y;
	}

	public int getKind() 
	{
		return kind;
	}
}