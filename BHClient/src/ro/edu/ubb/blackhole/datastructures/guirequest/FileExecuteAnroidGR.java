package ro.edu.ubb.blackhole.datastructures.guirequest;

import java.io.File;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * execute a file on the Android.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class FileExecuteAnroidGR extends GuiRequest {

	/**
	 * The file which we would execute on the phone.
	 */
	private File file = null;

	public FileExecuteAnroidGR(Context applicationContext) {
		super(R.integer.COMMAND_FILE_EXECUTE_ANDROID, applicationContext);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
