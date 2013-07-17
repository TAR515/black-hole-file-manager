package ro.edu.ubb.blackhole.gui.activityes;

import java.util.ArrayList;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.guirequest.DeleteServerGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetIPAndPortGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetServerInfGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetServerNamesGR;
import ro.edu.ubb.blackhole.gui.GuiMessageInterpreter;
import ro.edu.ubb.blackhole.gui.GuiRequestHandler;
import ro.edu.ubb.blackhole.gui.MessageBox;
import ro.edu.ubb.blackhole.gui.ResponseHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The first activity of the application. Here are visualize the current servers which are saved into the database. In this
 * activity you can add a new server to database or you can select one for the upcoming work.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ServerSelectActivity extends Activity implements ResponseHandler {

	/**
	 * This contains the server names.
	 */
	private ListView serverNamesListView = null;

	/**
	 * @see #onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_select_);

		// Getting the list view.
		this.serverNamesListView = (ListView) findViewById(R.id.listview_servernames);
		this.serverNamesListView.setCacheColorHint(0); // No selection when scroll.
		registerForContextMenu(this.serverNamesListView);

		if (this.serverNamesListView != null) { // If the list view exists in layout file.
			// Get the current server information, if the password were not stored then the user must grant it.
			this.serverNamesListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// Getting the server informations from the database to fill up the edit texts. e.g. ServerName.
					String selectedServerFromList = (String) (serverNamesListView.getItemAtPosition(position));

					GetServerInfGR getAllServerInformationsRequest = new GetServerInfGR(
							ServerSelectActivity.this);
					getAllServerInformationsRequest.setServerName(selectedServerFromList);

					new GuiRequestHandler(ServerSelectActivity.this, false)
							.execute(getAllServerInformationsRequest);
				}
			});
		}
	}

	/**
	 * @see #onCreateOptionsMenu(Menu)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// Adding the two context menu. This two will be showed when somebody clicks long to an item from the server names list
		// view
		menu.add(getString(R.string.context_meniu_edit));
		menu.add(getString(R.string.context_meniu_delete));
	}

	/**
	 * @see #onContextItemSelected(MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);

		// Getting the target server name which will be deleting.
		AdapterView.AdapterContextMenuInfo servernameListViewInformation = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		String targetServerName = (String) this.serverNamesListView
				.getItemAtPosition(servernameListViewInformation.position);

		// Checking which button were pressed.
		if (item.getTitle().equals(getString(R.string.context_meniu_edit))) { // Edit Server
			// Starts the edit server activity.
			Intent myIntent = new Intent(ServerSelectActivity.this, EditServerActivity.class);
			myIntent.putExtra("OldServerName", targetServerName);
			startActivityForResult(myIntent, 0);
		} else if (item.getTitle().equals(getString(R.string.context_meniu_delete))) { // Delete Server
			// Deleting the target server from the database.
			DeleteServerGR request = new DeleteServerGR(getApplication());
			request.setServerName(targetServerName);

			new GuiRequestHandler(this, true).execute(request);
		}

		return true;
	}

	/**
	 * @see #onCreateOptionsMenu(Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_server__select_, menu);
		return true;
	}

	/**
	 * @see #onOptionsItemSelected(MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection.
		switch (item.getItemId()) {
		case R.id.menu_about:
			AlertDialog aboutDialog = new AlertDialog.Builder(this).create();

			TextView aboutDialogTextView = new TextView(this);
			aboutDialogTextView.setGravity(Gravity.CENTER);
			aboutDialogTextView.setText(getString(R.string.inf_about_informations));

			aboutDialog.setTitle(getString(R.string.about_dialog_title));
			aboutDialog.setView(aboutDialogTextView);

			aboutDialog.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/**
	 * @see #onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// Getting the current server names from the database.
		// This starts a new thread (AsyncTask) which will fill up the server name list with the server names.
		GetServerNamesGR request = new GetServerNamesGR(getApplication());

		new GuiRequestHandler(this, false).execute(request);
	}

	/**
	 * Go for the {@link AddServerActivity}
	 * 
	 * @param v
	 *            View
	 */
	public void addServerButton(View v) {
		Intent myIntent = new Intent(ServerSelectActivity.this, AddServerActivity.class);
		startActivityForResult(myIntent, 0);
	}

	/**
	 * @see #responseHandler(CommandToGui)
	 */
	@Override
	public void responseHandler(CommandToGui response) {
		try {
			switch (response.getCommandID()) {
			case R.integer.COMMAND_GET_SERVER_NAMES:
				setServerNames(new ArrayList<String>(response.getAllServerNames()));
				break;
			case R.integer.COMMAND_DELETE_SERVER:
				// Refreshing the list of servers.
				onResume();
				break;
			case R.integer.COMMAND_GET_SERVER_INFORMATIONS:
				logIn(response);
				break;
			case R.integer.COMMAND_GET_IP_AND_PORT:
				passwordCheck(response);
				break;
			default:
				Log.w("Not implemented proccess for this response.");
			}
		} catch (NullPointerException e) {
			Log.e("Gui can't process the response! Exception: " + e.getMessage());
		}
	}

	/**
	 * Check if the server name and password are correct, if they are then the user logs in.
	 * 
	 * @param response
	 *            Contain the good password of the server.
	 */
	private void passwordCheck(CommandToGui response) {
		if (response.getIP() == null || response.getPort() == -1) { // Unsuccessful login.
			MessageBox wrongPassMessageBox = new MessageBox(this);
			String errMsg = GuiMessageInterpreter.getErrorMessage(response.getSimpleResponse());
			wrongPassMessageBox.createErrorMessageBox(errMsg);
		} else { // Successful login.
			String serverName = response.getServer().getServerName();
			final Intent myIntent = new Intent(ServerSelectActivity.this, ModeSelectActivity.class);
			myIntent.putExtra("SERVER_NAME", serverName);
			startActivityForResult(myIntent, 0);
		}
	}

	/**
	 * Creates a log in dialog if the password were not storred into the local database, and try logging in the user.
	 * 
	 * @param command
	 *            Command to Gui.
	 */
	private void logIn(final CommandToGui command) {
		// If the password were not stored then the user must grant it for the password check.
		if (command.getServer().getPassword() == null) {
			// Creating the dialog where the user have to enter her password.
			AlertDialog.Builder passwordCheckDialog = new AlertDialog.Builder(ServerSelectActivity.this);
			passwordCheckDialog.setTitle(getString(R.string.password_check_title));
			passwordCheckDialog.setMessage(getString(R.string.password_check_message));

			final EditText passwordField = new EditText(ServerSelectActivity.this);
			passwordCheckDialog.setView(passwordField);

			passwordCheckDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String serverName = command.getServer().getServerName();
					String password = passwordField.getText().toString();

					GetIPAndPortGR request = new GetIPAndPortGR(ServerSelectActivity.this);
					request.setServerName(serverName);
					request.setPassword(password);

					new GuiRequestHandler(ServerSelectActivity.this, true).execute(request);
				}
			});

			passwordCheckDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					onResume();
				}
			});

			passwordCheckDialog.show();
		} else {
			String serverName = command.getServer().getServerName();
			String password = command.getServer().getPassword();

			GetIPAndPortGR request = new GetIPAndPortGR(ServerSelectActivity.this);
			request.setServerName(serverName);
			request.setPassword(password);

			new GuiRequestHandler(ServerSelectActivity.this, true).execute(request);
		}
	}

	/**
	 * Fill up the list of server names.
	 * 
	 * @param serverNames
	 *            actual server names.
	 */
	private void setServerNames(final ArrayList<String> serverNames) {
		// Getting the list view.
		ListView serverNamesListView = (ListView) findViewById(R.id.listview_servernames);

		if (serverNames != null) { // If there are data in the server names.
			// Fill up the list view.
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.textview_simple_line,
					serverNames);
			serverNamesListView.setAdapter(listAdapter);
		}
	}

}
