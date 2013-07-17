package ro.edu.ubb.blackhole.gui;

import java.io.IOException;
import java.util.Properties;

import ro.edu.ubb.blackhole.libs.errmsg.ErrorMessageInterpreter;

/**
 * 
 * Provides possibility to get an error message from an error code(Black Hole error code and message).
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GuiMessageInterpreter {

	/**
	 * Property
	 */
	private static Properties property = new Properties();

	/**
	 * The location of the property file which contains the error messages.
	 */
	private static final String ERROR_MESSAGE_PROP_LOCATION = "GuiMessages.properties";

	/**
	 * Loads the given property file.
	 */
	static {
		try {
			property.load(ErrorMessageInterpreter.class.getClassLoader().getResourceAsStream(
					ERROR_MESSAGE_PROP_LOCATION));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the error massage which benefits to the given error code.
	 * 
	 * @param errCode
	 *            Black Hole error code.
	 * @return Black Hole error message.
	 */
	public static String getErrorMessage(int errCode) {
		return GuiMessageInterpreter.property.getProperty(Integer.toString(errCode));
	}
}
