package crusade;

/* test 1
5 3
0 0 -3 
0 0 0 
0 2 0 
0 0 0 
-3 0 0 
2
2 0 TOP
0
 * */
/* test 2
8 4
0 -3 0 0 
0 0 0 0 
0 12 3 3 
2 3 12 0 
0 0 0 0 
0 0 2 0 
0 -12 3 2 
2 3 13 0 
1
1 0 TOP
0
 * */
/* test 3
6 9
0 0 0 0 0 -3 
8 3 3 2 2 10 
2 0 0 0 10 13 
11 3 -2 3 1 13 
-3 10 0 0 2 0 
0 6 3 3 4 13 
0 3 0 13 -4 10 
0 13 2 4 10 0 
0 0 0 -3 0 0 
3
5 0 TOP
0
 */
/* test 4
13 10
-3 12 8 6 3 2 7 2 7 0 0 0 0 
11 5 13 0 0 0 3 0 3 0 0 0 0 
0 11 2 2 3 3 8 2 -9 2 3 13 0 
0 0 0 0 0 12 8 3 1 3 2 7 0 
0 0 11 2 3 1 5 2 10 0 0 11 13 
0 0 3 0 0 6 8 0 0 0 0 0 2 
0 0 11 3 3 10 11 2 3 2 3 2 8 
0 12 6 3 2 3 3 6 3 3 2 3 12 
0 11 4 2 3 2 2 11 12 13 13 13 0 
0 0 -3 12 7 8 13 13 4 5 4 10 0 
2
0 0 TOP
0
 */
/* test 5
13 8
0 0 0 0 0 0 -3 0 0 0 0 0 0 
0 0 0 8 3 3 5 2 2 8 2 3 13 
0 0 11 5 13 0 3 0 0 3 0 0 2 
0 10 10 0 3 0 2 0 11 4 10 0 2 
0 3 0 0 2 0 2 0 2 0 3 0 3 
0 2 0 12 10 10 1 2 10 0 3 12 10 
12 6 0 2 0 3 2 12 3 3 10 4 -13 
11 -1 2 -6 2 -6 6 -6 2 3 2 -6 -10 
1
6 0 TOP
0
 * */

/* test 6
10 8
0 0 0 0 0 0 0 0 -3 0 
0 7 -2 3 -2 3 -2 3 11 0 
0 -7 -2 2 2 2 2 2 2 -2 
0 6 -2 2 2 2 2 2 2 -2 
0 -7 -2 2 2 2 2 2 2 -2 
0 8 -2 2 2 2 2 2 2 -2 
0 -7 -2 2 2 2 2 2 2 -2 
0 -3 0 0 0 0 0 0 0 0 
1
8 0 TOP
0
8 1 TOP
1
9 2 RIGHT
7 1 RIGHT
2
8 2 RIGHT
9 3 RIGHT
 * */

/* test 7
10 8
0 -3 0 -3 0 -3 0 -3 -3 0 
0 7 -2 3 -2 2 -2 3 11 0 
0 -7 -2 -2 -2 -2 2 -2 2 -2 
0 6 -2 -2 -2 -2 -2 2 -2 -2 
0 -7 -2 -2 -2 -2 2 -2 2 -2 
0 8 -2 -2 -2 -2 -2 2 -2 -2 
0 -7 -2 -2 -2 -2 2 -2 2 -2 
0 -3 0 0 0 0 0 0 0 0 
1
8 0 TOP
0
8 1 TOP
2
9 2 RIGHT
8 0 TOP
 * */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Cell
{
	int x;
	int y;
	int kind;
	boolean locked = false;
	
	public Cell(int x, int y, int k) 
	{
		this.x = x;
		this.y = y;
		if ( k < 0 )
		{
			locked = true;
			this.kind = -k;
		}
		else
		{
			this.kind = k;
			if ( k < 2 )
				locked = true;
		}
	}

	public Cell(Cell cell) 
	{
		this.x = cell.x;
		this.y = cell.y;
		this.kind = cell.kind;
		this.locked = cell.locked; 
	}

	public String toString()
	{
		return x + " " + y;
	}

	// return true if the cell is symetric on double move
	public boolean isSymetric() 
	{
		return (  ( kind == 2 )
				||( kind == 3 )
				||( kind == 4 )
				||( kind == 5 )  );
	}
}

class Position
{
	int x;
	int y;
	String pos;
	
