package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to get
 * the list of the files contained into the given directory.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetListOfFilesAndroidGR extends GuiRequest {

	/**
	 * Path of the directory.
	 */
	private String path = "";

	public GetListOfFilesAndroidGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_LIST_OF_FILES_ANDROID, applicationContext);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
