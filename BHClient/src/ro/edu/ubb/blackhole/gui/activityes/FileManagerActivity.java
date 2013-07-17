package ro.edu.ubb.blackhole.gui.activityes;

import java.util.ArrayList;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.GuiRequest;
import ro.edu.ubb.blackhole.gui.GuiRequestHandler;
import ro.edu.ubb.blackhole.gui.MessageBox;
import ro.edu.ubb.blackhole.gui.PhoneListViewAdapter;
import ro.edu.ubb.blackhole.gui.ResponseHandler;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FileManagerActivity extends Activity implements ResponseHandler {
	// ASK Clickelve atmeretezes az adapter segitsegevel!
	private final static String TAG = FileManagerActivity.class.getSimpleName();

	private NavigationTab phoneTab = new NavigationTab();

	private NavigationTab pcTab = new NavigationTab();

	private FileItem selectedFile = null;

	@SuppressWarnings("unused")
	private View selectedListView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_manager);

		// Initialize phoneTab.
		this.phoneTab.view = (ListView) findViewById(R.id.listview_phone_files);
		this.phoneTab.adapter = new PhoneListViewAdapter(this, R.layout.listview_fm_item_row,
				new ArrayList<FileItem>(), this);
		View phoneListViewHeader = (View) getLayoutInflater()
				.inflate(R.layout.listview_fm_header_phone, null);
		this.phoneTab.view.addHeaderView(phoneListViewHeader, null, false);
		this.phoneTab.view.setCacheColorHint(0);
		this.phoneTab.view.setAdapter(this.phoneTab.adapter);
		registerForContextMenu(this.phoneTab.view);

		// TODO Change PC adapter!!!
		// Initialize pcTab.
		this.pcTab.view = (ListView) findViewById(R.id.listview_pc_files);
		this.pcTab.adapter = new PhoneListViewAdapter(this, R.layout.listview_fm_item_row,
				new ArrayList<FileItem>(), this);
		View pcListViewHeader = (View) getLayoutInflater().inflate(R.layout.listview_fm_header_pc, null);
		this.pcTab.view.addHeaderView(pcListViewHeader, null, false);
		this.pcTab.view.setCacheColorHint(0);
		this.pcTab.view.setAdapter(this.pcTab.adapter);
		registerForContextMenu(this.pcTab.view);

		ImageButton activeTabChanger = (ImageButton) findViewById(R.id.button_active_tab_changer);
		activeTabChanger.setOnClickListener(activeTabChangerListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_file_manager, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		this.selectedListView = v;

		menu.add(getString(R.string.context_meniu_rename));
		menu.add(getString(R.string.context_meniu_delete));
		menu.add(getString(R.string.context_meniu_properties));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		String selectedProcess = (String) item.getTitle();

		if (selectedProcess.equals(getString(R.string.context_meniu_properties))) {
			getProperties();
		} else if (selectedProcess.equals(getString(R.string.context_meniu_delete))) {
			GuiRequest request = new GuiRequest(R.integer.COMMAND_DELETE_FILE_ANDROID, this);
			request.setPath(this.selectedFile.getFilePath());

			new GuiRequestHandler(FileManagerActivity.this, true).execute(request);
		}

		return true;
	}

	private void getProperties() {
		if (this.selectedFile.isThisFolder()) {
			GuiRequest request = new GuiRequest(R.integer.COMMAND_GET_DIRECTORY_SIZE_ANDROID, this);
			request.setPath(this.selectedFile.getFilePath());

			new GuiRequestHandler(FileManagerActivity.this, true).execute(request);
		} else {
			MessageBox messageBox = new MessageBox(this);
			messageBox.createPropertiesMessageBox(selectedFile);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Phone rotate: save list view adapters!
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Phone rotate: restores list view adapters!
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Change the active tab between Phone Tab and PC Tab.
	 */
	private void changeActiveTab() {
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) this.phoneTab.view
				.getLayoutParams();
		params.weight = 1 - params.weight;
		this.phoneTab.view.setLayoutParams(params);

		params = (android.widget.LinearLayout.LayoutParams) this.pcTab.view.getLayoutParams();
		params.weight = 1 - params.weight;
		this.pcTab.view.setLayoutParams(params);
	}

	@Override
	public void responseHandler(CommandToGui command) {
		try {
			switch (command.getCommandID()) {
			case R.integer.COMMAND_GET_LIST_OF_FILES_ANDROID:
				this.phoneTab.adapter.fillUpPhoneListView(command.getFiles());
				this.phoneTab.adapter.setCurrentPath(command.getPath());
				break;
			case R.integer.COMMAND_GET_DIRECTORY_SIZE_ANDROID:
				this.selectedFile.setDirectorySize(command.getDirectorySize());
				MessageBox messageBox = new MessageBox(this);
				messageBox.createPropertiesMessageBox(selectedFile);
				break;
			case R.integer.COMMAND_DELETE_FILE_ANDROID:
				this.phoneTab.adapter.refresh();
				MessageBox fileDeletedMessageBox = new MessageBox(this);
				fileDeletedMessageBox.createInformationMessageBox("File deleted!");
				break;
			case R.integer.BLACK_HOLE_EXCEPTION:
				MessageBox errorMessageBox = new MessageBox(this);
				errorMessageBox.createErrorMessageBox(command.getBlackHoleException().getDetailMessage());
			default:
				Log.w(TAG, "responseHandler() - Not implemented proccess for this response.");
			}
		} catch (NullPointerException e) {
			Log.e(TAG, "responseHandler() - Gui can't process the response!");
		}
	}

	public void setSelectedFile(FileItem selectedFile) {
		this.selectedFile = selectedFile;
	}

	View.OnClickListener activeTabChangerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			changeActiveTab();
		}
	};

	class NavigationTab {
		private ListView view = null;

		private PhoneListViewAdapter adapter = null;
	}
}
