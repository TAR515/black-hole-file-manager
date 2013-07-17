package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.datastructures.Server;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * insert a new Server to the SQLite database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class InsertNewServerGR extends GuiRequest {

	/**
	 * New server informations.
	 */
	private Server newServer = new Server();

	public InsertNewServerGR(Context applicationContext) {
		super(R.integer.COMMAND_INSERT_NEW_SERVER, applicationContext);
	}

	public Server getNewServer() {
		return newServer;
	}

	public void setNewServer(Server newServer) {
		this.newServer = newServer;
	}
}
