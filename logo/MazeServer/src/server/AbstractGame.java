package server;


public interface AbstractGame {

	public void init();

	public void setup();

	public void tearDown();

	public void finish();

	public void start();
	
	public void stop();
}