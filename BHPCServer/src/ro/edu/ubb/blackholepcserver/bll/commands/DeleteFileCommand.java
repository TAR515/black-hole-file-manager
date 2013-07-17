package ro.edu.ubb.blackholepcserver.bll.commands;

import java.util.Arrays;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.DeleteFileBHMessage;
import ro.edu.ubb.blackhole.libs.coding.HashCoding;
import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholepcserver.bll.PhoneCommand;
import ro.edu.ubb.blackholepcserver.filemanager.FileManager;
import ro.edu.ubb.blackholepcserver.filemanager.WindowsFileManager;
import ro.edu.ubb.blackholepcserver.gui.windows.MainWindow;

/**
 * This command deletes the given files from the local machine.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DeleteFileCommand implements PhoneCommand {
	
	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger(DeleteFileCommand.class);

	/**
	 * Request from the phone which
	 */
	private String request = null;

	/**
	 * File Explorer
	 */
	private FileManager fileExplorer = null;

	public DeleteFileCommand(String request) {
		this.request = request;
		this.fileExplorer = new WindowsFileManager();
	}

	/**
	 * @see #execute()
	 */
	@Override
	public String execute() {
		DeleteFileBHMessage requestObj = (DeleteFileBHMessage) JSonParser.loadBHMessage(this.request,
				DeleteFileBHMessage.class);
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
			// Checking the server password.
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

		// Deleting the given files.
		boolean fileDeleted = this.fileExplorer.deleteFiles(requestObj.getPaths());
		int responseCode = 1;
		if (!fileDeleted) {
			responseCode = PCSErrorMessageInterpreter
					.getErrorCode(PCSErrorMessageInterpreter.PCS_FILE_NOT_DELETED);
		}

		return JSonParser.createSimpleResponse(responseCode, requestMessageType);
	}
}
