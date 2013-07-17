package ro.edu.ubb.blackhole.dal.pcfileservice;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.dal.androiddatabase.AndroidDatabaseService;
import ro.edu.ubb.blackhole.dal.androiddatabase.SQLiteDatabaseProvider;
import ro.edu.ubb.blackhole.dal.pcfileservice.filetransfermanagers.BHDownloadManager;
import ro.edu.ubb.blackhole.dal.pcfileservice.filetransfermanagers.BHUploadManager;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.Server;
import ro.edu.ubb.blackhole.libs.bhmessage.BHMessageType;
import ro.edu.ubb.blackhole.libs.bhmessage.DeleteFileBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.DirectorySizeBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.GetDirectorySizeBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.GetIPAndPortBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.GetListOfFilesBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.GetRunningProgramsBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.IpAndPortBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.ListOfFilesBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.PCServerCheckBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.RunningProgramsBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.SimpleBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.StartProgramsBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.StopRunningProgramsBHMessage;
import ro.edu.ubb.blackhole.libs.coding.HashCoding;
import ro.edu.ubb.blackhole.libs.datastructures.Process;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple2;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple3;
import ro.edu.ubb.blackhole.libs.errmsg.AErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import android.content.Context;
import android.net.Uri;

//TODO Javitani a hardcoded reszeket! Ezt egyszerre kell az osszes komponensen!
public class PCFileProvider implements PCFileService {

	private static final String MAIN_SERVER_URL = "http://192.168.1.100:8080/BlackHoleMainServer/pcservlet";

	private AndroidDatabaseService dbService = null;

	private int numberOfProbes = 0;

	private Context applicationContext = null;

	public PCFileProvider(Context applicationContext) {
		this.applicationContext = applicationContext;
		this.dbService = new SQLiteDatabaseProvider(applicationContext);
	}

	/**
	 * @see #getPCIpAndPort(String, String)
	 */
	@Override
	public Tuple3<String, Integer, Integer> getPCIpAndPort(String serverName, String password) {
		GetIPAndPortBHMessage ipAndPortDataStorage = new GetIPAndPortBHMessage();

		try {
			ipAndPortDataStorage.setServerName(serverName);
			ipAndPortDataStorage.setServerPassword(HashCoding.hashString(password));

			String xmlMessage = JSonParser.createBHMessage(ipAndPortDataStorage);
			String xmlResponse = sendMessage(PCFileProvider.MAIN_SERVER_URL, xmlMessage);

			int errCode = responseCheck(true, BHMessageType.BH_PC_SERVER_IP_AND_PORT_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				return new Tuple3<String, Integer, Integer>(null, -1, errCode);
			}

			IpAndPortBHMessage responseObject = (IpAndPortBHMessage) JSonParser.loadBHMessage(xmlResponse,
					IpAndPortBHMessage.class);
			String ip = responseObject.getServerIP();
			int port = responseObject.getServerPort();

			// Save the IP and port to the Android database.
			saveServerIPAndPort(serverName, ip, port);

			return new Tuple3<String, Integer, Integer>(ip, port, errCode);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("getPCIpAndPort(String, String) -> Exception: " + e.getMessage());

			int errCode = AErrorMessageInterpreter
					.getErrorCode(AErrorMessageInterpreter.A_INVALID_INPUT_TYPE);
			return new Tuple3<String, Integer, Integer>(null, -1, errCode);
		}
	}

