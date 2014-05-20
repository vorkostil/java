package ScreenManager.ScreenControl;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public abstract class AbstractControl {

	private Map<String,Image> images_ = new HashMap<String,Image>();

	public abstract void init();

	private String currentImage_ = null;
	protected Rectangle box_ = null;
	protected String name_ = null;
	protected boolean selected_ = false;
	protected boolean over_ = false;
	protected boolean visible_ = false;

	public AbstractControl() {
		super();
	}

	public AbstractControl(String name, int x, int y, int width, int height, boolean visible) {
		name_  = name;
		visible_ = visible;
		box_ = new Rectangle(x, y, width, height);
	}

	protected void addOrReplaceImage_(String name, String imagePath) {
		Image image = null;
		try {
			image = ImageIO.read(new File(imagePath));
			images_.put(name, image);
		} 
		catch (IOException e) {
		}
	}

	protected void setCurrentImage_(String name) {
		Image image = images_.get(name);
		if (image != null) {
			currentImage_ = name;
			box_.setSize(new Dimension(image.getWidth(null),image.getHeight(null)));
		}
	}

	public boolean contains(int x, int y) {
		return box_.contains(x, y);
	}

	public boolean isVisible() {
		return visible_ ;
	}

	public void setup() {
		selected_ = false;
		over_ = false;
		onNotOverNotSelected_();
	}

	protected Image getCurrentImage_() {
		return images_.get(currentImage_);
	}

	protected boolean changeCurrentImage_(String newImageName) {
		if (images_.get(newImageName)!= null) {
			currentImage_ = newImageName;
			return true;
		}
		return false;
	}
	
	protected void onNotOverNotSelected_() {
	}

	protected void onNotOverSelected_() {
	}

	protected void onOverNotSelected_() {
	}

	protected void onOverSelected_() {
	}

	protected void onActivate_() {
	}

	public void draw(Graphics g) {
		if (isVisible()&& getCurrentImage_() != null)
			g.drawImage(getCurrentImage_(), (int) box_.getX(), (int) box_.getY(), null);
	}
	
	public void keyReleased(KeyEvent event) {
	}

	public void keyPressed(KeyEvent event) {
	}

	public void mouseReleased() {
		if (selected_ && over_) {
			onActivate_();
		}
		selected_ = false;
		if (over_) {
			onOverNotSelected_();
		}
		else {
			onNotOverNotSelected_();
		}
	}

	public void mousePressed() {
		selected_ = true;
		if (over_) {
			onOverSelected_();
		}
		else {
			onNotOverSelected_();
		}
	}

	public void mouseExit() {
		over_ = false;
		if (selected_) {
			onNotOverSelected_();
		}
		else {
			onNotOverNotSelected_();
		}
	}

	public void mouseOver() {
		over_ = true;
		if (selected_) {
			onOverSelected_();
		}
		else {
			onOverNotSelected_();
		}
	}
	
}