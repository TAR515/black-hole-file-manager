package ro.edu.ubb.blackhole.dal.pcfileservice.filetransfermanagers;

import java.util.List;

import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.gui.activityes.FileManager;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

/**
 * This object manage all file downloads from a given source.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class BHDownloadManager {

	/**
	 * System's Downalod Manager.
	 */
	private DownloadManager downloadManager = null;

	/**
	 * The ID of the latest download.
	 */
	private long lastDownload = -1L;

	/**
	 * Context of the application.
	 */
	private static Context applicationContext = null;

	/**
	 * With help of this {@link Object} the download request do not returns until the started downloads were not terminated
	 * successfully.
	 */
	private static Object semaphore = new Object();

	public BHDownloadManager(Context applicationContext) {
		BHDownloadManager.applicationContext = applicationContext;

		this.downloadManager = (DownloadManager) BHDownloadManager.applicationContext
				.getSystemService(Context.DOWNLOAD_SERVICE);

		registerReceivers(applicationContext);
	}

	/**
	 * Registers the Receivers to the application context.
	 * 
	 * @param applicationContext
	 *            Context of the applicaiton.
	 */
	public static void registerReceivers(Context applicationContext) {
		applicationContext.registerReceiver(onComplete, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		applicationContext.registerReceiver(onNotificationClick, new IntentFilter(
				DownloadManager.ACTION_NOTIFICATION_CLICKED));
	}

	/**
	 * Unregisters the Receivers from the application context.
	 * 
	 * @param applicationContext
	 *            Context of the application.
	 */
	public static void unregisterReceivers(Context applicationContext) {
		applicationContext.unregisterReceiver(onComplete);
		applicationContext.unregisterReceiver(onNotificationClick);
	}

	/**
	 * Starts to download the given files from the given URI.
	 * 
	 * @param serverUri
	 *            Source machine's URI.
	 * @param destinationPath
	 *            Target place where the downloaded files will be appear.
	 * @param sources
	 *            Files which will be downloading.
	 * @return True - if all downloads starts successfully / False - otherwise.
	 */
	public boolean startDownload(Uri serverUri, String destinationPath, List<FileItem> sources) {
		for (FileItem currentFileItem : sources) {
			if (currentFileItem.getFilePath().contains(Environment.getExternalStorageDirectory().getPath())) {
				return false;
			}
		}

		for (FileItem currentFileItem : sources) {
			if (!currentFileItem.isThisFolder()) {
				downloadFile(currentFileItem, destinationPath, serverUri);
			} else {
				downloadDirectory(currentFileItem, destinationPath, serverUri);
			}
		}

		return true;
	}

	/**
	 * Download a single file from the given machine.
	 * 
	 * @param file
	 *            File.
	 * @param destinationPath
	 *            Target folder where the file will be downloading.
	 * @param serverUri
	 *            Source machine's URI.
	 */
	private void downloadFile(FileItem file, String destinationPath, Uri serverUri) {
		String sourcePath = file.getFilePath();

		Environment.getExternalStoragePublicDirectory(getRealDestination(destinationPath)).mkdirs();

		lastDownload = downloadManager.enqueue(new DownloadManager.Request(serverUri)
				.addRequestHeader("file", sourcePath)
				.setAllowedNetworkTypes(
						DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
				.setAllowedOverRoaming(false)
				.setTitle(getFileName(sourcePath))
				.setDescription(getFileName(sourcePath))
				.setDestinationInExternalPublicDir(getRealDestination(destinationPath),
						getFileName(sourcePath)));

		synchronized (BHDownloadManager.semaphore) {
			try {
				BHDownloadManager.semaphore.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Not implemented yet!
	 * 
	 * @param file
	 * @param destinationPath
	 * @param serverUri
	 */
	private void downloadDirectory(FileItem file, String destinationPath, Uri serverUri) {
		// TODO implement this method.
	}

	/**
	 * Returns name of the given file.
	 * 
	 * @param filePath
	 *            File's path.
	 * @return Name of the file.
	 */
	private String getFileName(String filePath) {
		if (filePath != null) {
			int lastIndOfSlash = filePath.lastIndexOf("/");
			int lastIndOfBackSlash = filePath.lastIndexOf("\\");

			int lastSpecialChar = (lastIndOfSlash > lastIndOfBackSlash) ? lastIndOfSlash : lastIndOfBackSlash;
			return filePath.substring(lastSpecialChar + 1, filePath.length());
		}

		return null;
	}

	/**
	 * Returns the real path of the destination path.
	 * 
	 * @param destinationPath
	 *            destination path.
	 * @return Real folder's path.
	 */
	private String getRealDestination(String destinationPath) {
		String externalStoragePath = Environment.getExternalStorageDirectory().getPath();

		return destinationPath.replaceAll(externalStoragePath, "");
	}

	/**
	 * Get the query status of the download.
	 * 
	 * @param v
	 * @return Query status.
	 */
	public String getQueryStatus(View v) {
		Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(lastDownload));

		if (c == null) {
			return "Download not found!";
		} else {
			c.moveToFirst();

			return statusMessage(c);
		}
	}

	/**
	 * Starts the View Downloads activity.
	 */
	public static void viewLog() {
		applicationContext.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
	}

	/**
	 * Returns the download status message.
	 * 
	 * @param c
	 *            Cursor to the download.
	 * @return Status message.
	 */
	private String statusMessage(Cursor c) {
		String msg = "???";

		switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
		case DownloadManager.STATUS_FAILED:
			msg = "Download failed!";
			break;

		case DownloadManager.STATUS_PAUSED:
			msg = "Download paused!";
			break;

		case DownloadManager.STATUS_PENDING:
			msg = "Download pending!";
			break;

		case DownloadManager.STATUS_RUNNING:
			msg = "Download in progress!";
			break;

		case DownloadManager.STATUS_SUCCESSFUL:
			msg = "Download complete!";
			break;

		default:
			msg = "Download is nowhere in sight";
			break;
		}

		return (msg);
	}

	/**
	 * This {@link BroadcastReceiver} is called when the download is finished.
	 */
	public static BroadcastReceiver onComplete = new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent intent) {
			if (applicationContext instanceof FileManager) {
				synchronized (semaphore) {
					semaphore.notify();
				}

				FileManager fileManager = (FileManager) applicationContext;
				fileManager.refreshPhone();
			}
		}
	};

	/**
	 * This {@link BroadcastReceiver} is called when somebody clicks to the download notification bar.
	 */
	public static BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent intent) {
			viewLog();
		}
	};

}
