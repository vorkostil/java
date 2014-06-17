package client.model;

import client.GraphDisplayGameFrame;
import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

public class AStarButtonModel extends AbstractButtonModel {

	private static final String HEURISTIC = "heuristic";
	
	private GraphDisplayGameFrame mainFrame;
	private String heuristic;

	public AStarButtonModel( GraphDisplayGameFrame graphDisplayGameFrame,
						     DataInformation data ) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
		this.heuristic = data.getStringValue( HEURISTIC );
	}

	@Override
	public void callAction() 
	{
		mainFrame.callForAstar( heuristic );
	}
}
