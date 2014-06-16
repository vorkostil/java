package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

public class NetworkHelper 
{
	public static String readOnSocket( BufferedReader reader ) throws IOException
	{
		// prepare the resulting line
		String line = new String();
		
		// the temporary storage buffer
		char[] charBuffer;
		
		// the number of character read
		int charPos = 0;
		
		// the character itself
		int charRead = -1;
		while( charRead != 0 )
		{
			// initialize the temporary buffer
			charBuffer =  new char[ 1024 ];
			charPos = 0;
			
			// read into the temporary buffer
			while ( charPos < 1024 && charRead != 0 )
			{
				// red the character
				charRead = reader.read();
				
				// check the end of socket
				if ( charRead == -1 )
				{
					throw new SocketException( "Socket has been closed - End Of File" );
				}
				
				// store the character
				charBuffer[ charPos++ ] = (char) charRead;
			}
			
			// remove the EOL character
			if ( charRead == 0 )
			{
				charPos--;
			}
			
			line += new String( charBuffer,
                    			0,
                    			charPos );
				
		}
		
		return line;
	}

	public static void writeOnSocket( PrintWriter writer, 
									  String message ) 
	{
		writer.print( message );
		writer.print( (char) 0 );
		writer.flush();
	}
}
