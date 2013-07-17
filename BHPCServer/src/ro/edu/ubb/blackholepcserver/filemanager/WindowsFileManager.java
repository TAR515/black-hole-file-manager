package ro.edu.ubb.blackholepcserver.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.datastructures.FileItem;

/**
 * This class realize the basic file operations on Windows platform.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class WindowsFileManager implements FileManager {

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getLogger(WindowsFileManager.class);

	/**
	 * @see #getListOfFiles(String)
	 */
	@Override
	public List<FileItem> getListOfFiles(String path) {
		if ("".equals(path)) {
			return getRoots();
		} else {
			return getNonRoot(path);
		}
	}

	/**
	 * Get all files contained by the given non root folder.
	 * @param path Non root folder.
	 * @return All files contained by the given non root folder.
	 */
	private List<FileItem> getNonRoot(String path) {
		List<FileItem> listOfFiles = new ArrayList<FileItem>();

		try {
			File file = new File(path);

			for (File currentFile : file.listFiles()) {
				// Checking the file access rights.
				boolean isWriteable = currentFile.canWrite();
				boolean isReadable = currentFile.canRead();
				boolean isExecutable = currentFile.canExecute();

				// Getting the file informations.
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

	/**
	 * Get the root directories of the system.
	 * 
	 * @return Root directories.
	 */
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

	/**
	 * @see #getDirectorySize(String)
	 */
	@Override
	public long getDirectorySize(String path) {
		return getDirectorySize(new File(path), 0L);
	}

	/**
	 * Getting the size of the given directory.
	 * @param currentDirectory Target directory.
	 * @param size Current size of the directory. (recursive call)
	 * @return Size of the given directory.
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

	/**
	 * @see #deleteFiles(List)
	 */
	@Override
	public boolean deleteFiles(List<String> paths) {
		boolean allFilesWereDeleted = true;

		for (String currentPath : paths) {
			File targetFileOrDirectory = new File(currentPath);

			// In case of directory.
			if (targetFileOrDirectory.isDirectory()) {
				boolean directoryDeleted = deleteDirectory(targetFileOrDirectory);
				boolean fileDeleted = deleteFileOrEmptyDirectory(targetFileOrDirectory);

				if (!(directoryDeleted & fileDeleted)) {
					allFilesWereDeleted = false;
				}
			} else { // In case of file.
				if (!deleteFileOrEmptyDirectory(targetFileOrDirectory)) {
					allFilesWereDeleted = false;
				}
			}
		}

		return allFilesWereDeleted;
	}

	/**
	 * Delete the given directory.
	 * 
	 * @param currentDirectory
	 *            Target directory.
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
	 * Delete a file or an empty folder.
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
