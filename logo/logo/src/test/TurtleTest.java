package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import server.Parser;
import server.Parser.AtomicAction;
import server.Parser.TokenKind;
import server.Turtle;
import server.Turtle.TurtleAction;

public class TurtleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// parser initialization
		Parser parser = new Parser();
		
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
		lines.add("call CARRE");
		lines.add("unwrite");

		// script analyzed
		List<AtomicAction> result = parser.parse(lines);
		
		// turtle initialization
		Turtle turtle = new Turtle(300,300, new Date().getTime());
		
		// set new action plan
		List<TurtleAction> actions = turtle.createActionPlan(result);
		for (TurtleAction action : actions) {
			System.out.println("action: " + action);
		}
	}

}
