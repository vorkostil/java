package server.model.object;

import java.awt.Polygon;

import server.AbstractGame;
import server.model.action.ActionMoveOn;
import server.model.action.IAction;

public class TriggerEndGame extends Trigger {

	AbstractGame game_ = null;
	public TriggerEndGame(String name, boolean visible, boolean crossable, boolean active, AbstractGame game) {
		super(name, visible, crossable, active);
		game_ = game;
	}
	
	public TriggerEndGame(String line, AbstractGame game)
	{
		String values[] = line.split(";");
		name_ = values[0];
		setVisibility(Boolean.parseBoolean(values[1]));
		setCrossable(Boolean.parseBoolean(values[2]));
		setActive(Boolean.parseBoolean(values[3]));
		setPolicy(values[4]);
		
		Polygon model = new Polygon();
		for (int i = 0; i < Integer.parseInt(values[5]); ++i)
			model.addPoint(Integer.parseInt(values[i+6].split(",")[0]),Integer.parseInt(values[i+6].split(",")[1]));
		setModel(model);
		
		game_ = game;
	}
	

	@Override
	public boolean applyAction(IAction action) {
		if (action instanceof ActionMoveOn) {
			PhysicalObject physicalObject = ((ActionMoveOn)action).getSource();
			if (physicalObject instanceof Player) {
				game_.finish();
				return true;
			}
		}
		return false;
	}

}
