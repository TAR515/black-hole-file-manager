package ro.edu.ubb.blackhole.gui.activityes;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.gui.RunningServicesListViewAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

/**
 * By using this activity the user can view all running processes on the PC Server and it can stops/kills one or more processes.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class RunningServicesActivity extends Activity {

	/**
	 * View of the running processes {@link ListView}.
	 */
	private ListView runningProcessesListView = null;

	/**
	 * Adapter of the running processes {@link ListView}.
	 */
	private RunningServicesListViewAdapter adapter = null;

	/**
	 * @see #onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_running_services);

		String serverName = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			serverName = extras.getString("SERVER_NAME");
		}

		runningProcessesListView = (ListView) findViewById(R.id.listview_running_processes);
		adapter = new RunningServicesListViewAdapter(this, R.layout.listview_running_programs_item_row, this,
				serverName);
		runningProcessesListView.setCacheColorHint(0);
		runningProcessesListView.setAdapter(adapter);
		registerForContextMenu(runningProcessesListView);
	}

	/**
	 * @see #onCreateOptionsMenu(Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_running_services, menu);
		return true;
	}
}
