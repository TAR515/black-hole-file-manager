package ro.edu.ubb.blackhole.dal.pcfilemanager;

import java.util.List;

import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.libs.datastructures.Process;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple2;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple3;
import android.content.Context;

/**
 * This is the interface of the PC File Service component. This component realize the basic file manager and process manager
 * operations on the PC.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface PCFileService {

	/**
	 * Get the given PC Server's IP and Port.
	 * 
	 * @param request
	 * @return The server IP, the server Port and an error code.
	 */
	public Tuple3<String, Integer, Integer> getPCIpAndPort(String serverName, String password);

	/**
	 * Get all files contained into the given directory on the PC.
	 * 
	 * @param serverName
	 *            Name of the Server.
	 * @param path
	 *            Path of the directory.
	 * @return All files contained the given directory.
	 */
	public Tuple2<List<FileItem>, Integer> getListOfFiles(String serverName, String path);

	/**
	 * Get the given directory size on the PC.
	 * 
	 * @param serverName
	 *            Name of the Server.
	 * @param path
	 *            Path of the directory.
	 * @return Size of the directory.
	 */
	public long getDirectorySize(String serverName, String path);

	/**
	 * Delete the given files form the PC.
	 * 
	 * @param serverName
	 *            Name of the Server.
	 * @param paths
	 *            Path of the files.
	 * @return True - if all files were deleted successfully / False - otherwise.
	 */
	public boolean deleteFilesPC(String serverName, List<String> paths);

	/**
	 * Starts the downloading files from the PC.
	 * 
	 * @param serverName
	 *            Name of the server.
	 * @param destinationPath
	 *            The target folder where the files will be downloading.
	 * @param sources
	 *            The files which the system downloads.
	 * @return True - if all Files were downloaded successfully / False - otherwise.
	 */
	public boolean downloadFileFromPC(String serverName, String destinationPath, List<FileItem> sources);

	/**
	 * Start the uploading the given files to the Server.
	 * 
	 * @param serverName
	 *            Name of the Server.
	 * @param destinationPath
	 *            The target folder where the files will be uploding on the PC.
	 * @param sources
	 *            The files which the system uploads.
	 * @return True - if all Files were uploaded successfully / False - otherwise.
	 */
	public boolean uploadFileToPC(String serverName, String destinationPath, List<FileItem> sources);

	/**
	 * Register the Download Manager to the application context.
	 * 
	 * @param applicationContext
	 *            The context of the application.
	 */
	public void registerDownloadManager(Context applicationContext);

	/**
	 * Unregister the Download Manager from the application context.
	 * 
	 * @param applicationContext
	 *            The context of the application.
	 */
	public void unregisterDownloadManager(Context applicationContext);

	/**
	 * Returns all running programs(processes) on the server computer.
	 * 
	 * @param serverName
	 *            Name of the server.
	 * @return All running programs on the server computer or an empty list if the operating system is not supported.
	 */
	public List<Process> getRunningPrograms(String serverName);

	/**
	 * Starts the given programs on the PC computer. All programs will be starting with its default application.
	 * 
	 * @param serverName
	 *            Name of the server.
	 * @param programPaths
	 *            Paths of the programs or files.
	 * @return True - If all programs were started successfully.
	 */
	public boolean startProgramsOnPC(String serverName, List<String> programPaths);

	/**
	 * Stops the given processes on the PC computer.
	 * 
	 * @param serverName
	 *            Name of the server.
	 * @param processNames
	 *            Names of the processes.
	 * @return True - If all processes were stopped / False - If one or more process didn't stopped.
	 */
	public boolean stopProgramsOnPC(String serverName, List<String> processNames);

}
