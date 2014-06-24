package client.model;

import client.GraphDisplayGameFrame;
import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

public class MessageButtonModel extends AbstractButtonModel 
{
	private static final String MESSAGE = "message";
	
	protected GraphDisplayGameFrame mainFrame;
	protected String message;

	public MessageButtonModel( GraphDisplayGameFrame graphDisplayGameFrame,
						   	   DataInformation data ) 
	{
		super(data);
		this.mainFrame = graphDisplayGameFrame;
		this.message = data.getStringValue( MESSAGE );
	}

	@Override
	public void callAction() 
	{
		mainFrame.sendMessage( message );
	}

}
