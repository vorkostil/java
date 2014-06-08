package network;

// TODO try to do something better like a client's connection manager + forward message
public interface ConnectionServer {

	public void forwardToClient( String clientName, String message);

	public void closeGame(String gameId);
}
