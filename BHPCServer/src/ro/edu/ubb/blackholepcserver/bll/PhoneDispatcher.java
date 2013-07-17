package ro.edu.ubb.blackholepcserver.bll;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.BHMessageType;
import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholepcserver.bll.commands.DeleteFileCommand;
import ro.edu.ubb.blackholepcserver.bll.commands.GetDirectorySizeCommand;
import ro.edu.ubb.blackholepcserver.bll.commands.GetListOfFilesCommand;
import ro.edu.ubb.blackholepcserver.bll.commands.GetRunningProgramsCommand;
import ro.edu.ubb.blackholepcserver.bll.commands.KillProcessesCommand;
import ro.edu.ubb.blackholepcserver.bll.commands.PCServerCheckCommand;
import ro.edu.ubb.blackholepcserver.bll.commands.StartProgramCommand;

/**
 * This class recognize the request type of the request got from the phone and transmits it to the proper object to execute it.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class PhoneDispatcher {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(PhoneDispatcher.class);

	/**
	 * Request from the phone.
	 */
	private String request = null;

	@SuppressWarnings("unused")
	private String phoneIP = null;

	public PhoneDispatcher(String request, String clientIP) throws NullPointerException {
		this.request = request;
		this.phoneIP = clientIP;
	}

	/**
	 * Create the response XML.
	 * 
	 * @return An XML formated string, which represents the response to the request.
	 */
	public String getResult() {
		PhoneCommand command = null;
		
		if (this.request == null || JSonParser.getMessageType(this.request) == Integer.MIN_VALUE) {
			PhoneDispatcher.logger.error("Request is not a BlackHoleMessage!");
			return JSonParser.createSimpleResponse(-1, Integer.MIN_VALUE); // Invalid Request // error!
		} else {
			switch (JSonParser.getMessageType(this.request)) {
			case BHMessageType.BH_GET_LIST_OF_FILES_MESSAGE:
				command = new GetListOfFilesCommand(this.request);
				return command.execute();
			case BHMessageType.BH_GET_DIRECTORY_SIZE_MESSAGE:
				command = new GetDirectorySizeCommand(this.request);
				return command.execute();
			case BHMessageType.BH_DELETE_FILE_MESSAGE:
				command = new DeleteFileCommand(this.request);
				return command.execute();
			case BHMessageType.BH_PC_SERVER_CHECK_MESSAGE:
				command = new PCServerCheckCommand(this.request);
				return command.execute();
			case BHMessageType.BH_GET_RUNNING_PROGRAMS_MESSAGE:
				command = new GetRunningProgramsCommand(this.request);
				return command.execute();
			case BHMessageType.BH_START_PROGRAM_MESSAGE:
				command = new StartProgramCommand(this.request);
				return command.execute();
			case BHMessageType.BH_STOP_PROGRAM_MESSAGE:
				command = new KillProcessesCommand(this.request);
				return command.execute();
			default:
				int errCode = PCSErrorMessageInterpreter
						.getErrorCode(PCSErrorMessageInterpreter.PCS_INVALID_REQUEST);
				PhoneDispatcher.logger.error("Request type not found!");
				return JSonParser.createSimpleResponse(errCode, Integer.MIN_VALUE); // Invalid Request error!
			}
		}
	}
}
