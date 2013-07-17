package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * This Black Hole message is just for checking the BHPCServer if it's is online or not.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class PCServerCheckBHMessage extends BlackHoleMessage {

	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	public PCServerCheckBHMessage() {
		super(BHMessageType.BH_PC_SERVER_CHECK_MESSAGE);
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
