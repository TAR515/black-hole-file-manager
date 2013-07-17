package ro.edu.ubb.blackholepcserver.bll.commands;

import java.util.Arrays;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.DirectorySizeBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.GetListOfFilesBHMessage;
import ro.edu.ubb.blackhole.libs.coding.HashCoding;
import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholepcserver.bll.PhoneCommand;
import ro.edu.ubb.blackholepcserver.filemanager.FileManager;
import ro.edu.ubb.blackholepcserver.filemanager.WindowsFileManager;
import ro.edu.ubb.blackholepcserver.gui.windows.MainWindow;

/**
 * This command get the size of the given directory.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetDirectorySizeCommand implements PhoneCommand {
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(GetDirectorySizeCommand.class);

	/**
	 * Request from the phone
	 */
	private String request = null;

	/**
	 * File Explorer
	 */
	private FileManager fileExplorer = null;

	public GetDirectorySizeCommand(String request) {
		this.request = request;
		this.fileExplorer = new WindowsFileManager();
	}

	/**
	 * @see #execute()
	 */
	@Override
	public String execute() {
		GetListOfFilesBHMessage requestObj = (GetListOfFilesBHMessage) JSonParser.loadBHMessage(this.request,
				GetListOfFilesBHMessage.class);
		int requestMessageType = requestObj.getMessageType();

		String serverName = requestObj.getServerName();
		byte[] password = requestObj.getServerPassword();

		// Checking the server name.
		if (!serverName.equals(MainWindow.serverName)) {
			int errCode = PCSErrorMessageInterpreter
					.getErrorCode(PCSErrorMessageInterpreter.PCS_WRONG_SERVERNAME);
			logger.info("Problem: " + errCode);
			return JSonParser.createSimpleResponse(errCode, requestMessageType);
		}

		try {
			// Checking the password.
			if (!Arrays.equals(HashCoding.hashString(MainWindow.password), password)) {
				int errCode = PCSErrorMessageInterpreter
						.getErrorCode(PCSErrorMessageInterpreter.PCS_WRONG_PASSWORD);
				logger.info("Problem: " + errCode);
				return JSonParser.createSimpleResponse(errCode, requestMessageType);
			}
		} catch (Exception e) {
			logger.warn("HashCoding error: " + e.getMessage());
			int errCode = PCSErrorMessageInterpreter
					.getErrorCode(PCSErrorMessageInterpreter.PCS_WRONG_PASSWORD);
			return JSonParser.createSimpleResponse(errCode, requestMessageType);
		}
		
		// Getting the directory size.
		long directorySize = this.fileExplorer.getDirectorySize(requestObj.getPath());

		DirectorySizeBHMessage response = new DirectorySizeBHMessage(requestMessageType);
		response.setDirectorySize(directorySize);

		return JSonParser.createBHMessage(response);
	}
}
