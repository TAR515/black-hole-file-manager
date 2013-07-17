package ro.edu.ubb.blackhole.gui.activityes;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.Server;
import ro.edu.ubb.blackhole.datastructures.guirequest.EditServerGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetServerInfGR;
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
 * By using this {@link Activity} the user can edit an existing server information on the local database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class EditServerActivity extends Activity implements ResponseHandler {

	/**
	 * The server name which we are editing.
	 */
	private String oldServerName = null;

	/**
	 * @see #onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_server);

		// Getting the extras, the server name which we have to edit.
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.oldServerName = extras.getString("OldServerName");
		}

		// Giving the functionality to the check box.
		CheckBox storePass = (CheckBox) findViewById(R.id.checkbox_store_password);
		storePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				LinearLayout serverInfoLayout = (LinearLayout) findViewById(R.id.linear_layout_edit_server_info);
				LayoutInflater inflator = LayoutInflater.from(EditServerActivity.this);

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
	 * Update the given server informations and returns back to the {@link ServerSelectActivity}
	 * 
	 * @param v
	 *            View.
	 */
	public void editServerButton(View v) {
		// Initializing the components.
		EditText serverNameView = (EditText) findViewById(R.id.editText_server_name);
		EditText passwordView = (EditText) findViewById(R.id.editText_password);

		// Getting the informations from the edit texts.
		String serverName = serverNameView.getText().toString();
		String password = (passwordView == null) ? null : passwordView.getText().toString();

		// If all of the edit texts were extended.
		if (!"".equals(serverName) && (password == null || !"".equals(password))) {
			// Adding the new server to the database.
			// Update the server informations.

			EditServerGR updateRequest = new EditServerGR(EditServerActivity.this);
			updateRequest.setOldServerName(oldServerName);
			updateRequest.setNewServer(new Server(serverName, password));

			new GuiRequestHandler(EditServerActivity.this, true).execute(updateRequest);
		} else { // Visualize a warning! // TODO make an error message here!
			AlertDialog alertDialog = new AlertDialog.Builder(EditServerActivity.this).create();
			alertDialog.setTitle(getString(R.string.warning));
			alertDialog.setMessage(getString(R.string.warning_empty_field));
			alertDialog.show();
		}
	}

	/**
	 * @see #onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// Getting the server informations from the database to fill up the edit texts. e.g. ServerName.
		GetServerInfGR getAllServerInformationsRequest = new GetServerInfGR(this);
		getAllServerInformationsRequest.setServerName(this.oldServerName);

		new GuiRequestHandler(EditServerActivity.this, false).execute(getAllServerInformationsRequest);
	}

	/**
	 * @see #onCreateOptionsMenu(Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_server, menu);
		return true;
	}

	/**
	 * Fill up the edit texts with the response informations about the server: server name, server ip, server port, user name.
	 * 
	 * @param command
	 *            Data from the database.
	 */
	private void setServerInformations(CommandToGui command) {
		if (command != null) { // There were server with the given server name.
			// Initializing the edit texts.
			EditText serverNameView = (EditText) findViewById(R.id.editText_server_name);
			serverNameView.setText(command.getServer().getServerName());

			// If the user password were stored then load it.
			String password = command.getServer().getPassword();
			if (password != null) {
				CheckBox storePass = (CheckBox) findViewById(R.id.checkbox_store_password);
				storePass.setChecked(true);

				EditText passwordView = (EditText) findViewById(R.id.editText_password);
				passwordView.setText(password);
			}
		}
	}

	/**
	 * @see #responseHandler(CommandToGui)
	 */
	@Override
	public void responseHandler(CommandToGui command) {
		try {
			switch (command.getCommandID()) {
			case R.integer.COMMAND_GET_SERVER_INFORMATIONS:
				setServerInformations(command);
				break;
			case R.integer.COMMAND_EDIT_SERVER:
				// Back to Server Select Activity.
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
