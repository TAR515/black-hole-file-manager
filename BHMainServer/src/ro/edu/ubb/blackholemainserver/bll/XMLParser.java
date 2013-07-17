package ro.edu.ubb.blackholemainserver.bll;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ro.edu.ubb.blackholemainserver.bll.commands.RegistrationCommand;

public class XMLParser {

	private static final String TAG = XMLParser.class.getSimpleName();

	private static final Logger logger = Logger.getLogger(RegistrationCommand.class);

	/**
	 * Creates a simple empty XML document.
	 * 
	 * @param rootElementName
	 *            The name of the root element.
	 * @param documentType
	 *            Adds an attribute to the root named "type" with the given value. If the documentType is null then the type
	 *            indicator will not exists.
	 * @param xmlHeader
	 * @return A new XML document.
	 * @throws Exception
	 */
	public static Document createXMLDocument(String rootElementName, String documentType, boolean xmlHeader)
			throws XMLParseException {
		Document document = null;

		try {
			// Creating the simple document.
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.newDocument();

			// Creating the root element.
			Element rootElement = document.createElement(rootElementName);
			if (documentType != null) {
				rootElement.setAttribute("type", documentType);
			}
			document.appendChild(rootElement);

			// Adding the XML type node.
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();

			if (xmlHeader) {
				trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
				trans.setOutputProperty(OutputKeys.INDENT, "yes");
			}

		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			XMLParser.logger.error(TAG + ".createXMLDocument() - " + e.getMessage());
			throw new XMLParseException(TAG + ".createXMLDocument() - " + e.getMessage());
		}
		return document;
	}

	/**
	 * Append the XML with a new element.
	 * 
	 * @param document
	 *            The XML document witch you want to append.
	 * @param parentElement
	 *            The parent node of the new one.
	 * @param elementName
	 *            The new element name.
	 * @param elementValue
	 *            Set the element value if it isn't null. If it is null then the new element will haven't value.
	 * @param attributeNamesAndValues
	 *            Keys are the attribute names and the values are the attribute values. If it is null then there are no arguments.
	 * @return The new element.
	 * @throws XMLParseException
	 *             if document = null or parentElement = null or elementName = null.
	 */
	public static Element addElement(Document document, Element parentElement, String elementName)
			throws XMLParseException {

		if (document == null || parentElement == null || elementName == null) {
			XMLParser.logger.error(TAG + ".addElement() - Illegal Arguments!");
			throw new XMLParseException(
					".addElement() - Illegal Argument Exception - document or parentElement or elementName is null!");
		}

		// Creating the new element.
		Element newElement = document.createElement(elementName);

		// Appending the XML with the created element.
		parentElement.appendChild(newElement);

		return newElement;
	}

	/**
	 * Adds one or more attributes to an element.
	 * 
	 * @param document
	 *            XML Document
	 * @param element
	 *            The target element.
	 * @param attributeNamesAndValues
	 *            HashMap which must contain the attribute name and value.
	 * @return The edited element or null if the given element were null.
	 */
	public static Element addAttributeToElement(Document document, Element element,
			HashMap<String, String> attributeNamesAndValues) {
		Set<String> attributeNames = new HashSet<String>();

		// Set attributes if they are.
		if (attributeNamesAndValues != null) { // if there are attributes.
			for (Entry<String, String> entry : attributeNamesAndValues.entrySet()) {
				attributeNames.add(entry.getKey());
			}

			String currentAttributeValue = null;
			for (String currentAttributeName : attributeNames) {
				currentAttributeValue = attributeNamesAndValues.get(currentAttributeName);
				element.setAttribute(currentAttributeName, currentAttributeValue);
			}
		}

		return element;
	}

