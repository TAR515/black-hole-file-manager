package ro.edu.ubb.blackhole.gui.activityes;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.dal.pcfileservice.filetransfermanagers.BHDownloadManager;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.guirequest.DeleteFileAndroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.DownloadFileGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.RegisterDownloadManagerGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.UploadFileGR;
import ro.edu.ubb.blackhole.gui.BHListViewAdapter;
import ro.edu.ubb.blackhole.gui.GuiRequestHandler;
import ro.edu.ubb.blackhole.gui.MessageBox;
import ro.edu.ubb.blackhole.gui.ObjectParcelable;
import ro.edu.ubb.blackhole.gui.PCListViewAdapter;
import ro.edu.ubb.blackhole.gui.PhoneListViewAdapter;
import ro.edu.ubb.blackhole.gui.ResponseHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * By using this activity the user can manage files on her phone and PC as well.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class FileManager extends Activity implements ResponseHandler {

	/**
	 * Contains the view and the adapter of the phone {@link ListView}.
	 */
	private NavigationTab phoneTab = new NavigationTab();

	/**
	 * Contains the view and the adapter of the pc {@link ListView}.
	 */
	private NavigationTab pcTab = new NavigationTab();

	/**
	 * This is the active list view, which the user use at current time.
	 */
	private BHListViewAdapter activeListView = null;

	/**
	 * Name of the server where the user is logged in.
	 */
	private String serverName = null;

	/**
	 * @see #onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_manager);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			serverName = extras.getString("SERVER_NAME");
		}

		// Initialize phoneTab.
		this.phoneTab.view = (ListView) findViewById(R.id.listview_phone_files);
		this.phoneTab.adapter = new PhoneListViewAdapter(this, R.layout.listview_fm_item_row, this);
		View phoneListViewHeader = (View) getLayoutInflater()
				.inflate(R.layout.listview_fm_header_phone, null);
		this.phoneTab.view.addHeaderView(phoneListViewHeader, null, false);
		this.phoneTab.view.setCacheColorHint(0);
		this.phoneTab.view.setAdapter(this.phoneTab.adapter);
		registerForContextMenu(this.phoneTab.view);

		// Initialize pcTab.
		this.pcTab.view = (ListView) findViewById(R.id.listview_pc_files);
		this.pcTab.adapter = new PCListViewAdapter(this, R.layout.listview_fm_item_row, this, serverName);
		View pcListViewHeader = (View) getLayoutInflater().inflate(R.layout.listview_fm_header_pc, null);
		this.pcTab.view.addHeaderView(pcListViewHeader, null, false);
		this.pcTab.view.setCacheColorHint(0);
		this.pcTab.view.setAdapter(this.pcTab.adapter);
		registerForContextMenu(this.pcTab.view);

		if (savedInstanceState == null) {
			this.pcTab.adapter.init();
			this.phoneTab.adapter.init();
		}

		new GuiRequestHandler(this, false).execute(new RegisterDownloadManagerGR(this));
	}

	/**
	 * @see #onDestroy()
	 */
	@Override
	protected void onDestroy() {
		this.unregisterReceiver(BHDownloadManager.onComplete);
		this.unregisterReceiver(BHDownloadManager.onNotificationClick);

		super.onDestroy();
	}

	/**
	 * @see #onCreateOptionsMenu(Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_file_manager, menu);
		return true;
	}

	/**
	 * @see #onOptionsItemSelected(MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_view_downloads:
			startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
			return true;
		case R.id.menu_view_running_programs:
			Intent startRunningServicesActivityIntent = new Intent(FileManager.this,
					RunningServicesActivity.class);
			startRunningServicesActivityIntent.putExtra("SERVER_NAME", serverName);
			startActivity(startRunningServicesActivityIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * @see #onCreateContextMenu(ContextMenu, View, ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v == this.phoneTab.view) {
			this.activeListView = this.phoneTab.adapter;
		} else if (v == this.pcTab.view) {
			this.activeListView = this.pcTab.adapter;
		}

		menu.add(getString(R.string.context_meniu_rename));
		menu.add(getString(R.string.context_meniu_delete));
		menu.add(getString(R.string.context_meniu_properties));
	}

	/**
	 * @see #onContextItemSelected(MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		String selectedProcess = (String) item.getTitle();

		if (selectedProcess.equals(getString(R.string.context_meniu_properties))) {
			this.activeListView.getPropertiesOfSelectedFile();
		} else if (selectedProcess.equals(getString(R.string.context_meniu_delete))) {
			this.activeListView.deleteSelectedFiles();
		}

		return true;
	}

	/**
	 * @see #onSaveInstanceState(Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		ObjectParcelable phoneTabInfo = new ObjectParcelable();
		phoneTabInfo.put("PHONE_TAB_FILES", this.phoneTab.adapter.getFileItems());
		phoneTabInfo.put("PHONE_TAB_CURRENT_PATH", this.phoneTab.adapter.getCurrentPath());
		outState.putParcelable("PHONE_TAB_INFO", phoneTabInfo);

		ObjectParcelable pcTabInfo = new ObjectParcelable();
		pcTabInfo.put("PC_TAB_CURRENT_PATH", this.pcTab.adapter.getCurrentPath());
		pcTabInfo.put("PC_TAB_FILES", this.pcTab.adapter.getFileItems());
		outState.putParcelable("PC_TAB_INFO", pcTabInfo);

		if (this.pcTab.adapter.getTemporalyFile() != null) {
			outState.putString("PC_TAB_TMP_FILE_PATH", this.pcTab.adapter.getTemporalyFile().getFilePath());
			System.out.println("PATH1: " + this.pcTab.adapter.getTemporalyFile().getFilePath());
		} else {
			outState.putString("PC_TAB_TMP_FILE_PATH", "");
			System.out.println("PATH1: " + "");
		}

		super.onSaveInstanceState(outState);
	}

	/**
	 * @see #onRestoreInstanceState(Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		ObjectParcelable phoneTabInfo = savedInstanceState.getParcelable("PHONE_TAB_INFO");
		this.phoneTab.adapter.fillUpPhoneListView((List<FileItem>) phoneTabInfo.get("PHONE_TAB_FILES"));
		this.phoneTab.adapter.setCurrentPath((String) phoneTabInfo.get("PHONE_TAB_CURRENT_PATH"));

		ObjectParcelable pcTabInfo = savedInstanceState.getParcelable("PC_TAB_INFO");
		this.pcTab.adapter.fillUpPhoneListView((List<FileItem>) pcTabInfo.get("PC_TAB_FILES"));
		this.pcTab.adapter.setCurrentPath((String) pcTabInfo.get("PC_TAB_CURRENT_PATH"));

		String tmpFilePath = savedInstanceState.getString("PC_TAB_TMP_FILE_PATH");
		if (!"".equals(tmpFilePath)) { // If there is a temporary downloaded file then delete it!
			List<String> deletableFilePath = new ArrayList<String>();
			deletableFilePath.add(tmpFilePath);

			DeleteFileAndroidGR request = new DeleteFileAndroidGR(this);
			request.setPaths(deletableFilePath);
			new GuiRequestHandler(this, true).execute(request);
		}

		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * @see #onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();

		// If we downloaded a temporaly file we need to delete it.
		FileItem tmpFile = this.pcTab.adapter.getTemporalyFile();
		if (tmpFile != null) { // If there is a temporary downloaded file then delete it!
			List<String> deletableFilePath = new ArrayList<String>();
			deletableFilePath.add(tmpFile.getFilePath());

			DeleteFileAndroidGR request = new DeleteFileAndroidGR(this);
			request.setPaths(deletableFilePath);
			new GuiRequestHandler(this, true).execute(request);
		}
	}

	/**
	 * Change the active tab between Phone Tab and PC Tab.
	 */
	private void changeActiveTab() {
		LinearLayout.LayoutParams phoneTabParams = (android.widget.LinearLayout.LayoutParams) this.phoneTab.view
				.getLayoutParams();
		phoneTabParams.weight = 1 - phoneTabParams.weight;
		this.phoneTab.view.setLayoutParams(phoneTabParams);

		LinearLayout.LayoutParams pcTabParams = (android.widget.LinearLayout.LayoutParams) this.pcTab.view
				.getLayoutParams();
		pcTabParams.weight = 1 - pcTabParams.weight;
		this.pcTab.view.setLayoutParams(pcTabParams);

		TextView phoneTabTextView = (TextView) findViewById(R.id.phone_tab_text_view);
		phoneTabTextView.setLayoutParams(phoneTabParams);
		TextView pcTabTextView = (TextView) findViewById(R.id.pc_tab_text_view);
		pcTabTextView.setLayoutParams(pcTabParams);

		TextView phoneTabPathTextView = (TextView) findViewById(R.id.phone_path_text_view);
		phoneTabPathTextView.setLayoutParams(phoneTabParams);
		TextView pcTabPathTextView = (TextView) findViewById(R.id.pc_path_text_view);
		pcTabPathTextView.setLayoutParams(pcTabParams);
	}

	/**
	 * Set phone tab as active tab.
	 */
	public void setPhoneTabActive() {
		LinearLayout.LayoutParams phoneTabParams = (android.widget.LinearLayout.LayoutParams) this.phoneTab.view
				.getLayoutParams();
		LinearLayout.LayoutParams pcTabParams = (android.widget.LinearLayout.LayoutParams) this.pcTab.view
				.getLayoutParams();

		if (phoneTabParams.weight < pcTabParams.weight) {
			changeActiveTab();
		}
	}

	/**
	 * Set pc tab as active tab.
	 */
	public void setPcTabActive() {
		LinearLayout.LayoutParams phoneTabParams = (android.widget.LinearLayout.LayoutParams) this.phoneTab.view
				.getLayoutParams();
		LinearLayout.LayoutParams pcTabParams = (android.widget.LinearLayout.LayoutParams) this.pcTab.view
				.getLayoutParams();

		if (phoneTabParams.weight > pcTabParams.weight) {
			changeActiveTab();
		}
	}

	/**
	 * Set the Phone tab current location.
	 * 
	 * @param currentPath
	 *            Path of the location.
	 */
	public void setPhoneCurrentPath(String currentPath) {
		if ("".equals(currentPath)) {
			currentPath = getString(R.string.title_home_path);
		}

		TextView phoneTabPathTextView = (TextView) findViewById(R.id.phone_path_text_view);
		phoneTabPathTextView.setText(currentPath);
	}

	/**
	 * Set the PC tab current location.
	 * 
	 * @param currentPath
	 *            Path of the location.
	 */
	public void setPCCurrentPath(String currentPath) {
		if ("".equals(currentPath)) {
			currentPath = getString(R.string.title_home_path);
		}

		TextView pcTabPathTextView = (TextView) findViewById(R.id.pc_path_text_view);
		pcTabPathTextView.setText(currentPath);
	}

	/**
	 * Changes the currently active tab.
	 * 
	 * @param v
	 *            View.
	 */
	public void changeActiveTabButton(View v) {
		changeActiveTab();
	}

	/**
	 * Starts a new file or files downloads from the PC Server.
	 * 
	 * @param v
	 *            View.
	 */
	public void startDownloadButton(View v) {
		if (!isWifiConnected()) { // If no WIFI connection were detected then visualize a warning dialog.
			AlertDialog.Builder noWifiWarningDialog = new AlertDialog.Builder(this);
			noWifiWarningDialog.setTitle(getString(R.string.warning_dialog_title));
			noWifiWarningDialog.setMessage(getString(R.string.no_wifi_connection));
			noWifiWarningDialog.setPositiveButton(getString(R.string.button_start_download),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							FileManager.this.startDownload();
						}
					});
			noWifiWarningDialog.setNegativeButton(getString(R.string.button_cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// Nothing to do.
						}
					});
			noWifiWarningDialog.show();
		} else {
			FileManager.this.startDownload();
		}
	}

	/**
	 * Start a file or files upload to the PC Server.
	 * 
	 * @param v
	 *            View.
	 */
	public void startUploadButton(View v) {
		if (!isWifiConnected()) { // If no WIFI connection were detected then visualize a warning dialog.
			AlertDialog.Builder noWifiWarningDialog = new AlertDialog.Builder(this);
			noWifiWarningDialog.setTitle(getString(R.string.warning_dialog_title));
			noWifiWarningDialog.setMessage(getString(R.string.no_wifi_connection));
			noWifiWarningDialog.setPositiveButton(getString(R.string.button_start_upload),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							FileManager.this.startUpload();
						}
					});
			noWifiWarningDialog.setNegativeButton(getString(R.string.button_cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// Nothing to do.
						}
					});
			noWifiWarningDialog.show();
		} else {
			FileManager.this.startUpload();
		}
	}

	/**
	 * Starts the download.
	 */
	private void startDownload() {
		if (this.serverName != null) {
			DownloadFileGR downloadFileGR = new DownloadFileGR(this);
			downloadFileGR.setServerName(serverName);
			downloadFileGR.setDestinationPath(this.phoneTab.adapter.getCurrentPath());
			downloadFileGR.setSources(this.pcTab.adapter.getSelectedFileItems());

			new GuiRequestHandler(this, false).execute(downloadFileGR);
		}
	}

	/**
	 * Starts the upload.
	 */
	private void startUpload() {
		if (this.serverName != null) {
			UploadFileGR uploadFileGR = new UploadFileGR(this);
			uploadFileGR.setServerName(serverName);
			uploadFileGR.setDestinationPath(this.pcTab.adapter.getCurrentPath());
			uploadFileGR.setSources(this.phoneTab.adapter.getSelectedFileItems());

			new GuiRequestHandler(this, false).execute(uploadFileGR);
		}
	}

	/**
	 * Check if the device is connected to WIFI network or not.
	 * 
	 * @return True - If the device is connected to any WIFI network, False otherwise.
	 */
	public boolean isWifiConnected() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mWifi.isConnected();
	}

	/**
	 * Refresh the phone adapter.
	 */
	public void refreshPhone() {
		this.phoneTab.adapter.refresh();
	}

	/**
	 * @see #responseHandler(CommandToGui)
	 */
	@Override
	public void responseHandler(CommandToGui command) {
		try {
			switch (command.getCommandID()) {
			case R.integer.COMMAND_DOWNLOAD_FILE:
				int downloadStarted = command.getSimpleResponse();
				MessageBox informMessage = new MessageBox(this);

				if (downloadStarted == 1) {
					informMessage.createInformationMessageBox(getString(R.string.download_started));
				} else {
					informMessage.createInformationMessageBox(getString(R.string.download_not_started));
				}

				break;
			case R.integer.COMMAND_UPLOAD_FILE:
				int uploadSuccessfully = command.getSimpleResponse();

				informMessage = new MessageBox(this);

				if (uploadSuccessfully == 1) {
					informMessage.createInformationMessageBox(getString(R.string.upload_successfully));
				} else {
					informMessage.createInformationMessageBox(getString(R.string.upload_unsuccessfully));
				}

				this.pcTab.adapter.refresh();
				break;
			case R.integer.COMMAND_DELETE_FILE_ANDROID:
				if (this.pcTab.adapter.getTemporalyFile() == null) {
					this.phoneTab.adapter.refresh();
					MessageBox androidFileDeletedMessageBox = new MessageBox(this);
					androidFileDeletedMessageBox.createInformationMessageBox(this
							.getString(R.string.all_files_were_deleted));
				} else {
					this.pcTab.adapter.setTemporalyFile(null);
				}

				break;
			default:
				Log.w("Not implemented proccess for this response.");
			}
		} catch (NullPointerException e) {
			Log.e("Gui can't process the response! Exception: " + e.getMessage());
		}
	}

	/**
	 * This object contains the {@link View} and the {@link Adapter} of a {@link ListView}.
	 * 
	 * @author Turdean Arnold Robert
	 * @version 1.0
	 */
	class NavigationTab {
		private ListView view = null;

		private BHListViewAdapter adapter = null;
	}
}
