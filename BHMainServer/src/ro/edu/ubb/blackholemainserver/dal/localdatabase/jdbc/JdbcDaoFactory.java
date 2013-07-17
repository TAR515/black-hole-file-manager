package ro.edu.ubb.blackholemainserver.dal.localdatabase.jdbc;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackholemainserver.dal.localdatabase.DaoFactory;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.ServersDao;
import ro.edu.ubb.blackholemainserver.dal.localdatabase.impls.ServersJdbcDao;

/**
 * This object provides a JDBC based implementation of the Data Access Layer.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class JdbcDaoFactory extends DaoFactory {

	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger(JdbcDaoFactory.class);

	/**
	 * Data Source.
	 */
	private DataSource ds = null;

	public JdbcDaoFactory() throws SQLException {
		Context initContext;
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/blackholedb");
		} catch (NamingException e) {
			logger.error(e.getMessage());
			throw new SQLException(e);
		}
	}

	/**
	 * @see #getServersDao()
	 */
	@Override
	public ServersDao getServersDao() {
		return new ServersJdbcDao(ds);
	}
}