	/**
	 * Convert an XML document to String.
	 * 
	 * @param xmlDocument
	 *            Target XML document.
	 * @return Null if an error occurs, String if not.
	 */
	public static String xmlDocumentToString(Document xmlDocument) {
		try {
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();

			StringWriter sw = new StringWriter();
			StreamResult streamResult = new StreamResult(sw);
			DOMSource source = new DOMSource(xmlDocument);
			trans.transform(source, streamResult);

			return sw.toString();
		} catch (TransformerException e) {
			XMLParser.logger.error(TAG + ".createXMLDocument() - " + e.getMessage());
		}

		return null;
	}

	/**
	 * This method returns a list which contains the given attributes from the given elements.
	 * 
	 * @param xmlDocument
	 *            XML document.
	 * @param elementName
	 *            The target elements.
	 * @param attributeName
	 *            The attribute name which you are searching for.
	 * @return All attribute values from the searching area.
	 * @throws IllegalArgumentException
	 */
	public static List<String> getAttribute(Document xmlDocument, String elementName, String attributeName)
			throws XMLParseException {

		List<String> result = new ArrayList<String>();

		if (xmlDocument == null || elementName == null || attributeName == null) {
			XMLParser.logger.error(TAG + ".getAttributeFromElement() - Illegal argument!");
			throw new XMLParseException(TAG + ".getAttributeFromElement() - Illegal argument!");
		}

		NodeList allSoughtElements = xmlDocument.getElementsByTagName(elementName);
		Element currentElement = null;
		for (int i = 0; i < allSoughtElements.getLength(); i++) {
			currentElement = (Element) allSoughtElements.item(i);

			String attributeValue = currentElement.getAttribute(attributeName);

			if ("".equals(attributeValue)) {
				XMLParser.logger.error(TAG
						+ ".getAttributeFromElement() - Attribute not Found / Attribute has no value!");
				throw new XMLParseException(TAG
						+ ".getAttributeFromElement() - Attribute not Found / Attribute has no value!");
			}

			result.add(attributeValue);
		}

		return result;
	}

	/**
	 * Converts an XML String to Document.
	 * 
	 * @param xmlString
	 *            The XML formated String.
	 * @return XML Document, or null if the conversion were not successful.
	 * @throws XMLParseException
	 */
	public static Document xmlStringToDocument(String xmlString) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			return builder.parse(new InputSource(new StringReader(xmlString)));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			XMLParser.logger.error(TAG + ".xmlStringToDocument() - " + e.getMessage());
		}

		return null;
	}

	/**
	 * Create an error XML.
	 * 
	 * @param messageCode
	 * 
	 * @returnError Response XML if it were created / null if the XML were not created.
	 */
	public static Document createSimpleResponse(String messageCode) {
		try {
			Document responseXML = createXMLDocument("BlackHoleMessage", "10", true);

			Element e = addElement(responseXML, responseXML.getDocumentElement(), "Response");
			e.setAttribute("ResponseCode", messageCode);

			return responseXML;
		} catch (XMLParseException e1) {
			XMLParser.logger.error(TAG + ".createSimpleResponse() - XMLParseException: " + e1.getMessage());
			return null;
		}
	}

	/**
	 * Returns the Document type.
	 * 
	 * @param xmlDocument
	 * @return Document type if the type is specified or empty String if not.
	 */
	public static String getDocumentType(Document xmlDocument) {
		Element rootElement = xmlDocument.getDocumentElement();
		return rootElement.getAttribute("type");
	}

	/**
	 * Return a response which contains the server IP and server port.
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @return Response XML if the it were created / null if the XML were not created.
	 */
	public static Document createGetIPAndPortResponse(String serverIp, int serverPort) {
		try {
			Document responseXML = createXMLDocument("BlackHoleMessage", "11", true);
			Element e = addElement(responseXML, responseXML.getDocumentElement(), "Response");
			e.setAttribute("ServerIP", serverIp);
			e.setAttribute("ServerPort", Integer.toString(serverPort));

			return responseXML;
		} catch (XMLParseException e) {
			XMLParser.logger
					.error(TAG + ".createGetIPAndPortResponse - XMLParseException: " + e.getMessage());
			return null;
		}
	}
}
