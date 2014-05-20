package ScreenManager.ScreenControl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class ControlLabel extends AbstractControl {

	private static final String STANDARD = "Standard";
	
	private String label_ = null;
	private Font font_ = null;
	protected int textX_ = 0;
	protected int textY_ = 0;
	protected int currentTextX_ = 0;
	protected int currentTextY_ = 0;
	private Color color_ = Color.BLACK;

	public ControlLabel() {
		super();
	}

	public ControlLabel(String name, int x, int y, int width, int height, boolean visible) {
		super(name,x,y,width,height,visible);
	}

	public ControlLabel(String name, String label, Font font, Color color, int x, int y, int width, int height, int offsetTextX, int offsetTextY, boolean visible) {
		super(name,x,y,width,height,visible);
		setLabel(label,font,color,offsetTextX,offsetTextY);
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

	@Override
	public void init() {
		addOrReplaceImage_(STANDARD, "pictures/labelControlStandard.jpg");
		setCurrentImage_(STANDARD);
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (isVisible()) {
			g.setFont(font_);
			g.setColor(color_);
			g.drawString(label_, currentTextX_, currentTextY_);
		}
	}
}