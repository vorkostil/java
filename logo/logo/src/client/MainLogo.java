package client;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import server.ServerManager;
import server.Turtle;

public class MainLogo {
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Turtle turtle = new Turtle(300,300, new Date().getTime());
		
		// server manager initialization
		ServerManager manager = new ServerManager(turtle);
		manager.run();
		
		MainView fen = new MainView(turtle) ; 
		fen.setVisible(true) ;
	}
}
