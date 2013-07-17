package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * An object which contains all necessary data for an IP change to the main server.
 * 
 * @author Administrator
 * @version 1.0
 */

public class IpChangeBHMessage extends BlackHoleMessage {

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

	public IpChangeBHMessage() {
		super(BHMessageType.BH_IP_CHANGE_MESSAGE);
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
