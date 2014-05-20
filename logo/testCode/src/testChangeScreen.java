import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;


public class testChangeScreen {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Determine if the display mode can be changed");
		// Determine if the display mode can be changed
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();

		try {
		    GraphicsDevice[] gss = ge.getScreenDevices();

		    // Get number of screens
		    int numScreens = gss.length;
		    System.out.println("# of screen: " + numScreens);
		} catch (HeadlessException e) {
		    // Is thrown if there are no screen devices
		}
		
		DisplayMode[] dmodes = gs.getDisplayModes();
		for (int i=0; i<dmodes.length; i++) {
		    int screenWidth = dmodes[i].getWidth();
		    int screenHeight = dmodes[i].getHeight();
		    int bitDepth = dmodes[i].getBitDepth();
		    int refreshRate = dmodes[i].getRefreshRate();
		    System.out.println(screenWidth + "x" + screenHeight + " - " + bitDepth + " / " + refreshRate);
		}
		
		System.out.println("Determine if the display mode can be changed");
		boolean canChg = gs.isDisplayChangeSupported();
		if (canChg) {
			System.out.println("Change the screen size and number of colors");
		    DisplayMode displayMode = gs.getDisplayMode();
		    int screenWidth = 1024;
		    int screenHeight = 768;
		    int bitDepth = 8;
		    displayMode = new DisplayMode(
		        screenWidth, screenHeight, bitDepth, displayMode.getRefreshRate());
		    try {
		        gs.setDisplayMode(displayMode);
		    } catch (Throwable e) {
		        System.out.println("Desired display mode is not supported; leave full-screen mode");
		        gs.setFullScreenWindow(null);
		    }
		} else if (gs.getFullScreenWindow() != null) {
			System.out.println("Try enabling full-screen mode");
			System.out.println("see Enabling Full-Screen Mode");
		} else {
			System.out.println("Display mode cannot be changed");
		}	}

}
