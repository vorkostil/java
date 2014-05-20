package server.event;

import server.model.object.Player;

public class EndGameEvent extends GameEvent {

	private Player player_ = null;

	public EndGameEvent(Player player) {
		player_ = player;
	}
	
	public Player getPlayer() {
		return player_ ;
	}
}
