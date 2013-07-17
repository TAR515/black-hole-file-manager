package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * Contains all necessary information for getting the actual running programs on the PC.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetRunningProgramsBHMessage extends BlackHoleMessage {

	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	public GetRunningProgramsBHMessage() {
		super(BHMessageType.BH_GET_RUNNING_PROGRAMS_MESSAGE);
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
