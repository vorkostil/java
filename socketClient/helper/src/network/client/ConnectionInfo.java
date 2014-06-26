package network.client;

import java.net.Socket;

public class ConnectionInfo {

	private String server = null;
	private String port = null;
	private String login = null;
	private String passwd = null;
	
	// use for provider
	private String baseLogin = null;
	
	public ConnectionInfo() 
	{
	}
	
	public ConnectionInfo( String server,
						   String port,
						   String login,
						   String passwd )
	{
		this.server = server;
		this.port = port;
		this.login = login;
		this.passwd = passwd;
	}
	
	public String toString()
	{
		String result = "";
		if (  ( server != null )
			&&( port != null )
			&&( login != null )
			&&( passwd != null )  )
		{
			result = server + " " + port + " " + login + " " + passwd;
		}
		return result;
	}

	public boolean isValid() 
	{
		return (  ( server != null ) && ( server.length() > 0 )
				&&( port != null ) && ( port.length() > 0 )
				&&( login != null ) && ( login.length() > 0 )
				&&( passwd != null ) && ( passwd.length() > 0 )  );
	}
	
	public String getServer()
	{
		return server;
	}
	
	public String getPort()
	{
		return port;
	}
	
	public String getLogin()
	{
		return login;
	}
	
	public String getPasswd()
	{
		return passwd;
	}

	public void useSocketForLogin(Socket socket) 
	{
		if ( baseLogin == null )
		{
			baseLogin = login;
		}
		login = baseLogin + socket.getLocalAddress() + "@" + socket.getLocalPort();
		passwd = login;
	}
}
