package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

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
		
		public RunAction(MainView view, String text, ImageIcon icon, String desc, Integer mnemonic) {
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
	
	class ResetAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3082512710975231375L;
		
		public ResetAction(MainView view, String text, ImageIcon icon, String desc, Integer mnemonic) {
	        super(text, icon);
	        view_ = view;
	        putValue(SHORT_DESCRIPTION, desc);
	        putValue(MNEMONIC_KEY, mnemonic);
	    }
	    public void actionPerformed(ActionEvent e) {
	    	view_.reset();
	    }
	    MainView view_;
	}
	
	 class RenderingThread extends Thread {
		 public void run(){
			 while(true){
				 try {
					 repaint(); 
					 sleep( 10 );
				 } catch ( Exception e ) {} 
			 }
		 }
	 }
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 697074122578477076L;
	
	RenderingThread renderingThread = new RenderingThread();
	PaintScreen screen;
	TextArea input = new TextArea();
	JButton run = new JButton();
	JButton reset = new JButton();
	JRadioButton speed0 = new JRadioButton();
	JRadioButton speed1 = new JRadioButton();
	JRadioButton speed2 = new JRadioButton();
	JRadioButton speed3 = new JRadioButton();
	
	JRadioButtonMenuItem itemSpeed0 = new JRadioButtonMenuItem();
	JRadioButtonMenuItem itemSpeed1 = new JRadioButtonMenuItem();
	JRadioButtonMenuItem itemSpeed2 = new JRadioButtonMenuItem();
	JRadioButtonMenuItem itemSpeed3 = new JRadioButtonMenuItem();

	final JFileChooser fc = new JFileChooser();
	
	private Turtle turtle_ = null;
	private double speed_ = 0.25;
	Parser parser_ = new Parser();
	
	public MainView(Turtle turtle) 
	{ 
		turtle_ = turtle;
		
		createParserTokenForEnglish_();
		createView_();
		
		renderingThread.start();
	}

	private void createView_() {
		setTitle ("LOGO"); 
		setSize (800, 600); 
		
		// create menu
		JMenuItem itemNew = new JMenuItem("New");
		itemNew.setMnemonic(KeyEvent.VK_N);
		itemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		itemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				reset();
				input.setText("");
			}
		});
		JMenuItem itemLoad = new JMenuItem("Load");
		itemLoad.setMnemonic(KeyEvent.VK_L);
		itemLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
		        int returnVal = fc.showOpenDialog(MainView.this);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            try {
						java.io.FileInputStream stream = new java.io.FileInputStream(file);
						byte[] text = new byte[100000];
						stream.read(text);
						input.setText(new String(text));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} 
		        }
			}
		});
		JMenuItem itemSave = new JMenuItem("Save");
		itemSave.setMnemonic(KeyEvent.VK_S);
		itemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
		        int returnVal = fc.showSaveDialog(MainView.this);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
		            }
		            try {
						java.io.FileOutputStream stream = new java.io.FileOutputStream(file);
						stream.write(input.getText().getBytes());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} 
		        }
			}
		});
		JMenuItem itemQuit = new JMenuItem("Quit");
		itemQuit.setMnemonic(KeyEvent.VK_Q);
		itemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		itemQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		ButtonGroup languageMenu = new ButtonGroup();
		JRadioButtonMenuItem itemEnglish = new JRadioButtonMenuItem("English");
		itemEnglish.setMnemonic(KeyEvent.VK_E);
		itemEnglish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				reset();
				createParserTokenForEnglish_();
				input.setText("");
			}
		});
		languageMenu.add(itemEnglish);

		JRadioButtonMenuItem itemFrench = new JRadioButtonMenuItem("Français");
		itemFrench.setMnemonic(KeyEvent.VK_F);
		itemFrench.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				reset();
				createParserTokenForFrench_();
				input.setText("");
			}
		});
		languageMenu.add(itemFrench);
		
		JRadioButtonMenuItem itemCP = new JRadioButtonMenuItem("CP");
		itemCP.setMnemonic(KeyEvent.VK_C);
		itemCP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				reset();
				createParserTokenForCP_();
				turtle_.setStep(20);
				input.setText("");
			}
		});
		languageMenu.add(itemCP);
		
		JMenuItem itemRun = new JMenuItem("Run");
		itemRun.setMnemonic(KeyEvent.VK_R);
		itemRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.ALT_MASK));
		itemRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				run();
			}
		});
		ButtonGroup speedMenu = new ButtonGroup();
		itemSpeed0 = new JRadioButtonMenuItem("0");
		itemSpeed0.setMnemonic(KeyEvent.VK_0);
		speedMenu.add(itemSpeed0);
		itemSpeed0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				speed0.setSelected(true);
				itemSpeed0.setSelected(true);
			}
		});
		itemSpeed1 = new JRadioButtonMenuItem("1");
		speedMenu.add(itemSpeed1);
		itemSpeed1.setMnemonic(KeyEvent.VK_1);
		itemSpeed1.setSelected(true);
		itemSpeed1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				speed1.setSelected(true);
				itemSpeed1.setSelected(true);
			}
		});
		itemSpeed2 = new JRadioButtonMenuItem("2");
		itemSpeed2.setMnemonic(KeyEvent.VK_2);
		speedMenu.add(itemSpeed2);
		itemSpeed2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				speed2.setSelected(true);
				itemSpeed2.setSelected(true);
			}
		});
		itemSpeed3 = new JRadioButtonMenuItem("3");
		itemSpeed3.setMnemonic(KeyEvent.VK_3);
		speedMenu.add(itemSpeed3);
		itemSpeed3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				speed3.setSelected(true);
				itemSpeed3.setSelected(true);
			}
		});
		
		JMenuItem itemHelp = new JMenuItem("Help");
		itemHelp.setMnemonic(KeyEvent.VK_H);
		itemHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JMenu menuHelp = new JMenu("?");
		menuHelp.add(itemHelp);
		
		JMenu menuSpeed = new JMenu("Speed");
		menuSpeed.setMnemonic(KeyEvent.VK_S);
		menuSpeed.add(itemSpeed0);
		menuSpeed.add(itemSpeed1);
		menuSpeed.add(itemSpeed2);
		menuSpeed.add(itemSpeed3);
		
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuFile.add(itemNew);
		menuFile.addSeparator();
		menuFile.add(itemLoad);
		menuFile.add(itemSave);
		menuFile.addSeparator();
		menuFile.add(itemQuit);
		
		JMenu menuExec = new JMenu("Exec");
		menuExec.setMnemonic(KeyEvent.VK_E);
		menuExec.add(itemRun);
		menuExec.add(menuSpeed);
		
		JMenu menuLanguage = new JMenu("Language");
		menuLanguage.setMnemonic(KeyEvent.VK_L);
		itemEnglish.setSelected(true);
		menuLanguage.add(itemEnglish);
		menuLanguage.add(itemFrench);
		menuLanguage.add(itemCP);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(menuExec);
		menuBar.add(menuLanguage);
		menuBar.add(menuHelp);
		
		setJMenuBar(menuBar);
		
		// create frame
		screen = new PaintScreen(new TurtleGraphicDisplayer(turtle_)); 
		screen.setPreferredSize(new Dimension(600, 600));
		screen.setBorder(BorderFactory.createEtchedBorder());
		
		input.setPreferredSize(new Dimension(200, 400));
		run.setAction(new RunAction(this,"run", null, "launch execution", new Integer(KeyEvent.VK_F5)));
		run.setPreferredSize(new Dimension(100, 32));
		
		reset.setAction(new ResetAction(this,"reset", null, "clear screen and reset turtle coordinates", new Integer(KeyEvent.VK_F8)));
		reset.setPreferredSize(new Dimension(100, 32));
		
		speed0.setText(">");
		speed1.setText(">>");
		speed2.setText(">>>");
		speed3.setText(">>>>");
		
		JPanel mainFrame = new JPanel();
		mainFrame.setLayout(new BorderLayout(5,5));
		JPanel inputFrame = new JPanel();
		inputFrame.setLayout(new BorderLayout(5,5));
		JPanel buttonFrame = new JPanel();
		buttonFrame.setLayout(new GridLayout(3,1,5,5));
		JPanel speedFrame = new JPanel();
		speedFrame.setLayout(new GridLayout(1,4,5,5));

		mainFrame.add(screen,BorderLayout.CENTER);
		mainFrame.add(inputFrame,BorderLayout.LINE_END);
		
		inputFrame.add(input,BorderLayout.CENTER);
		inputFrame.add(buttonFrame,BorderLayout.PAGE_END);
		
		buttonFrame.add(speedFrame);
		buttonFrame.add(run);
		buttonFrame.add(reset);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(speed0);
		bg.add(speed1);
		bg.add(speed2);
		bg.add(speed3);
		speedFrame.add(speed0);
		speedFrame.add(speed1);
		speedFrame.add(speed2);
		speedFrame.add(speed3);
		speed0.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					speed_ = 0.1;
					itemSpeed0.setSelected(true);
				}
			}
		});
		speed1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					speed_ = 0.25;
					itemSpeed1.setSelected(true);
				}
			}
		});
		speed2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					speed_ = 1;
					itemSpeed2.setSelected(true);
				}
			}
		});
		speed3.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					speed_ = 10;
					itemSpeed3.setSelected(true);
				}
			}
		});
		speed1.setSelected(true);
		
		getContentPane().setLayout(new GridLayout(1,1,5, 5));
		getContentPane().add(mainFrame);
	}

	private void createParserTokenForEnglish_() {
		parser_.clearTokens();
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
	}

	private void createParserTokenForFrench_() {
		parser_.clearTokens();
		parser_.addToken("avance", 		Turtle.Function.fctMOVE, 		TokenKind.tkAtomic);
		parser_.addToken("tourne", 		Turtle.Function.fctTURN, 		TokenKind.tkAtomic);
		parser_.addToken("baisser", 	Turtle.Function.fctDOWN, 		TokenKind.tkAtomic);
		parser_.addToken("lever",		Turtle.Function.fctUP, 			TokenKind.tkAtomic);
		parser_.addToken("haut", 		Turtle.Function.fctNORTH, 		TokenKind.tkAtomic);
		parser_.addToken("bas", 		Turtle.Function.fctSOUTH, 		TokenKind.tkAtomic);
		parser_.addToken("gauche", 		Turtle.Function.fctWEST, 		TokenKind.tkAtomic);
		parser_.addToken("droite", 		Turtle.Function.fctEAST, 		TokenKind.tkAtomic);
		parser_.addToken("pas", 		Turtle.Function.fctSTEP, 		TokenKind.tkAtomic);
		parser_.addToken("boucle", 		Turtle.Function.fctUNKNOWN, 	TokenKind.tkBeginLoop);
		parser_.addToken("fin",			Turtle.Function.fctUNKNOWN, 	TokenKind.tkEnd);
		parser_.addToken("fonction", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkBeginFunction);
		parser_.addToken("utilise", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkCallFunction);
	}

	private void createParserTokenForCP_() {
		parser_.clearTokens();
		parser_.addToken("baisser", 	Turtle.Function.fctDOWN, 		TokenKind.tkAtomic);
		parser_.addToken("lever",		Turtle.Function.fctUP, 			TokenKind.tkAtomic);
		parser_.addToken("haut", 		Turtle.Function.fctNORTH, 		TokenKind.tkAtomic);
		parser_.addToken("bas", 		Turtle.Function.fctSOUTH, 		TokenKind.tkAtomic);
		parser_.addToken("gauche", 		Turtle.Function.fctWEST, 		TokenKind.tkAtomic);
		parser_.addToken("droite", 		Turtle.Function.fctEAST, 		TokenKind.tkAtomic);
	}

	public void reset() {
		turtle_.stop(new Date().getTime());
		turtle_.reset(new Date().getTime());
	}

	public void run() {
		List<TurtleAction> actions = turtle_.createActionPlan(parser_.parse(input.getText().split("\r\n")));
		turtle_.setSpeed(speed_);
		turtle_.init(new Date().getTime(), actions);
	}
}
