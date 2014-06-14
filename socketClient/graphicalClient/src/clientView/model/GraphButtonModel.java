package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import clientView.GraphicalClientFrame;

import common.GraphCommonInformation;

public class GraphButtonModel extends AbstractButtonModel {

	GraphicalClientFrame gClient;
	
	public GraphButtonModel( GraphicalClientFrame gClient, 
			   				 DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		gClient.askForSoloGame( GraphCommonInformation.GAME_NAME );
	}
}
