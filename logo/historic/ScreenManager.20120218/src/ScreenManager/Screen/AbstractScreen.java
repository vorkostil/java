package ScreenManager.Screen;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ScreenManager.ScreenManager;

public abstract class AbstractScreen {

	protected String name_ = null;

	public abstract void init();
	
	public abstract void setup();

	public abstract void tearDown();

	public abstract void drawBackground(Graphics g);

	public abstract void drawForeground(Graphics g);

	public abstract void keyReleased(KeyEvent event);

	public abstract void keyPressed(KeyEvent event);

	public abstract void mouseDragged(MouseEvent event);

	public abstract void mouseMoved(MouseEvent event);

	public abstract void mousePressed(MouseEvent event);

	public abstract void mouseReleased(MouseEvent event);

	protected Image backgroundImage_ = null;
	private int width_ = 0;
	private int height_ = 0;
	protected ScreenManager screenManager_ = null;
	private boolean surrounded_ = true;
	private int x_ = 0; 
	private int y_ = 0; 
	
	public AbstractScreen(ScreenManager screenManager, String name, String backgroundImageName) {
		super();
		screenManager_ = screenManager;
		name_ = name;
		try {
			backgroundImage_ = ImageIO.read(new File(backgroundImageName));
			width_ = backgroundImage_.getWidth(null);
			height_ = backgroundImage_.getHeight(null);
		} 
		catch (IOException e) {
		}
	}
	
	public AbstractScreen(ScreenManager screenManager, String name, String backgroundImageName, int x, int y) {
		super();
		screenManager_ = screenManager;
		name_ = name;
		try {
			backgroundImage_ = ImageIO.read(new File(backgroundImageName));
			width_ = backgroundImage_.getWidth(null);
			height_ = backgroundImage_.getHeight(null);
			setPosition(x, y);
		} 
		catch (IOException e) {
		}
	}
	
	public void setPosition(int x, int y) {
		x_ = x;
		y_ = y;
	}
	
	protected ScreenManager getScreenManager_() {
		return screenManager_;
	}

	public String getName() {
		return name_;
	}

	public boolean isSurrounded() {
		return surrounded_;
	}
	
	public int getWidth() {
		return width_ + (surrounded_ ? 8 : 0);
	}

	public int getHeight() {
		return height_ + (surrounded_ ? 30 : 0);
	}

	public int getX() {
		return x_ - (surrounded_ ? 4 : 0);
	}

	public int getY() {
		return y_ - (surrounded_ ? 15 : 0);
	}
}