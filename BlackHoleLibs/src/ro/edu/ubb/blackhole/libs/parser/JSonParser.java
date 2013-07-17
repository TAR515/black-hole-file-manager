package ro.edu.ubb.blackhole.libs.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ro.edu.ubb.blackhole.libs.bhmessage.BlackHoleMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.SimpleBHMessage;

import com.google.gson.Gson;

/**
 * Provides a runtime JSon parsing framework for client applications including creating and loading mostly Black Hole messages. 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class JSonParser {
	
	/**
	 * Regular expansion to get the message type from a JSon formated string.
	 */
	private static final String MESSAGE_TYPE_REGEX = "(\"messageType\":)(\\d+)(,|\\})";
	
	/**
	 * Regular expansion to get the request message type from a JSon formated string.
	 */
	private static final String REQUEST_MESSAGE_TYPE_REGEX = "(\"requestMessageType\":)(\\d+)(,|\\})";
	
	/**
	 * Creates a JSon string from a {@link BlackHoleMessage} object.
	 * 
	 * @param obj
	 *            Black Hole message.
	 * @return JSon string.
	 */
	public static <T extends BlackHoleMessage> String createBHMessage(T obj) {
		Gson gsonParser = new Gson();

		return gsonParser.toJson(obj);
	}

	/**
	 * Load the data to a specified object from the given JSon string.
	 * 
	 * @param message
	 *            JSon string.
	 * @param objType
	 *            The type of the object which the client application want to get, this object must be extended from the {@link BlackHoleMessage} class.
	 * @return A Black Hole message object.
	 */
	public static <T extends BlackHoleMessage> BlackHoleMessage loadBHMessage(String message, Class<T> objType) {
		Gson gsonParser = new Gson();

		return (BlackHoleMessage) gsonParser.fromJson(message, objType);
	}

	/**
	 * Get the type of a Black Hole message.
	 * 
	 * @param message
	 *            JSon message string.
	 * @return an integer which indicates the type of the message or -infinity value if the given string is not a Black Hole message.
	 */
	public static int getMessageType(String message) {
		if (message != null) {
			Pattern p = Pattern.compile(JSonParser.MESSAGE_TYPE_REGEX);
			Matcher m = p.matcher(message);

			while (m.find()) {
				return Integer.parseInt(m.group(2));
			}
		}
		
		return -Integer.MIN_VALUE;
	}

	/**
	 * Get the request type of a Black Hole message. 
	 * 
	 * @param message
	 *            JSon message string.
	 * @return an integer which indicates the request type of the message or -infinity value if the given string is not a Black Hole message.
	 */
	public static int getRequestMessageType(String message) {
		Pattern p = Pattern.compile(JSonParser.REQUEST_MESSAGE_TYPE_REGEX);
		Matcher m = p.matcher(message);

		while (m.find()) {
			return Integer.parseInt(m.group(2));
		}

		return -Integer.MIN_VALUE;
	}

	/**
	 * Creates a response to the client.
	 * 
	 * @param responseCode
	 *            server response code.
	 * @return an XML formated string or null if an exception were occurred.
	 */
	public static String createSimpleResponse(int responseCode, int requestMessageType) {
		Gson gsonParser = new Gson();

		SimpleBHMessage simpleResponse = new SimpleBHMessage(responseCode, requestMessageType);

		return gsonParser.toJson(simpleResponse);
	}

}
