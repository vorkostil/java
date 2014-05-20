package maze.screen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import maze.MazeGame;
import server.model.object.Player;
import ScreenManager.ScreenManager;
import ScreenManager.Screen.AbstractScreen;
import client.GraphicalEnvironment;
import client.visitor.AbstractVisitor;
import client.visitor.GraphicalVisitor;

public class MazeScreen extends AbstractScreen {

	public static final String NAME = "maze";

	private MazeGame maze_ = null;
	GraphicalEnvironment graphicalEnvironment_ = null;
	
	int viewX_;
	int viewY_;
	int viewWidth_;
	int viewHeight_;
	
	int viewSpeed_ = 10;
	
	int viewMask_ = InputEvent.CTRL_MASK;
	int viewMaxWidth_;
	int viewMaxHeight_;
	Image viewImage_ = null;
	
	boolean debug_ = false;
	
	public MazeScreen(ScreenManager screenManager, MazeGame maze) {
		super(screenManager, NAME, "pictures/maze1/fond_maze_1.jpg");
		maze_ = maze;
		viewX_ = 0;
		viewY_ = 0;
		viewWidth_ = 640;
		viewHeight_ = 480;
	}
	
	@Override
	public void init() {
		maze_.init();
		try {
			viewImage_ = ImageIO.read(new File("pictures/maze1/fond_maze_1.jpg")); //pictures/mazeScreenMax.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		viewMaxWidth_  = viewImage_.getWidth(null);
		viewMaxHeight_ = viewImage_.getHeight(null);
		viewSpeed_ = viewWidth_ / 10;
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
		g.drawImage(viewImage_,0,0,getWidth(),getHeight(),viewX_,viewY_,viewX_+viewWidth_,viewY_+viewHeight_,Color.BLACK,null);
	}

	@Override
	public void drawForeground(Graphics g) {
		ensureViewPlayerValidity_();
		ensureViewValidity_();
		if (graphicalEnvironment_ != null)
			graphicalEnvironment_.draw(g,viewX_,viewY_,viewWidth_,viewHeight_);
		
		if (debug_ == true) {
			String str = "screen " + getWidth() + "x" + getHeight() + " - image " + viewMaxWidth_ + "x" + viewMaxHeight_ + " - view " + viewX_ + "x" + viewY_ + "-" + viewWidth_ + "x" + viewHeight_;
			Color oldColor = g.getColor();
			g.setColor(Color.BLACK);
			g.drawChars(str.toCharArray(), 0, str.length(), 10, 24);
			g.setColor(oldColor);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		boolean found = false;
		if (event.getKeyCode() == KeyEvent.VK_UP)
		{
			maze_.getPlayer().stopMoving(Player.DIRECTION_UP);
			found = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_DOWN)
		{
			maze_.getPlayer().stopMoving(Player.DIRECTION_DOWN);
			found = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_LEFT)
		{
			maze_.getPlayer().stopMoving(Player.DIRECTION_LEFT);
			found = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			maze_.getPlayer().stopMoving(Player.DIRECTION_RIGHT);
			found = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) 
		{
			getScreenManager().addCurrentScreen(PauseScreen.NAME);
			found = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_B && ((event.getModifiers() & viewMask_) == viewMask_)) {
			debug_ = !debug_;
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		boolean found = false;
		if (event.getKeyCode() == KeyEvent.VK_UP)
		{
			if ((event.getModifiers() & viewMask_) == viewMask_) {
				viewY_ -= viewSpeed_;
				ensureViewValidity_();
			}
			else {
				maze_.getPlayer().startMoving(Player.DIRECTION_UP);
			}
			found = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if ((event.getModifiers() & viewMask_) == viewMask_) {
				viewY_ += viewSpeed_;
				ensureViewValidity_();
			}
			else {
				maze_.getPlayer().startMoving(Player.DIRECTION_DOWN);
			}
			found = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if ((event.getModifiers() & viewMask_) == viewMask_) {
				viewX_ -= viewSpeed_;
				ensureViewValidity_();
			}
			else {
				maze_.getPlayer().startMoving(Player.DIRECTION_LEFT);
			}
			found = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if ((event.getModifiers() & viewMask_) == viewMask_) {
				viewX_ += viewSpeed_;
				ensureViewValidity_();
			}
			else {
				maze_.getPlayer().startMoving(Player.DIRECTION_RIGHT);
			}
			found = true;
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
	}

	@Override
	public void mouseMoved(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
	}

	@Override
	public void mouseReleased(MouseEvent event) {
	}

	@Override
	public void changeScreenSize(int width, int height) {
		viewWidth_ = width;
		viewHeight_ = height;
		viewSpeed_ = viewWidth_ / 10;
		ensureViewPlayerValidity_();
		ensureViewValidity_();
	}

	private void ensureViewValidity_() {
		
		if (viewWidth_ > viewMaxWidth_) {
			viewX_ = 0;
			viewWidth_ = viewMaxWidth_;
		}
		if (viewHeight_ > viewMaxHeight_) {
			viewY_ = 0;
			viewHeight_ = viewMaxHeight_;
		}
		
		if (viewX_ < 0) {
			viewX_ = 0;
		}
		if (viewY_ < 0) {
			viewY_ = 0;
		}
		
		if (viewX_ + viewWidth_ > viewMaxWidth_) {
			viewX_ = viewMaxWidth_ - viewWidth_;
		}
		if (viewY_ + viewHeight_ > viewMaxHeight_) {
			viewY_ = viewMaxHeight_ - viewHeight_;
		}
	}

	private void ensureViewPlayerValidity_() {
		Rectangle player = maze_.getPlayer().getBounds(null);
		
		if (player.getX() < viewX_ + 100)
			viewX_ = (int) (player.getX() - 100); 
		if (player.getY() < viewY_ + 100)
			viewY_ = (int) (player.getY() - 100);
		
		if (player.getX() + player.getWidth() > viewX_  + viewWidth_ - 100)
			viewX_ = (int) (player.getX() + player.getWidth() + 100 - viewWidth_); 
		if (player.getY() + player.getHeight() > viewY_  + viewHeight_- 100)
			viewY_ = (int) (player.getY() + player.getHeight() + 100 - viewHeight_); 
	}
}
