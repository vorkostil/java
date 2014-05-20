package ScreenManager.ScreenControl;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import ScreenManager.ScreenAction.AbstractAction;


public class ScreenControlButton {

	private Map<String,Image> images_ = new HashMap<String,Image>();
	private String currentImage_ = null;
	private Rectangle box_ = null;
	private String name_ = null;
	private AbstractAction action_ = null;

	private boolean selected_ = false;
	private boolean over_ = false;
	private String label_ = null;
	private Font font_ = null;
	private int textX_ = 0;
	private int textY_ = 0;
	private int currentTextX_ = 0;
	private int currentTextY_ = 0;
	private boolean visible_ = false;
	
	private boolean keyMapped_ = false;
	private int key_ = KeyEvent.VK_ESCAPE;
	private Color color_ = Color.BLACK;
	
	public String toString() {
		return name_ + "> over: " + over_ + " / selected: " + selected_;
	}
	
	public ScreenControlButton(String name, int x, int y, int width, int height, boolean visible) {
		init(name,x,y,width,height,visible);
	}

	public ScreenControlButton(String name, String label, Font font, Color color, int x, int y, int width, int height, int offsetTextX, int offsetTextY, boolean visible) {
		init(name,x,y,width,height,visible);
		setLabel(label,font,color,offsetTextX,offsetTextY);
	}

	public ScreenControlButton(String name, String label, Font font, Color color, int x, int y, int width, int height, int offsetTextX, int offsetTextY, boolean visible, AbstractAction action) {
		init(name,x,y,width,height,visible);
		setLabel(label,font,color,offsetTextX,offsetTextY);
		setAction(action);
	}

	public void init(String name, int x, int y, int width, int height, boolean visible) {
		name_  = name;
		visible_ = visible;
		box_ = new Rectangle(x, y, width, height);

		initImage_();
	}
	
	private void initImage_() {
		addOrReplaceImage("NotOverNotSelected", "pictures/buttonControlNotOverNotSelected.jpg");
		addOrReplaceImage("NotOverSelected", "pictures/buttonControlNotOverSelected.jpg");
		addOrReplaceImage("OverNotSelected", "pictures/buttonControlOverNotSelected.jpg");
		addOrReplaceImage("OverSelected", "pictures/buttonControlOverSelected.jpg");
		setCurrentImage("NotOverNotSelected");
	}

	public void setKey(int key) {
		keyMapped_ = true;
		key_ = key;
	}
	
	public void addOrReplaceImage(String name, String imagePath) {
		Image image = null;
		try {
			image = ImageIO.read(new File(imagePath));
			images_.put(name, image);
		} 
		catch (IOException e) {
		}
	}
	
	public void setVisible(boolean visible) {
		visible_ = visible;
	}
	
	public void setCurrentImage(String name) {
		currentImage_ = name;
	}
	
	public String getName() {
		return name_;
	}
	
	public Image getCurrentImage() {
		return images_.get(currentImage_);
	}

	public int getX() {
		return (int) box_.getX();
	}

	public int getY() {
		return (int) box_.getY();
	}

	public void setLabel(String label, Font font, Color color, int offsetTextX, int offsetTextY) {
		label_ = label;
		font_ = font;
		color_ = color;
		
		textX_ = (int)box_.getX() + offsetTextX;
		textY_ = (int)box_.getY() + offsetTextY;
		
		currentTextX_ = textX_;
		currentTextY_ = textY_;
	}
	
	public void setAction(AbstractAction action) {
		action_  = action;
	}
	
	public boolean contains(int x, int y) {
		return box_.contains(x, y);
	}

	public void run() {
		if (action_ != null) {
			action_.run();
		}
	}

	public void mouseReleased() {
		if (selected_ && over_) {
			run();
		}
		selected_ = false;
		computeInformation_();
	}

	public void mousePressed() {
		selected_ = true;
		computeInformation_();
	}

	public void mouseExit() {
		over_ = false;
		computeInformation_();
	}

	public void mouseOver() {
		over_ = true;
		computeInformation_();
	}

	private void computeInformation_() {
		if (over_) {
			if (selected_) {
				if (images_.get("OverSelected") != null) {
					currentImage_ = "OverSelected";
				}
				currentTextX_ = textX_ + 1;
				currentTextY_ = textY_ + 1;
			}
			else {
				if (images_.get("OverNotSelected") != null) {
					currentImage_ = "OverNotSelected";
				}
				currentTextX_ = textX_;
				currentTextY_ = textY_;
			}
		}
		else {
			if (selected_) {
				if (images_.get("NotOverSelected") != null) {
					currentImage_ = "NotOverSelected";
				}
				currentTextX_ = textX_ + 1;
				currentTextY_ = textY_ + 1;
			}
			else {
				if (images_.get("NotOverNotSelected") != null) {
					currentImage_ = "NotOverNotSelected";
				}
				currentTextX_ = textX_;
				currentTextY_ = textY_;
			}
		}
	}

	public int getCurrentTextX() {
		return currentTextX_;
	}

	public int getCurrentTextY() {
		return currentTextY_;
	}

	public Font getFont() {
		return font_;
	}

	public Color getColor() {
		return color_;
	}

	public String getLabel() {
		return label_;
	}

	public void resetState() {
		selected_ = false;
		over_ = false;
		computeInformation_();
	}

	public boolean isVisible() {
		return visible_ ;
	}

	public void keyPressed(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
		if (keyMapped_ && event.getKeyCode() == key_ ) {
			run();
		}
	}
}
