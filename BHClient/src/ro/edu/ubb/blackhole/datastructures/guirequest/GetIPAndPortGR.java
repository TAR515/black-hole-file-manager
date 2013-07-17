package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to get
 * a PC Server's IP and port from the Main Server.
 * 
 * @author Turdean Arnold Robert
 * 
 */
public class GetIPAndPortGR extends GuiRequest {

	/**
	 * Name of the PC Server.
	 */
	private String serverName = null;

	/**
	 * Server's password.
	 */
	private String password = null;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GetIPAndPortGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_IP_AND_PORT, applicationContext);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
