package ro.edu.ubb.blackhole.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.guirequest.DeleteFileAndroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.DeleteFilePCGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.DownloadFileGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.FileExecuteAnroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetDirectorySizePCGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetListOfFilesPCGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.StartProgramOnPCGR;
import ro.edu.ubb.blackhole.gui.activityes.FileManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Provide an adapter to a {@link ListView}. This adapter provides all functionality for navigating and file managing on the PC.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class PCListViewAdapter extends BHListViewAdapter implements ResponseHandler {

	/**
	 * Name of the user server.
	 */
	private String serverName = null;

	public PCListViewAdapter(Context context, int layoutResourceId, FileManager fileManager, String serverName) {
		super(context, layoutResourceId, fileManager);

		this.serverName = serverName;
	}

	/**
	 * @see #init()
	 */
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
					PCListViewAdapter.this.fileManager.setPcTabActive();

					boolean newSelection = !fileItems.get(position).isSelected();

					PCListViewAdapter.this.fileItems.get(position).setSelected(newSelection);
					fileIconView.setImageResource(PCListViewAdapter.this.fileItems.get(position)
							.getFileIcon());
				}
			});

			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						final FileItem currentFile = PCListViewAdapter.this.fileItems.get(position);
						PCListViewAdapter.this.fileManager.setPcTabActive();

						if (currentFile.isThisFolder()) {
							GetListOfFilesPCGR request = new GetListOfFilesPCGR(
									PCListViewAdapter.this.fileManager.getApplicationContext());
							request.setServerName(PCListViewAdapter.this.serverName);
							request.setPath(currentFile.getFilePath());
							new GuiRequestHandler(PCListViewAdapter.this.fileManager, PCListViewAdapter.this,
									true).execute(request);
						} else {
							AlertDialog.Builder deviceSelect = new AlertDialog.Builder(context);
							deviceSelect.setTitle(context.getString(R.string.target_device_dialog_title));
							deviceSelect.setMessage(context.getString(R.string.choose_device));
							deviceSelect.setPositiveButton(context.getString(R.string.button_pc),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											executeFile(currentFile);
										}
									});
							deviceSelect.setNegativeButton(context.getString(R.string.button_phone),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											executeFileOnPhone(currentFile);
										}
									});
							deviceSelect.show();
						}
					} catch (Exception e) {
						// Nothing to do :) This try catch is to avoid a runtime exception when the user click too fast on the
						// items and the Android can't handle it.
					}
				}
			});

			// When somebody long clicks on a file than there will be appear a context menu.
			row.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					PCListViewAdapter.this.fileManager.setPcTabActive();
					selectedFile = fileItems.get(position);
					return false;
				}
			});

			ImageButton phoneHomeButton = (ImageButton) this.fileManager.findViewById(R.id.button_home_pc);
			phoneHomeButton.setOnClickListener(this.pcHomeListener);

			ImageButton phoneBackButton = (ImageButton) this.fileManager.findViewById(R.id.button_back_pc);
			phoneBackButton.setOnClickListener(this.pcBackListener);

			ImageButton phoneRefreshButton = (ImageButton) this.fileManager
					.findViewById(R.id.button_refresh_pc);
			phoneRefreshButton.setOnClickListener(this.pcRefreshListener);
		}

		return row;
	}

	/**
	 * @see #executeFile(FileItem)
	 */
	@Override
	public void executeFile(FileItem fileItem) {
		StartProgramOnPCGR request = new StartProgramOnPCGR(
				PCListViewAdapter.this.fileManager.getApplicationContext());
		request.setServerName(PCListViewAdapter.this.serverName);
		List<String> executableProgramPaths = new ArrayList<String>();
		executableProgramPaths.add(fileItem.getFilePath());
		request.setProgramPaths(executableProgramPaths);
		new GuiRequestHandler(PCListViewAdapter.this.fileManager, PCListViewAdapter.this, true)
				.execute(request);
	}

	/**
	 * Executes the given file on the Phone.
	 * 
	 * @param fileItem
	 *            Executable file.
	 */
	public void executeFileOnPhone(final FileItem fileItem) {
		if (!this.fileManager.isWifiConnected()) { // If no WIFI connection were detected then visualize a warning dialog.
			AlertDialog.Builder noWifiWarningDialog = new AlertDialog.Builder(this.context);
			noWifiWarningDialog.setTitle(this.context.getString(R.string.warning_dialog_title));
			noWifiWarningDialog.setMessage(this.context.getString(R.string.no_wifi_connection));
			noWifiWarningDialog.setPositiveButton(this.context.getString(R.string.button_start_download),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startTemporaryDownload(fileItem);
						}
					});
			noWifiWarningDialog.setNegativeButton(this.context.getString(R.string.button_cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// Nothing to do.
						}
					});
			noWifiWarningDialog.show();
		} else {
			startTemporaryDownload(fileItem);
		}
	}

	/**
	 * Starts the temporary download.
	 */
	private void startTemporaryDownload(FileItem file) {
		if (this.serverName != null) {
			this.setTemporalyFile(file);

			// Getting the downloads directory.
			File downloadsDirectory = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			List<FileItem> files = new ArrayList<FileItem>();
			files.add(file);

			DownloadFileGR downloadFileGR = new DownloadFileGR(this.context);
			downloadFileGR.setServerName(serverName);
			downloadFileGR.setDestinationPath(downloadsDirectory.getPath());
			downloadFileGR.setSources(files);

			new GuiRequestHandler(this.fileManager, this, true).execute(downloadFileGR);
		}
	}

	/**
	 * @see #refresh()
	 */
	@Override
	public void refresh() {
		GetListOfFilesPCGR request = new GetListOfFilesPCGR(
				PCListViewAdapter.this.fileManager.getApplicationContext());
		request.setServerName(PCListViewAdapter.this.serverName);
		request.setPath(PCListViewAdapter.this.currentPath);
		new GuiRequestHandler(PCListViewAdapter.this.fileManager, PCListViewAdapter.this, true)
				.execute(request);
	}

	/**
	 * Listener of the Home Button. Set PC tab as active tab on the screen and restore it's content to default content.
	 */
	View.OnClickListener pcHomeListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			PCListViewAdapter.this.fileManager.setPcTabActive();

			PCListViewAdapter.this.getHome();
			PCListViewAdapter.this.setCurrentPath("");
		}
	};

	/**
	 * Listener of the Back Button. Take a back step of current path, and refresh the content.
	 */
	View.OnClickListener pcBackListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			PCListViewAdapter.this.fileManager.setPcTabActive();

			if (!PCListViewAdapter.this.currentPath.equals("")) { // There is no more back.
				int lastSpecialChar = PCListViewAdapter.this.currentPath.lastIndexOf("\\");
				String newPath = PCListViewAdapter.this.currentPath.substring(0, lastSpecialChar);

				if (!newPath.contains("\\")) { // If we want to back into a root element. example: C:/
					newPath += "\\";
				}

				if (PCListViewAdapter.this.currentPath.length() == 3) { // If we want to back to the root!
					newPath = "";
				}

				GetListOfFilesPCGR request = new GetListOfFilesPCGR(
						PCListViewAdapter.this.fileManager.getApplicationContext());
				request.setServerName(PCListViewAdapter.this.serverName);
				request.setPath(newPath);
				new GuiRequestHandler(PCListViewAdapter.this.fileManager, PCListViewAdapter.this, true)
						.execute(request);
			}
		}
	};

	/**
	 * Listener of the Refresh Button. Refresh the content of the adapter.
	 */
	View.OnClickListener pcRefreshListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			PCListViewAdapter.this.fileManager.setPcTabActive();

			GetListOfFilesPCGR request = new GetListOfFilesPCGR(
					PCListViewAdapter.this.fileManager.getApplicationContext());
			request.setServerName(PCListViewAdapter.this.serverName);
			request.setPath(PCListViewAdapter.this.currentPath);
			new GuiRequestHandler(PCListViewAdapter.this.fileManager, PCListViewAdapter.this, true)
					.execute(request);
		}
	};

	/**
	 * @see #getHome()
	 */
	@Override
	public void getHome() {
		GetListOfFilesPCGR request = new GetListOfFilesPCGR(
				PCListViewAdapter.this.fileManager.getApplicationContext());
		request.setServerName(PCListViewAdapter.this.serverName);
		request.setPath("");
		new GuiRequestHandler(PCListViewAdapter.this.fileManager, PCListViewAdapter.this, true)
				.execute(request);
	}

	/**
	 * @see #getPropertiesOfSelectedFile()
	 */
	@Override
	public void getPropertiesOfSelectedFile() {
		if (this.selectedFile.isThisFolder()) {
			GetDirectorySizePCGR request = new GetDirectorySizePCGR(this.fileManager);
			request.setServerName(serverName);
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
		List<String> paths = new ArrayList<String>();
		List<FileItem> selectedFiles = this.getSelectedFileItems();

		for (FileItem currentSelectedFile : selectedFiles) {
			paths.add(currentSelectedFile.getFilePath());
		}

		if (!paths.contains(selectedFile.getFilePath())) {
			paths.add(selectedFile.getFilePath());
		}

		DeleteFilePCGR request = new DeleteFilePCGR(this.fileManager);
		request.setServerName(serverName);
		request.setPaths(paths);

		new GuiRequestHandler(this.fileManager, this, true).execute(request);
	}

	/**
	 * @see #responseHandler(CommandToGui)
	 */
	@Override
	public void responseHandler(CommandToGui command) {
		try {
			switch (command.getCommandID()) {
			case R.integer.COMMAND_GET_LIST_OF_FILES_PC:
				if (command.getSimpleResponse() == 1) {
					fillUpPhoneListView(command.getFiles());
					setCurrentPath(command.getPath());
				} else {
					MessageBox errorMessageBox = new MessageBox(this.fileManager);
					errorMessageBox.createErrorMessageBox(GuiMessageInterpreter.getErrorMessage(command
							.getSimpleResponse()));
				}
				break;
			case R.integer.COMMAND_GET_DIRECTORY_SIZE_PC:
				this.selectedFile.setDirectorySize(command.getDirectorySize());
				MessageBox messageBox = new MessageBox(this.fileManager);
				messageBox.createPropertiesMessageBox(selectedFile);
				break;
			case R.integer.COMMAND_DELETE_FILE_PC:
				refresh();
				boolean isFileDeleted = command.isFileDeleted();
				MessageBox fileDeletedMessageBox = new MessageBox(this.fileManager);

				if (isFileDeleted) {
					fileDeletedMessageBox.createInformationMessageBox(this.fileManager
							.getString(R.string.file_deleted_dialog_message));
				} else {
					fileDeletedMessageBox.createInformationMessageBox(this.fileManager
							.getString(R.string.file_not_deleted_dialog_message));
				}

				break;

			case R.integer.COMMAND_START_PROGRAMS:
				boolean allProgramsStarted = (command.getSimpleResponse() == 1) ? true : false;
				MessageBox allProgramsStartedMessageBox = new MessageBox(this.fileManager);

				if (allProgramsStarted) {
					allProgramsStartedMessageBox.createInformationMessageBox(this.fileManager
							.getString(R.string.all_programs_were_started));
				} else {
					allProgramsStartedMessageBox.createInformationMessageBox(this.fileManager
							.getString(R.string.not_all_programs_were_started));
				}

				break;
			case R.integer.COMMAND_DOWNLOAD_FILE:
				int downloadStarted = command.getSimpleResponse();
				MessageBox informMessage = new MessageBox(this.context);

				if (downloadStarted == 1 && this.getTemporalyFile() != null) {
					File downloadsDirectory = Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
					String path = downloadsDirectory.getPath() + "/" + this.getTemporalyFile().getFileName();
					this.getTemporalyFile().setFilePath(path);

					FileExecuteAnroidGR request = new FileExecuteAnroidGR(
							this.fileManager.getApplicationContext());
					request.setFile(new File(this.getTemporalyFile().getFilePath()));

					new GuiRequestHandler(PCListViewAdapter.this.fileManager, PCListViewAdapter.this, false)
							.execute(request);
				} else {
					informMessage.createInformationMessageBox(this.context
							.getString(R.string.download_not_started));
				}
				break;
			case R.integer.COMMAND_FILE_EXECUTE_ANDROID:
				if (command.getSimpleResponse() != 1) {
					MessageBox fileCannotExecute = new MessageBox(this.fileManager);
					fileCannotExecute.createInformationMessageBox(this.fileManager
							.getString(R.string.file_cannot_executed_dialog_message));

					List<String> deletableFilePath = new ArrayList<String>();
					deletableFilePath.add(this.getTemporalyFile().getFilePath());

					DeleteFileAndroidGR request = new DeleteFileAndroidGR(this.fileManager);
					request.setPaths(deletableFilePath);

					new GuiRequestHandler(this.fileManager, this, true).execute(request);
				}
				break;
			case R.integer.COMMAND_DELETE_FILE_ANDROID:
				this.setTemporalyFile(null);
				break;
			case R.integer.BLACK_HOLE_EXCEPTION:
				MessageBox errorMessageBox = new MessageBox(this.fileManager);
				errorMessageBox.createErrorMessageBox(command.getBlackHoleException().getDetailMessage());
			default:
				Log.w("Not implemented proccess for this response.");
			}
		} catch (NullPointerException e) {
			Log.e("Gui can't process the response!");
		}
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @see #setCurrentPath(String)
	 */
	@Override
	public void setCurrentPath(String currentPath) {
		super.setCurrentPath(currentPath);
		this.fileManager.setPCCurrentPath(currentPath);
	}
}
