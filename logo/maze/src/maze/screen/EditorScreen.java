package maze.screen;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import maze.MazeGame;
import server.model.object.Player;
import ScreenManager.ScreenManager;
import ScreenManager.Screen.AbstractScreen;
import client.GraphicalEnvironment;
import client.visitor.AbstractVisitor;
import client.visitor.GraphicalVisitor;

public class EditorScreen extends AbstractScreen {

	public static final String NAME = "maze";

	private MazeGame maze_ = null;
	GraphicalEnvironment graphicalEnvironment_ = null;
	
	public EditorScreen(ScreenManager screenManager, MazeGame maze) {
		super(screenManager, NAME, "pictures/editorScreen" + maze.getGraphicalConfiguration().getWidth() + "x" + maze.getGraphicalConfiguration().getHeight() + ".jpg");
		maze_ = maze;
	}
	
	@Override
	public void init() {
		maze_.init();
	}

	@Override
	public void setup() {
		maze_.setup();
		
		List<AbstractVisitor> visitors = new ArrayList<AbstractVisitor>();
		visitors.add(new GraphicalVisitor());
		graphicalEnvironment_ = new GraphicalEnvironment(maze_.getEnvironment(),visitors);
	}

	@Override
	public void tearDown() {
		maze_.tearDown();
	}

	@Override
	public void drawBackground(Graphics g) {
		g.drawImage(backgroundImage_, 0, 0, null);
	}

	@Override
	public void drawForeground(Graphics g) {
		if (graphicalEnvironment_ != null)
			graphicalEnvironment_.draw(g,0,0,backgroundImage_.getWidth(null),backgroundImage_.getHeight(null));
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_UP)
		{
			maze_.getPlayer().stopMoving(Player.DIRECTION_UP);
		}
		else if (event.getKeyCode() == KeyEvent.VK_DOWN)
		{
			maze_.getPlayer().stopMoving(Player.DIRECTION_DOWN);
		}
		else if (event.getKeyCode() == KeyEvent.VK_LEFT)
		{
			maze_.getPlayer().stopMoving(Player.DIRECTION_LEFT);
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			maze_.getPlayer().stopMoving(Player.DIRECTION_RIGHT);
		}
		else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) 
		{
			getScreenManager().addCurrentScreen(PauseScreen.NAME);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_UP)
		{
			maze_.getPlayer().startMoving(Player.DIRECTION_UP);
		}
		else if (event.getKeyCode() == KeyEvent.VK_DOWN)
		{
			maze_.getPlayer().startMoving(Player.DIRECTION_DOWN);
		}
		else if (event.getKeyCode() == KeyEvent.VK_LEFT)
		{
			maze_.getPlayer().startMoving(Player.DIRECTION_LEFT);
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			maze_.getPlayer().startMoving(Player.DIRECTION_RIGHT);
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeScreenSize(int width, int height) {
		changeBackgroundImage("pictures/mazeScreen" + width + "x" + height + ".jpg");
	}
}
