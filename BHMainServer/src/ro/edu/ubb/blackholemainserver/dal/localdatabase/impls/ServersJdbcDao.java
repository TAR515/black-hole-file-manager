package ro.edu.ubb.blackholemainserver.dal.localdatabase.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import ro.edu.ubb.blackholemainserver.dal.localdatabase.ServersDao;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.model.Server;

/**
 * This object is the implementation of the {@link ServersDao} interface. The object implements all operations with the help of
 * the user can access the local database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ServersJdbcDao extends BaseDao implements ServersDao {
	public ServersJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * @see #addNewServer(Server)
	 */
	@Override
	public int addNewServer(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "new_server", 8);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setByteList(stmt, paramIndex++, server.getPassword());
			setString(stmt, paramIndex++, server.getEmail());
			setString(stmt, paramIndex++, server.getIp());
			setInt(stmt, paramIndex++, server.getPort());
			setDate(stmt, paramIndex++, server.getLastOnline());
			setBoolean(stmt, paramIndex++, server.isOnline());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") < 0) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}

	/**
	 * @see #deleteServer(Server)
	 */
	@Override
	public int deleteServer(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "delete_server", 2);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") < 0) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}

	/**
	 * @see #loadServer(Server)
	 */
	@Override
	public Server loadServer(Server server) throws SQLException {
		Server retServer = null;

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_server", 2);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			rs = stmt.executeQuery();

			int errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.warn("Server not found!");
				return retServer;
			}

			while (rs.next()) {
				retServer = (Server) fillObject(rs);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return retServer;
	}

	/**
	 * @see #changeIsOnline(Server)
	 */
	@Override
	public int changeIsOnline(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_server_online", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setBoolean(stmt, paramIndex++, server.isOnline());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") < 0) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}

	/**
	 * @see #isOnline(Server)
	 */
	@Override
	public boolean isOnline(Server server) throws SQLException {
		Server serverInf = loadServer(server);

		if (serverInf != null) {
			return serverInf.isOnline();
		}

		return false;
	}

	/**
	 * @see #changeServerIP(Server)
	 */
	@Override
	public int changeServerIP(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_server_ip", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setString(stmt, paramIndex++, server.getIp());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") < 0) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}

	/**
	 * @see #changeServerPort(Server)
	 */
	@Override
	public int changeServerPort(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_server_port", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setInt(stmt, paramIndex++, server.getPort());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") < 0) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;

	}

	/**
	 * @see #getIP(Server)
	 */
	@Override
	public String getIP(Server server) throws SQLException {
		Server serverInf = loadServer(server);

		if (serverInf != null) {
			return serverInf.getIp();
		}

		return null;
	}

	/**
	 * @see #getPort(Server)
	 */
	@Override
	public int getPort(Server server) throws SQLException {
		Server serverInf = loadServer(server);

		if (serverInf != null) {
			return serverInf.getPort();
		}

		return -3;
	}

	/**
	 * @see #changeServerLastOnline(Server)
	 */
	@Override
	public int changeServerLastOnline(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_server_last_online", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setDate(stmt, paramIndex++, server.getLastOnline());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") < 0) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;

	}

	/**
	 * @see #getLastOnline(Server)
	 */
	@Override
	public Calendar getLastOnline(Server server) throws SQLException {
		Server serverInf = loadServer(server);

		if (serverInf != null) {
			return serverInf.getLastOnline();
		}

		return null;
	}

	/**
	 * @see #changeUserEmail(Server)
	 */
	@Override
	public int changeUserEmail(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_server_email", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setString(stmt, paramIndex++, server.getEmail());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") < 0) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}

	/**
	 * @see #getEmail(Server)
	 */
	@Override
	public String getEmail(Server server) throws SQLException {
		Server serverInf = loadServer(server);

		if (serverInf != null) {
			return serverInf.getEmail();
		}

		return null;
	}

	/**
	 * @see #changePassword(Server)
	 */
	@Override
	public int changePassword(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_server_password", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setByteList(stmt, paramIndex++, server.getPassword());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") < 0) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}

	/**
	 * @see #passwordCheck(Server)
	 */
	@Override
	public int passwordCheck(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "password_check", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setByteList(stmt, paramIndex++, server.getPassword());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			connection.commit();

			return stmt.getInt("Oerrmsg");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}
	}

	/**
	 * @see #fillObject(ResultSet)
	 */
	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Server retValue = new Server();

		retValue.setServerName(getString(rs, "ServerName"));
		retValue.setIp(getString(rs, "IP"));
		retValue.setPort(getInt(rs, "Port"));
		retValue.setLastOnline(getTimestamp(rs, "LastOnline"));
		retValue.setOnline(getBool(rs, "Online"));
		retValue.setEmail(getString(rs, "Email"));

		return retValue;
	}

	/**
	 * @see #logIn(Server)
	 */
	@Override
	public int logIn(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "login", 6);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setByteList(stmt, paramIndex++, server.getPassword());
			setString(stmt, paramIndex++, server.getIp());
			setInt(stmt, paramIndex++, server.getPort());
			setDate(stmt, paramIndex++, server.getLastOnline());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") != 1) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}

	/**
	 * @see #logOut(Server)
	 */
	@Override
	public int logOut(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "logout", 4);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setByteList(stmt, paramIndex++, server.getPassword());
			setDate(stmt, paramIndex++, server.getLastOnline());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") != 1) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}

	/**
	 * @see #changeIpWithPswCheck(Server)
	 */
	@Override
	public int changeIpWithPswCheck(Server server) throws SQLException {
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "ip_change_with_psw_check", 5);

			int paramIndex = 1;
			setString(stmt, paramIndex++, server.getServerName());
			setByteList(stmt, paramIndex++, server.getPassword());
			setDate(stmt, paramIndex++, server.getLastOnline());
			setString(stmt, paramIndex++, server.getIp());
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			if (stmt.getInt("Oerrmsg") != 1) {
				return stmt.getInt("Oerrmsg");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
		}

		return 1;
	}
}
