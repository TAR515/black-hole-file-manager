package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * An object which contains all necessary data for a simple login to the main server.
 * 
 * @author Administrator
 * @version 1.0
 */

public class LoginBHMessage extends BlackHoleMessage {

	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	/**
	 * Server port.
	 */
	private int serverPort = -1;

	public LoginBHMessage() {
		super(BHMessageType.BH_LOG_IN_MESSAGE);
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

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
