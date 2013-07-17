package ro.edu.ubb.blackholemainserver.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This type of object contains a simple response from the Black Hole main server.
 * 
 * @author Administrator
 * 
 */
@XmlRootElement
public class SimpleResponseStorage extends DataStorage {

	/**
	 * This number indicates the type of the message.
	 */
	private int messageType = 10;

	/**
	 * This code represents the server response.
	 */
	private int responseCode = Integer.MIN_VALUE;

	public SimpleResponseStorage() {
		super();
	}

	public SimpleResponseStorage(int responseCode) {
		super();
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getMessageType() {
		return messageType;
	}

	@Deprecated
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

}
