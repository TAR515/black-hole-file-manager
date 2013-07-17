package ro.edu.ubb.blackholemainserver.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * An object which contains a server IP and Port.
 * 
 * @author Administrator
 * 
 */
@XmlRootElement
public class IpAndPortDataStorage extends DataStorage {

	/**
	 * This constant indicates the type of the message.
	 */
	private int messageType = 11;

	/**
	 * Server IP address.
	 */
	private String serverIP = null;

	/**
	 * Server port.
	 */
	private int serverPort = -1;

	public IpAndPortDataStorage() {
		super();
	}

	public int getMessageType() {
		return messageType;
	}

	@Deprecated
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
