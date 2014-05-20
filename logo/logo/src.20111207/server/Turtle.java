package server;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import server.Parser.AtomicAction;


public class Turtle extends Observable {
	public enum Function { 
		fctUNKNOWN,
		fctRESET, fctMOVE, fctTURN, fctDOWN, fctUP, fctNORTH, fctSOUTH, fctWEST, fctEAST, 
		fctSTEP, fctCOLOR, fctSIZE }
	
	private enum InternalAction { reset, setTarget, setStep, setSize, setColor, setDirection, setWriting }
	
	private static final Color[] colorTable_ = {
		Color.BLACK,Color.BLUE,Color.CYAN,Color.DARK_GRAY,Color.GRAY,Color.GREEN,Color.LIGHT_GRAY,
		Color.MAGENTA,Color.ORANGE,Color.PINK,Color.RED,Color.WHITE,Color.YELLOW};
	
	public class TurtleAction {
		Turtle turtle_;
		InternalAction action_;
		
		public TurtleAction(Turtle t, InternalAction action) {
			turtle_ = t;
			action_ = action;
		}
	}
	
	class DoubleTurtleAction extends TurtleAction {
		double value_;
		
		public DoubleTurtleAction(Turtle t, InternalAction action, double value) {
			super(t,action);
			value_ = value;
		}
	}

	class IntTurtleAction extends TurtleAction {
		int value_;
		
		public IntTurtleAction(Turtle t, InternalAction action, int value) {
			super(t,action);
			value_ = value;
		}
	}

	class BoolTurtleAction extends TurtleAction {
		boolean value_;
		
		public BoolTurtleAction(Turtle t, InternalAction action, boolean value) {
			super(t,action);
			value_ = value;
		}
	}

	class MoveTurtleAction extends TurtleAction {
		double targetX_;
		double targetY_;
		
		public MoveTurtleAction(Turtle t, InternalAction action, double targetX, double targetY) {
			super(t,action);
			targetX_ = targetX;
			targetY_ = targetY;
		}
	}

	class ColorTurtleAction extends TurtleAction {
		Color value_;
		
		public ColorTurtleAction(Turtle t, InternalAction action, Color value) {
			super(t,action);
			value_ = value;
		}
	}
	
	public class TurtlePoint {
		public double  	x_;
		public double  	y_;
		public boolean 	write_;
		public Color 	color_;
		public int 		direction_;
		
		public TurtlePoint(double x, double y, boolean write, Color color, int direction) {
			x_ = x;
			y_ = y;
			write_ = write;
			color_ = color;
			direction_ = direction;
		}
		
		public String toString() {
			return "(" + x_ + "," + y_ + ") " + direction_ + " " + color_ + " " + write_;
		}
	}

	private static final int	defaultStep_ 		= 1;
	private static final int 	defaultDirection_	= 270;
	private static final Color 	defaultColor_ 		= Color.BLACK;
	
	private double initialX_;
	private double initialY_;
	private double currentX_;
	private double currentY_;

	private int direction_ 		= defaultDirection_;
	private int step_ 			= defaultStep_;
	private Color color_ 		= defaultColor_;
	private boolean isWriting_ 	= false;
	private double speed_ 		= 1;

	List<TurtlePoint> drawPlan_;
	List<TurtlePoint> drawnPoints_;
	private long lastProcessedTimestamp_;
	
	public Turtle(double x, double y, long timestamp) {
		initialX_ 	= x;
		initialY_ 	= y;
		currentX_ 	= initialX_;
		currentY_ 	= initialY_;
		lastProcessedTimestamp_ = timestamp;
	}
	
