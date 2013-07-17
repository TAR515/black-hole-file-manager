package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * An object which contains a server IP and Port.
 * 
 * @author Administrator
 * @version 1.0
 */
public class IpAndPortBHMessage extends BlackHoleMessage {

	/**
	 * Server IP address.
	 */
	private String serverIP = null;

	/**
	 * Server port.
	 */
	private int serverPort = -1;

	public IpAndPortBHMessage() {
		super(BHMessageType.BH_PC_SERVER_IP_AND_PORT_MESSAGE);
	}

	public IpAndPortBHMessage(int requestMessageType) {
		super(BHMessageType.BH_PC_SERVER_IP_AND_PORT_MESSAGE, requestMessageType);
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
