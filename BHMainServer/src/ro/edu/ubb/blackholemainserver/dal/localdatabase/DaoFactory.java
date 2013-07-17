package ro.edu.ubb.blackholemainserver.dal.localdatabase;

import java.sql.SQLException;

import ro.edu.ubb.blackholemainserver.dal.localdatabase.jdbc.JdbcDaoFactory;

/**
 * This abstract object provides the concrete {@link DaoFactory} implementations.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public abstract class DaoFactory {

	/**
	 * Returns the instance of the {@link DaoFactory}.
	 * 
	 * @return An implementation of the {@link DaoFactory}
	 * @throws SQLException
	 *             If some error were occurred.
	 */
	public static DaoFactory getInstance() throws SQLException {
		return new JdbcDaoFactory();
	}

	/**
	 * Returns the instance of the {@link ServersDao}.
	 * 
	 * @return Instance of the {@link ServersDao}.
	 */
	public abstract ServersDao getServersDao();

}
