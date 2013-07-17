package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * register the download manager to the application context.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class RegisterDownloadManagerGR extends GuiRequest {

	public RegisterDownloadManagerGR(Context applicationContext) {
		super(R.integer.COMMAND_REGISTER_DOWNLOAD_MANAGER, applicationContext);
	}

}
