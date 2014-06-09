package clientView.model;

import graphic.model.AbstractButtonModel;
import helper.DataRepository.DataInformation;

import java.io.IOException;
import java.net.UnknownHostException;

import network.client.ConnectionInfo;
import clientView.GraphicalClientFrame;

import common.ConnectionDialog;

public class ConnectButtonModel extends AbstractButtonModel {

	private static final String SERVER_LOGIN_CONFIGURATION = "server_login_configuration";
	
	GraphicalClientFrame gClient;
	
	public ConnectButtonModel( GraphicalClientFrame gClient, 
							   DataInformation data) 
	{
		super(data);
		this.gClient = gClient;
	}

	@Override
	public void callAction() 
	{
		ConnectionDialog dialog = new ConnectionDialog(null, "Connection", true);
		dialog.setDataInformation( gClient.getRepository().getData( SERVER_LOGIN_CONFIGURATION ) );
		ConnectionInfo info = dialog.showConnectionDialog();
		if (  ( info != null )
			&&( info.isValid() == true )  )
		{
			try 
			{
				gClient.launchConnection( info );
			} 
			catch (UnknownHostException e1) 
			{
				e1.printStackTrace();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			}
		}
	}
}
