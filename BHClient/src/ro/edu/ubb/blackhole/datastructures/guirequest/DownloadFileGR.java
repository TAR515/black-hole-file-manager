package ro.edu.ubb.blackhole.datastructures.guirequest;

import java.util.List;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * download a file from the PC Server.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DownloadFileGR extends GuiRequest {

	/**
	 * Name of the server.
	 */
	private String serverName = "";

	/**
	 * Path where the file will be downloading.
	 */
	private String destinationPath = "";

	/**
	 * Files which we would download from the PC.
	 */
	private List<FileItem> sources = null;

	public DownloadFileGR(Context applicationContext) {
		super(R.integer.COMMAND_DOWNLOAD_FILE, applicationContext);
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