	Position( Scanner in )
	{
		x= in.nextInt();
		y= in.nextInt();
		pos= in.next();
		System.err.println(x + " " + y + " " + pos);
	}
	
	public Position(int x, int y, String p) 
	{
		this.x = x;
		this.y = y;
		this.pos = p;
	}

	public Position(Position old) 
	{
		this.x = old.x;
		this.y = old.y;
		this.pos = old.pos;
	}

	public String toString()
	{
		return x + " " + y;
	}
}

class State
{
	List< Cell > cells;
    List< Cell > movableCells;
    List< Position > rocks;
    Position indy;
	String action;
//	private State father = null;
//	List< State > children = new ArrayList< State>();
	private int libertyDegree;
    
	public String toString()
	{
		return indy.toString() + " / " + libertyDegree + " / " + action; 
	}
	
    public State( List<Cell> cells, 
    			  List<Cell> movableCells,
    			  List<Position> rocks, 
    			  Position currentPosition,
    			  int libertyDegree ) 
    {
    	this.cells = cells;
    	this.movableCells = movableCells;
    	this.rocks = rocks;
    	this.libertyDegree = libertyDegree;
    	indy = currentPosition;
	}

	public State(State old) 
	{
//		father = old;
		libertyDegree = old.libertyDegree;
		
		cells = new ArrayList<Cell>();
	    movableCells = new ArrayList<Cell>();
	    rocks = new ArrayList<Position>();
	    
		for ( Cell cell : old.cells )
		{
			Cell newCell = new Cell( cell );
			cells.add( newCell );
			if ( newCell.locked == false )
				movableCells.add( newCell );
		}
		for ( Position rock : old.rocks )
			rocks.add( new Position( rock ) );
		indy = new Position( old.indy );
	}

	public boolean win() 
	{
		return indy.x == Player.ex && indy.y == Player.h - 1;
	}

	public boolean loose() 
	{
		if ( Player.isValidMove(cells,indy) == false )
			return true;
		
		for ( Position rock : rocks )
			if ( rock.x == indy.x && rock.y == indy.y )
				return true;

//		for ( Position deadEnd : Player.indyDeadEnd )
//			if ( deadEnd.x == indy.x && deadEnd.y == indy.y && deadEnd.pos.compareTo(indy.pos) == 0 )
//				return true;
		
		return false;
	}

