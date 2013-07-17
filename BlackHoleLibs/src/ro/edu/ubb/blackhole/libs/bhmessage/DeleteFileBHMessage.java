package ro.edu.ubb.blackhole.libs.bhmessage;

import java.util.List;

/**
 * Contains all necessary information for deleting a file.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DeleteFileBHMessage extends BlackHoleMessage {
	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	/**
	 * Paths of the files which we would delete from the PC.
	 */
	private List<String> paths = null;

	public DeleteFileBHMessage() {
		super(BHMessageType.BH_DELETE_FILE_MESSAGE);
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

	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

}