	public List<TurtleAction> createActionPlan(List<AtomicAction> actions) {
		int direction = direction_;
		int step = step_;
		
		List<TurtleAction> actionPlan = new ArrayList<TurtleAction>();
		for (AtomicAction action : actions) {
			switch (action.function_) {
			case fctRESET:
				direction = defaultDirection_;
				actionPlan.add(new TurtleAction(this,InternalAction.reset));
				break;
			case fctMOVE:
				double x = Math.cos(Math.toRadians(direction));
				double y = Math.sin(Math.toRadians(direction));
				for (int i = 0; i < action.value_ * step; ++i) {
					actionPlan.add(new MoveTurtleAction(this,InternalAction.setTarget,x,y));
				}
				break;
			case fctTURN:
				direction = (int)action.value_;
				actionPlan.add(new IntTurtleAction(this,InternalAction.setDirection,(int)action.value_));
				break;
			case fctDOWN:
				actionPlan.add(new BoolTurtleAction(this,InternalAction.setWriting,true));
				break;
			case fctUP:
				actionPlan.add(new BoolTurtleAction(this,InternalAction.setWriting,false));
				break;
			case fctNORTH:
				for (int i = 0; i < action.value_ * step; ++i) {
					actionPlan.add(new MoveTurtleAction(this,InternalAction.setTarget,0,-1));
				}
				break;
			case fctSOUTH:
				for (int i = 0; i < action.value_ * step; ++i) {
					actionPlan.add(new MoveTurtleAction(this,InternalAction.setTarget,0,1));
				}
				break;
			case fctWEST:
				for (int i = 0; i < action.value_ * step; ++i) {
					actionPlan.add(new MoveTurtleAction(this,InternalAction.setTarget,-1,0));
				}
				break;
			case fctEAST:
				for (int i = 0; i < action.value_ * step; ++i) {
					actionPlan.add(new MoveTurtleAction(this,InternalAction.setTarget,1,0));
				}
				break;
			case fctSTEP:
				step = (int)action.value_;
				actionPlan.add(new DoubleTurtleAction(this,InternalAction.setStep,(int)action.value_));
				break;
			case fctCOLOR:
				actionPlan.add(new ColorTurtleAction(this,InternalAction.setStep,colorTable_[(int)action.value_]));
				break;
			case fctSIZE:
				actionPlan.add(new DoubleTurtleAction(this,InternalAction.setSize,(int)action.value_));
				break;
			default:
				break;
			}
		}
		return actionPlan;
	}
	
	public void setSpeed(double speed) {
		speed_ = speed;
	}
	
	public void init(long timestamp, List<TurtleAction> actionPlan) {
		lastProcessedTimestamp_ = timestamp;
		int localDirection = direction_;
		Color localColor = color_;
		double localX = currentX_;
		double localY = currentY_;
		boolean localIsWriting = isWriting_;
		
		drawPlan_ = new ArrayList<Turtle.TurtlePoint>();
		drawnPoints_ = new ArrayList<Turtle.TurtlePoint>();
		for (TurtleAction action : actionPlan) {
			switch (action.action_) {
			case reset:
				localDirection = defaultDirection_;
				localColor = defaultColor_;
//				localSize = defaultSize_;
				localX = initialX_;
				localY = initialY_;
				localIsWriting = false;
				drawPlan_.add(new TurtlePoint(localX,localY,localIsWriting,localColor,localDirection));
				break;
			case setDirection:
				localDirection = ((IntTurtleAction)action).value_;
				break;
			case setColor:
				localColor = ((ColorTurtleAction)action).value_;
				break;
			case setTarget:
				localX += ((MoveTurtleAction)action).targetX_;
				localY += ((MoveTurtleAction)action).targetY_;
				drawPlan_.add(new TurtlePoint(localX,localY,localIsWriting,localColor,localDirection));
				break;
			case setWriting:
				localIsWriting = ((BoolTurtleAction)action).value_;
				break;
			}
		}
	}
	
	public boolean process(long timestamp) {
		int processedMovement = (int)((timestamp - lastProcessedTimestamp_) * speed_);
		
		if (!drawPlan_.isEmpty() && processedMovement > 0) {
			for (int i = 0; i < processedMovement && drawPlan_.size() > 0; ++i) {
				drawnPoints_.add(drawPlan_.remove(0));
			}
			
			TurtlePoint lastPoint = drawnPoints_.get(drawnPoints_.size()-1); 
			currentX_ 	= lastPoint.x_;
			currentY_ 	= lastPoint.y_;
			color_ 		= lastPoint.color_;
			direction_ 	= lastPoint.direction_;
			isWriting_ 	= lastPoint.write_;
			
			lastProcessedTimestamp_ = timestamp;
			
			setChanged();
			notifyObservers();
			return true;
		}		
		return false;
	}

	public List<TurtlePoint> getDrawnPoints() {
		return drawnPoints_;
	}

	public long getTimestamp() {
		return lastProcessedTimestamp_;
	}
}
