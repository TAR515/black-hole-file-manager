package ro.edu.ubb.blackholemainserver.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * An object which contains all necessary data for a request to get server IP and Port.
 * 
 * @author Administrator
 * 
 */
@XmlRootElement
public class GetIPAndPortDataStorage extends DataStorage {
	/**
	 * This constant indicates the type of the message.
	 */
	private int messageType = 5;

	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	public GetIPAndPortDataStorage() {
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

	public byte[] getServerPassword() {
		return serverPassword;
	}

	public void setServerPassword(byte[] serverPassword) {
		this.serverPassword = serverPassword;
	}
}
