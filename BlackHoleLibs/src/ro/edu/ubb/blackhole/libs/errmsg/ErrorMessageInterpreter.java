package ro.edu.ubb.blackhole.libs.errmsg;

import java.io.IOException;
import java.util.Properties;

/**
 * This object contains functionalities by which the object's user can convert the Black Hole error messages to it's error codes and
 * vice versa.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ErrorMessageInterpreter {

	/**
	 * This properties file contains all the Black Hole error messages.
	 */
	private static Properties property = new Properties();

	/**
	 * Location of the property file.
	 */
	private static final String ERROR_MESSAGE_PROP_LOCATION = "ro/edu/ubb/blackhole/libs/errmsg/ErrorMessages.properties";

	// Other 0 - 100
	public static String OPERATION_COMPLETED = "OPERATION_COMPLETED";

	/**
	 * Load the property file.
	 */
	static {
		try {
			ErrorMessageInterpreter.property.load(ErrorMessageInterpreter.class.getClassLoader()
					.getResourceAsStream(ErrorMessageInterpreter.ERROR_MESSAGE_PROP_LOCATION));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the code of the given error message.
	 * 
	 * @param errMsg
	 *            Error Message.
	 * @return Code of the given error message.
	 */
	public static int getErrorCode(String errMsg) {
		return Integer.parseInt(ErrorMessageInterpreter.property.getProperty(errMsg));
	}

	/**
	 * Get the message of the black hole error message by the given error code.
	 * 
	 * @param errCode
	 *            Code of the error message.
	 * @return Error message.
	 */
	public static String getErrorMessage(int errCode) {
		return ErrorMessageInterpreter.property.getProperty(Integer.toString(errCode));
	}
}
