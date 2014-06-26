package network.client;

import game.AbstractGameClientFrame;

import java.io.IOException;

public interface GameConsumerObserver extends ConnectionObserver
{

	// the game manager require a game given its name through the connection client components
	abstract AbstractGameClientFrame requireGame(String gameName) throws IOException;

}
