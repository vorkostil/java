package helper;

public class StringHelper {

	// concatenate the last element of a list starting at index
	public static String concat( String[] list, int index )
	{
		String concat = new String();
		if ( index <= list.length )
		{
			for ( int i = index; i < list.length; ++i )
			{
				concat += list[i];
				if ( i < list.length - 1 )
					concat += " ";
			}
		}
		return concat;
	}
	
}
