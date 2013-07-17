package ro.edu.ubb.blackholemainserver.bll.commands;

import java.sql.SQLException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.RegistrationBHMessage;
import ro.edu.ubb.blackhole.libs.errmsg.MSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholemainserver.bll.Command;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.DaoFactory;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.ServersDao;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.jdbc.JdbcDaoFactory;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.model.Server;

/**
 * This command registers a new PC Server into the database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class RegistrationCommand implements Command {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(RegistrationCommand.class);

	/**
	 * Request from the PC Server
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
	 * PC Server's IP address.
	 */
	private String clientIPAdress = null;

	/**
	 * Type of the request message.
	 */
	private int requestMessageType = Integer.MIN_VALUE;

	public RegistrationCommand(String request, String clientIPAdress) throws SQLException {
		try {
			this.request = request;
			this.instance = JdbcDaoFactory.getInstance();
			this.serversDao = this.instance.getServersDao();
			this.clientIPAdress = clientIPAdress;
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
			Server newServer = loadServerFromMessage();

			int errMsg = this.serversDao.addNewServer(newServer);
			int errCode = errMsg;
			if (errMsg != 1) { // The server already exists!
				errCode = MSErrorMessageInterpreter
						.getErrorCode(MSErrorMessageInterpreter.MS_SERVER_ALLREADY_EXISTS);
				logger.info("Problem: " + errCode);
			}

			return JSonParser.createSimpleResponse(errCode, this.requestMessageType);
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
		RegistrationBHMessage data = (RegistrationBHMessage) JSonParser.loadBHMessage(this.request,
				RegistrationBHMessage.class);
		this.requestMessageType = data.getMessageType();

		Server newServer = new Server();
		newServer.setServerName(data.getServerName());
		newServer.setPassword(data.getServerPassword());
		newServer.setEmail(data.getEmail());
		newServer.setIp(this.clientIPAdress);
		newServer.setPort(data.getServerPort());
		newServer.setLastOnline(Calendar.getInstance());
		newServer.setOnline(false);

		return newServer;
	}
}
