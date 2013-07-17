package ro.edu.ubb.blackhole.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.guirequest.DeleteFileAndroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.FileExecuteAnroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetDirectorySizeAndroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetHomeAndroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetListOfFilesAndroidGR;
import ro.edu.ubb.blackhole.gui.activityes.FileManager;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Provide an adapter to a {@link ListView}. This adapter provides all functionality for navigating and file managing on the
 * phone.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class PhoneListViewAdapter extends BHListViewAdapter implements ResponseHandler {
	public PhoneListViewAdapter(Context context, int layoutResourceId, FileManager fileManager) {
		super(context, layoutResourceId, fileManager);
	}

	/**
	 * @see PhoneListViewAdapter#init()
	 */
	@Override
	public void init() {
		getHome();
	}

	/**
	 * @see #getView(int, View, ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(this.rowResourceId, parent, false);
		}

		FileItem currentFileItem = fileItems.get(position);
		final int iconResource = currentFileItem.getFileIcon();
		String fileName = currentFileItem.getFileName();
		String fileSize = currentFileItem.getFileSize();
		String fileLastModifiedDate = currentFileItem.getLastModifiedDateUserFriendly();

		if (currentFileItem != null) {
			final ImageButton fileIconView = (ImageButton) row.findViewById(R.id.imageview_icon);
			TextView fileNameView = (TextView) row.findViewById(R.id.textview_filename);
			TextView fileSizeView = (TextView) row.findViewById(R.id.textview_file_size);
			TextView fileLastModifiedDataView = (TextView) row.findViewById(R.id.textview_file_date);

			if (fileIconView != null) {
				fileIconView.setImageResource(iconResource);
			}

			if (fileNameView != null) {
				fileNameView.setText(fileName);
			}

			if (fileSizeView != null) {
				fileSizeView.setText(fileSize);
			}

			if (fileLastModifiedDataView != null) {
				fileLastModifiedDataView.setText(fileLastModifiedDate);
			}

			// When somebody clicks on file icon that file will be selected.
			fileIconView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PhoneListViewAdapter.this.fileManager.setPhoneTabActive();

					boolean newSelection = !fileItems.get(position).isSelected();

					PhoneListViewAdapter.this.fileItems.get(position).setSelected(newSelection);
					fileIconView.setImageResource(PhoneListViewAdapter.this.fileItems.get(position)
							.getFileIcon());
				}
			});

			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FileItem currentFile = PhoneListViewAdapter.this.fileItems.get(position);
					PhoneListViewAdapter.this.fileManager.setPhoneTabActive();

					if (currentFile.isThisFolder()) {
						GetListOfFilesAndroidGR request = new GetListOfFilesAndroidGR(
								PhoneListViewAdapter.this.fileManager.getApplicationContext());
						request.setPath(currentFile.getFilePath());
						new GuiRequestHandler(PhoneListViewAdapter.this.fileManager,
								PhoneListViewAdapter.this, false).execute(request);
					} else {
						executeFile(currentFile);
					}
				}
			});

			// When somebody long clicks on a file than there will be appear a context menu.
			row.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					PhoneListViewAdapter.this.fileManager.setPhoneTabActive();
					selectedFile = fileItems.get(position);
					return false;
				}
			});

			ImageButton phoneHomeButton = (ImageButton) this.fileManager.findViewById(R.id.button_home_phone);
			phoneHomeButton.setOnClickListener(this.phoneHomeListener);

			ImageButton phoneBackButton = (ImageButton) this.fileManager.findViewById(R.id.button_back_phone);
			phoneBackButton.setOnClickListener(this.phoneBackListener);

			ImageButton phoneRefreshButton = (ImageButton) this.fileManager
					.findViewById(R.id.button_refresh_phone);
			phoneRefreshButton.setOnClickListener(this.phoneRefreshListener);
		}

		return row;
	}

	/**
	 * @see #executeFile(FileItem)
	 */
	@Override
	public void executeFile(FileItem fileItem) {
		FileExecuteAnroidGR request = new FileExecuteAnroidGR(this.fileManager.getApplicationContext());
		request.setFile(new File(fileItem.getFilePath()));

		new GuiRequestHandler(PhoneListViewAdapter.this.fileManager, PhoneListViewAdapter.this, false)
				.execute(request);
	}

	/**
	 * @see #getHome()
	 */
	@Override
	public void getHome() {
		GetHomeAndroidGR request = new GetHomeAndroidGR(this.fileManager.getApplicationContext());
		new GuiRequestHandler(PhoneListViewAdapter.this.fileManager, PhoneListViewAdapter.this, false)
				.execute(request);
	}

	/**
	 * @see #refresh()
	 */
	@Override
	public void refresh() {
		GetListOfFilesAndroidGR request = new GetListOfFilesAndroidGR(PhoneListViewAdapter.this.fileManager);
		request.setPath(PhoneListViewAdapter.this.currentPath);

		new GuiRequestHandler(PhoneListViewAdapter.this.fileManager, PhoneListViewAdapter.this, false)
				.execute(request);
	}

	/**
	 * Listener of the Home Button. Set Phone tab as active tab on the screen and restore it's content to default content.
	 */
	View.OnClickListener phoneHomeListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			PhoneListViewAdapter.this.fileManager.setPhoneTabActive();
			getHome();
		}
	};

	/**
	 * Listener of the Back Button. Take a back step of current path, and refresh the content.
	 */
	View.OnClickListener phoneBackListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			PhoneListViewAdapter.this.fileManager.setPhoneTabActive();

			if (!PhoneListViewAdapter.this.currentPath.equals("")) { // There is no more back.
				if (!PhoneListViewAdapter.this.currentPath.equals("/") && !currentPath.equals("/mnt")) {
					int lastSpecialChar = PhoneListViewAdapter.this.currentPath.lastIndexOf("/");
					String newPath = PhoneListViewAdapter.this.currentPath.substring(0, lastSpecialChar);

					if (newPath.equals("")) {
						newPath += "/";
					}

					GetListOfFilesAndroidGR request = new GetListOfFilesAndroidGR(
							PhoneListViewAdapter.this.fileManager);
					request.setPath(newPath);

					new GuiRequestHandler(PhoneListViewAdapter.this.fileManager, PhoneListViewAdapter.this,
							false).execute(request);
				} else { // Back to home.
					getHome();
				}
			}
		}
	};

	/**
	 * Listener of the Refresh Button. Refresh the content of the adapter.
	 */
	View.OnClickListener phoneRefreshListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			PhoneListViewAdapter.this.fileManager.setPhoneTabActive();

			if (!PhoneListViewAdapter.this.currentPath.equals("")) { // Because this is an illegal path!
				GetListOfFilesAndroidGR request = new GetListOfFilesAndroidGR(
						PhoneListViewAdapter.this.fileManager);
				request.setPath(PhoneListViewAdapter.this.currentPath);

				new GuiRequestHandler(PhoneListViewAdapter.this.fileManager, PhoneListViewAdapter.this, false)
						.execute(request);
			}
		}
	};

	/**
	 * @see #getPropertiesOfSelectedFile()
	 */
	@Override
	public void getPropertiesOfSelectedFile() {
		if (this.selectedFile.isThisFolder()) {
			GetDirectorySizeAndroidGR request = new GetDirectorySizeAndroidGR(this.fileManager);
			request.setPath(this.selectedFile.getFilePath());

			new GuiRequestHandler(this.fileManager, this, true).execute(request);
		} else {
			MessageBox messageBox = new MessageBox(this.fileManager);
			messageBox.createPropertiesMessageBox(selectedFile);
		}
	}

	/**
	 * @see #deleteSelectedFiles()
	 */
	@Override
	public void deleteSelectedFiles() {
		// Getting the paths of files which we would like to delete.
		List<String> paths = new ArrayList<String>();
		List<FileItem> selectedFiles = this.getSelectedFileItems();

		for (FileItem currentSelectedFile : selectedFiles) {
			paths.add(currentSelectedFile.getFilePath());
		}

		if (!paths.contains(selectedFile.getFilePath())) {
			paths.add(selectedFile.getFilePath());
		}

		// Make a delete request.
		DeleteFileAndroidGR request = new DeleteFileAndroidGR(this.fileManager);
		request.setPaths(paths);

		new GuiRequestHandler(this.fileManager, this, true).execute(request);
	}

	/**
	 * @see #setCurrentPath(String)
	 */
	@Override
	public void setCurrentPath(String currentPath) {
		super.setCurrentPath(currentPath);
		this.fileManager.setPhoneCurrentPath(currentPath);
	}

	/**
	 * @see #responseHandler(CommandToGui)
	 */
	@Override
	public void responseHandler(CommandToGui command) {
		try {
			switch (command.getCommandID()) {
			case R.integer.COMMAND_GET_LIST_OF_FILES_ANDROID:
				fillUpPhoneListView(command.getFiles());
				setCurrentPath(command.getPath());
				break;

			case R.integer.COMMAND_GET_DIRECTORY_SIZE_ANDROID:
				this.selectedFile.setDirectorySize(command.getDirectorySize());
				MessageBox messageBox = new MessageBox(this.fileManager);
				messageBox.createPropertiesMessageBox(selectedFile);
				break;

			case R.integer.COMMAND_DELETE_FILE_ANDROID:
				refresh();
				MessageBox fileDeletedMessageBox = new MessageBox(this.fileManager);
				fileDeletedMessageBox.createInformationMessageBox(this.context
						.getString(R.string.all_files_were_deleted));
				break;

			case R.integer.COMMAND_FILE_EXECUTE_ANDROID:
				if (command.getSimpleResponse() != 1) {
					MessageBox fileCannotExecute = new MessageBox(this.fileManager);
					fileCannotExecute.createInformationMessageBox(this.fileManager
							.getString(R.string.file_cannot_executed_dialog_message));
				}
				break;

			case R.integer.COMMAND_GET_HOME_ANDROID:
				fillUpPhoneListView(command.getFiles());
				this.setCurrentPath("");
				break;

			case R.integer.BLACK_HOLE_EXCEPTION:
				MessageBox errorMessageBox = new MessageBox(this.fileManager);
				errorMessageBox.createErrorMessageBox(command.getBlackHoleException().getDetailMessage());
				break;

			default:
				Log.w("Not implemented proccess for this response.");
			}
		} catch (NullPointerException e) {
			Log.e("Gui can't process the response!");
		}
	}
}
