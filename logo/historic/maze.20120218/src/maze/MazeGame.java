package maze;

import java.awt.Polygon;

import server.ServerManager;
import server.model.Environment;
import server.model.object.Player;
import server.model.object.TriggerModifyPhysicalAttributes;
import server.model.object.Wall;
import server.model.object.PhysicalObject.Attributes;

public class MazeGame extends AbstractGame {

	Environment environment_ = null;
	
	protected void initDefaultEnvironment_() {
		environment_ = new Environment();
		
		Polygon wall = new Polygon();
		wall.addPoint(0,0);
		wall.addPoint(100,0);
		wall.addPoint(100,100);
		wall.addPoint(0,100);
		Wall w = new Wall("wall1", wall, true);
		
		environment_.addPhysicalObject(w);
		
		TriggerModifyPhysicalAttributes t1 = new TriggerModifyPhysicalAttributes("t1", true, true, true);
		Polygon pt1 = new Polygon();
		pt1.addPoint(150,150);
		pt1.addPoint(200,150);
		pt1.addPoint(200,160);
		pt1.addPoint(150,160);
		t1.setModel(pt1);
		t1.setTarget(w);
		t1.addModification(Attributes.aVISIBLE, false);
		t1.addModification(Attributes.aACTIVE, true);
		environment_.addPhysicalObject(t1);
		
		TriggerModifyPhysicalAttributes t2 = new TriggerModifyPhysicalAttributes("t1", true, true, true);
		Polygon pt2 = new Polygon();
		pt2.addPoint(150,250);
		pt2.addPoint(200,250);
		pt2.addPoint(200,260);
		pt2.addPoint(150,260);
		t2.setModel(pt2);
		t2.setTarget(w);
		t2.addModification(Attributes.aVISIBLE, true);
		t2.addModification(Attributes.aACTIVE, false);
		environment_.addPhysicalObject(t2);
		
		environment_.addPlayer(200,300);
	}

	@Override
	public void init() {
	}
	
	@Override
	public void setup() {
		initDefaultEnvironment_();
		manager_ = new ServerManager(environment_);
		manager_.run();
	}

	@Override
	public void tearDown() {
		manager_.stop();
	}

	public Player getPlayer() {
		return environment_.getPlayer();
	}

	public Environment getEnvironment() {
		return environment_;
	}

}
