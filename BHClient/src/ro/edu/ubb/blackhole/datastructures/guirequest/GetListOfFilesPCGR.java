package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to get
 * all files contained into the given directory located in PC.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetListOfFilesPCGR extends GuiRequest {

	/**
	 * Path of the directory located at the PC Server.
	 */
	private String path = "";

	/**
	 * Name of the server.
	 */
	private String serverName = "";

	public GetListOfFilesPCGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_LIST_OF_FILES_PC, applicationContext);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
