package ro.edu.ubb.blackholemainserver.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * An object which contains all necessary data for a server registration.
 * 
 * @author Administrator
 * 
 */
@XmlRootElement
public class RegistrationDataStorage extends DataStorage {

	/**
	 * This constant indicates the type of the message.
	 */
	private int messageType = 1;

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

	/**
	 * The user's email address.
	 */
	private String email = null;

	public RegistrationDataStorage() {
		super();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getMessageType() {
		return messageType;
	}

	@Deprecated
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
}
