package server;

public class MainChatServer 
{
	public static void main(String[] args) 
	{
		// create the chat server
		ChatServer server = new ChatServer();
		server.start();
	}
}
