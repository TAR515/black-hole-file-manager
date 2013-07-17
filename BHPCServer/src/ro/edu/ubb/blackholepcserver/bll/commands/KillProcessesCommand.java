package ro.edu.ubb.blackholepcserver.bll.commands;

import java.util.Arrays;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.StopRunningProgramsBHMessage;
import ro.edu.ubb.blackhole.libs.coding.HashCoding;
import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholepcserver.bll.PhoneCommand;
import ro.edu.ubb.blackholepcserver.bll.processcontroller.ProcessController;
import ro.edu.ubb.blackholepcserver.bll.processcontroller.ProcessOperationAdapter;
import ro.edu.ubb.blackholepcserver.gui.windows.MainWindow;

/**
 * This command kills the given process on the current machine.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class KillProcessesCommand implements PhoneCommand {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(GetListOfFilesCommand.class);

	/**
	 * Request from the phone.
	 */
	private String request = null;

	/**
	 * The component which kill the given process.
	 */
	private ProcessController processController = null;

	public KillProcessesCommand(String request) {
		this.request = request;
		this.processController = new ProcessOperationAdapter();
	}

	/**
	 * @see #execute()
	 */
	@Override
	public String execute() {
		StopRunningProgramsBHMessage requestObj = (StopRunningProgramsBHMessage) JSonParser.loadBHMessage(
				this.request, StopRunningProgramsBHMessage.class);
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
		
		// Killing the given processes.
		boolean allProcessesWereKilled = this.processController.killProcesses(requestObj.getProcessNames());

		return JSonParser.createSimpleResponse((allProcessesWereKilled) ? 1 : 0, requestMessageType);
	}

}
