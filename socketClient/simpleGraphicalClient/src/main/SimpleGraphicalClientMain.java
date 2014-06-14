package main;

import java.io.IOException;
import java.net.UnknownHostException;

public class SimpleGraphicalClientMain {
		
	public static void main(String[] args) throws InterruptedException, UnknownHostException, IOException 
	{
		// @86.76.205.31 // local:192.168.1.76
		GraphicalClient gClient = new GraphicalClient();
		gClient.setVisible( true );
	}
}