package client.model;

import client.GraphDisplayGameFrame;
import graphic.model.AbstractStateButtonModel;
import helper.DataRepository.DataInformation;

public class SetKindButtonModel extends AbstractStateButtonModel 
{
	private static final String KIND = "kind";
	
	private GraphDisplayGameFrame mainFrame;
	private String kind;
	
	public SetKindButtonModel( GraphDisplayGameFrame graphDisplayGameFrame, 
							   DataInformation data ) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
		this.kind = data.getStringValue( KIND );
	}

	@Override
	public void callAction() 
	{
		mainFrame.setCurrentStateForCell( kind );
	}

}
