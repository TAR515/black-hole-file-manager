package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to
 * unregister a Download Manager from the context.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class UnRegisterDownloadManagerGR extends GuiRequest {

	public UnRegisterDownloadManagerGR(Context applicationContext) {
		super(R.integer.COMMAND_UNREGISTER_DOWNLOAD_MANAGER, applicationContext);
	}
}
