package client.model;

import client.GraphDisplayGameFrame;
import graphic.model.AbstractStateButtonModel;
import helper.DataRepository.DataInformation;

public class SetBlockButtonModel extends AbstractStateButtonModel 
{

	private GraphDisplayGameFrame mainFrame;

	public SetBlockButtonModel( GraphDisplayGameFrame graphDisplayGameFrame, 
								DataInformation data) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
	}

	@Override
	public void callAction() 
	{
		mainFrame.setCurrentStateForCell( GraphDisplayGameFrame.BLOCK );
	}

}
