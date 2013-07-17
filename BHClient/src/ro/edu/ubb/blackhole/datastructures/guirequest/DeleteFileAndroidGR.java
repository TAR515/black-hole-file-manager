package ro.edu.ubb.blackhole.datastructures.guirequest;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * delete files from the Android phone.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DeleteFileAndroidGR extends GuiRequest {

	/**
	 * All file paths which we would to delete.
	 */
	private List<String> paths = new ArrayList<String>();

	public DeleteFileAndroidGR(Context applicationContext) {
		super(R.integer.COMMAND_DELETE_FILE_ANDROID, applicationContext);
	}

	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	public void addPath(String path) {
		this.paths.add(path);
	}

}
