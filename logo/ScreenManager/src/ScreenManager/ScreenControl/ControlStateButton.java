package ScreenManager.ScreenControl;

public class ControlStateButton extends ControlButton {

	private boolean checked_ = false;
	
	public ControlStateButton(String name, int x, int y, int width, int height, boolean visible, boolean checked) {
		super(name, x, y, width, height, visible);
		checked_ = checked;
	}

	public void setChecked(boolean checked) {
		checked_ = checked;
		if (checked_)
			setCurrentImage_(NOT_OVER_SELECTED);
		else
			setCurrentImage_(NOT_OVER_NOT_SELECTED);
	}
	
	public boolean isChecked() {
		return checked_;
	}
	
	@Override
	public void init() {
		addOrReplaceImage_(NOT_OVER_NOT_SELECTED, "pictures/buttonControlNotOverNotSelected.jpg");
		addOrReplaceImage_(NOT_OVER_SELECTED, "pictures/buttonControlNotOverSelected.jpg");
		addOrReplaceImage_(OVER_NOT_SELECTED, "pictures/buttonControlOverNotSelected.jpg");
		addOrReplaceImage_(OVER_SELECTED, "pictures/buttonControlOverSelected.jpg");
		
		if (checked_)
			setCurrentImage_(NOT_OVER_SELECTED);
		else
			setCurrentImage_(NOT_OVER_NOT_SELECTED);
	}

	@Override
	protected void onActivate_() {
		super.onActivate_();
		checked_ = !checked_;
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
		if (checked_)
			changeCurrentImage_(OVER_SELECTED);
		else
			changeCurrentImage_(OVER_NOT_SELECTED);
	}

	@Override
	protected void onNotOverNotSelected_() {
		currentTextX_ = textX_;
		currentTextY_ = textY_;
		if (checked_)
			changeCurrentImage_(NOT_OVER_SELECTED);
		else
			changeCurrentImage_(NOT_OVER_NOT_SELECTED);
	}
}
