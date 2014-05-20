package ScreenManager.ScreenControl;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import ScreenManager.ScreenAction.AbstractAction;


public class ControlButton extends ControlLabel {

	protected static final String NOT_OVER_NOT_SELECTED = "NotOverNotSelected";
	protected static final String NOT_OVER_SELECTED = "NotOverSelected";
	protected static final String OVER_NOT_SELECTED = "OverNotSelected";
	protected static final String OVER_SELECTED = "OverSelected";
	
	private AbstractAction action_ = null;

	boolean keyMapped_ = false;
	int key_ = KeyEvent.VK_ESCAPE;
	
	public ControlButton(String name, int x, int y, int width, int height, boolean visible) {
		super(name,x,y,width,height,visible);
	}

	public ControlButton(String name, String label, Font font, Color color, int x, int y, int width, int height, int offsetTextX, int offsetTextY, boolean visible) {
		super(name,label,font,color,x,y,width,height,offsetTextX,offsetTextY,visible);
	}

	public ControlButton(String name, String label, Font font, Color color, int x, int y, int width, int height, int offsetTextX, int offsetTextY, boolean visible, AbstractAction action) {
		super(name,label,font,color,x,y,width,height,offsetTextX,offsetTextY,visible);
		setAction(action);
	}

	public void setKey(int key) {
		keyMapped_ = true;
		key_ = key;
	}
	
	public void setAction(AbstractAction action) {
		action_  = action;
	}
	
	public void setAction(AbstractAction action, int key) {
		action_  = action;
		setKey(key);
	}
	
	@Override
	public void init() {
		addOrReplaceImage_(NOT_OVER_NOT_SELECTED, "pictures/buttonControlNotOverNotSelected.jpg");
		addOrReplaceImage_(NOT_OVER_SELECTED, "pictures/buttonControlNotOverSelected.jpg");
		addOrReplaceImage_(OVER_NOT_SELECTED, "pictures/buttonControlOverNotSelected.jpg");
		addOrReplaceImage_(OVER_SELECTED, "pictures/buttonControlOverSelected.jpg");
		setCurrentImage_(NOT_OVER_NOT_SELECTED);
	}

	@Override
	protected void onActivate_() {
		if (action_ != null) {
			action_.run();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (keyMapped_ && event.getKeyCode() == key_ ) {
			onActivate_();
		}
	}
	
	@Override
	protected void onOverSelected_() {
		currentTextX_ = textX_ + 1;
		currentTextY_ = textY_ + 1;
		changeCurrentImage_(OVER_SELECTED);
	}

	@Override
	protected void onNotOverSelected_() {
		currentTextX_ = textX_ + 1;
		currentTextY_ = textY_ + 1;
		changeCurrentImage_(NOT_OVER_SELECTED);
	}

	@Override
	protected void onOverNotSelected_() {
		currentTextX_ = textX_;
		currentTextY_ = textY_;
		changeCurrentImage_(OVER_NOT_SELECTED);
	}

	@Override
	protected void onNotOverNotSelected_() {
		currentTextX_ = textX_;
		currentTextY_ = textY_;
		changeCurrentImage_(NOT_OVER_NOT_SELECTED);
	}
}
