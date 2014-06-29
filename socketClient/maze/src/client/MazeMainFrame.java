package client;

import game.AbstractGameClientFrame;
import graphic.GraphicalEnvironment;
import graphic.GraphicalEnvironment.ImageLevel;
import graphic.item.GraphicalButtonItem;
import graphic.model.VisibilityModel;

import java.io.IOException;

import client.displayer.MainViewDisplayer;
import client.item.GameDisclaimerItem;
import client.model.ReadyButtonModel;
import client.panel.MainView;

import common.MazeCommonInformation;


@SuppressWarnings("serial")
public class MazeMainFrame extends AbstractGameClientFrame 
{
	// the mandatory part
	private MainViewDisplayer mainDisplayer;
	private MainView displayPanel;
	
	// the static element to manage
	private GameDisclaimerItem gameDisclaimer;
	private GraphicalButtonItem readyButton;

	public MazeMainFrame() throws IOException 
	{
		super( MazeCommonInformation.CONFIG_PATH );
		
		// characteristics of the frame
		this.setTitle( MazeCommonInformation.GAME_SHORT_NAME );

		// create the main panel
		mainDisplayer = new MainViewDisplayer();
		displayPanel = new MainView( repository, tracker, GraphicalEnvironment.TEMPO_60_HZ );
		displayPanel.addDisplayer( MainViewDisplayer.NAME, mainDisplayer );

		// add the panel to the view
		this.setContentPane( displayPanel );

		// change the size of the frame to match the display
		this.setSize( displayPanel.viewMaxWidth + 7, displayPanel.viewMaxHeight + 29 ); // add the border to the size and the padding
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		
		// add the element in the view
		addStaticElement();
		
		// initialize the state of the visible items
		displayPanel.computeDisplayableItems();
	}

	private void addStaticElement() throws IOException 
	{
		// add the game describer which is a simple gItem
		gameDisclaimer = new GameDisclaimerItem( new VisibilityModel( true ),
												 repository.getData( "game_disclaimer_configuration " ),
												 tracker,
												 ImageLevel.ENVIRONMENT_IMAGE.index() );
		displayPanel.addItem( gameDisclaimer, 
							  MainViewDisplayer.NAME,
							  MainViewDisplayer.FIRST_LAYER_LEVEL_TO_DRAW );
	
		// add the ready button
		readyButton = new GraphicalButtonItem( new ReadyButtonModel( this,
																	 repository.getData( "ready_button_configuration" ) ), 
											   repository.getData( "ready_button_configuration" ), 
											   tracker, 
											   ImageLevel.ENVIRONMENT_IMAGE.index() );
		displayPanel.addItem( readyButton, 
							  MainViewDisplayer.NAME, 
							  MainViewDisplayer.LAST_LAYER_LEVEL_TO_DRAW );
	}

	@Override
	public void handleServerMessage(String remain) 
	{
	}

	// send the ready message to the server
	@Override
	public void readyToPlay()
	{
		// remove the disclaimer and the readyButton
		gameDisclaimer.getModel().hide();
		readyButton.getModel().hide();
		
		// send the ready message
		super.readyToPlay();
	}
}
