//package crusade;
//
//import java.awt.Point;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.TimeUnit;
//
//class Cell
//{
//	int kind;
//	boolean locked;
//	int x;
//	int y;
//	
//	public Cell( int value, int column, int line )
//	{
//		if ( value < 0 )
//		{
//			kind = -value;
//			locked = true;
//		}
//		else
//		{
//			kind = value;
//			locked = false;
//		}
//		x = column;
//		y = line;
//	}
//	
//	public int getKind()
//	{
//		return kind;
//	}
//	
//	public boolean isLocked()
//	{
//		return (  ( locked == true )
//				||( kind == 0 )
//				||( kind == 1 )  );
//	}
//	
//	public String toString()
//	{
//		return x + " " + y;
//	}
//
//	public void setKind(int newKind) 
//	{
//		kind = newKind;
//	}
//
//	public int getX() 
//	{
//		return x;
//	}
//
//	public int getY() 
//	{
//		return y;
//	}
//}
//
//class ObjectPosition
//{
//	int x;
//	int y;
//	String pos;
//	
//	ObjectPosition( int x, int y, String pos )
//	{
//		this.x = x;
//		this.y = y;
//		this.pos = pos;
//	}
//
//	public ObjectPosition( Scanner in ) 
//	{
//        x = in.nextInt();
//        y = in.nextInt();
//        pos = in.next();
//	}
//
//	public int getX() 
//	{
//		return x;
//	}
//
//	public int getY() 
//	{
//		return y;
//	}
//
//	public String getPos() 
//	{
//		return pos;
//	}
//	
//	public String toString()
//	{
//		return x + " " + y + " " + pos;
//	}
//	
//}
//class CellVisited
//{
//	int x;
//	int y;
//	String pos;
//	int kind;
//	
//	CellVisited( ObjectPosition op, Cell cell )
//	{
//		this.x = op.getX();
//		this.y = op.getY();
//		this.pos = op.getPos();
//		this.kind = cell.kind;
//	}
//
//	public int getX() 
//	{
//		return x;
//	}
//
//	public int getY() 
//	{
//		return y;
//	}
//
//	public String getPos() 
//	{
//		return pos;
//	}
//	public int getKind()
//	{
//		return kind;
//	}
//	
//	public String toString()
//	{
//		return x + " " + y + " " + pos + " " + kind;
//	}
//	
//}
//class MainCrusade2_old {
//
//    private static final String TOP = "TOP";
//    private static final String LEFT = "LEFT";
//    private static final String RIGHT = "RIGHT";
//    
//	static int width;
//    static int height;    
//    static List< Cell > cells;
//	private static Point exit;
//	
//    /*
//13 8
//0 0 0 0 0 0 -3 0 0 0 0 0 0 
//0 0 0 8 3 3 5 2 2 8 2 3 13 
//0 0 11 5 13 0 3 0 0 3 0 0 2 
//0 10 10 0 3 0 2 0 11 4 10 0 2 
//0 3 0 0 2 0 2 0 2 0 3 0 3 
//0 2 0 12 10 10 1 2 10 0 3 12 10 
//12 6 0 2 0 3 2 12 3 3 10 4 -13 
//11 -1 2 -6 2 -6 6 -6 2 3 2 -6 -10 
//1
//6 0 TOP
//0
//     * */
//    public static void main(String args[]) {
//        Scanner in = new Scanner(System.in);
//
//        // Read init information from standard input, if any
//        width = in.nextInt();
//        height = in.nextInt();
//        cells = new ArrayList< Cell >();
//        
//        for ( int line = 0; line < height; line++ )
//        {
//            for ( int column = 0; column < width; column++ )
//            {
//                cells.add( new Cell( in.nextInt(), column, line ) );
//            }
//        }
//        
//    	exit = new Point( in.nextInt(), height - 1);
//        
//    	List< ObjectPosition > rocks = new ArrayList< ObjectPosition >();
//    	List< String > path = null;
//        int previousRock = 0; 
//        while (true) {
//            // Read information from standard input
//        	ObjectPosition indy = new ObjectPosition( in );
//
//            int rocksNumber = in.nextInt();
//            for ( int i = 0; i < rocksNumber; i++ )
//            {
//            	rocks.add( new ObjectPosition( in ) );
//            }
//            
//            // Compute logic here
//            if (  ( path == null )
//            	||( previousRock != rocksNumber )  )
//            {
//            	long begin = System.nanoTime();
//            	path = computeExitPath( indy );
//            	long end = System.nanoTime();
//            	System.err.println( "time elapsed: " + TimeUnit.MICROSECONDS.convert(end - begin, TimeUnit.NANOSECONDS) );
//            }
//
//            if (  ( path != null ) 
//            	&&( path.size() > 0 )  )
//            {
//            	System.out.println( path.get( 0 ) );
//            	path.remove( 0 );
//            }
//            
//            previousRock = rocksNumber;
//        }
//    }
//
//    // DFS FTW
//    private static List<String> computeExitPath( ObjectPosition indy )
//    {
//    	return null;
//	}
//
//	private static List<ObjectPosition> copyAndAdd(List<ObjectPosition> visited, ObjectPosition indy) 
//	{
//		List<ObjectPosition> result = new ArrayList<ObjectPosition>();
//		for ( ObjectPosition o : visited)
//			result.add(o);
//		result.add(indy);
//		return result;
//	}
//
//	private static boolean notInPosition( List<ObjectPosition> rocks, ObjectPosition indy) 
//	{
//		for ( ObjectPosition pos : rocks )
//		{
//			if (  ( pos.getX() == indy.getX() )
//				&&( pos.getY() == indy.getY() )  )
//			{
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private static boolean notIn(List<CellVisited> visited, ObjectPosition indy, Cell cell) 
//	{
//		for ( CellVisited pos : visited )
//		{
//			if (  ( pos.getX() == indy.getX() )
//				&&( pos.getY() == indy.getY() )
//				&&( pos.getKind() == cell.getKind() )
//				&&( pos.getPos().compareTo( indy.getPos() ) == 0 )  )
//			{
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private static boolean isMoveValid( ObjectPosition indy ) 
//	{
//	    // indy must at least stay on board
//	    if (  ( indy.getX() < 0 )
//	        ||( indy.getY() < 0 )
//	        ||( indy.getX() > width - 1)
//	        ||( indy.getY() > height - 1 )  )
//		{
//	    	return false;
//		}
//	    
//		int cellType = getCellTypeAt( indy.getX(), indy.getY() ).getKind();
//		
//		// compute the validity of the movement
//		switch ( cellType )
//		{
//		case 0:
//			return false;
//		case 2:
//		case 6:
//		case 8:
//			return (  ( indy.getPos().compareTo( LEFT ) == 0 )
//					||( indy.getPos().compareTo( RIGHT ) == 0 )  );
//		case 3:
//		case 10:
//		case 11:
//			return ( indy.getPos().compareTo( TOP ) == 0 );
//		case 4:
//		case 7:
//			return (  ( indy.getPos().compareTo( TOP ) == 0 )
//					||( indy.getPos().compareTo( RIGHT ) == 0 )  );
//		case 5:
//		case 9:
//			return (  ( indy.getPos().compareTo( TOP ) == 0 )
//					||( indy.getPos().compareTo( LEFT ) == 0 )  );
//		case 12:
//			return ( indy.getPos().compareTo( RIGHT ) == 0 );
//		case 13:
//			return ( indy.getPos().compareTo( LEFT ) == 0 );
//		default:
//			return true;
//		}
//	}
//
//	private static void computeNextEnv( Cell cell, String direction ) 
//	{
//		int cellType = cell.getKind();
//		int newCellType;
//		
//		// compute the new kind of cell
//		switch ( cellType )
//		{
//		case 2:
//			newCellType = 3;
//			break;
//		case 3:
//			newCellType = 2;
//			break;
//		case 4:
//			newCellType = 5;
//			break;
//		case 5:
//			newCellType = 4;
//			break;
//		case 6:
//			if ( direction.compareTo( LEFT ) == 0 )
//			{
//				newCellType = 9;
//			}
//			else
//			{
//				newCellType = 7;
//			}
//			break;
//		case 7:
//			if ( direction.compareTo( LEFT ) == 0 )
//			{
//				newCellType = 6;
//			}
//			else
//			{
//				newCellType = 8;
//			}
//			break;
//		case 8:
//			if ( direction.compareTo( LEFT ) == 0 )
//			{
//				newCellType = 7;
//			}
//			else
//			{
//				newCellType = 9;
//			}
//			break;
//		case 9:
//			if ( direction.compareTo( LEFT ) == 0 )
//			{
//				newCellType = 8;
//			}
//			else
//			{
//				newCellType = 6;
//			}
//			break;
//		case 10:
//			if ( direction.compareTo( LEFT ) == 0 )
//			{
//				newCellType = 13;
//			}
//			else
//			{
//				newCellType = 11;
//			}
//			break;
//		case 11:
//			if ( direction.compareTo( LEFT ) == 0 )
//			{
//				newCellType = 10;
//			}
//			else
//			{
//				newCellType = 12;
//			}
//			break;
//		case 12:
//			if ( direction.compareTo( LEFT ) == 0 )
//			{
//				newCellType = 11;
//			}
//			else
//			{
//				newCellType = 13;
//			}
//			break;
//		case 13:
//			if ( direction.compareTo( LEFT ) == 0 )
//			{
//				newCellType = 12;
//			}
//			else
//			{
//				newCellType = 10;
//			}
//			break;
//		default:
//			newCellType = cellType;
//			break;
//		}
//		
//		changeTypeAt( cell.getX(), cell.getY(), newCellType );
//	}
//
//	// compute the next cell given the current cell coordinate and the entry position
//    // return null is no the move is invalid
//	private static ObjectPosition computeNextCell( ObjectPosition currentPosition ) 
//	{
//		// get information
//		int x0 = currentPosition.getX();
//		int y0 = currentPosition.getY();
//		String pos = currentPosition.getPos();
//		
//		// get current cell
//		int cellType = getCellTypeAt( x0, y0).getKind();
//		
//		
//		// compute next cell
//		// case simple full block, no output
//		if ( cellType == 0 )
//		{
//			// NOP
//		}
//		// case 3 to 1 injection
//		else if ( cellType == 1 )
//		{
//			return new ObjectPosition(  x0, y0 + 1, TOP );
//		}
//		// forward direction on horizontal
//		else if ( cellType == 2 )
//		{
//			// left to right
//			if ( pos.compareTo( LEFT ) == 0 )
//			{
//				return new ObjectPosition( x0 + 1, y0, pos );
//			}
//			// right to left
//			else if ( pos.compareTo( RIGHT ) == 0 )
//			{
//				return new ObjectPosition( x0 - 1, y0, pos );
//			}
//		}
//		// forward direction: top to bottom
//		else if ( cellType == 3 )
//		{
//			if ( pos.compareTo( TOP ) == 0 )
//			{
//				return new ObjectPosition( x0, y0 + 1, TOP );
//			}
//		}
//		// simple turn left with block LEFT
//		else if ( cellType == 4 )
//		{
//			if ( pos.compareTo( TOP ) == 0 )
//			{
//				return new ObjectPosition( x0 - 1, y0, RIGHT );
//			}
//			else if ( pos.compareTo( RIGHT ) == 0 )
//			{
//				return new ObjectPosition( x0, y0 + 1, TOP );
//			}
//		}
//		// simple turn right with block RIGHT
//		else if ( cellType == 5 )
//		{
//			if ( pos.compareTo( TOP ) == 0 )
//			{
//				return new ObjectPosition( x0 + 1, y0, LEFT );
//			}
//			else if ( pos.compareTo( LEFT ) == 0 )
//			{
//				return new ObjectPosition( x0, y0 + 1, TOP );
//			}
//		}
//		// forward horizontal but block TOP 
//		else if ( cellType == 6 )
//		{
//			// left to right
//			if ( pos.compareTo( LEFT ) == 0 )
//			{
//				return new ObjectPosition( x0 + 1, y0, pos );
//			}
//			// right to left
//			else if ( pos.compareTo( RIGHT ) == 0 )
//			{
//				return new ObjectPosition( x0 - 1, y0, pos );
//			}
//		}
//		// forward vertical + injection RIGHT
//		else if ( cellType == 7 )
//		{
//			if (  ( pos.compareTo( TOP ) == 0 )
//				||( pos.compareTo( RIGHT ) == 0 )  )
//			{
//				return new ObjectPosition( x0, y0 + 1, TOP );
//			}
//		}
//		// injection RIGHT + LEFT to bottom
//		else if ( cellType == 8 )
//		{
//			if (  ( pos.compareTo( LEFT ) == 0 )
//				||( pos.compareTo( RIGHT ) == 0 )  )
//			{
//				return new ObjectPosition( x0, y0 + 1, TOP );
//			}
//		}
//		// forward vertical + injection LEFT
//		else if ( cellType == 9 )
//		{
//			if (  ( pos.compareTo( TOP ) == 0 )
//				||( pos.compareTo( LEFT ) == 0 )  )
//			{
//				return new ObjectPosition( x0, y0 + 1, TOP );
//			}
//		}
//		// turn TOP to RIGHT with block LEFT
//		else if ( cellType == 10 )
//		{
//			if ( pos.compareTo( TOP ) == 0 )
//			{
//				return new ObjectPosition( x0 - 1, y0, RIGHT );
//			}
//		}
//		// turn TOP to LEFT with block RIGHT
//		else if ( cellType == 11 )
//		{
//			if ( pos.compareTo( TOP ) == 0 )
//			{
//				return new ObjectPosition( x0 + 1, y0, LEFT );
//			}
//		}
//		// turn RIGHT to bottom
//		else if ( cellType == 12 )
//		{
//			if ( pos.compareTo( RIGHT ) == 0 )
//			{
//				return new ObjectPosition( x0, y0 + 1, TOP );
//			}
//		}
//		// turn LEFT to bottom
//		else if ( cellType == 13 )
//		{
//			if ( pos.compareTo( LEFT ) == 0 )
//			{
//				return new ObjectPosition( x0, y0 + 1, TOP );
//			}
//		}
//		return null;
//	}
//
//    public static Cell getCellTypeAt( int column, int line )
//    {
//	    if (  ( column < 0 )
//		    ||( column > width - 1)
//		    ||( line > height - 1 )  )
//		{
//	    	return null;
//		}
//        return cells.get( line * width + column );
//    }
//    
//    public static void changeTypeAt( int column, int line, int newKind )
//    {
//        cells.get( line * width + column ).setKind( newKind );
//    }
//    
//}