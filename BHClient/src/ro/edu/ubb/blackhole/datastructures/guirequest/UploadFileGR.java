package ro.edu.ubb.blackhole.datastructures.guirequest;

import java.util.List;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * start uploading the given files to the PC Server.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class UploadFileGR extends GuiRequest {

	/**
	 * Name of the Server.
	 */
	private String serverName = "";

	/**
	 * The path on the PC where the files will be uploading.
	 */
	private String destinationPath = "";

	/**
	 * All files which we would upload.
	 */
	private List<FileItem> sources = null;

	public UploadFileGR(Context applicationContext) {
		super(R.integer.COMMAND_UPLOAD_FILE, applicationContext);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	public List<FileItem> getSources() {
		return sources;
	}

	public void setSources(List<FileItem> sources) {
		this.sources = sources;
	}

}
