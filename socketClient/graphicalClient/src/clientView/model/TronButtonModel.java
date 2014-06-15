package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import clientView.GraphicalClientFrame;

import common.TronCommonInformation;

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
		String opponent = gClient.displayOpponentChoiceDialog( TronCommonInformation.GAME_NAME, false );
		if ( opponent != null )
		{
// TODO use new protocol
//			gClient.askForGameTo( opponent, 
//								  TronCommonInformation.GAME_NAME );
		}
	}
}
