package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.datastructures.Server;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * edit a server informations in the SQLite database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class EditServerGR extends GuiRequest {

	/**
	 * Name of the editable server.
	 */
	private String oldServerName = "";

	/**
	 * New server informations.
	 */
	private Server newServer = null;

	public EditServerGR(Context applicationContext) {
		super(R.integer.COMMAND_EDIT_SERVER, applicationContext);
	}

	public String getOldServerName() {
		return oldServerName;
	}

	public void setOldServerName(String oldServerName) {
		this.oldServerName = oldServerName;
	}

	public Server getNewServer() {
		return newServer;
	}

	public void setNewServer(Server newServer) {
		this.newServer = newServer;
	}

}
