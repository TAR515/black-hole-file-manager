package ro.edu.ubb.blackholemainserver.bll.commands;

import java.sql.SQLException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.IpChangeBHMessage;
import ro.edu.ubb.blackhole.libs.errmsg.MSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholemainserver.bll.Command;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.DaoFactory;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.ServersDao;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.jdbc.JdbcDaoFactory;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.model.Server;

/**
 * This command refresh the PC Server's IP address inti the database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class IpChangeCommand implements Command {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(IpChangeCommand.class);

	/**
	 * Request from the PC Server.
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

	public IpChangeCommand(String request, String clientIPAdress) throws SQLException {
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
			Server ipInf = loadServerFromMessage();

			int errMsg = this.serversDao.changeIpWithPswCheck(ipInf);
			int errCode = errMsg;
			if (errMsg != 1) { // The server already exists!
				if (errMsg == -3) {
					errCode = MSErrorMessageInterpreter
							.getErrorCode(MSErrorMessageInterpreter.MS_WRONG_SERVERNAME);
				} else {
					errCode = MSErrorMessageInterpreter
							.getErrorCode(MSErrorMessageInterpreter.MS_WRONG_PASSWORD);
				}

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
		IpChangeBHMessage data = (IpChangeBHMessage) JSonParser.loadBHMessage(this.request,
				IpChangeBHMessage.class);
		this.requestMessageType = data.getMessageType();

		Server retValue = new Server();
		retValue.setServerName(data.getServerName());
		retValue.setPassword(data.getServerPassword());
		retValue.setIp(this.clientIPAdress);
		retValue.setPort(data.getServerPort());
		retValue.setLastOnline(Calendar.getInstance());
		retValue.setOnline(true);

		return retValue;
	}

}
