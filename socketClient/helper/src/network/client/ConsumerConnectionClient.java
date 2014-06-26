package network.client;

import game.AbstractGameClientFrame;

import java.io.IOException;

public class ConsumerConnectionClient extends ConnectionClient {

	GameConsumerObserver gameConsumer;
	
	public ConsumerConnectionClient(GameConsumerObserver observer) 
	{
		super(observer);
		gameConsumer = observer;
	}

	public AbstractGameClientFrame requireGame(String gameName) throws IOException 
	{
		return gameConsumer.requireGame(gameName);
	}
}
