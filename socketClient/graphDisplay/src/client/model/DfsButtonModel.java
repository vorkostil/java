package client.model;

import client.GraphDisplayGameFrame;
import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

public class DfsButtonModel extends AbstractButtonModel 
{

	private GraphDisplayGameFrame mainFrame;

	public DfsButtonModel( GraphDisplayGameFrame graphDisplayGameFrame,
						   DataInformation data ) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
	}

	@Override
	public void callAction() 
	{
		mainFrame.callForDfs();
	}

}
