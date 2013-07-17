package ro.edu.ubb.blackholepcserver.bll.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.GetRunningProgramsBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.RunningProgramsBHMessage;
import ro.edu.ubb.blackhole.libs.coding.HashCoding;
import ro.edu.ubb.blackhole.libs.datastructures.Process;
import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholepcserver.bll.PhoneCommand;
import ro.edu.ubb.blackholepcserver.bll.processcontroller.ProcessController;
import ro.edu.ubb.blackholepcserver.bll.processcontroller.ProcessOperationAdapter;
import ro.edu.ubb.blackholepcserver.gui.windows.MainWindow;

/**
 * This command get all running programs on the current machine.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetRunningProgramsCommand implements PhoneCommand {
	
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(GetListOfFilesCommand.class);

	/**
	 * Request from the phone.
	 */
	private String request = null;

	/**
	 * The component which can return the running programs.
	 */
	private ProcessController processController = null;

	public GetRunningProgramsCommand(String request) {
		this.request = request;
		this.processController = new ProcessOperationAdapter();
	}

	/**
	 * @see #execute()
	 */
	@Override
	public String execute() {
		GetRunningProgramsBHMessage requestObj = (GetRunningProgramsBHMessage) JSonParser.loadBHMessage(
				this.request, GetRunningProgramsBHMessage.class);
		int requestMessageType = requestObj.getMessageType();

		String serverName = requestObj.getServerName();
		byte[] password = requestObj.getServerPassword();

		// Checking the server name
		if (!serverName.equals(MainWindow.serverName)) {
			int errCode = PCSErrorMessageInterpreter
					.getErrorCode(PCSErrorMessageInterpreter.PCS_WRONG_SERVERNAME);
			logger.info("Problem: " + errCode);
			return JSonParser.createSimpleResponse(errCode, requestMessageType);
		}

		try {
			// Checking the password
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

		// Getting the running programs.
		List<Process> runningProcesses = new ArrayList<Process>();
		try {
			runningProcesses = this.processController.getRunningProcesses();

			if (runningProcesses == null) {
				logger.warn("Do not support this operation system!");
			}
		} catch (IOException e) {
			logger.error("Can't get running prcesses! Exception: " + e.getMessage());
		}

		RunningProgramsBHMessage response = new RunningProgramsBHMessage(requestMessageType);
		response.setRunningProcesses(runningProcesses);

		return JSonParser.createBHMessage(response);
	}
}
