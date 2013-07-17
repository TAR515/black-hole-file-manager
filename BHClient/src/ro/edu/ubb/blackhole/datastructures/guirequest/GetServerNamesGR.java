package ro.edu.ubb.blackhole.datastructures.guirequest;

import ro.edu.ubb.blackhole.R;
import android.content.Context;

/**
 * This data structure is used when the User Interface send a request to the Data Access Layer. At this case the request is to get
 * all server names contained into the SQLite database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetServerNamesGR extends GuiRequest {

	public GetServerNamesGR(Context applicationContext) {
		super(R.integer.COMMAND_GET_SERVER_NAMES, applicationContext);
	}

}
