package client;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import server.Parser;
import server.Parser.AtomicAction;
import server.Parser.TokenKind;
import server.ServerManager;
import server.Turtle;
import server.Turtle.TurtleAction;

public class MainTest {
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// parser initialization
		Parser parser = new Parser();
		
		parser.addToken("reset", 	Turtle.Function.fctRESET, 		TokenKind.tkAtomic);
		parser.addToken("move", 	Turtle.Function.fctMOVE, 		TokenKind.tkAtomic);
		parser.addToken("rotate", 	Turtle.Function.fctTURN, 		TokenKind.tkAtomic);
		parser.addToken("write", 	Turtle.Function.fctDOWN, 		TokenKind.tkAtomic);
		parser.addToken("unwrite", 	Turtle.Function.fctUP, 			TokenKind.tkAtomic);
		parser.addToken("up", 		Turtle.Function.fctNORTH, 		TokenKind.tkAtomic);
		parser.addToken("down", 	Turtle.Function.fctSOUTH, 		TokenKind.tkAtomic);
		parser.addToken("left", 	Turtle.Function.fctWEST, 		TokenKind.tkAtomic);
		parser.addToken("right", 	Turtle.Function.fctEAST, 		TokenKind.tkAtomic);
		parser.addToken("step", 	Turtle.Function.fctSTEP, 		TokenKind.tkAtomic);
		parser.addToken("loop", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkBeginLoop);
		parser.addToken("func", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkBeginFunction);
		parser.addToken("end",		Turtle.Function.fctUNKNOWN, 	TokenKind.tkEnd);
		parser.addToken("call", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkCallFunction);
		
		// sample logo script
		List<String> lines = new ArrayList<String>();
		lines.add("func CARRE");
		lines.add("loop 4");
		lines.add("move 20");
		lines.add("rotate 90");
		lines.add("end");
		lines.add("end");
		lines.add("reset");
		lines.add("write");
//		lines.add("loop 2");
//		lines.add("move 40");
//		lines.add("rotate 60");
		lines.add("call CARRE");
//		lines.add("end");
		lines.add("unwrite");

		// script analyzed
		List<AtomicAction> result = parser.parse(lines);
		for (AtomicAction action : result) {
			System.out.println(action);
		}

		// turtle initialization
		Turtle turtle = new Turtle(300,300, new Date().getTime());
		
		// server manager initialization
		ServerManager manager = new ServerManager(turtle);
		manager.run();
		
		// TurtleText initialization
		new TurtleTextDisplayer(turtle);
		new TurtleGraphicDisplayer(turtle);
		
		// set new action plan
		List<TurtleAction> actions = turtle.createActionPlan(result);
		turtle.setSpeed(0.25);
		turtle.init(new Date().getTime(), actions);
		
	}
}
