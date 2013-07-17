package ro.edu.ubb.blackhole.dal.pcfileservice;

import java.util.List;

import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.libs.datastructures.Process;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple2;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple3;
import android.content.Context;

public interface PCFileService {

	/**
	 * Get the given PC Server IP and Port.
	 * 
	 * @param request
	 * @return The server IP, the server Port and an error code.
	 */
	public Tuple3<String, Integer, Integer> getPCIpAndPort(String serverName, String password);

	public Tuple2<List<FileItem>, Integer> getListOfFiles(String serverName, String path);

	public long getDirectorySize(String serverName, String path);

	public boolean deleteFilesPC(String serverName, List<String> paths);

	public boolean downloadFileFromPC(String serverName, String destinationPath, List<FileItem> sources);

	public boolean uploadFileToPC(String serverName, String destinationPath, List<FileItem> sources);

	public void registerDownloadManager(Context applicationContext);

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
