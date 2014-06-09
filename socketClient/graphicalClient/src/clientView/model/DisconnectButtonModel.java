package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import clientView.GraphicalClientFrame;

public class DisconnectButtonModel extends AbstractButtonModel {

	GraphicalClientFrame gClient;
	
	public DisconnectButtonModel( GraphicalClientFrame gClient, 
							   	  DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		gClient.closeConnection();
	}
}
