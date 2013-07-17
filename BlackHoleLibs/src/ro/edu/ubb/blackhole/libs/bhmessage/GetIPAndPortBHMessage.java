package ro.edu.ubb.blackhole.libs.bhmessage;


/**
 * An object which contains all necessary data for a request to get server IP and Port.
 * 
 * @author Administrator
 * @version 1.0
 */
public class GetIPAndPortBHMessage extends BlackHoleMessage {
	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	public GetIPAndPortBHMessage() {
		super(BHMessageType.BH_GET_IP_AND_PORT_MESSAGE);
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
