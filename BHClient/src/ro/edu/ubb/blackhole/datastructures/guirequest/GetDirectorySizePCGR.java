package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to get
 * the size of a folder located at PC.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetDirectorySizePCGR extends GuiRequest {

	/**
	 * Name of the server.
	 */
	private String serverName = "";

	/**
	 * Path of the directory located at the PC Server.
	 */
	private String path = "";

	public GetDirectorySizePCGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_DIRECTORY_SIZE_PC, applicationContext);
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
