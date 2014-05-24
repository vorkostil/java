package ragnarok;

import java.util.Scanner;

class MainRagna1
{

    public static void main(String args[]) 
    {
        Scanner in = new Scanner(System.in);

        // Read init information from standard input, if any
        int lx, ly, tx, ty;
        lx = in.nextInt();
        ly = in.nextInt();
        tx = in.nextInt();
        ty = in.nextInt();
        while (true) 
        {
            // Read information from standard input
            in.nextInt();
            
            // Compute logic here
            if ( lx < tx )
            {
            	if ( ly < ty )
            	{
            		System.out.println( "SW" );
            	}
            	else if ( ly > ty )
            	{
            		System.out.println( "NW" );
            	}
            	else
            	{
            		System.out.println( "W" );
            	}
            }
            else if ( lx > ly )
            {
            	if ( ly < ty )
            	{
            		System.out.println( "SE" );
            	}
            	else if ( ly > ty )
            	{
            		System.out.println( "NE" );
            	}
            	else
            	{
            		System.out.println( "E" );
            	}
            }
            else
            {
            	if ( ly < ty )
            	{
            		System.out.println( "S" );
            	}
            	else if ( ly > ty )
            	{
            		System.out.println( "N" );
            	}
            }
        }
    }
}