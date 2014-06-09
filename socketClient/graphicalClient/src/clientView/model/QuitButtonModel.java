package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

public class QuitButtonModel extends AbstractButtonModel {

	public QuitButtonModel(DataInformation data) 
	{
		super(data);
	}

	@Override
	public void callAction() 
	{
		System.exit( 0 );
	}

}
