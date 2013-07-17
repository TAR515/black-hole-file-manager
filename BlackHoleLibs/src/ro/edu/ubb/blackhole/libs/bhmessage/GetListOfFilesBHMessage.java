package ro.edu.ubb.blackhole.libs.bhmessage;


/**
 * An object which contains all necessary data for a request to get list of files.
 * 
 * @author Administrator
 * @version 1.0
 */
public class GetListOfFilesBHMessage extends BlackHoleMessage {

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
	
	public GetListOfFilesBHMessage() {
		super(BHMessageType.BH_GET_LIST_OF_FILES_MESSAGE);
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
