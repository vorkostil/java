//package crusade;
//
//import java.awt.Point;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//class MainCrusade1_old
//{
//
//    private static final String TOP = "TOP";
//    private static final String LEFT = "LEFT";
//    private static final String RIGHT = "RIGHT";
//    
//	static int width;
//    static int height;    
//    static List< Integer > cells;
//	
//    public static Integer getCellTypeAt( int column, int line )
//    {
//        return cells.get( line * width + column );
//    }
//    
//    public static void main(String args[]) {
//        Scanner in = new Scanner(System.in);
//
//        // Read init information from standard input, if any
//        width = in.nextInt();
//        height = in.nextInt();
//        cells = new ArrayList< Integer >();
//        
//        for ( int line = 0; line < height; line++ )
//        {
//            for ( int column = 0; column < width; column++ )
//            {
//                cells.add( in.nextInt() );
//            }
//        }
//        
//        int ex = in.nextInt();
//        
//        while (true) {
//            // Read information from standard input
//            int x0 = in.nextInt();
//            int y0 = in.nextInt();
//            String pos = in.next();
//
//            // Compute logic here
//            Point out = computeNextCell( x0, y0, pos );
//            if ( out != null )
//            {
//            	// Write action to standard output
//            	System.out.println( ((int) out.getX()) + " " + ((int) out.getY()) );
//            }
//        }
//    }
//
//    // compute the next cell given the current cell coordinate and the entry position
//    // return null is no cell available after movement (either full bloc cell or invalid move)
//	private static Point computeNextCell(int x0, int y0, String pos) 
//	{
//		int cellType = getCellTypeAt( x0, y0);
//
//		// case simple full block, no output
//		if ( cellType == 0 )
//		{
//			// NOP
//		}
//		// case 3 to 1 injection
//		else if ( cellType == 1 )
//		{
//			return new Point(  x0, y0 + 1 );
//		}
//		// forward direction on horizontal
//		else if ( cellType == 2 )
//		{
//			// left to right
//			if ( pos.compareTo( LEFT ) == 0 )
//			{
//				return new Point( x0 + 1, y0 );
//			}
//			// right to left
//			else
//			{
//				return new Point( x0 - 1, y0 );
//			}
//		}
//		// forward direction: top to bottom
//		else if ( cellType == 3 )
//		{
//			if ( pos.compareTo( TOP ) == 0 )
//			{
//				return new Point( x0, y0 + 1 );
//			}
//		}
//		// simple turn left with block LEFT
//		else if ( cellType == 4 )
//		{
//			if ( pos.compareTo( TOP ) == 0 )
//			{
//				return new Point( x0 - 1, y0 );
//			}
//			else if ( pos.compareTo( RIGHT ) == 0 )
//			{
//				return new Point( x0, y0 + 1 );
//			}
//		}
//		// simple turn right with block RIGHT
//		else if ( cellType == 5 )
//		{
//			if ( pos.compareTo( TOP ) == 0 )
//			{
//				return new Point( x0 + 1, y0 );
//			}
//			else if ( pos.compareTo( LEFT ) == 0 )
//			{
//				return new Point( x0, y0 + 1 );
//			}
//		}
//		// forward horizontal but block TOP 
//		else if ( cellType == 6 )
//		{
//			// left to right
//			if ( pos.compareTo( LEFT ) == 0 )
//			{
//				return new Point( x0 + 1, y0 );
//			}
//			// right to left
//			else if ( pos.compareTo( RIGHT ) == 0 )
//			{
//				return new Point( x0 - 1, y0 );
//			}
//		}
//		// forward vertical + injection RIGHT
//		else if ( cellType == 7 )
//		{
//			return new Point( x0, y0 + 1 );
//		}
//		// injection RIGHT + LEFT to bottom
//		else if ( cellType == 8 )
//		{
//			return new Point( x0, y0 + 1 );
//		}
//		// forward vertical + injection LEFT
//		else if ( cellType == 9 )
//		{
//			return new Point( x0, y0 + 1 );
//		}
//		// turn TOP to RIGHT with block LEFT
//		else if ( cellType == 10 )
//		{
//			if ( pos.compareTo( LEFT ) != 0 )
//			{
//				return new Point( x0 - 1, y0 );
//			}
//		}
//		// turn TOP to LEFT with block RIGHT
//		else if ( cellType == 11 )
//		{
//			if ( pos.compareTo( RIGHT ) != 0 )
//			{
//				return new Point( x0 + 1, y0 );
//			}
//		}
//		// turn RIGHT to bottom
//		else if ( cellType == 12 )
//		{
//			return new Point( x0, y0 + 1 );
//		}
//		// turn LEFT to bottom
//		else if ( cellType == 13 )
//		{
//			return new Point( x0, y0 + 1 );
//		}
//		return null;
//	}
//}