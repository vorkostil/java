package clientView.model;

import graphic.model.AbstractStateButtonModel;
import helper.DataRepository.DataInformation;
import clientView.GraphicalClientFrame;

public class ConsoleButtonModel extends AbstractStateButtonModel {

	GraphicalClientFrame gClient;
	
	public ConsoleButtonModel(GraphicalClientFrame gClient, DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		if ( isUp == true )
		{
			gClient.showConsoleFrame();
		}
		else
		{
			gClient.hideConsoleFrame();
		}
	}

}
