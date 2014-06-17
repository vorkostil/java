package client.model;

import client.GraphDisplayGameFrame;
import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

public class BfsButtonModel extends AbstractButtonModel {


	private GraphDisplayGameFrame mainFrame;

	public BfsButtonModel( GraphDisplayGameFrame graphDisplayGameFrame,
						   DataInformation data ) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
	}

	@Override
	public void callAction() 
	{
		mainFrame.callForBfs();
	}

}
