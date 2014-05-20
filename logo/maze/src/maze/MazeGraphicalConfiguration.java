package maze;

public class MazeGraphicalConfiguration implements Cloneable {

	private int width_ = -1;
	private int height_ = -1;
	private boolean fullscreen_ = false;
	
	@Override
	public MazeGraphicalConfiguration clone() {
		MazeGraphicalConfiguration result = null;
	    try {
	    	result = (MazeGraphicalConfiguration) super.clone();
	    } catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
	    }
	    return result;	
	}

	public void setWidth(int width) {
		width_ = width;
	}

	public int getWidth() {
		return width_;
	}

	public void setHeight(int height) {
		height_ = height;
	}

	public int getHeight() {
		return height_;
	}

	public void setFullscreen(boolean fullscreen) {
		fullscreen_ = fullscreen;		
	}
	
	public boolean isFullscreen() {
		return fullscreen_;
	}
}
