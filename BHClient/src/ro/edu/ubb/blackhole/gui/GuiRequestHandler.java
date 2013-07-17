package ro.edu.ubb.blackhole.gui;

import java.util.List;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.dal.DataProvider;
import ro.edu.ubb.blackhole.dal.DataService;
import ro.edu.ubb.blackhole.datastructures.BlackHoleException;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.Server;
import ro.edu.ubb.blackhole.datastructures.guirequest.DeleteFileAndroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.DeleteFilePCGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.DeleteServerGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.DownloadFileGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.EditServerGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.FileExecuteAnroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetDirectorySizeAndroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetDirectorySizePCGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetIPAndPortGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetListOfFilesAndroidGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetListOfFilesPCGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetRunningProgramsGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetServerInfGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.GuiRequest;
import ro.edu.ubb.blackhole.datastructures.guirequest.InsertNewServerGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.RegisterDownloadManagerGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.StartProgramOnPCGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.StopProgramsOnPCGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.UnRegisterDownloadManagerGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.UploadFileGR;
import ro.edu.ubb.blackhole.libs.datastructures.Process;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple2;
import ro.edu.ubb.blackhole.libs.datastructures.Tuple3;
import android.app.Activity;
import android.os.AsyncTask;

/**
 * Creates a connection between the User Interface and the Data Access Layer. It also insure that the background processes will
 * not arrests the work of the User Interface.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GuiRequestHandler extends AsyncTask<Object, Void, CommandToGui> {

	/**
	 * Interface of the Data Access Layer.
	 */
	private DataService dataService = null;

	/**
	 * The caller activity.
	 */
	private Activity activity = null;

	/**
	 * A caller class which implements the {@link ResponseHandler} interface.
	 */
	private ResponseHandler responseHandler = null;

	/**
	 * True if we need loading screen while the background processes are working, false if not.
	 */
	private boolean needLoadingScreen = false;

	/**
	 * Loading Screen
	 */
	private LoadingScreen loadingScreen = null;

	/**
	 * Black Hole Exception
	 */
	private BlackHoleException blackHoleException = null;

	/**
	 * When we cancel the process this will become true. This attribute indicates that the process is stopped or not.
	 */
	private boolean isStopped = false;

	/**
	 * Use this constructor if you are calling this object from an Activity.
	 * 
	 * @param activity
	 *            Caller activity.
	 * @param needLoadingScreen
	 *            True - if we need loading screen while the background process is working.
	 */
	public GuiRequestHandler(Activity activity, boolean needLoadingScreen) {
		this.activity = activity;
		this.needLoadingScreen = needLoadingScreen;

		// If the caller activity implements the ResponseHandler interface, so it implements the responseHandler function
		// which handle the response for the requests and do something with it. e.g. Fill up a list view.
		if (activity instanceof ResponseHandler) {
			this.responseHandler = (ResponseHandler) activity;
		} else {
			Log.w("Caller activity do no implement ResponseHandler.");
		}
	}

	/**
	 * Use this constructor if you are calling this object from a class which isn't extends from Activity.
	 * 
	 * @param activity
	 *            Context of the Activity on which you want to visualize the changes.
	 * @param responseHandler
	 *            Caller object. (not the activity)
	 * @param needLoadingScreen
	 *            True - if we need loading screen while the background process is working.
	 */
	public GuiRequestHandler(Activity activity, ResponseHandler responseHandler, boolean needLoadingScreen) {
		this.activity = activity;
		this.needLoadingScreen = needLoadingScreen;
		this.responseHandler = responseHandler;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		// Creates a loading screen if we need it.
		if (this.needLoadingScreen) {
			this.loadingScreen = new LoadingScreen(this.activity, this);
		}
	}

	/**
	 * @see #onCancelled()
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();

		this.dataService.stopExecution();
		this.isStopped = true;
	}

	/**
	 * @see #doInBackground(Object...)
	 */
	@Override
	protected CommandToGui doInBackground(Object... params) {
		try {
			GuiRequest request = (GuiRequest) params[0];
			CommandToGui response = new CommandToGui(request.getCommandID());

			switch (request.getCommandID()) {
			case R.integer.COMMAND_GET_SERVER_NAMES:
				this.dataService = new DataProvider(request.getApplicationContext());
				response.setAllServerNames(this.dataService.getAllServerNames());
				return response;
			case R.integer.COMMAND_INSERT_NEW_SERVER:
				this.dataService = new DataProvider(request.getApplicationContext());
				InsertNewServerGR insertNewServerGR = (InsertNewServerGR) request;
				this.dataService.insertNewServer(insertNewServerGR.getNewServer());
				return new CommandToGui(request.getCommandID());
			case R.integer.COMMAND_DELETE_SERVER:
				this.dataService = new DataProvider(request.getApplicationContext());
				this.dataService.deleteServer(((DeleteServerGR) request).getServerName());
				return new CommandToGui(request.getCommandID());
			case R.integer.COMMAND_GET_SERVER_INFORMATIONS:
				this.dataService = new DataProvider(request.getApplicationContext());
				response.setServer(this.dataService.getServerInformations(((GetServerInfGR) request)
						.getServerName()));
				return response;
			case R.integer.COMMAND_EDIT_SERVER:
				this.dataService = new DataProvider(request.getApplicationContext());
				EditServerGR editServerGR = (EditServerGR) request;
				String oldServerName = editServerGR.getOldServerName();
				Server newServer = editServerGR.getNewServer();
				this.dataService.updateServer(oldServerName, newServer);

				return response;
			case R.integer.COMMAND_GET_LIST_OF_FILES_ANDROID:
				this.dataService = new DataProvider(request.getApplicationContext());
				Tuple2<List<FileItem>, String> res = this.dataService
						.getFilesAndroid(((GetListOfFilesAndroidGR) request).getPath());
				response.setFiles(res.t);
				response.setPath(res.u);
				return response;
			case R.integer.COMMAND_GET_DIRECTORY_SIZE_ANDROID:
				this.dataService = new DataProvider(request.getApplicationContext());
				response.setDirectorySize(this.dataService
						.getDirectorySizeAndroid(((GetDirectorySizeAndroidGR) request).getPath()));
				return response;
			case R.integer.COMMAND_FILE_EXECUTE_ANDROID:
				this.dataService = new DataProvider(request.getApplicationContext());
				boolean fileExecuted = this.dataService.fileExecuteAndroid(((FileExecuteAnroidGR) request)
						.getFile());
				if (fileExecuted) {
					response.setSimpleResponse(1);
				} else {
					response.setSimpleResponse(0);
				}

				return response;
			case R.integer.COMMAND_GET_HOME_ANDROID:
				this.dataService = new DataProvider(request.getApplicationContext());
				response.setFiles(this.dataService.getHomeAndroid());
				return response;
			case R.integer.COMMAND_DELETE_FILE_ANDROID:
				this.dataService = new DataProvider(request.getApplicationContext());
				this.dataService.deleteFilesAndroid(((DeleteFileAndroidGR) request).getPaths());
				return response;
			case R.integer.COMMAND_GET_IP_AND_PORT:
				this.dataService = new DataProvider(request.getApplicationContext());
				GetIPAndPortGR getIPAndPortGR = (GetIPAndPortGR) request;
				String serverName = getIPAndPortGR.getServerName();
				String password = getIPAndPortGR.getPassword();

				Tuple3<String, Integer, Integer> ipAndPortRes = this.dataService.getPCIpAndPort(serverName,
						password);
				if (ipAndPortRes.v == 1) { // Correct response.
					response.setIP(ipAndPortRes.t);
					response.setPort(ipAndPortRes.u);
					response.setSimpleResponse(ipAndPortRes.v);
					response.setServer(new Server(serverName, password));
				} else {
					response.setSimpleResponse(ipAndPortRes.v);
				}

				return response;
			case R.integer.COMMAND_GET_LIST_OF_FILES_PC:
				this.dataService = new DataProvider(request.getApplicationContext());
				GetListOfFilesPCGR getListOfFilesPCGR = (GetListOfFilesPCGR) request;
				serverName = getListOfFilesPCGR.getServerName();
				String path = getListOfFilesPCGR.getPath();

				Tuple2<List<FileItem>, Integer> listOfFiles = this.dataService.getListOfFiles(serverName,
						path);
				if (listOfFiles.u == 1) { // Correct response.
					response.setFiles(listOfFiles.t);
					response.setPath(getListOfFilesPCGR.getPath());
					response.setSimpleResponse(listOfFiles.u);
				} else {
					response.setSimpleResponse(listOfFiles.u);
				}

				return response;
			case R.integer.COMMAND_GET_DIRECTORY_SIZE_PC:
				this.dataService = new DataProvider(request.getApplicationContext());
				GetDirectorySizePCGR getDirectorySizePCGR = (GetDirectorySizePCGR) request;
				serverName = getDirectorySizePCGR.getServerName();
				path = getDirectorySizePCGR.getPath();

				long directorySize = this.dataService.getDirectorySize(serverName, path);
				response.setDirectorySize(directorySize);

				return response;
			case R.integer.COMMAND_DELETE_FILE_PC:
				this.dataService = new DataProvider(request.getApplicationContext());
				DeleteFilePCGR deleteFilePCGR = (DeleteFilePCGR) request;
				serverName = deleteFilePCGR.getServerName();
				List<String> paths = deleteFilePCGR.getPaths();

				boolean fileDeleted = this.dataService.deleteFilesPC(serverName, paths);
				response.setFileDeleted(fileDeleted);

				return response;

			case R.integer.COMMAND_DOWNLOAD_FILE:
				this.dataService = new DataProvider(request.getApplicationContext());
				DownloadFileGR downloadFileGR = (DownloadFileGR) request;
				serverName = downloadFileGR.getServerName();
				String destinationPath = downloadFileGR.getDestinationPath();
				List<FileItem> sources = downloadFileGR.getSources();

				boolean downloadStarted = this.dataService.downloadFileFromPC(serverName, destinationPath,
						sources);

				response.setSimpleResponse((downloadStarted) ? 1 : 0);

				return response;
			case R.integer.COMMAND_REGISTER_DOWNLOAD_MANAGER:
				this.dataService = new DataProvider(request.getApplicationContext());
				RegisterDownloadManagerGR registerDownloadManagerGR = (RegisterDownloadManagerGR) request;
				this.dataService.registerDownloadManager(registerDownloadManagerGR.getApplicationContext());

				return response;
			case R.integer.COMMAND_UNREGISTER_DOWNLOAD_MANAGER:
				this.dataService = new DataProvider(request.getApplicationContext());
				UnRegisterDownloadManagerGR unregisterDownloadManagerGR = (UnRegisterDownloadManagerGR) request;
				this.dataService.registerDownloadManager(unregisterDownloadManagerGR.getApplicationContext());

				return response;
			case R.integer.COMMAND_UPLOAD_FILE:
				this.dataService = new DataProvider(request.getApplicationContext());
				UploadFileGR uploadFileGR = (UploadFileGR) request;
				serverName = uploadFileGR.getServerName();
				destinationPath = uploadFileGR.getDestinationPath();
				sources = uploadFileGR.getSources();

				boolean uploadStarted = this.dataService.uploadFileToPC(serverName, destinationPath, sources);

				response.setSimpleResponse((uploadStarted) ? 1 : 0);

				return response;

			case R.integer.COMMAND_GET_RUNNING_PROGRAMS:
				this.dataService = new DataProvider(request.getApplicationContext());
				GetRunningProgramsGR getRunningProgramsGR = (GetRunningProgramsGR) request;
				serverName = getRunningProgramsGR.getServerName();

				List<Process> runningProcesses = this.dataService.getRunningPrograms(serverName);
				response.setProcesses(runningProcesses);

				return response;
			case R.integer.COMMAND_START_PROGRAMS:
				this.dataService = new DataProvider(request.getApplicationContext());
				StartProgramOnPCGR startProgramOnPCGR = (StartProgramOnPCGR) request;
				serverName = startProgramOnPCGR.getServerName();
				List<String> programPaths = startProgramOnPCGR.getProgramPaths();

				boolean programsStarted = this.dataService.startProgramsOnPC(serverName, programPaths);
				response.setSimpleResponse((programsStarted) ? 1 : 0);

				return response;
			case R.integer.COMMAND_STOP_PROGRAMS:
				this.dataService = new DataProvider(request.getApplicationContext());
				StopProgramsOnPCGR stopProgramsOnPCGR = (StopProgramsOnPCGR) request;
				serverName = stopProgramsOnPCGR.getServerName();
				List<String> processNames = stopProgramsOnPCGR.getProcessNames();

				boolean programsStopped = this.dataService.stopProgramsOnPC(serverName, processNames);
				response.setSimpleResponse((programsStopped) ? 1 : 0);

				return response;

			default:
				return new CommandToGui(request.getCommandID());
			}
		} catch (BlackHoleException e) {
			this.blackHoleException = e;
			return null;
		}
	}

	/**
	 * @see #onPostExecute(CommandToGui)
	 */
	@Override
	protected void onPostExecute(CommandToGui result) {
		super.onPostExecute(result);

		if (!this.isStopped) {
			// The caller Activity implements the ResponseHandler interface, so it can handle the response.
			if (this.responseHandler != null) {
				if (result != null) {
					this.responseHandler.responseHandler(result);
				} else { // result == null.
					if (this.blackHoleException != null) { // An exception were triggered.
						CommandToGui errorCommand = new CommandToGui(R.integer.BLACK_HOLE_EXCEPTION);
						errorCommand.setBlackHoleException(this.blackHoleException);
						this.responseHandler.responseHandler(errorCommand);
					}
				}
			}
		}

		// Dismiss the loading screen if it were created.
		if (this.needLoadingScreen) {
			this.loadingScreen.dismissDialog();
		}

	}

}
