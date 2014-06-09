package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import server.ChessGameServer;
import clientView.GraphicalClientFrame;

public class ChessButtonModel extends AbstractButtonModel {

	GraphicalClientFrame gClient;
	
	public ChessButtonModel( GraphicalClientFrame gClient, 
			   				DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		String opponent = gClient.displayOpponentChoiceDialog( ChessGameServer.NAME, false );
		if ( opponent != null )
		{
			gClient.askForGameTo( opponent, 
								  ChessGameServer.NAME );
		}
	}
}
