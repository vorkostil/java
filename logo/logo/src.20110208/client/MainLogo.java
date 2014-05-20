package client;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import server.ServerManager;
import server.Turtle;

public class MainLogo {
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		Parser parser = new Parser();
//		
//		parser.addToken("reset", 	Turtle.Function.fctRESET, 		TokenKind.tkAtomic);
//		parser.addToken("move", 	Turtle.Function.fctMOVE, 		TokenKind.tkAtomic);
//		parser.addToken("rotate", 	Turtle.Function.fctTURN, 		TokenKind.tkAtomic);
//		parser.addToken("write", 	Turtle.Function.fctDOWN, 		TokenKind.tkAtomic);
//		parser.addToken("unwrite", 	Turtle.Function.fctUP, 			TokenKind.tkAtomic);
//		parser.addToken("up", 		Turtle.Function.fctNORTH, 		TokenKind.tkAtomic);
//		parser.addToken("down", 	Turtle.Function.fctSOUTH, 		TokenKind.tkAtomic);
//		parser.addToken("left", 	Turtle.Function.fctWEST, 		TokenKind.tkAtomic);
//		parser.addToken("right", 	Turtle.Function.fctEAST, 		TokenKind.tkAtomic);
//		parser.addToken("step", 	Turtle.Function.fctSTEP, 		TokenKind.tkAtomic);
//		parser.addToken("loop", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkBeginLoop);
//		parser.addToken("end",		Turtle.Function.fctUNKNOWN, 	TokenKind.tkEndLoop);
//		parser.addToken("func", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkBeginFunction);
//		parser.addToken("end",		Turtle.Function.fctUNKNOWN, 	TokenKind.tkEndFunction);
//		parser.addToken("call", 	Turtle.Function.fctUNKNOWN, 	TokenKind.tkCallFunction);
//		
//		List<String> lines = new ArrayList<String>();
//		
//		lines.add("reset");
//		lines.add("write");
//		lines.add("func CARRE");
//		lines.add("loop 4");
//		lines.add("move 20");
//		lines.add("rotate 90");
//		lines.add("end_loop");
//		lines.add("end_func");
//		lines.add("loop 2");
//		lines.add("move 40");
//		lines.add("rotate 60");
//		lines.add("call CARRE");
//		lines.add("end_loop");
//		lines.add("unwrite");
//		
//		
//		Turtle turtle = new Turtle(300,300);
//		List<AtomicAction> result = parser.parse(lines);
//		for (AtomicAction action : result) {
//			System.out.println(action);
//		}
//		turtle.createActionPlan(result);
//		turtle.setSpeed(1);
//		turtle.init(0);
//		List<TurtlePoint> points = turtle.process(1000);
//		for (TurtlePoint tp : points) {
//			System.out.println(tp);
//		}
		
		/*
reset
function CARRE
loop 4
move 40
turn 90
end
end
write
loop 2
call CARRE
move 20
turn 60
end
		 */
		Turtle turtle = new Turtle(300,300, new Date().getTime());
		
		// server manager initialization
		ServerManager manager = new ServerManager(turtle);
		manager.run();
		
		MainView fen = new MainView(turtle) ; 
		fen.setVisible(true) ;
	}
}
