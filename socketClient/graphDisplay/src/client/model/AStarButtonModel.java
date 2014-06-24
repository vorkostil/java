package client.model;

import helper.DataRepository.DataInformation;
import client.GraphDisplayGameFrame;

public class AStarButtonModel extends MessageButtonModel {

	private static final String HEURISTIC = "heuristic";
	
	private String heuristic;

	public AStarButtonModel( GraphDisplayGameFrame graphDisplayGameFrame,
						     DataInformation data ) 
	{
		super(graphDisplayGameFrame,data);
		this.heuristic = data.getStringValue( HEURISTIC );
	}

	@Override
	public void callAction() 
	{
		mainFrame.sendMessage( message + " " + heuristic );
	}
}
