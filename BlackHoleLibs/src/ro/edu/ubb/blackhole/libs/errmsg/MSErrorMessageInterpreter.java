package ro.edu.ubb.blackhole.libs.errmsg;

/**
 * Contains all BHMainServer's error messages.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class MSErrorMessageInterpreter extends ErrorMessageInterpreter {

	// Main Server Errors 100 <-> 400
	public static final String MS_INVALID_REQUEST = "MS_INVALID_REQUEST";
	public static final String MS_DATABASE_ERROR = "MS_DATABASE_ERROR";
	public static final String MS_WRONG_SERVERNAME = "MS_WRONG_SERVERNAME";
	public static final String MS_WRONG_PASSWORD = "MS_WRONG_PASSWORD";
	public static final String MS_PC_SERVER_IS_OFFLINE = "MS_PC_SERVER_IS_OFFLINE";
	public static final String MS_SERVER_ALLREADY_EXISTS = "MS_SERVER_ALLREADY_EXISTS";

}
