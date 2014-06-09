package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import clientView.GraphicalClientFrame;

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
		gClient.askForSoloGame( GraphicalClientFrame.GRAPH_GAME_NAME );
	}
}
