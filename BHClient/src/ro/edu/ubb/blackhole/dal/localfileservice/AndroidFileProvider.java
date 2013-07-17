package ro.edu.ubb.blackhole.dal.localfileservice;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.BlackHoleException;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.guirequest.GuiRequest;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple2;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

public class AndroidFileProvider implements LocalFileService {
	private boolean running = true;

	private Context applicationContext = null;

	public AndroidFileProvider(Context applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @see #getFilesAndroid(GuiRequest)
	 */
	@Override
	public Tuple2<List<FileItem>, String> getFilesAndroid(String path) throws BlackHoleException {
		List<FileItem> listOfFiles = new ArrayList<FileItem>();

		File file = new File(path);

		try {
			for (File currentFile : file.listFiles()) {
				boolean isWriteable = currentFile.canWrite();
				boolean isReadable = currentFile.canRead();
				boolean isExecutable = currentFile.canExecute();

				FileItem currentFileItem = new FileItem(currentFile.getPath(), currentFile.isDirectory());
				currentFileItem.setFileSize(currentFile.length());
				currentFileItem.setLastModifiedDate(currentFile.lastModified());
				currentFileItem.setFilePermissions(isExecutable, isReadable, isWriteable);

				listOfFiles.add(currentFileItem);
			}
		} catch (NullPointerException e) {
			Log.e("getFiles() - Invalid path: " + path);

			throw new BlackHoleException(BlackHoleException.FILE_NOT_FOUND_EXCEPTION, "File not found!");
		}

		Collections.sort(listOfFiles);

		return new Tuple2<List<FileItem>, String>(listOfFiles, path);
	}

	// ASK Tulzottan melyre megy a rekurzio!
	/**
	 * @see #getDirectorySizeAndroid(GuiRequest)
	 */
	@Override
	public long getDirectorySizeAndroid(String path) {
		return getDirectorySize(new File(path), 0L);
	}

	/**
	 * @see #getDirectorySizeAndroid(GuiRequest)
	 */
	private long getDirectorySize(File currentDirectory, long size) {
		File[] fileList = currentDirectory.listFiles();
		long sizeOfDirectory = 0;

		if (fileList != null) {
			for (File currentFile : fileList) {
				if (currentFile.isDirectory() && this.running) {
					sizeOfDirectory += getDirectorySize(currentFile, sizeOfDirectory);
				} else {
					sizeOfDirectory += currentFile.length();
				}
			}
		}

		return sizeOfDirectory;
	}

	/**
	 * @see #deleteFileAndroid(GuiRequest)
	 */
	@Override
	public void deleteFilesAndroid(List<String> paths) throws BlackHoleException {
		for (String currentPath : paths) {
			System.out.println("CurrentPath: " + currentPath);
			File targetFileOrDirectory = new File(currentPath);

			if (targetFileOrDirectory.isDirectory()) {
				deleteDirectory(targetFileOrDirectory);
				deleteFileOrEmptyDirectory(targetFileOrDirectory);
			} else {
				deleteFileOrEmptyDirectory(targetFileOrDirectory);
			}
		}
	}

	/**
	 * Delete a directory from the phone.
	 * 
	 * @param currentDirectory
	 *            target directory.
	 * @throws BlackHoleException
	 *             If the directory were not deleted.
	 */
	private void deleteDirectory(File currentDirectory) throws BlackHoleException {
		File[] fileList = currentDirectory.listFiles();

		if (fileList != null) {
			for (File currentFile : fileList) {
				if (currentFile.isDirectory() && this.running) {
					deleteDirectory(currentFile);
					deleteFileOrEmptyDirectory(currentFile);
				} else {
					deleteFileOrEmptyDirectory(currentFile);
				}
			}
		} else {
			Log.w("deleteDirectory() - The directory were not deleted: " + currentDirectory.getName());
			throw new BlackHoleException(BlackHoleException.FILE_NOT_FOUND_EXCEPTION,
					"You have no permission to delete: " + currentDirectory.getName());
		}
	}

	/**
	 * Delete a file or an empty folder from the phone.
	 * 
	 * @param file
	 *            The target file.
	 * @throws BlackHoleException
	 *             If the file were not deleted
	 */
	private void deleteFileOrEmptyDirectory(File file) throws BlackHoleException {
		if (!file.delete()) {
			Log.w("deleteFileOrEmptyDirectory() - The file were not deleted: " + file.getName());
			throw new BlackHoleException(BlackHoleException.FILE_NOT_FOUND_EXCEPTION,
					"You have no permission to delete: " + file.getName());
		}
	}

	/**
	 * @see #renameDirectoryAndroid(GuiRequest)
	 */
	@Override
	public boolean renameDirectoryAndroid(String path, String newName) {
		File targetFileOrDirectory = new File(path);

		File newNamedFileOrDirectory = new File(newName);

		if (targetFileOrDirectory.renameTo(newNamedFileOrDirectory)) {
			return true;
		} else {
			Log.w("renameDirectory() - The file were not renamed: " + targetFileOrDirectory.getName());

			return false;
		}
	}

	public boolean fileExecuteAndroid(File file) {
		try {
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);

			MimeTypeMap mime = MimeTypeMap.getSingleton();
			String ext = getFileExtension(file);
			String type = mime.getMimeTypeFromExtension(ext);

			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(file), type);

			this.applicationContext.startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			Log.e("executeFile(FileItem) - ActivityNotFoundException: " + e.getMessage());
			return false;
		}
	}

	private String getFileExtension(File file) {
		String path = file.getPath();

		if (path != null) {
			int lastSpecialChar = path.lastIndexOf(".");

			return path.substring(lastSpecialChar + 1, path.length());
		} else {
			return null;
		}
	}

	@Override
	public List<FileItem> getHomeAndroid() {
		List<FileItem> homeDirectories = new ArrayList<FileItem>();

		// Getting the system root directory.
		File rootDirectory = Environment.getRootDirectory();
		homeDirectories.add(new FileItem(rootDirectory));

		// If there is an SD Card then get the default SD Card directories.
		String state = Environment.getExternalStorageState();
		if (state.contentEquals(Environment.MEDIA_MOUNTED)
				|| state.contentEquals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			// Getting the SD Card Root directory.
			File sdCard = Environment.getExternalStorageDirectory();
			homeDirectories.add(new FileItem(sdCard));

			// Getting the Download directory.
			File downloadDirectory = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			homeDirectories.add(new FileItem(downloadDirectory));

			// Getting the DCIM directory.
			File dcimDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			homeDirectories.add(new FileItem(dcimDirectory));

			// Getting Music directory.
			File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			homeDirectories.add(new FileItem(musicDirectory));

			// Getting the Pictures directory.
			File photosDirectory = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			homeDirectories.add(new FileItem(photosDirectory));

			// Getting the Movies directory.
			File movieDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
			homeDirectories.add(new FileItem(movieDirectory));
		}

		return homeDirectories;
	}

	/**
	 * @see #stopExecution()
	 */
	@Override
	public void stopExecution() {
		Log.i("stopExecution() - Stops the current work!");

		this.running = false;
	}

	@Override
	public Tuple2<List<FileItem>, String> getParentDirectoryFilesAndroid(String path) {
		// TODO Auto-generated method stub
		return null;
	}

}
