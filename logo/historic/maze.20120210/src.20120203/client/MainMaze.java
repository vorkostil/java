package client;

import java.awt.Polygon;

import server.ServerManager;
import server.model.Environment;
import server.model.PhysicalObject.Attributes;
import server.model.TriggerModifyPhysicalAttributes;
import server.model.TriggerModifyPhysicalAttributes.Policy;
import server.model.Wall;

public class MainMaze 
{
	public static void main(String[] args) 
	{
		Environment env = new Environment();
		Polygon wall = new Polygon();
		wall.addPoint(0,0);
		wall.addPoint(100,0);
		wall.addPoint(100,100);
		wall.addPoint(0,100);
		Wall w = new Wall("wall1", wall, true);
		
		env.addPhysicalObject(w);
		
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
		env.addPhysicalObject(t1);
		
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
		env.addPhysicalObject(t2);
		
		env.addPlayer(200,300);
		
		// server manager initialization
		ServerManager manager = new ServerManager(env);
		manager.run();
		
		MainView view = new MainView("MAZE",env) ; 
		view.setVisible(true) ;
	}
}
