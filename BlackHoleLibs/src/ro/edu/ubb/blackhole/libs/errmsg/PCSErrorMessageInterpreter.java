package ro.edu.ubb.blackhole.libs.errmsg;

/**
 * Contains all BHPCServer's error messages.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class PCSErrorMessageInterpreter extends ErrorMessageInterpreter {

	// PC Server Errors 400 <-> 700
	public static final String PCS_SERVER_IS_RUNNING = "PCS_SERVER_IS_RUNNING";
	public static final String PCS_SERVER_IS_STOPPED = "PCS_SERVER_IS_STOPPED";
	
	public static final String PCS_INVALID_REQUEST = "PCS_INVALID_REQUEST";
	public static final String PCS_INVALID_RESPONSE = "PCS_INVALID_RESPONSE";
	public static final String PCS_WRONG_SERVERNAME = "PCS_WRONG_SERVERNAME";
	public static final String PCS_WRONG_PASSWORD = "PCS_WRONG_PASSWORD";
	public static final String PCS_MAIN_SERVER_IS_OFFLINE = "PCS_MAIN_SERVER_IS_OFFLINE";
	public static final String PCS_ALL_INFORMATIONS_ARE_REQUIRED = "PCS_ALL_INFORMATIONS_ARE_REQUIRED";
	public static final String PCS_INVALID_INPUT_TYPE = "PCS_INVALID_INPUT_TYPE";
	public static final String PCS_SERVER_WONT_STARTED = "PCS_SERVER_WONT_STARTED";
	public static final String PCS_SERVER_WONT_STOPPED = "PCS_SERVER_WONT_STOPPED";
	public static final String PCS_FILE_NOT_DELETED = "PCS_FILE_NOT_DELETED";
	
}
