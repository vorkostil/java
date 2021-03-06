package graphic.listener;

import graphic.GraphicalItem;
import graphic.GraphicalListeneredEnvironment;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MainViewMouseMotionListener implements MouseMotionListener 
{
	/**
	 * 
	 */
	private final GraphicalListeneredEnvironment mainView;

	/**
	 * @param mainView
	 */
	public MainViewMouseMotionListener(GraphicalListeneredEnvironment mainView) {
		this.mainView = mainView;
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// manage the mouse enter/left the item
		GraphicalItem mouseItem = this.mainView.getItemAt( arg0.getX(), arg0.getY() );
		if ( mouseItem != this.mainView.getCurrentItemBelowMouse() )
		{
			if ( this.mainView.getCurrentItemBelowMouse() != null )
			{
				this.mainView.getCurrentItemBelowMouse().mouseLeftItem( arg0.getX(), arg0.getY(), this.mainView.isLeftButtonIsUp(), this.mainView.isRightButtonIsUp() );
			}
			if ( mouseItem != null )
			{
				mouseItem.mouseEnterItem( arg0.getX(), arg0.getY(), this.mainView.isLeftButtonIsUp(), this.mainView.isRightButtonIsUp() );
			}
			this.mainView.setCurrentItemBelowMouse(mouseItem);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// manage the mouse enter/left the item with the button pressed
		GraphicalItem mouseItem = this.mainView.getItemAt( arg0.getX(), arg0.getY() );
		if ( mouseItem != this.mainView.getCurrentItemBelowMouse() )
		{
			if ( this.mainView.getCurrentItemBelowMouse() != null )
			{
				this.mainView.getCurrentItemBelowMouse().mouseLeftItem( arg0.getX(), arg0.getY(), this.mainView.isLeftButtonIsUp(), this.mainView.isRightButtonIsUp() );
			}
			if ( mouseItem != null )
			{
				mouseItem.mouseEnterItem( arg0.getX(), arg0.getY(), this.mainView.isLeftButtonIsUp(), this.mainView.isRightButtonIsUp() );
			}
			this.mainView.setCurrentItemBelowMouse(mouseItem);
		}
	}
}