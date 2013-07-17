package ro.edu.ubb.blackholepcserver.dal.fileexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.datastructures.FileItem;

public class WindowsFileExplorer implements FileExplorer {

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getLogger(WindowsFileExplorer.class);

	@Override
	public List<FileItem> getListOfFiles(String path) {
		if ("".equals(path)) {
			return getRoots();
		} else {
			return getNonRoot(path);
		}
	}

	private List<FileItem> getNonRoot(String path) {
		List<FileItem> listOfFiles = new ArrayList<FileItem>();

		try {
			File file = new File(path);

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
		} catch (Exception e) {
			logger.warn(e.getMessage());

			return new ArrayList<FileItem>();
		}

		Collections.sort(listOfFiles);

		return listOfFiles;
	}

	public List<FileItem> getRoots() {
		List<FileItem> listOfRoots = new ArrayList<FileItem>();

		// Run through all possible mount points and check for their existance.
		for (char c = 'C'; c <= 'Z'; c++) {
			char device[] = { c, ':', '\\' };
			String deviceName = new String(device);
			File deviceFile = new File(deviceName);
			if (deviceFile != null && deviceFile.exists()) {
				FileItem newRootElement = new FileItem(deviceFile.getPath(), true);
				newRootElement.setFileName(deviceName);
				newRootElement.setFileSize("");
				newRootElement.setLastModifiedDate(0);
				newRootElement.setFilePermissions(false, false, false);

				listOfRoots.add(newRootElement);
			}
		}

		return listOfRoots;
	}

	@Override
	public long getDirectorySize(String path) {
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
				if (currentFile.isDirectory()) {
					sizeOfDirectory += getDirectorySize(currentFile, sizeOfDirectory);
				} else {
					sizeOfDirectory += currentFile.length();
				}
			}
		}

		return sizeOfDirectory;
	}

	@Override
	public boolean deleteFiles(List<String> paths) {
		boolean allFilesWereDeleted = true;

		for (String currentPath : paths) {
			File targetFileOrDirectory = new File(currentPath);

			if (targetFileOrDirectory.isDirectory()) {
				boolean directoryDeleted = deleteDirectory(targetFileOrDirectory);
				boolean fileDeleted = deleteFileOrEmptyDirectory(targetFileOrDirectory);

				if (!(directoryDeleted & fileDeleted)) {
					allFilesWereDeleted = false;
				}
			} else {
				if (!deleteFileOrEmptyDirectory(targetFileOrDirectory)) {
					allFilesWereDeleted = false;
				}
			}
		}

		return allFilesWereDeleted;
	}

	/**
	 * Delete a directory from the phone.
	 * 
	 * @param currentDirectory
	 *            target directory.
	 * @throws BlackHoleException
	 *             If the directory were not deleted.
	 */
	private boolean deleteDirectory(File currentDirectory) {
		File[] fileList = currentDirectory.listFiles();

		if (fileList != null) {
			for (File currentFile : fileList) {
				if (currentFile.isDirectory()) {
					deleteDirectory(currentFile);
					deleteFileOrEmptyDirectory(currentFile);
				} else {
					deleteFileOrEmptyDirectory(currentFile);
				}
			}

			return true;
		} else {
			logger.warn("The directory were not deleted: " + currentDirectory.getPath());

			return false;
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
	private boolean deleteFileOrEmptyDirectory(File file) {
		if (!file.delete()) {
			logger.warn("The file were not deleted: " + file.getPath());

			return false;
		}

		return true;
	}

}
