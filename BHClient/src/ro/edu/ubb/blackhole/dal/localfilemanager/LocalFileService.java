package ro.edu.ubb.blackhole.dal.localfilemanager;

import java.io.File;
import java.util.List;

import ro.edu.ubb.blackhole.datastructures.BlackHoleException;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple2;

/**
 * This is the interface of the Local File Service component. This component provides the basic file managing operations for an
 * Android device.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface LocalFileService {

	/**
	 * Load all files and folders at the given path.
	 * 
	 * @param path
	 *            Target path.
	 * @return List of the files and the current path.
	 * @throws BlackHoleException
	 */
	public Tuple2<List<FileItem>, String> getFilesAndroid(String path) throws BlackHoleException;

	/**
	 * Load all files and folders containing the target path's parent directory.
	 * 
	 * @param path
	 *            Target path.
	 * @return List of the files and the parent directory path.
	 */
	public Tuple2<List<FileItem>, String> getParentDirectoryFilesAndroid(String path);

	/**
	 * Calculates the size of a directory. This method can be really slow if the target directory contains a lot of files. Returns
	 * 0 if the given path is not a directory.
	 * 
	 * @param path
	 *            Target directory path.
	 * @return The size of the target directory.
	 */
	public long getDirectorySizeAndroid(String path);

	/**
	 * Deletes the given files from the Android device.
	 * 
	 * @param paths
	 *            The file paths.
	 * @throws BlackHoleException
	 *             If some error were occurred.
	 */
	public void deleteFilesAndroid(List<String> paths) throws BlackHoleException;

	/**
	 * Rename a file or directory.
	 * 
	 * @param path
	 *            Target file/directory's path.
	 * @param newName
	 *            New file/directory name.
	 * @return True - if the rename were successful, false otherwise.
	 * @throws BlackHoleException
	 *             if the file/directory were not renamed.
	 */
	public boolean renameDirectoryAndroid(String path, String newName);

	/**
	 * Execute a file in Android system.
	 * 
	 * @param file
	 *            Target file.
	 * @return true - if the file were executed successfully / false - otherwise.
	 */
	public boolean fileExecuteAndroid(File file);

	/**
	 * Get the Android system default directories.
	 * 
	 * @return The Android system default directories.
	 */
	public List<FileItem> getHomeAndroid();

	/**
	 * Stops the execution of the current work.
	 */
	public void stopExecution();
}
