package game;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import main.ClientConnectionManager;

import common.MessageType;

public class ChessGameServer {

	private class ChessPiece
	{
		public static final int TOWER_ID = 1;
		public static final int HORSE_ID = 2;
		public static final int BISHOP_ID = 3;
		public static final int QUEEN_ID = 4;
		public static final int KING_ID = 5;
		public static final int PEON_ID = 6;
		 
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
			return kind + " " + x + " " + y + " " + alive; 
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
		public List<Point> availableMoveForPeon( ChessGameServer chess )
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
					&&( lastMoveUnlockEnPassant == x - 1 )  )
				{
					result.add( new Point( x - 1, y + 1 ) );
				}
				if (  ( y == 5 )
					&&( x + 1 < 8 )
					&&( lastMoveUnlockEnPassant == x + 1 )  )
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
					&&( lastMoveUnlockEnPassant == x - 1 )  )
				{
					result.add( new Point( x - 1, y - 1 ) );
				}
				if (  ( y == 4 )
					&&( x + 1 < 8 )
					&&( lastMoveUnlockEnPassant == x + 1 )  )
				{
					result.add( new Point( x + 1, y - 1 ) );
				}
			}
			
			return result;
		}

		public List<Point> availableMoveForHorse( ChessGameServer chess )
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
			while (  ( x + d > 0 )
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
							if (  ( ( currentPiece = chess.pieceAt(x + dx, y + dy) ) == null )
								||( isWhite != currentPiece.isWhite )  )
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
						// no pieces on the path
						if (  ( chess.pieceAt( 1, 0) == null )
							&&( chess.pieceAt( 2, 0) == null )
							&&( chess.pieceAt( 3, 0) == null )  )
						{
							// the cells are not watched by adversary piece
							boolean roqueIsValid = true;
							
							// no tower or queen on E side
							for ( int i = 5; i < 8; i++ )
							{
								// if there is a piece
								if ( ( currentPiece = chess.pieceAt( i, 0 ) ) != null )
								{
									// and this piece is a Tower or a queen black
									if (  ( currentPiece.isWhite == false )
										&&(  ( currentPiece.kind == TOWER_ID )
										   ||( currentPiece.kind == QUEEN_ID )  )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no tower or queen on N side for each cells
							for ( int i = 2; i < 5 && roqueIsValid; i++ )
							{
								for ( int j = 1; j < 8 && roqueIsValid; j++ )
								{
									if ( ( currentPiece = chess.pieceAt( i, j ) ) != null )
									{
										// and this piece is a Tower or a queen black
										if (  ( currentPiece.isWhite == false )
											&&(  ( currentPiece.kind == TOWER_ID )
											   ||( currentPiece.kind == QUEEN_ID )  )  )
										{
											roqueIsValid = false;
										}
										break;
									}
								}
							}
							
							// no peon on cells (1,1) to (5,1)
							for ( int i = 1; i < 6 && roqueIsValid; i++ )
							{
								if ( ( currentPiece = chess.pieceAt( i, 1 ) ) != null )
								{
									// and this piece is a peon black
									if (  ( currentPiece.isWhite == false )
										&&( currentPiece.kind == PEON_ID )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no horse on (0,1)-(2,1),(4,1)-(6,1)
							for ( int i = 0; i < 7 && roqueIsValid; i++ )
							{
								if ( i != 3 )
								{
									if ( ( currentPiece = chess.pieceAt( i, 1 ) ) != null )
									{
										// and this piece is a horse black
										if (  ( currentPiece.isWhite == false )
											&&( currentPiece.kind == HORSE_ID )  )
										{
											roqueIsValid = false;
										}
										break;
									}
								}
							}
							
							// no horse on (1,2)-(5,2)
							for ( int i = 1; i < 6 && roqueIsValid; i++ )
							{
								if ( ( currentPiece = chess.pieceAt( i, 2 ) ) != null )
								{
									// and this piece is a Tower horse black
									if (  ( currentPiece.isWhite == false )
										&&( currentPiece.kind == HORSE_ID )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no bishop or queen on the diagonal of the 3 cells
							for ( int i = 2; i < 5 && roqueIsValid; i++ )
							{
								for ( int d = 1; d < 8 && roqueIsValid; d++ )
								{
									if ( i - d > 0 )
									{
										if ( ( currentPiece = chess.pieceAt( i - d, d ) ) != null )
										{
											// and this piece is a bishop or a queen black
											if (  ( currentPiece.isWhite == false )
												&&(  ( currentPiece.kind == BISHOP_ID )
												   ||( currentPiece.kind == QUEEN_ID )  )  )
											{
												roqueIsValid = false;
											}
											break;
										}
									}
									else
									{
										break;
									}
								}
								
								for ( int d = 1; d < 8 && roqueIsValid; d++ )
								{
									if ( i + d < 8 )
									{
										if ( ( currentPiece = chess.pieceAt( i + d, d ) ) != null )
										{
											// and this piece is a bishop or a queen black
											if (  ( currentPiece.isWhite == false )
												&&(  ( currentPiece.kind == BISHOP_ID )
												   ||( currentPiece.kind == QUEEN_ID )  )  )
											{
												roqueIsValid = false;
											}
											break;
										}
									}
									else
									{
										break;
									}
								}
							}
							
							// don't test he adversary king as if we are in this case, we better had win
							if ( roqueIsValid == true )
							{
								result.add( new Point( 2, 0 ) );
							}
						}
					}
					
					// check on E tower
					if (  ( ( tower = chess.pieceAt( 7, 0) ) != null )
							&&( tower.hasMoved == false )  )
					{
						// no pieces on the path
						if (  ( chess.pieceAt( 5, 0) == null )
							&&( chess.pieceAt( 6, 0) == null )  )
						{
							// the cells are not watched by adversary piece
							boolean roqueIsValid = true;
							
							// no tower or queen on E side
							for ( int i = 0; i < 4; i++ )
							{
								// if there is a piece
								if ( ( currentPiece = chess.pieceAt( i, 0 ) ) != null )
								{
									// and this piece is a Tower or a queen black
									if (  ( currentPiece.isWhite == false )
										&&(  ( currentPiece.kind == TOWER_ID )
										   ||( currentPiece.kind == QUEEN_ID )  )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no tower or queen on N side for each cells
							for ( int i = 5; i < 7 && roqueIsValid; i++ )
							{
								for ( int j = 1; j < 8 && roqueIsValid; j++ )
								{
									if ( ( currentPiece = chess.pieceAt( i, j ) ) != null )
									{
										// and this piece is a Tower or a queen black
										if (  ( currentPiece.isWhite == false )
											&&(  ( currentPiece.kind == TOWER_ID )
											   ||( currentPiece.kind == QUEEN_ID )  )  )
										{
											roqueIsValid = false;
										}
										break;
									}
								}
							}
							
							// no peon on cells (3,1) to (7,1)
							for ( int i = 3; i < 8 && roqueIsValid; i++ )
							{
								if ( ( currentPiece = chess.pieceAt( i, 1 ) ) != null )
								{
									// and this piece is a peon black
									if (  ( currentPiece.isWhite == false )
										&&( currentPiece.kind == PEON_ID )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no horse on (2,1)-(4,1),(6,1)-(7,1)
							for ( int i = 2; i < 8 && roqueIsValid; i++ )
							{
								if ( i != 5 )
								{
									if ( ( currentPiece = chess.pieceAt( i, 1 ) ) != null )
									{
										// and this piece is a horse black
										if (  ( currentPiece.isWhite == false )
											&&( currentPiece.kind == HORSE_ID )  )
										{
											roqueIsValid = false;
										}
										break;
									}
								}
							}
							
							// no horse on (3,2)-(7,2)
							for ( int i = 3; i < 8 && roqueIsValid; i++ )
							{
								if ( ( currentPiece = chess.pieceAt( i, 2 ) ) != null )
								{
									// and this piece is a Tower horse black
									if (  ( currentPiece.isWhite == false )
										&&( currentPiece.kind == HORSE_ID )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no bishop or queen on the diagonal of the 3 cells
							for ( int i = 6; i < 7 && roqueIsValid; i++ )
							{
								for ( int d = 1; d < 8 && roqueIsValid; d++ )
								{
									if ( i - d > 0 )
									{
										if ( ( currentPiece = chess.pieceAt( i - d, d ) ) != null )
										{
											// and this piece is a bishop or a queen black
											if (  ( currentPiece.isWhite == false )
												&&(  ( currentPiece.kind == BISHOP_ID )
												   ||( currentPiece.kind == QUEEN_ID )  )  )
											{
												roqueIsValid = false;
											}
											break;
										}
									}
									else
									{
										break;
									}
								}
								
								for ( int d = 1; d < 8 && roqueIsValid; d++ )
								{
									if ( i + d < 8 )
									{
										if ( ( currentPiece = chess.pieceAt( i + d, d ) ) != null )
										{
											// and this piece is a bishop or a queen black
											if (  ( currentPiece.isWhite == false )
												&&(  ( currentPiece.kind == BISHOP_ID )
												   ||( currentPiece.kind == QUEEN_ID )  )  )
											{
												roqueIsValid = false;
											}
											break;
										}
									}
									else
									{
										break;
									}
								}
							}
							
							// don't test he adversary king as if we are in this case, we better had win
							if ( roqueIsValid == true )
							{
								result.add( new Point( 6, 0 ) );
							}
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
						// no pieces on the path
						if (  ( chess.pieceAt( 1, 7) == null )
							&&( chess.pieceAt( 2, 7) == null )
							&&( chess.pieceAt( 3, 7) == null )  )
						{
							// the cells are not watched by adversary piece
							boolean roqueIsValid = true;
							
							// no tower or queen on E side
							for ( int i = 5; i < 8; i++ )
							{
								// if there is a piece
								if ( ( currentPiece = chess.pieceAt( i, 7 ) ) != null )
								{
									// and this piece is a Tower or a queen black
									if (  ( currentPiece.isWhite == true )
										&&(  ( currentPiece.kind == TOWER_ID )
										   ||( currentPiece.kind == QUEEN_ID )  )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no tower or queen on N side for each cells
							for ( int i = 2; i < 5 && roqueIsValid; i++ )
							{
								for ( int j = 7; j >= 0 && roqueIsValid; j-- )
								{
									if ( ( currentPiece = chess.pieceAt( i, j ) ) != null )
									{
										// and this piece is a Tower or a queen black
										if (  ( currentPiece.isWhite == true )
											&&(  ( currentPiece.kind == TOWER_ID )
											   ||( currentPiece.kind == QUEEN_ID )  )  )
										{
											roqueIsValid = false;
										}
										break;
									}
								}
							}
							
							// no peon on cells (1,6) to (5,6)
							for ( int i = 1; i < 6 && roqueIsValid; i++ )
							{
								if ( ( currentPiece = chess.pieceAt( i, 6 ) ) != null )
								{
									// and this piece is a peon black
									if (  ( currentPiece.isWhite == false )
										&&( currentPiece.kind == PEON_ID )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no horse on (0,6)-(2,6),(4,6)-(6,6)
							for ( int i = 0; i < 7 && roqueIsValid; i++ )
							{
								if ( i != 3 )
								{
									if ( ( currentPiece = chess.pieceAt( i, 6 ) ) != null )
									{
										// and this piece is a horse black
										if (  ( currentPiece.isWhite == true )
											&&( currentPiece.kind == HORSE_ID )  )
										{
											roqueIsValid = false;
										}
										break;
									}
								}
							}
							
							// no horse on (1,5)-(5,5)
							for ( int i = 1; i < 6 && roqueIsValid; i++ )
							{
								if ( ( currentPiece = chess.pieceAt( i, 5 ) ) != null )
								{
									// and this piece is a Tower horse black
									if (  ( currentPiece.isWhite == true )
										&&( currentPiece.kind == HORSE_ID )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no bishop or queen on the diagonal of the 3 cells
							for ( int i = 2; i < 5 && roqueIsValid; i++ )
							{
								for ( int d = 1; d < 8 && roqueIsValid; d++ )
								{
									if ( i - d > 0 )
									{
										if ( ( currentPiece = chess.pieceAt( i - d, 7 - d ) ) != null )
										{
											// and this piece is a bishop or a queen black
											if (  ( currentPiece.isWhite == true )
												&&(  ( currentPiece.kind == BISHOP_ID )
												   ||( currentPiece.kind == QUEEN_ID )  )  )
											{
												roqueIsValid = false;
											}
											break;
										}
									}
									else
									{
										break;
									}
								}
								
								for ( int d = 1; d < 8 && roqueIsValid; d++ )
								{
									if ( i + d < 8 )
									{
										if ( ( currentPiece = chess.pieceAt( i + d, 7 - d ) ) != null )
										{
											// and this piece is a bishop or a queen black
											if (  ( currentPiece.isWhite == true )
												&&(  ( currentPiece.kind == BISHOP_ID )
												   ||( currentPiece.kind == QUEEN_ID )  )  )
											{
												roqueIsValid = false;
											}
											break;
										}
									}
									else
									{
										break;
									}
								}
							}
							
							// don't test he adversary king as if we are in this case, we better had win
							if ( roqueIsValid == true )
							{
								result.add( new Point( 2, 7 ) );
							}
						}
					}
					
					// check on E tower
					if (  ( ( tower = chess.pieceAt( 7, 7) ) != null )
							&&( tower.hasMoved == false )  )
					{
						// no pieces on the path
						if (  ( chess.pieceAt( 5, 7) == null )
							&&( chess.pieceAt( 6, 7) == null )  )
						{
							// the cells are not watched by adversary piece
							boolean roqueIsValid = true;
							
							// no tower or queen on E side
							for ( int i = 0; i < 4; i++ )
							{
								// if there is a piece
								if ( ( currentPiece = chess.pieceAt( i, 7 ) ) != null )
								{
									// and this piece is a Tower or a queen black
									if (  ( currentPiece.isWhite == true )
										&&(  ( currentPiece.kind == TOWER_ID )
										   ||( currentPiece.kind == QUEEN_ID )  )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no tower or queen on N side for each cells
							for ( int i = 5; i < 7 && roqueIsValid; i++ )
							{
								for ( int j = 1; j < 8 && roqueIsValid; j++ )
								{
									if ( ( currentPiece = chess.pieceAt( i, 7 - j ) ) != null )
									{
										// and this piece is a Tower or a queen black
										if (  ( currentPiece.isWhite == true )
											&&(  ( currentPiece.kind == TOWER_ID )
											   ||( currentPiece.kind == QUEEN_ID )  )  )
										{
											roqueIsValid = false;
										}
										break;
									}
								}
							}
							
							// no peon on cells (3,6) to (7,6)
							for ( int i = 3; i < 8 && roqueIsValid; i++ )
							{
								if ( ( currentPiece = chess.pieceAt( i, 6 ) ) != null )
								{
									// and this piece is a peon black
									if (  ( currentPiece.isWhite == true )
										&&( currentPiece.kind == PEON_ID )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no horse on (2,6)-(4,6),(6,6)-(7,6)
							for ( int i = 2; i < 8 && roqueIsValid; i++ )
							{
								if ( i != 5 )
								{
									if ( ( currentPiece = chess.pieceAt( i, 6 ) ) != null )
									{
										// and this piece is a horse black
										if (  ( currentPiece.isWhite == true )
											&&( currentPiece.kind == HORSE_ID )  )
										{
											roqueIsValid = false;
										}
										break;
									}
								}
							}
							
							// no horse on (3,5)-(7,5)
							for ( int i = 3; i < 8 && roqueIsValid; i++ )
							{
								if ( ( currentPiece = chess.pieceAt( i, 5 ) ) != null )
								{
									// and this piece is a Tower horse black
									if (  ( currentPiece.isWhite == true )
										&&( currentPiece.kind == HORSE_ID )  )
									{
										roqueIsValid = false;
									}
									break;
								}
							}
							
							// no bishop or queen on the diagonal of the 3 cells
							for ( int i = 6; i < 7 && roqueIsValid; i++ )
							{
								for ( int d = 1; d < 8 && roqueIsValid; d++ )
								{
									if ( i - d > 0 )
									{
										if ( ( currentPiece = chess.pieceAt( i - d, 7 - d ) ) != null )
										{
											// and this piece is a bishop or a queen black
											if (  ( currentPiece.isWhite == true )
												&&(  ( currentPiece.kind == BISHOP_ID )
												   ||( currentPiece.kind == QUEEN_ID )  )  )
											{
												roqueIsValid = false;
											}
											break;
										}
									}
									else
									{
										break;
									}
								}
								
								for ( int d = 1; d < 8 && roqueIsValid; d++ )
								{
									if ( i + d < 8 )
									{
										if ( ( currentPiece = chess.pieceAt( i + d, 7 - d ) ) != null )
										{
											// and this piece is a bishop or a queen black
											if (  ( currentPiece.isWhite == true )
												&&(  ( currentPiece.kind == BISHOP_ID )
												   ||( currentPiece.kind == QUEEN_ID )  )  )
											{
												roqueIsValid = false;
											}
											break;
										}
									}
									else
									{
										break;
									}
								}
							}
							
							// don't test he adversary king as if we are in this case, we better had win
							if ( roqueIsValid == true )
							{
								result.add( new Point( 6, 7 ) );
							}
						}
					}
				}
			}
			
			return result;
		}
	}

	private static final String CREATE_PIECE_INFORMATION_MESSAGE( String id, ChessPiece piece )
	{
		return MessageType.MessageSystem + " " + MessageType.MessageGameUpdatePieceInformation + " " + id + " " + piece.toString();
	}
	
	private static final String CREATE_PLAYER_TURN_TO_PLAY(String id, boolean whiteToPlay ) 
	{
		return MessageType.MessageSystem + " " + MessageType.MessageGamePlayerToPlay + " " + id + " " + whiteToPlay;
	}
	
	private ClientConnectionManager manager;
	private String id;
	private String playerBlack;
	private String playerWhite;
	
	private boolean whiteToPlay = true;
	private ChessPiece board[] = new ChessPiece[64];
	private List< ChessPiece > whitePieceEated = new ArrayList< ChessPiece >();
	private List< ChessPiece > blackPieceEated = new ArrayList< ChessPiece >();
	private int lastMoveUnlockEnPassant = -1;
	
	public ChessGameServer( String playerBlackName, 
							String playerWhiteName, 
							String gameId, 
							ClientConnectionManager connectionManager ) 
	{
		// store information
		manager = connectionManager;
		id = gameId;
		
		// create environment
		playerBlack = playerBlackName;
		playerWhite = playerWhiteName;

		// board 0 = A1, board 63 = G8, A1 is SW
		board[ 0 ] = new ChessPiece( true, ChessPiece.TOWER_ID, 0, 0 );
		board[ 1 ] = new ChessPiece( true, ChessPiece.HORSE_ID, 1, 0 );
		board[ 2 ] = new ChessPiece( true, ChessPiece.BISHOP_ID, 2, 0 );
		board[ 3 ] = new ChessPiece( true, ChessPiece.QUEEN_ID, 3, 0 );
		board[ 4 ] = new ChessPiece( true, ChessPiece.KING_ID, 4, 0 );
		board[ 5 ] = new ChessPiece( true, ChessPiece.BISHOP_ID, 5, 0 );
		board[ 6 ] = new ChessPiece( true, ChessPiece.HORSE_ID, 6, 0 );
		board[ 7 ] = new ChessPiece( true, ChessPiece.TOWER_ID, 7, 0 );
		board[ 8 ] = new ChessPiece( true, ChessPiece.PEON_ID, 0, 1 );
		board[ 9 ] = new ChessPiece( true, ChessPiece.PEON_ID, 1, 1 );
		board[ 10 ] = new ChessPiece( true, ChessPiece.PEON_ID, 2, 1 );
		board[ 11 ] = new ChessPiece( true, ChessPiece.PEON_ID, 3, 1 );
		board[ 12 ] = new ChessPiece( true, ChessPiece.PEON_ID, 4, 1 );
		board[ 13 ] = new ChessPiece( true, ChessPiece.PEON_ID, 5, 1 );
		board[ 14 ] = new ChessPiece( true, ChessPiece.PEON_ID, 6, 1 );
		board[ 15 ] = new ChessPiece( true, ChessPiece.PEON_ID, 7, 1 );
		
		board[ 48 ] = new ChessPiece( false, ChessPiece.PEON_ID, 0, 6 );
		board[ 49 ] = new ChessPiece( false, ChessPiece.PEON_ID, 1, 6 );
		board[ 50 ] = new ChessPiece( false, ChessPiece.PEON_ID, 2, 6 );
		board[ 51 ] = new ChessPiece( false, ChessPiece.PEON_ID, 3, 6 );
		board[ 52 ] = new ChessPiece( false, ChessPiece.PEON_ID, 4, 6 );
		board[ 53 ] = new ChessPiece( false, ChessPiece.PEON_ID, 5, 6 );
		board[ 54 ] = new ChessPiece( false, ChessPiece.PEON_ID, 6, 6 );
		board[ 55 ] = new ChessPiece( false, ChessPiece.PEON_ID, 7, 6 );
		board[ 56 ] = new ChessPiece( false, ChessPiece.TOWER_ID, 0, 7 );
		board[ 57 ] = new ChessPiece( false, ChessPiece.HORSE_ID, 1, 7 );
		board[ 58 ] = new ChessPiece( false, ChessPiece.BISHOP_ID, 2, 7 );
		board[ 59 ] = new ChessPiece( false, ChessPiece.QUEEN_ID, 3, 7 );
		board[ 60 ] = new ChessPiece( false, ChessPiece.KING_ID, 4, 7 );
		board[ 61 ] = new ChessPiece( false, ChessPiece.BISHOP_ID, 5, 7 );
		board[ 62 ] = new ChessPiece( false, ChessPiece.HORSE_ID, 6, 7 );
		board[ 63 ] = new ChessPiece( false, ChessPiece.TOWER_ID, 7, 7 );
		
		// send the open message
		manager.forwardToClient( playerBlack, MessageType.MessageSystem + " " + MessageType.MessageGameOpen + " " + id + " " + playerWhite + " " + playerBlack);
		manager.forwardToClient( playerWhite , MessageType.MessageSystem + " " + MessageType.MessageGameOpen + " " + id + " " + playerWhite + " " + playerBlack);
	}
	
	public void movePiece( String player, int x0, int y0, int x1, int y1 )
	{
		if ( isMoveValid( x0, y0, x1, y1 ) == false )
		{
			manager.forwardToClient( player, MessageType.MessageSystem + " " + MessageType.MessageGameMoveInvalid + " " + id);
		}
		else
		{
			// get the piece before movement and prepare the message to send to the clients
			ChessPiece movedPiece = pieceAt( x0, y0 );
			String messageMove = CREATE_PIECE_INFORMATION_MESSAGE( id, movedPiece );
			
			// apply the movement and get the eated piece if any
			ChessPiece eatedPiece = move( x0, y0, x1, y1 );
			if ( eatedPiece != null )
			{
				// prepare the message for the eated piece to send to the clients
				String messageEated = CREATE_PIECE_INFORMATION_MESSAGE( id, eatedPiece );
				
				// add the piece to the eated repository
				if ( whiteToPlay == true )
				{
					blackPieceEated.add( eatedPiece );
				}
				else
				{
					whitePieceEated.add( eatedPiece );
				}
				
				// modify the piece status
				eatedPiece.modifyStatus( -1, -1, false );

				// send the eated piece message
				manager.forwardToClient( playerBlack, messageEated + " " + eatedPiece.toString() );
				manager.forwardToClient( playerWhite , messageEated + " " + eatedPiece.toString() );
			}
			
			// send the moved piece message to clients
			manager.forwardToClient( playerBlack, messageMove + " " + movedPiece.toString() );
			manager.forwardToClient( playerWhite , messageMove + " " + movedPiece.toString() );
			
			// check if the game is finish
			String winner;
			if ( ( winner = chessMatOccurs() ) != null )
			{
				System.out.println( "Game " + id + " end, winner is " + winner );
				manager.forwardToClient( playerBlack, MessageType.MessageSystem + " " + MessageType.MessageGameEnd + " " + id + " " + winner );
				manager.forwardToClient( playerWhite, MessageType.MessageSystem + " " + MessageType.MessageGameEnd + " " + id + " " + winner );
				manager.closeGame( id );
			}
			else
			{
				// send the next player turn
				whiteToPlay = !whiteToPlay;
				manager.forwardToClient( playerBlack, CREATE_PLAYER_TURN_TO_PLAY( id, whiteToPlay ) );
				manager.forwardToClient( playerWhite , CREATE_PLAYER_TURN_TO_PLAY( id, whiteToPlay ) );
				
				// send the list of movable piece
				sendMovablePiece();
			}
		}
	}

	private ChessPiece move(int x0, int y0, int x1, int y1) 
	{
		ChessPiece from = pieceAt( x0, y0 );
		ChessPiece to = pieceAt( x1, y1 );
		
		// check for specific eat behavior if not already eated
		if (  ( to == null )
			&&( from.kind == ChessPiece.PEON_ID )  )
		{
			if ( lastMoveUnlockEnPassant == x1 )
			{
				if (  ( from.isWhite == true )
					&&( y0 == 5 )  )
				{
					to = pieceAt( x1, 5 );
				}
				else if (  ( from.isWhite == false )
						 &&( y0 == 4 )  )
				{
					to = pieceAt( x1, 4 );
				}
			}
		}

		// if not piece eated, check for roque move
		if (  ( to == null )
			&&( from.kind == ChessPiece.KING_ID )  )
		{
			// is it a white king on it's base line
			if (  ( from.isWhite == true )
				&&( y0 == 0 )  )
			{
				ChessPiece tower;
				
				// check if it move from 2 to the W
				if (  ( x0 == 4 )
					&&( x1 == 2 )
					&&( ( tower = pieceAt( 0, 0) ) != null )  )
				{
					// move the W tower to its new position
					String messageEated = CREATE_PIECE_INFORMATION_MESSAGE( id, tower );
					
					tower.modifyStatus( 3, 0, true );

					// send the eated piece message
					manager.forwardToClient( playerBlack, messageEated + " " + tower.toString() );
					manager.forwardToClient( playerWhite , messageEated + " " + tower.toString() );
				}
				// check if it move from 2 to the E
				else if (  ( x0 == 4 )
						 &&( x1 == 6 )
						 &&( ( tower = pieceAt( 7, 0) ) != null )  )
				{
					// move the W tower to its new position
					String messageEated = CREATE_PIECE_INFORMATION_MESSAGE( id, tower );
					
					tower.modifyStatus( 5, 0, true );

					// send the eated piece message
					manager.forwardToClient( playerBlack, messageEated + " " + tower.toString() );
					manager.forwardToClient( playerWhite , messageEated + " " + tower.toString() );
				}
			}
			// or a black king on it's base line
			else if (  ( from.isWhite == false )
					 &&( y0 == 7 )  )
			{
				ChessPiece tower;
				
				// check if it move from 2 to the W
				if (  ( x0 == 4 )
					&&( x1 == 2 )
					&&( ( tower = pieceAt( 0, 7) ) != null )  )
				{
					// move the W tower to its new position
					String messageEated = CREATE_PIECE_INFORMATION_MESSAGE( id, tower );
					
					tower.modifyStatus( 3, 7, true );

					// send the eated piece message
					manager.forwardToClient( playerBlack, messageEated + " " + tower.toString() );
					manager.forwardToClient( playerWhite , messageEated + " " + tower.toString() );
				}
				// check if it move from 2 to the E
				else if (  ( x0 == 4 )
						 &&( x1 == 6 )
						 &&( ( tower = pieceAt( 7, 7) ) != null )  )
				{
					// move the W tower to its new position
					String messageEated = CREATE_PIECE_INFORMATION_MESSAGE( id, tower );
					
					tower.modifyStatus( 5, 7, true );

					// send the eated piece message
					manager.forwardToClient( playerBlack, messageEated + " " + tower.toString() );
					manager.forwardToClient( playerWhite , messageEated + " " + tower.toString() );
				}
			}
		}
		
		from.modifyStatus( x1, y1, true );
		return to;
	}

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

	public ChessPiece pieceAt( int x, int y ) 
	{
		if (  ( x < 0 ) ||( x > 7 )
			||( y < 0 ) ||( y > 7 )  )
		{
			return null;
		}
		return board[ y * 8 + x ];
	}
}
