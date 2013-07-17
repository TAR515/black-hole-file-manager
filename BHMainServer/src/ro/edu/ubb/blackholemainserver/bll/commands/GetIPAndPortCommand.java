package ro.edu.ubb.blackholemainserver.bll.commands;

import java.sql.SQLException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.GetIPAndPortBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.IpAndPortBHMessage;
import ro.edu.ubb.blackhole.libs.errmsg.MSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholemainserver.bll.Command;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.DaoFactory;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.ServersDao;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.jdbc.JdbcDaoFactory;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.model.Server;

/**
 * This command gets the IP address and the port of the given PC server.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class GetIPAndPortCommand implements Command {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(GetIPAndPortCommand.class);

	/**
	 * Request from the phone.
	 */
	private String request = null;

	/**
	 * Instance of the {@link DaoFactory}.
	 */
	private DaoFactory instance = null;

	/**
	 * Instance of the {@link ServersDao}.
	 */
	private ServersDao serversDao = null;

	/**
	 * Type of the request message.
	 */
	private int requestMessageType = Integer.MIN_VALUE;

	public GetIPAndPortCommand(String request) throws SQLException {
		try {
			this.request = request;
			this.instance = JdbcDaoFactory.getInstance();
			this.serversDao = this.instance.getServersDao();
		} catch (SQLException e) {
			logger.error("getInstance error: " + e.getMessage());
			throw new SQLException(e);
		}
	}

	/**
	 * @see #execute()
	 */
	@Override
	public String execute() {
		try {
			Server server = loadServerFromMessage();

			int errMsg = this.serversDao.passwordCheck(server);
			if (errMsg != 1) {
				int errCode = 0;
				if (errMsg == -3) {
					errCode = MSErrorMessageInterpreter
							.getErrorCode(MSErrorMessageInterpreter.MS_WRONG_SERVERNAME);
				} else {
					errCode = MSErrorMessageInterpreter
							.getErrorCode(MSErrorMessageInterpreter.MS_WRONG_PASSWORD);
				}

				logger.info("Problem: " + errCode);

				return JSonParser.createSimpleResponse(errCode, this.requestMessageType);
			}

			if (!this.serversDao.isOnline(server)) { // If the pc server is offline.
				int errCode = MSErrorMessageInterpreter
						.getErrorCode(MSErrorMessageInterpreter.MS_PC_SERVER_IS_OFFLINE);
				logger.info("PC Server is offline.");

				return JSonParser.createSimpleResponse(errCode, this.requestMessageType); // PC server is offline.
			}

			String serverIp = this.serversDao.getIP(server);
			int serverPort = this.serversDao.getPort(server);

			if (serverIp == null || serverPort == -3) {
				int errCode = MSErrorMessageInterpreter
						.getErrorCode(MSErrorMessageInterpreter.MS_WRONG_SERVERNAME);
				logger.info("PC Server not exists.");
				return JSonParser.createSimpleResponse(errCode, this.requestMessageType); // Server not exists.
			}

			// Returns the response.
			IpAndPortBHMessage response = new IpAndPortBHMessage(this.requestMessageType);
			response.setServerIP(serverIp);
			response.setServerPort(serverPort);

			return JSonParser.createBHMessage(response);
		} catch (Exception e) {
			int errCode = MSErrorMessageInterpreter.getErrorCode(MSErrorMessageInterpreter.MS_DATABASE_ERROR);
			logger.error(e.getMessage());
			return JSonParser.createSimpleResponse(errCode, this.requestMessageType);
		}
	}

	/**
	 * @see #loadServerFromMessage()
	 */
	@Override
	public Server loadServerFromMessage() throws Exception {
		GetIPAndPortBHMessage data = (GetIPAndPortBHMessage) JSonParser.loadBHMessage(this.request,
				GetIPAndPortBHMessage.class);
		this.requestMessageType = data.getMessageType();

		Server retValue = new Server();
		retValue.setServerName(data.getServerName());
		retValue.setPassword(data.getServerPassword());
		retValue.setLastOnline(Calendar.getInstance());
		retValue.setOnline(false);

		return retValue;
	}
}
