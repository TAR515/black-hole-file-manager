package ro.edu.ubb.blackhole.datastructures.guirequest;

import android.content.Context;

/**
 * This data structure is the base of all Gui Requests. This contains the ID of the command which specify the command type and the
 * context of the application.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public abstract class GuiRequest {

	/**
	 * ID of the command.
	 */
	private Integer commandID = null;

	/**
	 * Context of the application
	 */
	private Context applicationContext = null;

	public GuiRequest(Context applicationContext) {
		this.applicationContext = applicationContext;
	}

	public GuiRequest(Integer commandID, Context applicationContext) {
		this.applicationContext = applicationContext;
		this.commandID = commandID;
	}

	public Integer getCommandID() {
		return commandID;
	}

	@Deprecated
	public void setCommandID(Integer commandID) {
		this.commandID = commandID;
	}

	public Context getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(Context applicationContext) {
		this.applicationContext = applicationContext;
	}
}