	public List<State> createChildren() 
	{
		List< State > children = new ArrayList< State >();
		
		// always compute the wait
		Position nextPosition = Player.computeNextPosition(cells, indy);
		
		// test the move validity or the out of bounds
		if (  ( nextPosition != null )
			&&( Player.onBoard(nextPosition.x, nextPosition.y) == true )  )
		{
			if ( Player.isValidMove(cells,nextPosition) == true )
			{
				// the move is valid, add the wait transition
				State newState = computeNextState();
				if ( newState != null )
				{
					children.add( newState );
					if ( newState.win() == true )
					{
						return children;
					}
				}
			}
		
			if (  ( libertyDegree > 0 )
				&&( Player.getCellAt(cells, nextPosition).locked == false )  )
			{
				// create the leftState
				State leftState = new State( this );
				Player.changeCellState( leftState.cells, nextPosition.x, nextPosition.y, Player.LEFT );
				if ( Player.isValidMove(leftState.cells, nextPosition) == true )
				{
					leftState.computeTransition();
					leftState.action = nextPosition.x + " " + nextPosition.y + " " + Player.LEFT;
					
					if ( leftState.loose() == false )
					{
						children.add( leftState );
					}					
					else if ( leftState.win() == true )
					{
						return children;
					}
				}
				
				// create the rightState
				State rightState = new State( this );
				Player.changeCellState( rightState.cells, nextPosition.x, nextPosition.y, Player.RIGHT );
				if ( Player.isValidMove(rightState.cells, nextPosition) == true )
				{
					rightState.computeTransition();
					rightState.action = nextPosition.x + " " + nextPosition.y + " " + Player.RIGHT;
					
					if ( rightState.loose() == false )
					{
						children.add( rightState );
					}					
					else if ( rightState.win() == true )
					{
						return children;
					}
				}
			}
			
			if (  ( libertyDegree >= 2 )
				&&( Player.getCellAt(cells, nextPosition).locked == false ) 
				&&( Player.getCellAt(cells, nextPosition).isSymetric() == false )  )
			{
				// create the leftState 2 moves
				State leftState = new State( this );
				Player.changeCellState( leftState.cells, nextPosition.x, nextPosition.y, Player.LEFT );
				Player.changeCellState( leftState.cells, nextPosition.x, nextPosition.y, Player.LEFT );
				if ( Player.isValidMove(leftState.cells, nextPosition) == true )
				{
					leftState.libertyDegree--;
					leftState.computeTransition();
					leftState.action = nextPosition.x + " " + nextPosition.y + " " + Player.LEFT + "," + nextPosition.x + " " + nextPosition.y + " " + Player.LEFT;
					
					if ( leftState.loose() == false )
					{
						children.add( leftState );
					}					
					else if ( leftState.win() == true )
					{
						return children;
					}
				}
			}
			
			// if there is only one children, the Wait's one, try to kill a stone
			// if there is enough liberty
			if ( libertyDegree > 0 )
			{
				for ( Position rock : rocks )
				{
					// always compute the wait
					Position nextRock = Player.computeNextPosition(cells, rock);
					
					// test the move validity or the out of bounds
					if (  ( nextRock != null )
						&&( Player.onBoard(nextRock.x, nextRock.y) == true )
						&&( Player.getCellAt(cells, nextPosition).locked == false )
						&&(  ( nextRock.x != indy.x ) 
						   ||( nextRock.y != indy.y )  )  )
					{
						// create the leftState
						State leftState = new State( this );
						Player.changeCellState( leftState.cells, nextRock.x, nextRock.y, Player.LEFT );
//						if ( Player.isValidMove(leftState.cells, nextRock) == false )
						{
							leftState.computeTransition();
							leftState.action = nextRock.x + " " + nextRock.y + " " + Player.LEFT;
							
							if ( leftState.loose() == false )
							{
								children.add( leftState );
							}					
						}
						
						// create the rightState
						State rightState = new State( this );
						Player.changeCellState( rightState.cells, nextRock.x, nextRock.y, Player.RIGHT );
//						if ( Player.isValidMove(rightState.cells, nextRock) == false )
						{
							rightState.computeTransition();
							rightState.action = nextRock.x + " " + nextRock.y + " " + Player.RIGHT;
							
							if ( rightState.loose() == false )
							{
								children.add( rightState );
							}					
						}
					
						if (  ( libertyDegree > 1 )
							&&( Player.getCellAt(cells, nextRock).isSymetric() == false )  )
						{
							// create the leftState 2 moves
							State doubleState = new State( this );
							Player.changeCellState( doubleState.cells, nextRock.x, nextRock.y, Player.LEFT );
							Player.changeCellState( doubleState.cells, nextRock.x, nextRock.y, Player.LEFT );
//							if ( Player.isValidMove(doubleState.cells, nextRock) == false )
							{
								doubleState.libertyDegree--;
								doubleState.computeTransition();
								doubleState.action = nextRock.x + " " + nextRock.y + " " + Player.LEFT + "," + nextRock.x + " " + nextRock.y + " " + Player.LEFT;
								
								if ( doubleState.loose() == false )
								{
									children.add( doubleState );
								}					
							}
						}
					}
				}
			}
		}
		
		return children;
	}
	
	private State computeNextState() 
	{
		State newState = new State( this );
		newState.libertyDegree++;
		newState.computeTransition();
		newState.action = "WAIT";
		
		if ( newState.loose() == false )
			return newState;
		return null;
	}

	private void computeTransition() 
	{
		// let advance indy
		indy = Player.computeNextPosition(cells, indy);
		
		// and lets advance the rocks
		List< Position > newRocks = new ArrayList< Position >();
		for ( Position rock : rocks )
		{
			Position newRock = Player.computeNextPosition(cells, rock); 
			if ( newRock != null )
				newRocks.add( newRock );
		}
		rocks = newRocks;
	}

}

class Player 
{
	public static final String TOP = "TOP";
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";

	static int w;
	static int h;
	static int ex;
	
	//public static List< Position > indyDeadEnd = new ArrayList< Position >();
	
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // Read init information from standard input, if any
        w = in.nextInt(); System.err.print(w + " ");
        h = in.nextInt(); System.err.println(h);
        
        List< Cell > cells = new ArrayList< Cell >();
        List< Cell > movableCells = new ArrayList< Cell >();
        
