package common;

public class MessageType {

	public static final String MessageClose = "CLOSE";
	public static final String MessageOpen = "OPEN";
	public static final String MessageReady = "READY";
	public static final String MessageStart = "START";
	public static final String MessageStartSoon = "STARTSOON";
	public static final String MessageAsked = "ASKED";
	public static final String MessageAccepted = "ACCEPTED";
	public static final String MessageRefused = "REFUSED";
	public static final String MessageUpdatePosition = "UPDATE_POSITION";
	public static final String MessageChangeDirection = "CHANGE_DIRECTION";
	public static final String MessageEnd = "END";
	
	public static final String MessageInitPieceInformation = "INIT_PIECE_INFORMATION";
	public static final String MessageUpdatePieceInformation = "UPDATE_PIECE_INFORMATION";
	public static final String MessagePlayerToPlay = "UPDATE_PLAYER_TO_PLAY";
	public static final String MessageMoveInvalid = "MOVE_INVALID";
	public static final String MessagePlayerMovablePieces = "MOVABLE_PIECES";
	public static final String MessagePlayerTargetCells = "TARGET_CELLS";
	public static final String MessagePlayerAskTargetCells = "ASK_TARGET_CELLS";
	public static final String MessagePlayerMovePiece = "MOVE_PIECE";
	
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
	public static final String MessageGameStartSoon = MessageGame + " " + MessageStartSoon;
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

	public static final String MessageGameInitPieceInformation = MessageGame + " " + MessageInitPieceInformation;
	public static final String MessageGameUpdatePieceInformation = MessageGame + " " + MessageUpdatePieceInformation;
	public static final String MessageGamePlayerToPlay = MessageGame + " " + MessagePlayerToPlay;
	public static final String MessageGameMoveInvalid = MessageGame + " " + MessageMoveInvalid;
	public static final String MessageGamePlayerMovablePieces = MessageGame + " " + MessagePlayerMovablePieces;
	public static final String MessageGamePlayerTargetCells = MessageGame + " " + MessagePlayerTargetCells;
	public static final String MessageGamePlayerAskTargetCells = MessageGame + " " + MessagePlayerAskTargetCells;
	public static final String MessageGamePlayerMovePiece = MessageGame + " " + MessagePlayerMovePiece;

	public static final String MessageGameSoloGameOpponent = "SOLO_OPPONENT";
	
	// new message for interaction with BackBoneServer
	public static final String MessageSystem = "SYSTEM";
	public static final String MessageSystemInit = MessageSystem + "_INIT_CONNECTION";
	public static final String MessageSystemClose = MessageSystem + "_CLOSE_CONNECTION";
	
	public static final String MessageSystemLoginAccepted = MessageSystem + "_" + MessageLoginAccepted;
	public static final String MessageSystemLoginRefused = MessageSystem + "_" + MessageLoginRefused;
	public static final String MessageSystemLoginAsked = MessageSystem + "_" + MessageLoginAsked;
	public static final String MessageSystemRegister = MessageSystem + "_" + "REGISTER";
	public static final String RegistrationAsConsumer = "CONSUMER";
	public static final String RegistrationAsProvider = "PROVIDER";
}
