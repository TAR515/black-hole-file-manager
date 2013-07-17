package ro.edu.ubb.blackhole.datastructures.guirequest;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * stop/kill all the given processes on the PC machine.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class StopProgramsOnPCGR extends GuiRequest {

	/**
	 * Name of the server.
	 */
	private String serverName = "";

	/**
	 * All process names which we would like to kill at the PC.
	 */
	private List<String> processNames = new ArrayList<String>();

	public StopProgramsOnPCGR(Context applicationContext) {
		super(R.integer.COMMAND_STOP_PROGRAMS, applicationContext);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public List<String> getProcessNames() {
		return processNames;
	}

	public void setProcessNames(List<String> processNames) {
		this.processNames = processNames;
	}
}
