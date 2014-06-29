package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import clientView.GraphicalClientFrame;

import common.MazeCommonInformation;

public class MazeButtonModel extends AbstractButtonModel {

	GraphicalClientFrame gClient;
	
	public MazeButtonModel( GraphicalClientFrame gClient, 
			   				 DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		gClient.requestGame( MazeCommonInformation.GAME_NAME );
	}
}
