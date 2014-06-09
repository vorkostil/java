package clientView.panel.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import clientView.panel.MainView;

public class MainViewMouseListener implements MouseListener {
	/**
	 * 
	 */
	private final MainView mainView;

	/**
	 * @param mainView
	 */
	public MainViewMouseListener(MainView mainView) {
		this.mainView = mainView;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// call the release
		if ( this.mainView.getCurrentItemBelowMouse() != null )
		{
			this.mainView.getCurrentItemBelowMouse().mouseReleasedItem( arg0.getX(), arg0.getY(), this.mainView.isLeftButtonIsUp(), this.mainView.isRightButtonIsUp() );
			
			// call the activation if needed
			if ( this.mainView.getCurrentItemBelowMouse() == this.mainView.getPressedItem() )
			{
				this.mainView.getCurrentItemBelowMouse().activate( this.mainView.isLeftButtonIsUp(), this.mainView.isRightButtonIsUp() );
			}
		}
		
		// reset the button status
		if ( arg0.getButton() == MouseEvent.BUTTON1 )
		{
			this.mainView.setLeftButtonIsUp(true);
		}
		if ( arg0.getButton() == MouseEvent.BUTTON3 )
		{
			this.mainView.setRightButtonIsUp(true);
		}
		
		// reset the pressed item
		this.mainView.setPressedItem(null);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// set the button status
		if ( arg0.getButton() == MouseEvent.BUTTON1 )
		{
			this.mainView.setLeftButtonIsUp(false);
		}
		if ( arg0.getButton() == MouseEvent.BUTTON3 )
		{
			this.mainView.setRightButtonIsUp(false);
		}
		
		// call the pressed state
		if ( this.mainView.getCurrentItemBelowMouse() != null )
		{
			this.mainView.getCurrentItemBelowMouse().mousePressedItem( arg0.getX(), arg0.getY(), this.mainView.isLeftButtonIsUp(), this.mainView.isRightButtonIsUp() );
		}
		
		// set the pressed item
		this.mainView.setPressedItem(this.mainView.getCurrentItemBelowMouse());
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
}