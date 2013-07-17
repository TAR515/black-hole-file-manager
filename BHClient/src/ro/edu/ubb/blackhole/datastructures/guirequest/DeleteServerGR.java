package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * delete a Server from the SQLite database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DeleteServerGR extends GuiRequest {

	/**
	 * Name of the server which we would to delete.
	 */
	private String serverName = "";

	public DeleteServerGR(Context applicationContext) {
		super(R.integer.COMMAND_DELETE_SERVER, applicationContext);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
