package ro.edu.ubb.blackhole.gui.activityes;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.Server;
import ro.edu.ubb.blackhole.datastructures.guirequest.InsertNewServerGR;
import ro.edu.ubb.blackhole.gui.GuiRequestHandler;
import ro.edu.ubb.blackhole.gui.ResponseHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * By using this activity the user can add new servers into the local database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class AddServerActivity extends Activity implements ResponseHandler {

	/**
	 * @see #onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_server);

		// Giving the functionality to the check box.
		CheckBox storePass = (CheckBox) findViewById(R.id.checkbox_store_password);
		storePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				LinearLayout serverInfoLayout = (LinearLayout) findViewById(R.id.linear_layout_server_info);
				LayoutInflater inflator = LayoutInflater.from(AddServerActivity.this);

				if (isChecked) { // Visualize the password text box in the activity.
					LinearLayout passLayout = (LinearLayout) inflator.inflate(R.layout.listview_password_inf,
							null);
					serverInfoLayout.addView(passLayout, 3);
				} else { // Hide the password text box in the activity.
					LinearLayout passLayout = (LinearLayout) findViewById(R.id.linear_layout_password);
					serverInfoLayout.removeView(passLayout);
				}
			}
		});
	}

	/**
	 * Creates a new server and returns back to {@link ServerSelectActivity}
	 * 
	 * @param v
	 *            View.
	 */
	public void createServerButton(View v) {
		EditText serverNameView = (EditText) findViewById(R.id.editText_server_name);
		EditText passwordView = (EditText) findViewById(R.id.editText_password);
		String serverName = serverNameView.getText().toString();
		// If the user wants to store the password then get it else set it to null.
		String password = (passwordView == null) ? null : passwordView.getText().toString();

		// If all of the edit texts were extended.
		if (!"".equals(serverName) && (password == null || !"".equals(password))) {
			// Adding the new server to the database.
			Server newServer = new Server(serverName, password);

			InsertNewServerGR createServerRequest = new InsertNewServerGR(getApplication());
			createServerRequest.setNewServer(newServer);
			new GuiRequestHandler(AddServerActivity.this, true).execute(createServerRequest);
		} else { // Visualize a warning!
			AlertDialog alertDialog = new AlertDialog.Builder(AddServerActivity.this).create();
			alertDialog.setTitle(getString(R.string.warning));
			alertDialog.setMessage(getString(R.string.warning_empty_field));
			alertDialog.show();
		}
	}

	/**
	 * @see #onSaveInstanceState(Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		EditText passwordView = (EditText) findViewById(R.id.editText_password);
		String password = (passwordView == null) ? null : passwordView.getText().toString();

		// If the user want to store her password then save the password on state.
		if (password != null) {
			outState.putString("password", password);
		}
	}

	/**
	 * @see #onRestoreInstanceState(Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		EditText passwordView = (EditText) findViewById(R.id.editText_password);

		// If the user want to store her password then restore the value of the password text box.
		if (passwordView != null) {
			String password = savedInstanceState.getString("password");
			passwordView.setText(password);
		}

	}

	/**
	 * @see #onCreateOptionsMenu(Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_server, menu);
		return true;
	}

	/**
	 * @see #responseHandler(CommandToGui)
	 */
	@Override
	public void responseHandler(CommandToGui command) {
		try {
			switch (command.getCommandID()) {
			case R.integer.COMMAND_INSERT_NEW_SERVER:
				onBackPressed();
				break;
			default:
				Log.w("Not implemented proccess for this response.");
			}
		} catch (NullPointerException e) {
			Log.e("Gui can't process the response! Exception: " + e.getMessage());
		}
	}

}
