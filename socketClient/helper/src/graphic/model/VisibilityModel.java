package graphic.model;

// just here to manage the visible/invisible state
public class VisibilityModel 
{
	private boolean visible;
	
	public VisibilityModel( boolean visible )
	{
		this.visible = visible;
	}
	
	public void hide() 
	{
		visible = false;
	}
	
	public void show() 
	{
		visible = true;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
}