	@Override
	public Tuple2<List<FileItem>, Integer> getListOfFiles(String serverName, String path) {
		Server serverInf = this.dbService.getServerInformations(serverName);
		String password = serverInf.getPassword();

		GetListOfFilesBHMessage getListOfFilesBHMessage = new GetListOfFilesBHMessage();

		try {
			String pcServerURL = "http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/DataProviderServlet";

			System.out.println("getListOfFiles URL: " + pcServerURL);

			getListOfFilesBHMessage.setServerName(serverName);
			getListOfFilesBHMessage.setServerPassword(HashCoding.hashString(password));
			getListOfFilesBHMessage.setPath(path);

			String xmlMessage = JSonParser.createBHMessage(getListOfFilesBHMessage);
			String xmlResponse = sendMessage(pcServerURL, xmlMessage);

			int errCode = responseCheck(false, BHMessageType.BH_LIST_OF_FILES_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				if (errCode == AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE)
						&& this.numberOfProbes++ == 0) { // PC Server is offline or it have a new IP.
					// Trying with the new IP.
					getPCIpAndPort(serverName, password);
					return getListOfFiles(serverName, path);
				} else {
					return new Tuple2<List<FileItem>, Integer>(null, errCode);
				}
			}

			ListOfFilesBHMessage listOfFilesBHMessage = (ListOfFilesBHMessage) JSonParser.loadBHMessage(
					xmlResponse, ListOfFilesBHMessage.class);
			List<ro.edu.ubb.blackhole.libs.datastructures.FileItem> returnedFiles = listOfFilesBHMessage
					.getListOfFiles();
			List<FileItem> androidFiles = new ArrayList<FileItem>();

			for (ro.edu.ubb.blackhole.libs.datastructures.FileItem currentFile : returnedFiles) {
				FileItem newAndroidFileItem = new FileItem(currentFile.getFilePath(),
						currentFile.isThisFolder());
				newAndroidFileItem.setFileName(currentFile.getFileName());
				newAndroidFileItem.setFileSize(currentFile.getFileSize());
				newAndroidFileItem.setLastModifiedDate(currentFile.getLastModifiedDate());
				newAndroidFileItem.setFilePermissions(currentFile.getFilePermissions());

				androidFiles.add(newAndroidFileItem);
			}

			return new Tuple2<List<FileItem>, Integer>(androidFiles, errCode);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("getPCIpAndPort(String, String) -> Exception: " + e.getMessage());

			int errCode = AErrorMessageInterpreter
					.getErrorCode(AErrorMessageInterpreter.A_INVALID_INPUT_TYPE);
			return new Tuple2<List<FileItem>, Integer>(null, errCode);
		}
	}

	@Override
	public long getDirectorySize(String serverName, String path) {
		Server serverInf = this.dbService.getServerInformations(serverName);
		String password = serverInf.getPassword();

		GetDirectorySizeBHMessage getDirectorySizeBHMessage = new GetDirectorySizeBHMessage();

		try {
			String pcServerURL = "http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/DataProviderServlet";

			getDirectorySizeBHMessage.setServerName(serverName);
			getDirectorySizeBHMessage.setServerPassword(HashCoding.hashString(password));
			getDirectorySizeBHMessage.setPath(path);

			String xmlMessage = JSonParser.createBHMessage(getDirectorySizeBHMessage);
			String xmlResponse = sendMessage(pcServerURL, xmlMessage);

			int errCode = responseCheck(false, BHMessageType.BH_DIRECTORY_SIZE_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				if (errCode == AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE)
						&& this.numberOfProbes++ == 0) { // PC Server is offline or it have a new IP.
					// Trying with the new IP.
					getPCIpAndPort(serverName, password);
					return getDirectorySize(serverName, path);
				} else {
					return 0;
				}
			}

			DirectorySizeBHMessage directorySizeBHMessage = (DirectorySizeBHMessage) JSonParser
					.loadBHMessage(xmlResponse, DirectorySizeBHMessage.class);

			return directorySizeBHMessage.getDirectorySize();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("getDirectorySize(String, String, String) -> Exception: " + e.getMessage());

			return 0;
		}
	}

	@Override
	public boolean deleteFilesPC(String serverName, List<String> paths) {
		Server serverInf = this.dbService.getServerInformations(serverName);
		String password = serverInf.getPassword();

		DeleteFileBHMessage deleteFileBHMessage = new DeleteFileBHMessage();

		try {
			String pcServerURL = "http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/DataProviderServlet";

			deleteFileBHMessage.setServerName(serverName);
			deleteFileBHMessage.setServerPassword(HashCoding.hashString(password));
			deleteFileBHMessage.setPaths(paths);

			String xmlMessage = JSonParser.createBHMessage(deleteFileBHMessage);
			String xmlResponse = sendMessage(pcServerURL, xmlMessage);

			int errCode = responseCheck(false, BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				if (errCode == AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE)
						&& this.numberOfProbes++ == 0) { // PC Server is offline or it have a new IP.
					// Trying with the new IP.
					getPCIpAndPort(serverName, password);
					return deleteFilesPC(serverName, paths);
				} else {
					return false;
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("deleteFile(String, String, String) -> Exception: " + e.getMessage());

			return false;
		}
	}

	private String sendMessage(String URL, String xmlMessage) {
		ExecutorService executor = Executors.newCachedThreadPool();
		System.out.println("URL: " + URL + "XML: " + xmlMessage);

		try {
			ResponseStringContainer response = new ResponseStringContainer("");

			Future<?> f1 = executor.submit(new ServerCommunicator(URL, xmlMessage, response));
			f1.get(15, TimeUnit.SECONDS);

			System.out.println("Response: " + response.getResponse());
			return response.getResponse();
		} catch (Exception e) {
			Log.w(".sendMessage(String, String) - Exception: " + e.getMessage());

			return null;
		} finally {
			executor.shutdown();
		}
	}

	private boolean saveServerIPAndPort(String serverName, String ip, int port) {
		if (ip != null && port > 0) { // Save the current server IP and Port to the Android Database!
			Server server = this.dbService.getServerInformations(serverName);
			server.setIp(ip);
			server.setPort(port);

			return this.dbService.updateServer(serverName, server);
		}

		return false;
	}

	// TODO check this if the response is a random string.
	/**
	 * Check the response.
	 * 
	 * @param commandID
	 *            The request command ID.
	 * @param responseCommandID
	 *            The response command ID.
	 * @return 1 if the response were correct, error Code otherwise.
	 */
	private int responseCheck(boolean fromMainServer, int responseCommandID, String response) {
		if (response == null) { // No response from the server.
			if (fromMainServer) {
				return AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_MAIN_SERVER_IS_OFFLINE);
			} else {
				return AErrorMessageInterpreter.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE);
			}
		}

		int messageType = JSonParser.getMessageType(response);
		if (messageType == responseCommandID) {
			return 1;
		}

		if (messageType == BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE) { // This is a simple Response. (Error)
			SimpleBHMessage errorResponse = (SimpleBHMessage) JSonParser.loadBHMessage(response,
					SimpleBHMessage.class);
			return errorResponse.getResponseCode();
		}

		if (messageType == Integer.MIN_VALUE || messageType != responseCommandID) { // Not BH Response.
			return AErrorMessageInterpreter.getErrorCode(AErrorMessageInterpreter.A_INVALID_RESPONSE);
		}

		return 1;
	}

	@Override
	public boolean downloadFileFromPC(String serverName, String destinationPath, List<FileItem> sources) {
		Server serverInf = this.dbService.getServerInformations(serverName);
		String password = serverInf.getPassword();

		PCServerCheckBHMessage pcServerCheckBHMessage = new PCServerCheckBHMessage();

		try {
			String pcServerURL = "http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/DataProviderServlet";
			pcServerCheckBHMessage.setServerName(serverName);
			pcServerCheckBHMessage.setServerPassword(HashCoding.hashString(password));

			String xmlMessage = JSonParser.createBHMessage(pcServerCheckBHMessage);
			String xmlResponse = sendMessage(pcServerURL, xmlMessage);

			int errCode = responseCheck(false, BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				if (errCode == AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE)
						&& this.numberOfProbes++ == 0) { // PC Server is offline or it have a new IP.
					// Trying with the new IP.
					getPCIpAndPort(serverName, password);
					return downloadFileFromPC(serverName, destinationPath, sources);
				} else {
					return false;
				}
			}

			Uri serverUri = Uri.parse("http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/FileUploadServlet");
			BHDownloadManager downloadManager = new BHDownloadManager(this.applicationContext);

			return downloadManager.startDownload(serverUri, destinationPath, sources);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("downloadFileFromPC(String, String, List<FileItem>) -> Exception: " + e.getMessage());

			return false;
		}
	}

	@Override
	public boolean uploadFileToPC(String serverName, String destinationPath, List<FileItem> sources) {
		Server serverInf = this.dbService.getServerInformations(serverName);
		String password = serverInf.getPassword();

		PCServerCheckBHMessage pcServerCheckBHMessage = new PCServerCheckBHMessage();

		try {
			// String pcServerURL = "http://188.24.87.124:8080/FileUpload/uploadFile";
			pcServerCheckBHMessage.setServerName(serverName);
			pcServerCheckBHMessage.setServerPassword(HashCoding.hashString(password));

			String xmlMessage = JSonParser.createBHMessage(pcServerCheckBHMessage);
			String xmlResponse = sendMessage("http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/DataProviderServlet", xmlMessage);

			int errCode = responseCheck(false, BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				if (errCode == AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE)
						&& this.numberOfProbes++ == 0) { // PC Server is offline or it have a new IP.
					// Trying with the new IP.
					getPCIpAndPort(serverName, password);
					return uploadFileToPC(serverName, destinationPath, sources);
				} else {
					return false;
				}
			}

			URL serverURL = new URL("http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/FileDownloadServlet");

			BHUploadManager uploadManager = new BHUploadManager(this.applicationContext);

			return uploadManager.startUploading(serverURL, destinationPath, sources);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("uploadFileToPC(String, String, List<FileItem>) -> Exception: " + e.getMessage());

			return false;
		}
	}

	@Override
	public List<Process> getRunningPrograms(String serverName) {
		Server serverInf = this.dbService.getServerInformations(serverName);
		String password = serverInf.getPassword();

		GetRunningProgramsBHMessage getRunningProgramsBHMessage = new GetRunningProgramsBHMessage();

		try {
			String pcServerURL = "http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/DataProviderServlet";

			getRunningProgramsBHMessage.setServerName(serverName);
			getRunningProgramsBHMessage.setServerPassword(HashCoding.hashString(password));

			String xmlMessage = JSonParser.createBHMessage(getRunningProgramsBHMessage);
			String xmlResponse = sendMessage(pcServerURL, xmlMessage);

			int errCode = responseCheck(false, BHMessageType.BH_RUNNING_PROGRAMS_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				if (errCode == AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE)
						&& this.numberOfProbes++ == 0) { // PC Server is offline or it have a new IP.
					// Trying with the new IP.
					getPCIpAndPort(serverName, password);
					return getRunningPrograms(serverName);
				} else {
					return new ArrayList<Process>(); // Returns an empty list.
				}
			}

			RunningProgramsBHMessage runningProgramsBHMessage = (RunningProgramsBHMessage) JSonParser
					.loadBHMessage(xmlResponse, RunningProgramsBHMessage.class);

			return runningProgramsBHMessage.getRunningProcesses();
		} catch (Exception e) {
			int errCode = AErrorMessageInterpreter
					.getErrorCode(AErrorMessageInterpreter.A_INVALID_INPUT_TYPE);

			Log.e("ErrorCode: " + errCode + "Exception: " + e.getMessage());
			return new ArrayList<Process>();
		}
	}

	/**
	 * @see #startProgramsOnPC(String, List)
	 */
	@Override
	public boolean startProgramsOnPC(String serverName, List<String> programPaths) {
		Server serverInf = this.dbService.getServerInformations(serverName);
		String password = serverInf.getPassword();

		StartProgramsBHMessage startProgramsBHMessage = new StartProgramsBHMessage();

		try {
			String pcServerURL = "http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/DataProviderServlet";

			startProgramsBHMessage.setServerName(serverName);
			startProgramsBHMessage.setServerPassword(HashCoding.hashString(password));
			startProgramsBHMessage.setPaths(programPaths);

			String xmlMessage = JSonParser.createBHMessage(startProgramsBHMessage);
			String xmlResponse = sendMessage(pcServerURL, xmlMessage);

			int errCode = responseCheck(false, BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				if (errCode == AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE)
						&& this.numberOfProbes++ == 0) { // PC Server is offline or it have a new IP.
					// Trying with the new IP.
					getPCIpAndPort(serverName, password);
					return startProgramsOnPC(serverName, programPaths);
				} else {
					return false;
				}
			}

			SimpleBHMessage simpleBHMessage = (SimpleBHMessage) JSonParser.loadBHMessage(xmlResponse,
					SimpleBHMessage.class);

			return (simpleBHMessage.getResponseCode() == 1) ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(e.getMessage());

			return false;
		}
	}

	/**
	 * @see #stopProgramsOnPC(String, List)
	 */
	@Override
	public boolean stopProgramsOnPC(String serverName, List<String> processNames) {
		Server serverInf = this.dbService.getServerInformations(serverName);
		String password = serverInf.getPassword();

		StopRunningProgramsBHMessage stopRunningProgramsBHMessage = new StopRunningProgramsBHMessage();

		try {
			String pcServerURL = "http://" + serverInf.getIp() + ":" + serverInf.getPort()
					+ "/DataProviderServlet";

			stopRunningProgramsBHMessage.setServerName(serverName);
			stopRunningProgramsBHMessage.setServerPassword(HashCoding.hashString(password));
			stopRunningProgramsBHMessage.setProcessNames(processNames);

			String xmlMessage = JSonParser.createBHMessage(stopRunningProgramsBHMessage);
			String xmlResponse = sendMessage(pcServerURL, xmlMessage);

			int errCode = responseCheck(false, BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE, xmlResponse);
			if (errCode != 1) { // If the response were not correct or it were an error message.
				if (errCode == AErrorMessageInterpreter
						.getErrorCode(AErrorMessageInterpreter.A_PC_SERVER_IS_OFFLINE)
						&& this.numberOfProbes++ == 0) { // PC Server is offline or it have a new IP.
					// Trying with the new IP.
					getPCIpAndPort(serverName, password);
					return stopProgramsOnPC(serverName, processNames);
				} else {
					return false;
				}
			}

			SimpleBHMessage simpleBHMessage = (SimpleBHMessage) JSonParser.loadBHMessage(xmlResponse,
					SimpleBHMessage.class);

			return (simpleBHMessage.getResponseCode() == 1) ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(e.getMessage());

			return false;
		}
	}

	@Override
	public void registerDownloadManager(Context applicationContext) {
		BHDownloadManager.registerReceivers(applicationContext);

	}

	@Override
	public void unregisterDownloadManager(Context applicationContext) {
		BHDownloadManager.unregisterReceivers(applicationContext);
	}

}
