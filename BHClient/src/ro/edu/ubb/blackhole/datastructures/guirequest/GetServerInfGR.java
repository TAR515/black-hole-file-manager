package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to get
 * all information about a server contained in SQLite database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetServerInfGR extends GuiRequest {

	/**
	 * Name of the server.
	 */
	private String serverName = "";

	public GetServerInfGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_SERVER_INFORMATIONS, applicationContext);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
