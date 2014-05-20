package maze;

import maze.screen.EndScreen;
import server.AbstractGame;
import server.AbstractServerManager;
import server.ServerManager;
import server.model.Environment;
import server.model.object.ItemWithPicture;
import server.model.object.Player;
import server.model.object.TriggerEndGame;
import server.model.object.TriggerModifyPhysicalAttributesWithPicture;
import server.model.object.Wall;
import ScreenManager.ScreenManager;

public class MazeGame implements AbstractGame {

	Environment environment_ = null;
	AbstractServerManager manager_ = null;
	ScreenManager screenManager_ = null;
	private MazeGraphicalConfiguration graphicalConfiguration_ = new MazeGraphicalConfiguration();
	
	public MazeGame() {
		graphicalConfiguration_.setWidth(640);		
		graphicalConfiguration_.setHeight(480);		
	}
	
	protected void initDefaultEnvironment_() {
		environment_ = new Environment();
		environment_.addPhysicalObject(new Wall("wall1;false;4;0,0;640,0;640,20;0,20"));
		environment_.addPhysicalObject(new Wall("wall2;false;8;0,20;20,20;20,460;620,460;620,20;640,20;640,480;0,480;"));
		environment_.addPhysicalObject(new Wall("wall3;false;4;100,80;203,80;203,160;100,160"));
		environment_.addPhysicalObject(new Wall("wall4;false;8;304,80;540,80;540,360;100,360;100,230;440,230;440,160;304,160"));
		environment_.addPhysicalObject(new TriggerEndGame("triggerEnd1;false;true;true;pONCE;4;396,176;420,176;420,215;396,215", this));
		environment_.addPhysicalObject(new ItemWithPicture("item1;true;false;true;pictures/maze1/item_wall_1.jpg;4;320,165;339,165;339,224;320,224"));
		environment_.addPhysicalObject(new TriggerModifyPhysicalAttributesWithPicture("triggerOpen1;true;true;true;pFOREVER;pictures/maze1/item_trigger_open.jpg;4;214,90;293,90;293,109;214,109;item1;2;aVISIBLE,false;aACTIVE,false",environment_));
		environment_.addPhysicalObject(new TriggerModifyPhysicalAttributesWithPicture("triggerClose1;true;true;true;pFOREVER;pictures/maze1/item_trigger_close.jpg;4;214,131;293,131;293,150;214,150;item1;2;aVISIBLE,true;aACTIVE,true",environment_));
		environment_.addPlayer(250,400,32,32,"pictures/maze1/player.png");
	}

	public void setScreenManager(ScreenManager screenManager) {
		screenManager_ = screenManager;
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public void setup() {
		initDefaultEnvironment_();
		manager_ = new ServerManager(environment_);
		start();
	}

	@Override
	public void start() {
		manager_.run();
	}
	
	@Override
	public void tearDown() {
		stop();
	}
	
	@Override
	public void stop() {
		manager_.stop();
	}

	public Player getPlayer() {
		return environment_.getPlayer();
	}

	public Environment getEnvironment() {
		return environment_;
	}

	@Override
	public void finish() {
		screenManager_.setCurrentScreen(EndScreen.NAME);
	}

	public MazeGraphicalConfiguration getGraphicalConfiguration() {
		return graphicalConfiguration_ ;
	}

	public void modifyGraphicalConfiguration(MazeGraphicalConfiguration temporary) {
		graphicalConfiguration_ = temporary;
		
		screenManager_.changeScreenSize(graphicalConfiguration_.getWidth(),graphicalConfiguration_.getHeight());
		screenManager_.setFullscreen(graphicalConfiguration_.isFullscreen());
	}

	public void initConfiguration() {
	}
}
