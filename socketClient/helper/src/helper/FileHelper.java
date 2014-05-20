package helper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileHelper 
{
	
	public static final String DEFAULT_COMMENT = "--";

	// return null if an error occurs
	public static List<String> loadFromFile( String fileName, 
											 String commentString ) 
	{
		List< String > strs = new ArrayList< String >();
		try 
		{
			// Open the file
			FileInputStream fstream = new FileInputStream( fileName );

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream( fstream );
			BufferedReader br = new BufferedReader( new InputStreamReader( in ) );

			String strLine;
			// Read File Line By Line
			while ( (strLine = br.readLine() ) != null )
			{
				if ( strLine.startsWith( commentString ) == false )
				{
					strs.add(strLine);
				}
			}
			
			// Close the input stream
			in.close();
		} 
		catch (Exception e) 
		{// Catch exception if any
			System.err.println( "Error: " + e.getMessage() );
			return null;
		}
		return strs;
	}
}
