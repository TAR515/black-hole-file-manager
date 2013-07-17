package ro.edu.ubb.blackholemainserver.bll;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.BHMessageType;
import ro.edu.ubb.blackhole.libs.errmsg.MSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholemainserver.bll.commands.GetIPAndPortCommand;
import ro.edu.ubb.blackholemainserver.bll.commands.IpChangeCommand;
import ro.edu.ubb.blackholemainserver.bll.commands.LogInCommand;
import ro.edu.ubb.blackholemainserver.bll.commands.LogOutCommand;
import ro.edu.ubb.blackholemainserver.bll.commands.RegistrationCommand;

/**
 * Dispatcher of the command design pattern. This class recognize the type of the request method and transfers it to the proper
 * command object.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class Dispatcher {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(Dispatcher.class);

	/**
	 * Result of the command.
	 */
	private String request = null;

	/**
	 * Command.
	 */
	private Command command = null;

	/**
	 * Client IP address.
	 */
	private String clientIPAdress = null;

	public Dispatcher(String request, String clientIPAdress) {
		this.request = request;
		this.clientIPAdress = clientIPAdress;
	}

	/**
	 * Create the response XML.
	 * 
	 * @return An XML formated string which represents the response. This can be a response or an error response or null if the
	 *         error repose didn't created successfully.
	 */
	public String getResult() {
		System.out.println("Req. Message type; " + JSonParser.getMessageType(this.request));

		if (this.request == null || JSonParser.getMessageType(this.request) == Integer.MIN_VALUE) {
			Dispatcher.logger.error("Request is not a BlackHoleMessage!");
			int errCode = MSErrorMessageInterpreter
					.getErrorCode(MSErrorMessageInterpreter.MS_INVALID_REQUEST);

			return JSonParser.createSimpleResponse(errCode, Integer.MIN_VALUE); // Invalid Request error!
		} else {
			try {
				int requestMessageType = JSonParser.getMessageType(this.request);
				switch (requestMessageType) {
				case (BHMessageType.BH_REGISTRATION_MESSAGE): // RegistrationCommand
					command = new RegistrationCommand(request, this.clientIPAdress);
					return command.execute();
				case (BHMessageType.BH_LOG_IN_MESSAGE): // LogInCommand
					command = new LogInCommand(request, this.clientIPAdress);
					return command.execute();
				case (BHMessageType.BH_LOG_OUT_MESSAGE): // LogOutCommand
					command = new LogOutCommand(request, this.clientIPAdress);
					return command.execute();
				case (BHMessageType.BH_IP_CHANGE_MESSAGE): // IpChangeCommand
					command = new IpChangeCommand(request, this.clientIPAdress);
					return command.execute();
				case (BHMessageType.BH_GET_IP_AND_PORT_MESSAGE): // GetIPAndPortCommand
					command = new GetIPAndPortCommand(request);
					return command.execute();
				default:
					Dispatcher.logger.warn("Request type not found!");
					int errCode = MSErrorMessageInterpreter
							.getErrorCode(MSErrorMessageInterpreter.MS_INVALID_REQUEST);

					return JSonParser.createSimpleResponse(errCode, requestMessageType); // Invalid Request error!
				}
			} catch (SQLException e) {
				Dispatcher.logger.error(e.getMessage());
				int errCode = MSErrorMessageInterpreter
						.getErrorCode(MSErrorMessageInterpreter.MS_DATABASE_ERROR);

				return JSonParser.createSimpleResponse(errCode, Integer.MIN_VALUE); // Server database error!
			}
		}

	}
}
