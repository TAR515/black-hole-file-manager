package ro.edu.ubb.blackholemainserver.bll;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackholemainserver.datastructures.DataStorage;
import ro.edu.ubb.blackholemainserver.datastructures.SimpleResponseStorage;

public class BHMessageParser {

	private static final String TAG = XMLParser.class.getSimpleName();

	private static final Logger logger = Logger.getLogger(BHMessageParser.class);

	/**
	 * Creates a string from a DataStorage object.
	 * 
	 * @param obj
	 *            an object which contains all necessary data for the specified operation.
	 * @return An XML structured string.
	 * @throws JAXBException
	 */
	public static <T extends DataStorage> String createBHMessage(T obj) throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(obj.getClass());
		final Marshaller marshaller = context.createMarshaller();
		final StringWriter stringWriter = new StringWriter();

		marshaller.marshal(obj, stringWriter);

		return stringWriter.toString();
	}

	/**
	 * Load the data to a specified object from the given XML formated string.
	 * 
	 * @param message
	 *            XML formated string.
	 * @param objType
	 *            The type of the response object which u want to get.
	 * @return An object.
	 * @throws JAXBException
	 */
	public static DataStorage loadBHMessage(String message, Class<?> objType) throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(objType);
		final StringWriter stringWriter = new StringWriter();
		stringWriter.append(message);

		final Unmarshaller unmarshaller = context.createUnmarshaller();

		return (DataStorage) unmarshaller.unmarshal(new StringReader(stringWriter.toString()));
	}

	/**
	 * Get the type of a message.
	 * 
	 * @param message
	 *            XML formated string which contains a "messageType" element.
	 * @return an integer which indicates the type of the message or -infinity value if the element not exists.
	 */
	public static int getMessageType(String message) {
		Pattern p = Pattern.compile("(<messageType>)(\\d+)(</messageType>)");
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
	public static String createSimpleResponse(int responseCode) {
		try {
			final JAXBContext context = JAXBContext.newInstance(SimpleResponseStorage.class);
			final Marshaller marshaller = context.createMarshaller();
			final StringWriter stringWriter = new StringWriter();

			SimpleResponseStorage simpleResponse = new SimpleResponseStorage(responseCode);

			marshaller.marshal(simpleResponse, stringWriter);

			return stringWriter.toString();
		} catch (JAXBException e) {
			logger.error(TAG + ".createSimpleResponse() - Exception: " + e.getMessage());
			return null;
		}
	}

}
