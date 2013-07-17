package ro.edu.ubb.blackhole.datastructures;

import android.content.Context;

public class GuiRequest {

	private Integer commandID = null;

	private Server server = null;

	private Server updatedServer = null;

	private Context applicationContext = null;

	private String path = null;

	private String newName = null;

	public GuiRequest(Integer commandID, Context applicationContext) {
		super();

		this.commandID = commandID;
		this.server = new Server();
		this.updatedServer = new Server();
		this.applicationContext = applicationContext;
		this.setPath(new String("/"));
		this.setNewName("");
	}

	public Integer getCommandID() {
		return commandID;
	}

	public void setCommandID(Integer commandID) {
		this.commandID = commandID;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Context getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(Context applicationContext) {
		this.applicationContext = applicationContext;
	}

	public Server getUpdatedServer() {
		return updatedServer;
	}

	public void setUpdatedServer(Server updatedServer) {
		this.updatedServer = updatedServer;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
}
