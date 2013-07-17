package ro.edu.ubb.blackhole.gui.activityes;

import ro.edu.ubb.blackhole.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * This activity is not implemented yet!
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class RemoteDesktopActivity extends Activity {

	/**
	 * @see #onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_desktop);
	}

	/**
	 * @see #onCreateOptionsMenu(Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_remote_desktop, menu);
		return true;
	}
}
