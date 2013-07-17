package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to get
 * the size of a folder located on Android.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetDirectorySizeAndroidGR extends GuiRequest {

	/**
	 * Path of the folder.
	 */
	private String path = "";

	public GetDirectorySizeAndroidGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_DIRECTORY_SIZE_ANDROID, applicationContext);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
