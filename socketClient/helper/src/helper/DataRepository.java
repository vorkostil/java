package helper;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRepository 
{

	public class DataInformation 
	{
		public static final String OBJECT_SEPARATOR = "."; 
		public static final String VALUE_SEPARATOR = "="; 
		public static final String LIST_SEPARATOR = ",";

		public static final String OBJECT_DESCRIPTOR = "entity_kind";
		
		String name_ = null;
		Map<String,String> information_ = new HashMap<String, String>();
		
		public DataInformation( String name ) 
		{
			name_ = name;
		}
		
		public void add( String key,
						 String value ) 
		{
			information_.put( key, value );
		}

		public boolean contains( String key ) 
		{
			return information_.containsKey( key );
		}
		
		public String getStringValue( String key ) 
		{
			return information_.get( key );
		}

		public Point getPointValue( String key ) 
		{
			String[] info = getStringValue(key).split( LIST_SEPARATOR );
			return new Point( Integer.parseInt( info[ 0 ] ), Integer.parseInt( info[ 1 ] ) );
		}

		public int getIntegerValue( String key ) 
		{
			return Integer.parseInt( getStringValue( key ) );
		}

		public double getDoubleValue( String key ) 
		{
			return Double.parseDouble( getStringValue( key ) );
		}

		public boolean getBooleanValue( String key ) 
		{
			return Boolean.parseBoolean( getStringValue( key ) );
		}

		public String getName() 
		{
			return name_;
		}

		public Point2D getPointDoubleValue( String key ) 
		{
			String[] info = getStringValue( key ).split( LIST_SEPARATOR );
			return new Point2D.Double( Double.parseDouble( info[ 0 ] ), Double.parseDouble( info[ 1 ] ) );
		}
	}
	
	Map< String, DataInformation > datas_ = new HashMap< String, DataInformation >();
	Map< String, List< DataInformation > > datasByEntity_ = new HashMap< String, List< DataInformation > >();
	
	public void addFromFile( String fileName ) 
	{
		addLines( FileHelper.loadFromFile( fileName, FileHelper.DEFAULT_COMMENT ) );
	}
	
	private void addLines( List< String > lines ) 
	{
		List< DataInformation > incomingData = new ArrayList< DataInformation >();
		for (String line : lines) 
		{
			int indexPoint = line.indexOf( DataInformation.OBJECT_SEPARATOR );
			int indexEqual = line.indexOf( DataInformation.VALUE_SEPARATOR );
			if (  ( indexPoint != -1 )
				&&( indexEqual != -1 )  ) 
			{
				String elementName = line.substring( 0, indexPoint ).trim();
				DataInformation data = datas_.get( elementName ); 
				if (data == null) 
				{
					data = new DataInformation( elementName );
					datas_.put( elementName, data );
					incomingData.add( data );
				}
				String key = line.substring( indexPoint + 1, indexEqual ).trim();
				String value = line.substring( indexEqual + 1 ).trim(); 
				data.add( key,value );
			}
		}
		
		for (DataInformation data :incomingData) 
		{
			String entityKind = data.getStringValue( DataInformation.OBJECT_DESCRIPTOR );
			if ( datasByEntity_.containsKey( entityKind ) == false ) 
			{
				datasByEntity_.put( entityKind, new ArrayList< DataInformation >() );
			}
			datasByEntity_.get( entityKind ).add( data );
		}
	}
	
	public DataInformation getData(String dataName) 
	{
		return datas_.get( dataName );
	}
	
	public Collection< DataInformation > getAllDatas() 
	{
		return datas_.values();
	}

	public List< DataInformation > getDatasByEntityKind( String entityKind ) 
	{
		return datasByEntity_.get( entityKind );
	}

	public void addFromStrings( List< String > lines ) 
	{
		addLines( lines );
	}
}
