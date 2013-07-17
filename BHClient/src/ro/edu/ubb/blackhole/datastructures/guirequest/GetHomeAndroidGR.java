package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is toget
 * the Android system home directories.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetHomeAndroidGR extends GuiRequest {

	public GetHomeAndroidGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_HOME_ANDROID, applicationContext);
	}
}
