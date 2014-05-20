package client;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import server.Turtle;
import server.Turtle.TurtlePoint;

public class TurtleTextDisplayer implements Observer {

	private Turtle turtle_;
	
	public TurtleTextDisplayer(Turtle turtle) {
		turtle_ = turtle;
		turtle.addObserver(this);
	}
	
	@Override
	public void update(Observable observable, Object argument) {
		if (observable == turtle_) {
			System.out.println("TurtleTextDisplayer> " + new Date(turtle_.getTimestamp()) + "-->");
			for (TurtlePoint point : turtle_.getDrawnPoints()) {
				System.out.println("\t" + point);
			}
		}
	}

}
