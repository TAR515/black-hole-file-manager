package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to get
 * all running processes on the PC computer.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetRunningProgramsGR extends GuiRequest {

	/**
	 * Name of the server.
	 */
	private String serverName = null;

	public GetRunningProgramsGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_RUNNING_PROGRAMS, applicationContext);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
