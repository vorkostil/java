import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * TODO
 * http://www.exampledepot.com/egs/java.awt/screen_Flip.html
 * */
public class mainFullScreen {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Determine if full-screen mode is supported directly
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		if (gs.isFullScreenSupported()) {
		    System.out.println("Full-screen mode is supported");
		} else {
			System.out.println("Full-screen mode will be simulated");
		}

		// Create a button that leaves full-screen mode
		Button btn = new Button("OK");
		btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		        // Return to normal windowed mode
		        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		        GraphicsDevice gs = ge.getDefaultScreenDevice();
		        gs.setFullScreenWindow(null);
		    }
		});

		// Create a window for full-screen mode; add a button to leave full-screen mode
		Frame frame = new Frame(gs.getDefaultConfiguration());
		//Window win = new Window(frame);
		frame.add(btn, BorderLayout.CENTER);

		try {
		    // Enter full-screen mode
		    gs.setFullScreenWindow(frame);
		    frame.validate();

		    // ...
		} finally {
		    // Exit full-screen mode
		    gs.setFullScreenWindow(null);
		}	
	}

}
