package main;


public class MainJavaGameProvider 
{
	public static void main(String[] args) 
	{
		ClientConnectionManager provider = new ClientConnectionManager();
		provider.start();
	}
}