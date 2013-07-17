package ro.edu.ubb.blackholemainserver.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * An object which contains all necessary data for an IP change.
 * 
 * @author Administrator
 * 
 */
@XmlRootElement
public class IpChangeDataStorage extends DataStorage {

	/**
	 * This constant indicates the type of the message.
	 */
	private int messageType = 4;

	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server port.
	 */
	private int serverPort = -1;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	public IpChangeDataStorage() {
		super();
	}

	public int getMessageType() {
		return messageType;
	}

	@Deprecated
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public byte[] getServerPassword() {
		return serverPassword;
	}

	public void setServerPassword(byte[] serverPassword) {
		this.serverPassword = serverPassword;
	}

}
