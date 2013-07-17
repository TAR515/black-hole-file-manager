package ro.edu.ubb.blackhole.libs.bhmessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This Black Hole message warns the BHPCServer to stop running the given application on the computer.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class StopRunningProgramsBHMessage extends BlackHoleMessage {

	/**
	 * The server name.
	 */
	private String serverName = null;

	/**
	 * Server password.
	 */
	private byte[] serverPassword = null;

	/**
	 * All stoppable process names.
	 */
	private List<String> processNames = null;

	public StopRunningProgramsBHMessage() {
		super(BHMessageType.BH_STOP_PROGRAM_MESSAGE);
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

	public List<String> getProcessNames() {
		return processNames;
	}

	public void setProcessNames(List<String> processNames) {
		this.processNames = processNames;
	}

	public void addProcessName(String processName) {
		if (this.processNames == null) {
			this.processNames = new ArrayList<String>();
		}

		this.processNames.add(processName);
	}

}
