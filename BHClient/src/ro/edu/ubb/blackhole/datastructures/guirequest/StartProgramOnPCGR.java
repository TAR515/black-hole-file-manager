package ro.edu.ubb.blackhole.datastructures.guirequest;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * execute applications on the PC machine.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class StartProgramOnPCGR extends GuiRequest {

	/**
	 * Name of the server.
	 */
	private String serverName = "";

	/**
	 * All path of the applications which we would execute on the PC.
	 */
	private List<String> programPaths = new ArrayList<String>();

	public StartProgramOnPCGR(Context applicationContext) {
		super(R.integer.COMMAND_START_PROGRAMS, applicationContext);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public List<String> getProgramPaths() {
		return programPaths;
	}

	public void setProgramPaths(List<String> programPaths) {
		this.programPaths = programPaths;
	}
}
