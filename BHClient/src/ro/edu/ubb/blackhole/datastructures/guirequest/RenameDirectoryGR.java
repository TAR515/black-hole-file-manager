package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * rename a directory.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class RenameDirectoryGR extends GuiRequest {

	/**
	 * Path of the directory.
	 */
	private String path = "";

	/**
	 * New name of the directory.
	 */
	private String newName = "";

	public RenameDirectoryGR(Context applicationContext) {
		super(R.integer.COMMAND_RENAME_FILE_ANDROID, applicationContext);
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
