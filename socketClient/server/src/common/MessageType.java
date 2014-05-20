package common;

public class MessageType {
	public static final String MessageSystem = "SYSTEM";
	
	public static final String MessageClose = "CLOSE";
	public static final String MessageOpen = "OPEN";
	public static final String MessageReady = "READY";
	public static final String MessageStart = "START";
	public static final String MessageAsked = "ASKED";
	public static final String MessageAccepted = "ACCEPTED";
	public static final String MessageRefused = "REFUSED";
	public static final String MessageUpdatePosition = "UPDATE_POSITION";
	public static final String MessageChangeDirection = "CHANGE_DIRECTION";
	public static final String MessageEnd = "END";
	
	public static final String MessageLogin = "LOGIN";
	public static final String MessageLoginAsked = MessageLogin + "_" + MessageAsked; 
	public static final String MessageLoginAccepted = MessageLogin + "_" + MessageAccepted; 
	public static final String MessageLoginRefused = MessageLogin + "_" + MessageRefused;
	
	public static final String MessageContactListSnapshot = "CONTACT_LIST_SNAPSHOT";

	public static final String MessageCommunicationSpecific = "SPECIFIC_MESSAGE";
	public static final String MessageCommunicationSpecificClose = MessageCommunicationSpecific + " " + MessageClose;
	public static final String MessageCommunicationSpecificOpen = MessageCommunicationSpecific + " " + MessageOpen;

	public static final String MessageGame = "GAME_MESSAGE";
	public static final String MessageGameClose = MessageGame + " " + MessageClose;
	public static final String MessageGameOpen = MessageGame + " " + MessageOpen;
	public static final String MessageGameReady = MessageGame + " " + MessageReady;
	public static final String MessageGameStart = MessageGame + " " + MessageStart;
	public static final String MessageGameAsked = MessageGame + " " + MessageAsked;
	public static final String MessageGameAccepted = MessageGame + " " + MessageAccepted;
	public static final String MessageGameRefused = MessageGame + " " + MessageRefused;
	public static final String MessageGameUpdatePosition = MessageGame + " " + MessageUpdatePosition;
	public static final String MessageGameChangeDirection = MessageGame + " " + MessageChangeDirection;
	public static final String MessageGameEnd = MessageGame + " " + MessageEnd;
	
	public static final String MessageMoveLeft = "MOVE_LEFT";
	public static final String MessageMoveRight = "MOVE_RIGHT";
	public static final String MessageMoveUp = "MOVE_UP";
	public static final String MessageMoveDown = "MOVE_DOWN";

}
