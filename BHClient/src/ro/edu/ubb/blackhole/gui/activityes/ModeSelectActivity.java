package ro.edu.ubb.blackhole.gui.activityes;

import ro.edu.ubb.blackhole.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * This {@link Activity} provides an interface for the user on which the user can choose what kind of operation he want to do.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ModeSelectActivity extends Activity {

	/**
	 * @see #onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mode_select);
	}

	/**
	 * Starts the {@link FileManager}.
	 * 
	 * @param v
	 *            View.
	 */
	public void startFileManagerButton(View v) {
		Intent myIntent = new Intent(ModeSelectActivity.this, FileManager.class);

		// Put the serverName to Bundle because the File Manager needs it.
		Bundle extras = ModeSelectActivity.this.getIntent().getExtras();
		if (extras != null) {
			String serverName = extras.getString("SERVER_NAME");

			myIntent.putExtra("SERVER_NAME", serverName);
		}

		// Starts File Manager.
		startActivityForResult(myIntent, 0);
	}

	/**
	 * Starts the {@link RemoteDesktopActivity}.
	 * 
	 * @param v
	 *            View.
	 */
	public void startRemoteDesktopButton(View v) {
		Intent myIntent = new Intent(ModeSelectActivity.this, RemoteDesktopActivity.class);

		Bundle extras = ModeSelectActivity.this.getIntent().getExtras();
		if (extras != null) {
			String serverName = extras.getString("SERVER_NAME");
			String password = extras.getString("SERVER_PASSWORD");

			myIntent.putExtra("SERVER_NAME", serverName);
			myIntent.putExtra("SERVER_PASSWORD", password);
		}

		// Starts Remote Desktop.
		startActivityForResult(myIntent, 0);
	}

	/**
	 * @see #onCreateOptionsMenu(Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_mode_select, menu);
		return true;
	}
}
