package ScreenManager.Screen;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import ScreenManager.ScreenManager;
import ScreenManager.ScreenControl.AbstractControl;


public class ScreenWithControl extends AbstractScreen {
	
	private List<AbstractControl> controls_ = new ArrayList<AbstractControl>();

	private AbstractControl controlUnder_ = null;
	private AbstractControl controlSelected_ = null;
	
	public ScreenWithControl(ScreenManager screenManager, String name, String backgroundImageName) {
		super(screenManager,name,backgroundImageName);
	}

	public void addControl(AbstractControl control) {
		controls_.add(control);
	}
	
	private AbstractControl retrieveControlAt_(int x, int y)
	{
		for (AbstractControl control : controls_) {
			if (control.contains(x, y)) {
				return control;
			}
		}
		return null;
	}
	
	@Override
	public void mouseReleased(MouseEvent event) {
		AbstractControl currentControlUnderMouse = retrieveControlAt_(event.getX(),event.getY());
		if (currentControlUnderMouse == null) {
			if (controlSelected_ != null) {
				controlSelected_.mouseReleased();
				controlSelected_ = null;
			}
		}
		else {
			if (controlSelected_ == null) {
				controlUnder_ = currentControlUnderMouse;
				controlUnder_.mouseOver();
			}
			else {
				controlSelected_.mouseReleased();
				if (controlSelected_ != currentControlUnderMouse) {
					controlSelected_ = currentControlUnderMouse;
					controlSelected_.mouseOver();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		AbstractControl currentControlUnderMouse = retrieveControlAt_(event.getX(),event.getY());
		if (currentControlUnderMouse != null) {
			controlSelected_ = currentControlUnderMouse;
			controlSelected_.mousePressed();
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		AbstractControl currentControlUnderMouse = retrieveControlAt_(event.getX(),event.getY());
		if (currentControlUnderMouse != null) {
			if (controlUnder_ == null) {
				controlUnder_ = currentControlUnderMouse;
				controlUnder_.mouseOver();
			}
		}
		else {
			if (controlUnder_ != null) {
				controlUnder_.mouseExit();
				controlUnder_ = null;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		AbstractControl currentControlUnderMouse = retrieveControlAt_(event.getX(),event.getY());
		if (currentControlUnderMouse == null) {
			if (controlUnder_ != null) {
				controlUnder_.mouseExit();
				controlUnder_ = null;
			}
		}
		else {
			if (controlUnder_ != null) {
				if (controlUnder_ != currentControlUnderMouse) {
					controlUnder_.mouseExit();
					controlUnder_ = null;
				}
			}
			else if (controlSelected_ == currentControlUnderMouse) {
				controlUnder_ = currentControlUnderMouse;
				controlUnder_.mouseOver();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		for (AbstractControl control : controls_) {
			if (control.isVisible()) {
				control.keyPressed(event);
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		for (AbstractControl control : controls_) {
			if (control.isVisible()) {
				control.keyReleased(event);
			}
		}
	}
	
	@Override
	public void drawBackground(Graphics g) {
		g.drawImage(backgroundImage_, 0, 0, null);
	}
	
	@Override
	public void drawForeground(Graphics g) {
		for (AbstractControl control : controls_) {
			control.draw(g);
		}
	}

	@Override
	public void tearDown() {
	}

	@Override
	public void setup() {
		controlUnder_ = null;
		controlSelected_ = null;
		for (AbstractControl control : controls_) {
			control.setup();
		}
	}

	@Override
	public void init() {
	}

}
