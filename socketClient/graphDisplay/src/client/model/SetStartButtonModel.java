package client.model;

import client.GraphDisplayGameFrame;
import graphic.model.AbstractStateButtonModel;
import helper.DataRepository.DataInformation;

public class SetStartButtonModel extends AbstractStateButtonModel 
{

	private GraphDisplayGameFrame mainFrame;

	public SetStartButtonModel(GraphDisplayGameFrame graphDisplayGameFrame, DataInformation data) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
	}

	@Override
	public void callAction() 
	{
		mainFrame.setCurrentStateForCell( GraphDisplayGameFrame.START );
	}

}
