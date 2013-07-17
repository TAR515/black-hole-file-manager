package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * Contains all necessary information for getting a directory size.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetDirectorySizeBHMessage extends BlackHoleMessage {
	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;
	
	/**
	 * Path of the directory.
	 */
	private String path;

	public GetDirectorySizeBHMessage() {
		super(BHMessageType.BH_GET_DIRECTORY_SIZE_MESSAGE);
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
