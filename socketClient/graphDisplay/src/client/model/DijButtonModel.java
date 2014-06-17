package client.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;
import client.GraphDisplayGameFrame;

public class DijButtonModel extends AbstractButtonModel {
	
	private GraphDisplayGameFrame mainFrame;
	
	public DijButtonModel( GraphDisplayGameFrame graphDisplayGameFrame,
						   DataInformation data ) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
	}
	
	@Override
	public void callAction() 
	{
		mainFrame.callForDij();
	}
	

}
