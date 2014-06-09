package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import server.TronGameServer;
import clientView.GraphicalClientFrame;

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
		String opponent = gClient.displayOpponentChoiceDialog( TronGameServer.NAME, false );
		if ( opponent != null )
		{
			gClient.askForGameTo( opponent, 
								  TronGameServer.NAME );
		}
	}
}
