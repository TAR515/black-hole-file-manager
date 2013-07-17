package ro.edu.ubb.blackhole.dal;

import java.io.File;
import java.util.List;

import ro.edu.ubb.blackhole.dal.localdatabase.AndroidDatabaseService;
import ro.edu.ubb.blackhole.dal.localdatabase.SQLiteDatabaseProvider;
import ro.edu.ubb.blackhole.dal.localfilemanager.AndroidFileProvider;
import ro.edu.ubb.blackhole.dal.localfilemanager.LocalFileService;
import ro.edu.ubb.blackhole.dal.pcfilemanager.PCFileProvider;
import ro.edu.ubb.blackhole.dal.pcfilemanager.PCFileService;
import ro.edu.ubb.blackhole.datastructures.BlackHoleException;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.Server;
import ro.edu.ubb.blackhole.datastructures.guirequest.GuiRequest;
import ro.edu.ubb.blackhole.libs.datastructures.Process;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple2;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple3;
import android.content.Context;
import android.database.SQLException;

/**
 * This component instantiates the following subcomponents: {@link AndroidDatabaseService}, {@link LocalFileService},
 * {@link PCFileService}. After the instantiates the object transfers the request to the proper subcomponent to process it.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DataProvider implements DataService {

	/**
	 * Interface of the Android Data Access Layer component.
	 */
	private static AndroidDatabaseService androidDatabaseService = null;

	/**
	 * Interface of the Local File Service component.
	 */
	private static LocalFileService localFileService = null;

	/**
	 * Interface of the PC File Service component.
	 */
	private static PCFileService pcFileService = null;

	public DataProvider(Context applicationContext) {
		DataProvider.androidDatabaseService = new SQLiteDatabaseProvider(applicationContext);
		DataProvider.localFileService = new AndroidFileProvider(applicationContext);
		DataProvider.pcFileService = new PCFileProvider(applicationContext);
	}

	/**
	 * @see #insertNewServer(GuiRequest)
	 */
	@Override
	public long insertNewServer(Server server) throws SQLException {
		return DataProvider.androidDatabaseService.insertNewServer(server);
	}

	/**
	 * @see #getAllServerNames(GuiRequest)
	 */
	@Override
	public List<String> getAllServerNames() {
		return DataProvider.androidDatabaseService.getAllServerNames();
	}

	/**
	 * @see #deleteServer(String)
	 */
	@Override
	public boolean deleteServer(String serverName) {
		return DataProvider.androidDatabaseService.deleteServer(serverName);
	}

	/**
	 * @see #getServerInformations(GuiRequest)
	 */
	@Override
	public Server getServerInformations(String serverName) {
		return DataProvider.androidDatabaseService.getServerInformations(serverName);
	}

	/**
	 * @see #updateServer(GuiRequest)
	 */
	@Override
	public boolean updateServer(String oldServerName, Server newServer) {
		return DataProvider.androidDatabaseService.updateServer(oldServerName, newServer);
	}

	/**
	 * @throws BlackHoleException
	 * @see #getFilesAndroid(GuiRequest)
	 */
	@Override
	public Tuple2<List<FileItem>, String> getFilesAndroid(String path) throws BlackHoleException {
		return DataProvider.localFileService.getFilesAndroid(path);
	}

	/**
	 * @see #getDirectorySizeAndroid(GuiRequest)
	 */
	@Override
	public long getDirectorySizeAndroid(String path) {
		return DataProvider.localFileService.getDirectorySizeAndroid(path);
	}

	/**
	 * @see #stopExecution()
	 */
	@Override
	public void stopExecution() {
		DataProvider.localFileService.stopExecution();
	}

	/**
	 * @see #deleteFileAndroid(GuiRequest)
	 */
	@Override
	public void deleteFilesAndroid(List<String> paths) throws BlackHoleException {
		DataProvider.localFileService.deleteFilesAndroid(paths);
	}

	/**
	 * @see #renameDirectoryAndroid(String, String)
	 */
	@Override
	public boolean renameDirectoryAndroid(String path, String newName) {
		return DataProvider.localFileService.renameDirectoryAndroid(path, newName);
	}

	/**
	 * @see #getPCIpAndPort(String, String)
	 */
	@Override
	public Tuple3<String, Integer, Integer> getPCIpAndPort(String serverName, String password) {
		return DataProvider.pcFileService.getPCIpAndPort(serverName, password);
	}

	/**
	 * @see #getListOfFiles(String, String, String)
	 */
	@Override
	public Tuple2<List<FileItem>, Integer> getListOfFiles(String serverName, String path) {
		return DataProvider.pcFileService.getListOfFiles(serverName, path);
	}

	/**
	 * @see #getDirectorySize(String, String, String)
	 */
	@Override
	public long getDirectorySize(String serverName, String path) {
		return DataProvider.pcFileService.getDirectorySize(serverName, path);
	}

	/**
	 * @see #deleteFile(String, String, String)
	 */
	@Override
	public boolean deleteFilesPC(String serverName, List<String> paths) {
		return DataProvider.pcFileService.deleteFilesPC(serverName, paths);
	}

	/**
	 * @see #fileExecuteAndroid(File)
	 */
	@Override
	public boolean fileExecuteAndroid(File file) {
		return DataProvider.localFileService.fileExecuteAndroid(file);
	}

	/**
	 * @see #getHomeAndroid()
	 */
	@Override
	public List<FileItem> getHomeAndroid() {
		return DataProvider.localFileService.getHomeAndroid();
	}

	/**
	 * @see #getParentDirectoryFilesAndroid(String)
	 */
	@Override
	public Tuple2<List<FileItem>, String> getParentDirectoryFilesAndroid(String path) {
		// TODO Implement this method.
		return null;
	}

	/**
	 * @see #downloadFileFromPC(String, String, List)
	 */
	@Override
	public boolean downloadFileFromPC(String serverName, String destinationPath, List<FileItem> sources) {
		return DataProvider.pcFileService.downloadFileFromPC(serverName, destinationPath, sources);
	}

	/**
	 * @see #uploadFileToPC(String, String, List)
	 */
	@Override
	public boolean uploadFileToPC(String serverName, String destinationPath, List<FileItem> sources) {
		return DataProvider.pcFileService.uploadFileToPC(serverName, destinationPath, sources);
	}

	/**
	 * @see #registerDownloadManager(Context)
	 */
	@Override
	public void registerDownloadManager(Context applicationContext) {
		DataProvider.pcFileService.registerDownloadManager(applicationContext);
	}

	/**
	 * @see #unregisterDownloadManager(Context)
	 */
	@Override
	public void unregisterDownloadManager(Context applicationContext) {
		DataProvider.pcFileService.unregisterDownloadManager(applicationContext);
	}

	/**
	 * @see #getRunningPrograms(String)
	 */
	@Override
	public List<Process> getRunningPrograms(String serverName) {
		return DataProvider.pcFileService.getRunningPrograms(serverName);
	}

	/**
	 * @see #startProgramsOnPC(String, List)
	 */
	@Override
	public boolean startProgramsOnPC(String serverName, List<String> programPaths) {
		return DataProvider.pcFileService.startProgramsOnPC(serverName, programPaths);
	}

	/**
	 * @see #stopProgramsOnPC(String, List)
	 */
	@Override
	public boolean stopProgramsOnPC(String serverName, List<String> processNames) {
		return DataProvider.pcFileService.stopProgramsOnPC(serverName, processNames);
	}

}
