package ScreenManager.Screen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import ScreenManager.ScreenManager;

public abstract class AbstractScreen {

	protected String name_ = null;

	// one time at launch
	public abstract void init();
	
	// each time the screen became the current screen or is added to the list of current screens
	public abstract void setup();

	// each time the screen is remove from the list of current screens
	public abstract void tearDown();

	public abstract void drawBackground(Graphics g);

	public abstract void drawForeground(Graphics g);

	public abstract boolean keyReleased(KeyEvent event);

	public abstract boolean keyPressed(KeyEvent event);

	public abstract boolean mouseDragged(MouseEvent event);

	public abstract boolean mouseMoved(MouseEvent event);

	public abstract boolean mousePressed(MouseEvent event);

	public abstract boolean mouseReleased(MouseEvent event);

	public abstract void changeScreenSize(int width, int height);
	
	private Map<String,Image> imagesCache_ = new HashMap<String, Image>();
	private Image backgroundImage_ = null;
	private int width_ = 0;
	private int height_ = 0;
	private ScreenManager screenManager_ = null;
	private boolean surrounded_ = true;
	private int x_ = 0; 
	private int y_ = 0; 
	private int relativeX_ = 0; 
	private int relativeY_ = 0; 
	protected boolean preemptive_ = true;
	
	public AbstractScreen(ScreenManager screenManager, String name) {
		super();
		screenManager_ = screenManager;
		name_ = name;
	}
	
	public boolean selectBackgroundImage(String backgroundImageName) {
		if (imagesCache_.containsKey(backgroundImageName)) {
			return setBackgroundImage(backgroundImageName);
		}
		return false;
	}

	protected void addBackgroundImage(String backgroundImageName, Image backgroundImage) {
		if (!imagesCache_.containsKey(backgroundImageName)) {
			imagesCache_.put(backgroundImageName, backgroundImage);
		}
	}

	protected boolean setBackgroundImage(String backgroundImageName) {
		backgroundImage_ = imagesCache_.get(backgroundImageName);
		width_ = backgroundImage_.getWidth(null);
		height_ = backgroundImage_.getHeight(null);
		
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int x = (dim.width - backgroundImage_.getWidth(null))/2;
		int y = (dim.height - backgroundImage_.getHeight(null))/2;

		// Move the window
		setPosition(x, y);
		
		return true;
	}
	
	protected Image getCurrentBackgroundImage() {
		return backgroundImage_;
	}
	
	protected Image getImageByName(String name) {
		return imagesCache_.get(name);
	}

	public void setPosition(int x, int y) {
		x_ = x;
		y_ = y;
	}
	
	protected void setRelativePosition(int x, int y) {
		relativeX_ = x;
		relativeY_ = y;
	}
	
	public ScreenManager getScreenManager() {
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

	protected int getRelativeX_() {
		return relativeX_;
	}

	protected int getRelativeY_() {
		return relativeY_;
	}

	public boolean isPreemptive() {
		return preemptive_;
	}
}