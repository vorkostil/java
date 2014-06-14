package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import clientView.GraphicalClientFrame;

import common.ChessCommonInformation;

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
		String opponent = gClient.displayOpponentChoiceDialog( ChessCommonInformation.GAME_NAME, false );
		if ( opponent != null )
		{
			gClient.askForGameTo( opponent, 
								  ChessCommonInformation.GAME_NAME );
		}
	}
}
