package ro.edu.ubb.blackhole.datastructures.guirequest;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * delete files from the PC Server.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DeleteFilePCGR extends GuiRequest {

	/**
	 * Name of the server.
	 */
	private String serverName = "";

	/**
	 * All file paths what we would delete from the PC.
	 */
	private List<String> paths = new ArrayList<String>();

	public DeleteFilePCGR(Context applicationContext) {
		super(R.integer.COMMAND_DELETE_FILE_PC, applicationContext);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	public void addPath(String path) {
		this.paths.add(path);
	}
}
