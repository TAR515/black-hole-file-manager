package ro.edu.ubb.blackhole.libs.bhmessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This Black Hole message warns the BHPCServer to start the given application or file on the computer.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class StartProgramsBHMessage extends BlackHoleMessage {

	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	/**
	 * All launched program paths.
	 */
	private List<String> paths = null;
	
	public StartProgramsBHMessage() {
		super(BHMessageType.BH_START_PROGRAM_MESSAGE);
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
	
	public void addPath(String path) {
		if (this.paths == null) {
			this.paths = new ArrayList<String>();
		}
		
		this.paths.add(path);
	}
	
}
