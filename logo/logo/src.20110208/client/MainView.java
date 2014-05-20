package client;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import server.Parser;
import server.Parser.TokenKind;
import server.Turtle;
import server.Turtle.TurtleAction;


public class MainView  extends JFrame 
{
	class RunAction extends AbstractAction {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1158238874395844931L;
		
		public RunAction(MainView view, String text, ImageIcon icon,
	                      String desc, Integer mnemonic) {
	        super(text, icon);
	        view_ = view;
	        putValue(SHORT_DESCRIPTION, desc);
	        putValue(MNEMONIC_KEY, mnemonic);
	    }
	    public void actionPerformed(ActionEvent e) {
	    	view_.run();
	    }
	    MainView view_;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 697074122578477076L;
	
	PaintScreen screen;
	TextArea input = new TextArea();
	JButton run = new JButton();

	private Turtle turtle_;
	Parser parser_ = new Parser();
	
	public MainView(Turtle turtle) 
	{ 
		parser_.addToken("reset", 	Turtle.Function.fctRESET, 		TokenKind.tkAtomic);
		parser_.addToken("move", 	Turtle.Function.fctMOVE, 		TokenKind.tkAtomic);
		parser_.addToken("rotate", 	Turtle.Function.fctTURN, 		TokenKind.tkAtomic);
		parser_.addToken("write", 	Turtle.Function.fctDOWN, 		TokenKind.tkAtomic);
		parser_.addToken("unwrite",	Turtle.Function.fctUP, 			TokenKind.tkAtomic);
		parser_.addToken("up", 		Turtle.Function.fctNORTH, 		TokenKind.tkAtomic);
		parser_.addToken("down", 	Turtle.Function.fctSOUTH, 		TokenKind.tkAtomic);
		parser_.addToken("left", 	Turtle.Function.fctWEST, 		TokenKind.tkAtomic);
		parser_.addToken("right", 	Turtle.Function.fctEAST, 		TokenKind.tkAtomic);
		parser_.addToken("step", 	Turtle.Function.fctSTEP, 		TokenKind.tkAtomic);
		parser_.addToken("loop", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkBeginLoop);
		parser_.addToken("end",		Turtle.Function.fctUNKNOWN, 	TokenKind.tkEnd);
		parser_.addToken("func", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkBeginFunction);
		parser_.addToken("call", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkCallFunction);
		
		turtle_ = turtle;
		setTitle ("LOGO"); 
		setSize (1200, 600); 
		screen = new PaintScreen(new TurtleGraphicDisplayer(turtle)); 
		getContentPane().setLayout(new GridLayout(1,2,5,5));
		getContentPane().add(screen);
		JPanel frame = new JPanel();
		frame.setLayout(new GridLayout(2,1,5,5));
		getContentPane().add(frame);
		frame.add(input);
		frame.add(run);
		run.setText("run");
		run.setAction(new RunAction(this,"run", null, "launch execution", new Integer(KeyEvent.VK_F5)));
	}

	public void run() {
		List<TurtleAction> actions = turtle_.createActionPlan(parser_.parse(input.getText().split("\r\n")));
		turtle_.setSpeed(0.25);
		turtle_.init(new Date().getTime(), actions);
	}

	private Color convertColor(String color) {
		if (color.toUpperCase().compareTo("BLEU") == 0) {
			return Color.BLUE;
		}
		else if (color.toUpperCase().compareTo("ROUGE") == 0) {
			return Color.RED;
		}
		else if (color.toUpperCase().compareTo("CYAN") == 0) {
			return Color.CYAN;
		}
		else if (color.toUpperCase().compareTo("GRIS") == 0) {
			return Color.GRAY;
		}
		else if (color.toUpperCase().compareTo("VERT") == 0) {
			return Color.GREEN;
		}
		else if (color.toUpperCase().compareTo("MAUVE") == 0) {
			return Color.MAGENTA;
		}
		else if (color.toUpperCase().compareTo("ORANGE") == 0) {
			return Color.ORANGE;
		}
		else if (color.toUpperCase().compareTo("ROSE") == 0) {
			return Color.PINK;
		}
		else if (color.toUpperCase().compareTo("JAUNE") == 0) {
			return Color.YELLOW;
		}
		return Color.BLACK;
	} 
}
