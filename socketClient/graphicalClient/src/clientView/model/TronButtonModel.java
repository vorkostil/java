package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

import javax.swing.JOptionPane;

import clientView.GraphicalClientFrame;

import common.TronCommonInformation;

public class TronButtonModel extends AbstractButtonModel {

	GraphicalClientFrame gClient;
	
	public TronButtonModel( GraphicalClientFrame gClient, 
			   				DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		int response = JOptionPane.showConfirmDialog( null, 
											  		  "Create a new " + TronCommonInformation.GAME_SHORT_NAME + " game (join an existing one if NO answered)",
											  		  "Playing " + TronCommonInformation.GAME_SHORT_NAME,
											  		  JOptionPane.YES_NO_OPTION,
											  		  JOptionPane.QUESTION_MESSAGE );
		if ( response == JOptionPane.YES_OPTION )
		{
			gClient.requestGame( TronCommonInformation.GAME_NAME );
		}
		else
		{
			gClient.joinGame( TronCommonInformation.GAME_NAME );
		}
	}
}
