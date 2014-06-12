package network;

import java.io.BufferedReader;
import java.io.IOException;

public class NetworkHelper 
{
	public static String fullRead( BufferedReader reader ) throws IOException
	{
		// prepare the resulting line
		String line = new String();
		char[] charBuffer = new char[ 1024 ];
		int size = 0;
		
		do 
		{
			size = reader.read( charBuffer );
			for ( int i = 0; i < size; ++i )
			{
				line += charBuffer[ i ];
			}
		}
		while( size == 1024 );
		
		return line;
	}
}
