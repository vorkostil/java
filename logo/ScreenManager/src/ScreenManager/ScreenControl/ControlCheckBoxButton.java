package ScreenManager.ScreenControl;

public class ControlCheckBoxButton extends ControlButton {

	protected static final String NOT_OVER_NOT_SELECTED_CHECKED = "NotOverNotSelectedChecked";
	protected static final String NOT_OVER_SELECTED_CHECKED = "NotOverSelectedChecked";
	protected static final String OVER_NOT_SELECTED_CHECKED = "OverNotSelectedChecked";
	protected static final String OVER_SELECTED_CHECKED = "OverSelectedChecked";
	
	private boolean checked_ = false;
	
	public ControlCheckBoxButton(String name, int x, int y, int width, int height, boolean visible, boolean checked) {
		super(name, x, y, width, height, visible);
		checked_ = checked;
	}

	public void setChecked(boolean checked) {
		checked_ = checked;
	}
	
	public boolean isChecked() {
		return checked_;
	}
	
	@Override
	public void init() {
		addOrReplaceImage_(NOT_OVER_NOT_SELECTED, "pictures/buttonControlNotOverNotSelectedNotChecked.jpg");
		addOrReplaceImage_(NOT_OVER_SELECTED, "pictures/buttonControlNotOverSelectedNotChecked.jpg");
		addOrReplaceImage_(OVER_NOT_SELECTED, "pictures/buttonControlOverNotSelectedNotChecked.jpg");
		addOrReplaceImage_(OVER_SELECTED, "pictures/buttonControlOverSelectedNotChecked.jpg");

		addOrReplaceImage_(NOT_OVER_NOT_SELECTED_CHECKED, "pictures/buttonControlNotOverNotSelected.jpg");
		addOrReplaceImage_(NOT_OVER_SELECTED_CHECKED, "pictures/buttonControlNotOverSelected.jpg");
		addOrReplaceImage_(OVER_NOT_SELECTED_CHECKED, "pictures/buttonControlOverNotSelected.jpg");
		addOrReplaceImage_(OVER_SELECTED_CHECKED, "pictures/buttonControlOverSelected.jpg");
		
		if (checked_)
			setCurrentImage_(NOT_OVER_NOT_SELECTED_CHECKED);
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
		if (checked_)
			changeCurrentImage_(OVER_SELECTED_CHECKED);
		else
			changeCurrentImage_(OVER_SELECTED);
	}

	@Override
	protected void onNotOverSelected_() {
		currentTextX_ = textX_ + 1;
		currentTextY_ = textY_ + 1;
		if (checked_)
			changeCurrentImage_(NOT_OVER_SELECTED_CHECKED);
		else
			changeCurrentImage_(NOT_OVER_SELECTED);
	}

	@Override
	protected void onOverNotSelected_() {
		currentTextX_ = textX_;
		currentTextY_ = textY_;
		if (checked_)
			changeCurrentImage_(OVER_NOT_SELECTED_CHECKED);
		else
			changeCurrentImage_(OVER_NOT_SELECTED);
	}

	@Override
	protected void onNotOverNotSelected_() {
		currentTextX_ = textX_;
		currentTextY_ = textY_;
		if (checked_)
			changeCurrentImage_(NOT_OVER_NOT_SELECTED_CHECKED);
		else
			changeCurrentImage_(NOT_OVER_NOT_SELECTED);
	}
}
