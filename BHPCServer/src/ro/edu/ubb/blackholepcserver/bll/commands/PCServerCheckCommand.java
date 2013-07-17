package ro.edu.ubb.blackholepcserver.bll.commands;

import java.util.Arrays;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.PCServerCheckBHMessage;
import ro.edu.ubb.blackhole.libs.coding.HashCoding;
import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholepcserver.bll.PhoneCommand;
import ro.edu.ubb.blackholepcserver.gui.windows.MainWindow;

/**
 * This command returns a simple response to the phone which indicate that the system in online.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class PCServerCheckCommand implements PhoneCommand {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(PCServerCheckCommand.class);

	/**
	 * Request from the phone.
	 */
	private String request = null;

	public PCServerCheckCommand(String request) {
		this.request = request;
	}

	/**
	 * @see #execute()
	 */
	@Override
	public String execute() {
		PCServerCheckBHMessage requestObj = (PCServerCheckBHMessage) JSonParser.loadBHMessage(this.request,
				PCServerCheckBHMessage.class);
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

		// Returning the simple response.
		return JSonParser.createSimpleResponse(1, requestMessageType);
	}

}