    	for ( int y = 0; y < h; y++)
    	{
			for ( int x = 0; x < w; x++)
			{
        		int k = in.nextInt();
        		System.err.print(k + " ");
        		Cell cell = new Cell(x,y,k); 
        		cells.add( cell );
        		if ( cell.locked == false )
        			movableCells.add( cell );
        	}
        	System.err.println();
		}
        
		ex = in.nextInt();
		System.err.println(ex);
		
        int oldNbRocks = 0;
        
        List< String > path = new ArrayList< String >();
        
        while (true) {
            // Read information from standard input
            Position currentPosition = new Position( in );
            int nbRock = in.nextInt();
            System.err.println(nbRock);
            List< Position > rocks = new ArrayList< Position >();
            for ( int i = 0; i < nbRock; i++ )
            {
            	rocks.add( new Position( in ) );
            }
            
            // Compute logic here
            if (  ( path == null )
            	||( path.size() == 0 )
            	||( nbRock != oldNbRocks )  )
            {
            	long begin = System.nanoTime();
            	path = computePath( new State( cells, movableCells, rocks, currentPosition, 1 ), "" );
            	path = shrinkPath( path );
            	long end = System.nanoTime();
            	//indyDeadEnd.clear();
            	System.err.println(" computation take " + ( end - begin ) / 1000 + " µs");
            }
            // Write action to standard output
            
            if (  ( path != null )
            	&&( path.size() > 0 )  )
            {
            	String action = path.remove( 0 );
            	applyAction( cells, action );
            	System.out.println( action );
            }
            else
            {
            	System.out.println( "WAIT" );
            }
            
            oldNbRocks = nbRock;
        }
    }

    private static List<String> shrinkPath( List< String > oldPath ) 
    {
    	if ( oldPath == null )
    		return null;
    	
        List< String > path = new ArrayList< String >();
        for ( String str : oldPath )
        {
        	if ( str.compareTo("WAIT") != 0 )
        	{
        		// manage the double actions
        		String[] splitted = str.split(",");
        		for ( String s : splitted)
        			path.add(s);
        	}
        }
		return path;
	}

	private static void applyAction(List<Cell> cells, String action) 
    {
    	if ( action.compareTo("WAIT") != 0)
    	{
    		String[] splitted = action.split( " " );
    		changeCellState( cells, Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), splitted[2]);
    	}
	}

	public static Cell getCellAt(List<Cell> cells, int x, int y) 
    {
		if ( onBoard( x, y ) == true )
		{
			return cells.get(  y * w + x );
		}
		return null;
	}

	public static boolean onBoard(int x, int y) 
	{
		return x >= 0 && x < w
				&& y >= 0 && y < h;
	}

	public static boolean isValidMove(List<Cell> cells, Position currentPosition) 
    {
		Cell currentCell = getCellAt( cells, currentPosition );
		if ( currentCell == null )
			return false;
		
		switch (currentCell.kind)
		{
		case 1:
			return true;
		case 2:
		case 6:
		case 8:
			return (  ( currentPosition.pos.compareTo(LEFT) == 0 )
					||( currentPosition.pos.compareTo(RIGHT) == 0 )  );
		case 3:
		case 10:
		case 11:
			return ( currentPosition.pos.compareTo(TOP) == 0 );
		case 4:
		case 7:
			return (  ( currentPosition.pos.compareTo(TOP) == 0 )
					||( currentPosition.pos.compareTo(RIGHT) == 0 )  );
		case 5:
		case 9:
			return (  ( currentPosition.pos.compareTo(TOP) == 0 )
					||( currentPosition.pos.compareTo(LEFT) == 0 )  );
		case 12:
			return ( currentPosition.pos.compareTo(RIGHT) == 0 );
		case 13:
			return ( currentPosition.pos.compareTo(LEFT) == 0 );
		}
		
		return false;
	}

	private static List<String> computePath(State state,String action) 
    {
    	List< String > path;
    	if ( state.win() == true )
    	{
    		path = new ArrayList<String>();
    		path.add( action );
    		return path;
    	}
    	
    	//System.err.println( "Exploring indy position --> " + state.indy.toString());
    	
    	List< State > children = state.createChildren();
    	for ( State child : children )
    	{
    		if ( ( path = computePath(child, child.action ) ) != null )
    		{
    			if ( action.length() > 0 )
    				path.add(0, action );
        		return path;
    		}
    	}
    	
		return null;
	}

	// compute the next position whatever the next cell can receive it or not
    // return null if the move is invalid
    // return null if the current cell is of kind 0
    public static Position computeNextPosition(List<Cell> cells, Position currentPosition) 
	{
		Cell currentCell = getCellAt( cells, currentPosition );
		if ( currentCell == null )
			return null;
		
		switch (currentCell.kind)
		{
		case 1:
			return new Position( currentPosition.x, currentPosition.y + 1, TOP );
		case 2:
		case 6:
			if ( currentPosition.pos.compareTo( LEFT ) == 0 )
			{
				return new Position( currentPosition.x + 1, currentPosition.y, LEFT );
			}
			else if ( currentPosition.pos.compareTo( RIGHT ) == 0 )
			{
				return new Position( currentPosition.x - 1, currentPosition.y, RIGHT );
			}
			break;
		case 3:
			return new Position( currentPosition.x, currentPosition.y + 1, TOP );
		case 4:
			if ( currentPosition.pos.compareTo( TOP ) == 0 )
			{
				return new Position( currentPosition.x - 1, currentPosition.y, RIGHT );
			}
			else if ( currentPosition.pos.compareTo( RIGHT ) == 0 )
			{
				return new Position( currentPosition.x, currentPosition.y + 1, TOP );
			}
			break;
		case 5:
			if ( currentPosition.pos.compareTo( TOP ) == 0 )
			{
				return new Position( currentPosition.x + 1, currentPosition.y, LEFT );
			}
			else if ( currentPosition.pos.compareTo( LEFT ) == 0 )
			{
				return new Position( currentPosition.x, currentPosition.y + 1, TOP );
			}
			break;
		case 7:			
		case 8:
		case 9:			
			return new Position( currentPosition.x, currentPosition.y + 1, TOP );
		case 10:
			if ( currentPosition.pos.compareTo( TOP ) == 0 )
			{
				return new Position( currentPosition.x - 1, currentPosition.y, RIGHT );
			}
			break;
		case 11:
			if ( currentPosition.pos.compareTo( TOP ) == 0 )
			{
				return new Position( currentPosition.x + 1, currentPosition.y, LEFT );
			}
			break;
		case 12:
			if ( currentPosition.pos.compareTo( RIGHT ) == 0 )
			{
				return new Position( currentPosition.x, currentPosition.y + 1, TOP );
			}
			break;
		case 13:
			if ( currentPosition.pos.compareTo( LEFT ) == 0 )
			{
				return new Position( currentPosition.x, currentPosition.y + 1, TOP );
			}
			break;
		}
		
		return null;
	}

	public static Cell getCellAt(List<Cell> cells, Position currentPosition) 
	{
		if ( onBoard( currentPosition.x, currentPosition.y ) == true )
		{
			return cells.get(  currentPosition.y * w + currentPosition.x );
		}
		return null;
	}
	public static void changeCellState(List< Cell > cells, int x, int y, String move) 
	{
		computeNewCellKind( Player.getCellAt(cells, x, y ), move );
	}

	public static void computeNewCellKind(Cell cell, String move) 
	{
		switch (cell.kind)
		{
		case 2:
			cell.kind = 3;
			break;
		case 3:
			cell.kind = 2;
			break;
		case 4:
			cell.kind = 5;
			break;
		case 5:
			cell.kind = 4;
			break;
		case 6:
			if ( move.compareTo( Player.LEFT) == 0 )
				cell.kind = 9;
			else
				cell.kind = 7;
			break;
		case 7:
			if ( move.compareTo( Player.LEFT) == 0 )
				cell.kind = 6;
			else
				cell.kind = 8;
			break;
		case 8:
			if ( move.compareTo( Player.LEFT) == 0 )
				cell.kind = 7;
			else
				cell.kind = 9;
			break;
		case 9:
			if ( move.compareTo( Player.LEFT) == 0 )
				cell.kind = 8;
			else
				cell.kind = 6;
			break;
		case 10:
			if ( move.compareTo( Player.LEFT) == 0 )
				cell.kind = 13;
			else
				cell.kind = 11;
			break;
		case 11:
			if ( move.compareTo( Player.LEFT) == 0 )
				cell.kind = 10;
			else
				cell.kind = 12;
			break;
		case 12:
			if ( move.compareTo( Player.LEFT) == 0 )
				cell.kind = 11;
			else
				cell.kind = 13;
			break;
		case 13:
			if ( move.compareTo( Player.LEFT) == 0 )
				cell.kind = 12;
			else
				cell.kind = 10;
			break;
		}
	}
}