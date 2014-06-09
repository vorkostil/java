package main;

import java.io.IOException;

import clientView.GraphicalClientFrame;

public class GraphicalClientMain {

	public static void main(String[] args) throws IOException 
	{
		// @86.76.205.31 // local:192.168.1.76
		GraphicalClientFrame gClient = new GraphicalClientFrame();
		gClient.setVisible( true );
	}

}
