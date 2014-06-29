package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

import javax.swing.JOptionPane;

import clientView.GraphicalClientFrame;

import common.ChessCommonInformation;

public class ChessButtonModel extends AbstractButtonModel {

	GraphicalClientFrame gClient;
	
	public ChessButtonModel( GraphicalClientFrame gClient, 
			   				DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		int response = JOptionPane.showConfirmDialog( null, 
				 							  		  "Create a new " + ChessCommonInformation.GAME_SHORT_NAME + " game (join an existing one if NO answered)",
				 							  		  "Playing " + ChessCommonInformation.GAME_SHORT_NAME,
				 							  		  JOptionPane.YES_NO_OPTION,
				 							  		  JOptionPane.QUESTION_MESSAGE );
		if ( response == JOptionPane.YES_OPTION )
		{
			gClient.requestGame( ChessCommonInformation.GAME_NAME );
		}
		else
		{
			gClient.joinGame( ChessCommonInformation.GAME_NAME );
		}
	}
}
