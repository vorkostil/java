package client.model;

import client.MazeMainFrame;
import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

public class ReadyButtonModel extends AbstractButtonModel {

	private MazeMainFrame mainFrame;

	public ReadyButtonModel( MazeMainFrame mazeMainFrame, 
							 DataInformation data) 
	{
		super(data);
		this.mainFrame = mazeMainFrame;
	}

	@Override
	public void callAction() 
	{
		mainFrame.readyToPlay();
	}

}
