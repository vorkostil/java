package maze.screen;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import ScreenManager.ScreenManager;
import ScreenManager.Screen.AbstractScreen;

public class SplashScreen extends AbstractScreen {

	public static final String NAME = "splash";

	public SplashScreen(ScreenManager screenManager) {
		super(screenManager, NAME, "pictures/splashScreen.jpg");

	}
	
	@Override
	public void init() {
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		getScreenManager().setCurrentScreen(MainScreen.NAME);
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			getScreenManager().setCurrentScreen(MainScreen.NAME);
		}
		else if (event.getKeyCode() == KeyEvent.VK_F1) {
			getScreenManager().setCurrentScreen(MazeScreen.NAME);
		}
	}

	@Override
	public void setup() {
	}

	@Override
	public void tearDown() {
	}

	@Override
	public void drawBackground(Graphics g) {
		g.drawImage(backgroundImage_, 0, 0, null);
	}

	@Override
	public void drawForeground(Graphics g) {
	}

	@Override
	public void keyPressed(KeyEvent event) {
	}

	@Override
	public void mouseDragged(MouseEvent event) {
	}

	@Override
	public void mouseMoved(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
	}

	@Override
	public void changeScreenSize(int width, int height) {
	}
}
