package clientView.model;

import clientView.GraphicalClientFrame;
import server.TronGameServer;
import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

public class TronButtonModel extends AbstractButtonModel {

	GraphicalClientFrame gClient;
	
	public TronButtonModel( GraphicalClientFrame gClient, 
			   				DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		gClient.askForGameTo( TronGameServer.NAME );
	}

}
