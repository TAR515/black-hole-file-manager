package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * An object which contains all necessary data for a server registration to Black Hole main server.
 * 
 * @author Administrator
 * @version 1.0
 */

public class RegistrationBHMessage extends BlackHoleMessage {

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

	public RegistrationBHMessage() {
		super(BHMessageType.BH_REGISTRATION_MESSAGE);
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
}
