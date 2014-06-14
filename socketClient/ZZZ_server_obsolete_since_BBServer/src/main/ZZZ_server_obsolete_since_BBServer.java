package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class ZZZ_server_obsolete_since_BBServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try 
		{
			int maxClient = 10;
			
			ServerSocket socketServer = new ServerSocket( 8001, maxClient );
			System.out.println( "Server created on " + InetAddress.getLocalHost() + ":" + socketServer.getLocalPort() + " waiting for client ...");
			Thread t = new Thread(  new ClientConnectionManager( socketServer, maxClient ) );
			t.start();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}

}