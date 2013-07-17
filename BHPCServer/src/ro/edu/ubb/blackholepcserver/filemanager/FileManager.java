package ro.edu.ubb.blackholepcserver.filemanager;

import java.util.List;

import ro.edu.ubb.blackhole.libs.datastructures.FileItem;

/**
 * This is the interface of the {@link FileManager} component. This component provides the basic file manager operations.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface FileManager {

	/**
	 * Get all files which contains the given folder.
	 * @param path Path of the folder.
	 * @return All files which contains the given folder.
	 */
	public List<FileItem> getListOfFiles(String path);

	/**
	 * Returns the size of the given directory.
	 * @param path Directory path.
	 * @return Size of the directory.
	 */
	public long getDirectorySize(String path);

	/**
	 * Deletes the given files from the computer.
	 * @param paths Paths of the files which we would to delete.
	 * @return True - if all files were deleted / False - otherwise.
	 */
	public boolean deleteFiles(List<String> paths);

}
