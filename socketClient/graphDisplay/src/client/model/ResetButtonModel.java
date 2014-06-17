package client.model;

import client.GraphDisplayGameFrame;
import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

public class ResetButtonModel extends AbstractButtonModel {


	private GraphDisplayGameFrame mainFrame;

	public ResetButtonModel( GraphDisplayGameFrame graphDisplayGameFrame,
						     DataInformation data ) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
	}

	@Override
	public void callAction() 
	{
		mainFrame.callForReset();
	}

}
