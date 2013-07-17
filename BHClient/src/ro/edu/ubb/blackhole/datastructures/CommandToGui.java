package ro.edu.ubb.blackhole.datastructures;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.libs.datastructures.Process;

/**
 * This data structure is used when the Data Access Layer returns and give a command to the User Interface.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class CommandToGui {

	/**
	 * Command ID. With use of this variable the User Interface can identify the type of the command.
	 */
	private Integer commandID = null;

	/**
	 * Contains server names.
	 */
	private List<String> allServerNames = null;

	/**
	 * All informations about a server.
	 */
	private Server server = null;

	/**
	 * List of files.
	 */
	private List<FileItem> files = null;

	/**
	 * A concrete file or folder path.
	 */
	private String path = null;

	/**
	 * Size of a directory.
	 */
	private long directorySize = 0;

	/**
	 * {@link BlackHoleException} contains the exception if it were occurred.
	 */
	private BlackHoleException blackHoleException = null;

	/**
	 * This variable indicates if the User Interface request were executed correctly or not. It may contain the error message if
	 * the request were not executed correct.
	 */
	private int simpleResponse = 1;

	/**
	 * IP address.
	 */
	private String IP = null;

	/**
	 * Port.
	 */
	private int Port = -1;

	/**
	 * This variable indicates if the file were deleted or not.
	 */
	private boolean fileDeleted = false;

	/**
	 * List of Processes.
	 */
	private List<Process> processes = null;

	public CommandToGui(Integer commandID) {
		super();

		this.commandID = commandID;
		this.allServerNames = new ArrayList<String>();
		this.server = new Server();
		this.files = new ArrayList<FileItem>();
		this.path = "";
		this.IP = "";
		this.setProcesses(new ArrayList<Process>());
	}

	public Integer getCommandID() {
		return commandID;
	}

	public void setCommandID(Integer commandID) {
		this.commandID = commandID;
	}

	public List<String> getAllServerNames() {
		return allServerNames;
	}

	public void setAllServerNames(List<String> allServerNames) {
		this.allServerNames = allServerNames;
	}

	/**
	 * Append the allServerNames record.
	 * 
	 * @param newServerName
	 *            The new record which you want to insert.
	 */
	public void appendAllServerNames(String newServerName) {
		this.allServerNames.add(newServerName);
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void appendFiles(FileItem newFile) {
		this.files.add(newFile);
	}

	public List<FileItem> getFiles() {
		return files;
	}

	public void setFiles(List<FileItem> files) {
		this.files = files;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getDirectorySize() {
		return directorySize;
	}

	public void setDirectorySize(long directorySize) {
		this.directorySize = directorySize;
	}

	public BlackHoleException getBlackHoleException() {
		return blackHoleException;
	}

	public void setBlackHoleException(BlackHoleException blackHoleException) {
		this.blackHoleException = blackHoleException;
	}

	public int getSimpleResponse() {
		return simpleResponse;
	}

	public void setSimpleResponse(int simpleResponse) {
		this.simpleResponse = simpleResponse;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPort() {
		return Port;
	}

	public void setPort(int port) {
		Port = port;
	}

	public boolean isFileDeleted() {
		return fileDeleted;
	}

	public void setFileDeleted(boolean fileDeleted) {
		this.fileDeleted = fileDeleted;
	}

	public List<Process> getProcesses() {
		return processes;
	}

	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}

}
