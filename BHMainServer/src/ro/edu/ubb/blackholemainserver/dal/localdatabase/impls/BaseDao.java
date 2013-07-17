package ro.edu.ubb.blackholemainserver.dal.localdatabase.impls;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.xs.datatypes.ByteList;

/**
 * This object provides basic get/set operations with the help of the user can access the result and the parameters of a
 * {@link CallableStatement}.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public abstract class BaseDao {

	/**
	 * Logger
	 */
	protected Logger logger = Logger.getLogger(BaseDao.class);

	/**
	 * Data Source.
	 */
	protected DataSource dataSource;

	/**
	 * The maximum size of the result.
	 */
	protected int maxResultSize = 1000;

	public BaseDao(DataSource dataSource, int maxResultSize) {
		this.dataSource = dataSource;
		this.maxResultSize = maxResultSize;
		logger = Logger.getLogger(getClass());
	}

	public BaseDao(DataSource dataSource) {
		this.dataSource = dataSource;
		logger = Logger.getLogger(getClass());
	}

	/**
	 * Sets a {@link String} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setString(CallableStatement stmt, int pos, String value) throws SQLException {
		if (value != null) {
			logger.debug("setString(" + pos + ", " + value + ")");
			stmt.setString(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.VARCHAR)");
			stmt.setNull(pos, Types.VARCHAR);
		}
	}

	/**
	 * Sets a {@link ByteList} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setByteList(CallableStatement stmt, int pos, byte[] value) throws SQLException {
		if (value != null) {
			logger.debug("setBytes(" + pos + ", " + value + ")");
			stmt.setBytes(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.INTEGER - Byte[] -)");
			stmt.setNull(pos, Types.INTEGER);
		}
	}

	/**
	 * Sets a {@link Array} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setArray(CallableStatement stmt, int pos, Array value) throws SQLException {
		if (value != null) {
			logger.debug("setString(" + pos + ", " + value + ")");
			stmt.setArray(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.VARCHAR)");
			stmt.setNull(pos, Types.VARCHAR);
		}
	}

	/**
	 * Sets a {@link Date} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setDate(CallableStatement stmt, int pos, java.util.Date value) throws SQLException {
		if (value != null) {
			logger.debug("setDate(" + pos + ", " + value + ")");
			stmt.setDate(pos, new java.sql.Date(value.getTime()));
		} else {
			logger.debug("setNull(" + pos + ", Types.DATE)");
			stmt.setNull(pos, Types.DATE);
		}
	}

	/**
	 * Sets a {@link Calendar} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setDate(CallableStatement stmt, int pos, Calendar value) throws SQLException {
		if (value != null) {
			logger.debug("setDate(" + pos + ", " + value + ")");
			stmt.setDate(pos, new java.sql.Date(value.getTimeInMillis()));
		} else {
			logger.debug("setNull(" + pos + ", Types.DATE)");
			stmt.setNull(pos, Types.DATE);
		}
	}

	/**
	 * Sets a {@link Integer} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setInteger(CallableStatement stmt, int pos, Integer value) throws SQLException {
		if (value != null) {
			logger.debug("setInteger(" + pos + ", " + value + ")");
			stmt.setInt(pos, value.intValue());
		} else {
			logger.debug("setNull(" + pos + ", Types.INTEGER)");
			stmt.setNull(pos, Types.INTEGER);
		}
	}

	/**
	 * Sets an int typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setInt(CallableStatement stmt, int pos, int value) throws SQLException {
		logger.debug("setInteger(" + pos + ", " + value + ")");
		stmt.setInt(pos, value);
	}

	/**
	 * Sets a {@link Double} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setDouble(CallableStatement stmt, int pos, Double value) throws SQLException {
		if (value != null) {
			logger.debug("setDouble(" + pos + ", " + value + ")");
			stmt.setDouble(pos, value.doubleValue());
		} else {
			logger.debug("setNull(" + pos + ", Types.DOUBLE)");
			stmt.setNull(pos, Types.DOUBLE);
		}
	}

	/**
	 * Sets a double typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setDouble(CallableStatement stmt, int pos, double value) throws SQLException {
		logger.debug("setDouble(" + pos + ", " + value + ")");
		stmt.setDouble(pos, value);
	}

	/**
	 * Sets a {@link Boolean} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setBoolean(CallableStatement stmt, int pos, Boolean value) throws SQLException {
		if (value != null) {
			logger.debug("setBoolean(" + pos + ", " + value + ")");
			stmt.setBoolean(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.VARCHAR)");
			stmt.setNull(pos, Types.VARCHAR);
		}
	}

	/**
	 * Sets a boolean typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setBoolean(CallableStatement stmt, int pos, boolean value) throws SQLException {
		logger.debug("setBoolean(" + pos + ", " + value + ")");
		setBoolean(stmt, pos, Boolean.valueOf(value));
	}

	/**
	 * Sets a {@link Long} typed parameter of the {@link CallableStatement}.
	 * 
	 * @param stmt
	 *            CallableStatement
	 * @param pos
	 *            Position of the parameter
	 * @param value
	 *            Value of the parameter
	 * @throws SQLException
	 *             If the value equals with null.
	 */
	protected void setLong(CallableStatement stmt, int pos, Long value) throws SQLException {
		if (value != null) {
			logger.debug("setLong(" + pos + ", " + value + ")");
			stmt.setLong(pos, value.longValue());
		} else {
			logger.debug("setNull(" + pos + ", Types.INTEGER)");
			stmt.setNull(pos, Types.INTEGER);
		}
	}

	/**
	 * Creates a {@link CallableStatement}.
	 * 
	 * @param connection
	 *            Connection to the database.
	 * @param name
	 *            Name of the {@link CallableStatement}
	 * @param argsNum
	 *            Number of parameters of the {@link CallableStatement}
	 * @return A new {@link CallableStatement}.
	 * @throws SQLException
	 *             If some error were occured.
	 */
	protected CallableStatement createProcedure(Connection connection, String name, int argsNum)
			throws SQLException {

		StringBuffer sb = new StringBuffer(10);

		for (int i = 0; i < argsNum; i++) {

			if (i < (argsNum - 1)) {
				sb.append("?, ");
			} else {
				sb.append("?");
			}
		}

		return connection.prepareCall("{ call " + name + "(" + sb.toString() + ") }");
	}

	/**
	 * Returns the {@link Connection} to the database.
	 * 
	 * @return {@link Connection} to the database.
	 * @throws SQLException
	 *             If some error were occured.
	 */
	protected Connection getConnection() throws SQLException {
		try {
			Connection conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			return conn;
		} catch (Exception e) {
			throw new SQLException("BaseDao.getConnection()", e);
		}
	}

	/**
	 * Close all the following SQL objects: {@link Connection}, {@link ResultSet}, {@link CallableStatement}.
	 * 
	 * @param connection
	 *            {@link Connection} to the database.
	 * @param rs
	 *            {@link ResultSet}.
	 * @param stmt
	 *            {@link CallableStatement}.
	 */
	protected void closeSQLObjects(Connection connection, ResultSet rs, CallableStatement stmt) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the informations from the {@link ResultSet} to an {@link Object}.
	 * 
	 * @param rs
	 *            {@link ResultSet}.
	 * @return An {@link Object} that contains all the informations from the {@link ResultSet}.
	 * @throws SQLException
	 *             If some error were occured.
	 */
	protected abstract Object fillObject(ResultSet rs) throws SQLException;

	/**
	 * Returns a {@link Long} typed object from the given {@link ResultSet}.
	 * 
	 * @param rs
	 *            {@link ResultSet}.
	 * @param columnName
	 *            Name of the column.
	 * @return {@link Long} typed object which contains the result.
	 * @throws SQLException
	 *             If some error were occurred.
	 */
	protected Long getLong(ResultSet rs, String columnName) throws SQLException {
		Long retVal = null;
		Long value = rs.getLong(columnName);
		if (!rs.wasNull()) {
			retVal = value;
		}
		return retVal;
	}

	/**
	 * Returns a {@link Integer} typed object from the given {@link ResultSet}.
	 * 
	 * @param rs
	 *            {@link ResultSet}.
	 * @param columnName
	 *            Name of the column.
	 * @return {@link Integer} typed object which contains the result.
	 * @throws SQLException
	 *             If some error were occurred.
	 */
	protected Integer getInt(ResultSet rs, String columnName) throws SQLException {
		Integer retVal = null;
		Integer value = rs.getInt(columnName);
		if (!rs.wasNull()) {
			retVal = value;
		}
		return retVal;
	}

	/**
	 * Returns a {@link String} typed object from the given {@link ResultSet}.
	 * 
	 * @param rs
	 *            {@link ResultSet}.
	 * @param columnName
	 *            Name of the column.
	 * @return {@link String} typed object which contains the result.
	 * @throws SQLException
	 *             If some error were occurred.
	 */
	protected String getString(ResultSet rs, String columnName) throws SQLException {
		String retVal = null;
		String value = rs.getString(columnName);
		if (!rs.wasNull()) {
			retVal = value;
		}
		return retVal;
	}

	/**
	 * Returns a {@link Boolean} typed object from the given {@link ResultSet}.
	 * 
	 * @param rs
	 *            {@link ResultSet}.
	 * @param columnName
	 *            Name of the column.
	 * @return {@link Boolean} typed object which contains the result.
	 * @throws SQLException
	 *             If some error were occurred.
	 */
	protected Boolean getBool(ResultSet rs, String columnName) throws SQLException {
		Boolean retVal = null;
		Boolean value = rs.getBoolean(columnName);
		if (!rs.wasNull()) {
			retVal = value;
		}
		return retVal;
	}

	/**
	 * Returns a {@link Calendar} typed object from the given {@link ResultSet}.
	 * 
	 * @param rs
	 *            {@link ResultSet}.
	 * @param columnName
	 *            Name of the column.
	 * @return {@link Calendar} typed object which contains the result.
	 * @throws SQLException
	 *             If some error were occurred.
	 */
	protected Calendar getTimestamp(ResultSet rs, String columnName) throws SQLException {
		Calendar retVal = null;
		Timestamp value = rs.getTimestamp(columnName);
		if (!rs.wasNull()) {
			retVal = asCalendar(value);
		}
		return retVal;
	}

	/**
	 * Convert a {@link Timestamp} to {@link Calendar}.
	 * 
	 * @param value
	 *            {@link Timestamp}
	 * @return The {@link Calendar} converted from {@link Timestamp}.
	 */
	protected Calendar asCalendar(Timestamp value) {
		if (value == null) {
			return null;
		}
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(value.getTime());

		return result;
	}
}
