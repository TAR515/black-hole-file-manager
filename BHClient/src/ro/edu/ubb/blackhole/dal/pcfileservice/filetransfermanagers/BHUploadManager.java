package ro.edu.ubb.blackhole.dal.pcfileservice.filetransfermanagers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * This object manage all file uploads to the given source.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class BHUploadManager {

	/**
	 * Context of the application.
	 */
	private Context applicationContext = null;

	/**
	 * This manage the upload notifications, it creates an upload notification bar during the upload.
	 */
	private NotificationManager notificationManager;

	/**
	 * Concrete notification, a progress bar until the upload.
	 */
	private Notification notification;

	/**
	 * Max streaming buffer size.
	 */
	private static final int MAX_BUFFER_SIZE = 1024 * 512;

	@SuppressWarnings("deprecation")
	public BHUploadManager(Context applicationContext) {
		this.applicationContext = applicationContext;

		Intent notificationIntent = new Intent();
		PendingIntent contentIntent = PendingIntent.getActivity(this.applicationContext, 0,
				notificationIntent, 0);

		notification = new Notification(android.R.drawable.stat_sys_upload, "Uploading file",
				System.currentTimeMillis());
		notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
		notification.contentView = new RemoteViews(this.applicationContext.getPackageName(),
				R.layout.upload_progress_bar);
		notification.contentIntent = contentIntent;
		notification.contentView.setProgressBar(R.id.progressBar1, 100, 0, false);

		if (this.notification == null) {
			System.out.println("NULL NOT");
		}

		notificationManager = (NotificationManager) this.applicationContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, notification);
	}

	/**
	 * Starts uploading the given files to the given URL.
	 * 
	 * @param serverUri
	 *            Server's URL.
	 * @param destinationPath
	 *            The folder where the files will be upload.
	 * @param sources
	 *            Files.
	 * @return True - if all uploads were successfully / False - otherwise.
	 */
	public boolean startUploading(URL serverUri, String destinationPath, List<FileItem> sources) {
		if (destinationPath == null || "".equals(destinationPath)) {
			notificationManager.cancelAll();
			return false;
		}

		boolean allFilesWereUploadedSuccessfully = true;

		for (FileItem currentFileItem : sources) {
			if (!currentFileItem.isThisFolder()) {
				if (!uploadFile(currentFileItem, destinationPath, serverUri)) {
					allFilesWereUploadedSuccessfully = false;
				}
			} else {
				if (!uploadDirectory(currentFileItem, destinationPath, serverUri)) {
					allFilesWereUploadedSuccessfully = false;
				}
			}
		}

		notificationManager.cancelAll();
		return allFilesWereUploadedSuccessfully;
	}

	/**
	 * Upload the given directory to the given URL
	 * 
	 * @param file
	 *            Folder.
	 * @param destinationPath
	 *            The folder where the folder will be upload.
	 * @param serverUri
	 *            URL of the target machine.
	 * @return True - if the folder were uploaded successfully / False - otherwise.
	 */
	public boolean uploadDirectory(FileItem file, String destinationPath, URL serverUri) {
		return false;
	}

	/**
	 * Upload the given file to the given URL.
	 * 
	 * @param file
	 *            File.
	 * @param destinationPath
	 *            The folder where the file will be upload.
	 * @param serverURL
	 *            Server's URL.
	 * @return True - if the file upload were successfully / False - otherwise.
	 */
	@SuppressWarnings("deprecation")
	public boolean uploadFile(FileItem file, String destinationPath, URL serverURL) {
		notification.contentView.setTextViewText(R.id.upload_notification_text_view, file.getFileName());
		notification.contentView.setTextViewText(R.id.upload_percent_text_view, "0%");

		HttpURLConnection connection = null;

		try {
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";

			String filePath = file.getFilePath();

			FileInputStream fileInputStream = new FileInputStream(new File(filePath));

			connection = (HttpURLConnection) serverURL.openConnection();
			connection.setDoInput(true);
			// Allow Outputs
			connection.setDoOutput(true);
			// Don't use a cached copy.
			connection.setUseCaches(false);
			// Use a post method.
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("DestinationPath", destinationPath + "\\");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			connection.setChunkedStreamingMode(MAX_BUFFER_SIZE);

			DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
			dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
			dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ file.getFileName() + "\"" + lineEnd);
			dataOutputStream.writeBytes(lineEnd);

			int bytesAvailable = fileInputStream.available();
			int bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);
			byte[] buffer = new byte[bufferSize];
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			long fileSize = new File(filePath).length();

			float percentPerPacket = (float) MAX_BUFFER_SIZE / (float) fileSize * 100;
			float currPercent = 0;

			while (bytesRead > 0) {
				dataOutputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				currPercent += percentPerPacket;
				notification.contentView.setProgressBar(R.id.progressBar1, 100, (int) currPercent, false);
				notification.contentView.setTextViewText(R.id.upload_percent_text_view,
						percentToString(currPercent));
				notificationManager.notify(1, notification);
			}

			dataOutputStream.writeBytes(lineEnd);
			dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			fileInputStream.close();
			dataOutputStream.flush();
			dataOutputStream.close();

			notification.contentView.setProgressBar(R.id.progressBar1, 100, 100, false);
			notification.contentView.setTextViewText(R.id.upload_percent_text_view, "100%");
			notificationManager.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(e.getMessage());
			return false;
		}

		try {
			DataInputStream inStream = new DataInputStream(connection.getInputStream());
			String str;
			while ((str = inStream.readLine()) != null) {
				inStream.close();

				if ("OK".equals(str)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (IOException e) {
			Log.e(e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Convert a float which indicates the current upload percent to {@link String}.
	 * 
	 * @param percent
	 *            Indicates the current upload percent.
	 * @return A String which indicates the percentage of the upload.
	 */
	private String percentToString(float percent) {
		return (int) percent + "%";
	}
}
