package ro.edu.ubb.blackholemainserver.dal.localdatabase;

import java.sql.SQLException;
import java.util.Calendar;

import ro.edu.ubb.blackholemainserver.dal.localdatabase.model.Server;

/**
 * This interface is the entry point of the Data Access Layer component. This component provides access to the local database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface ServersDao {

	/**
	 * Creates a new server in the database
	 * 
	 * @param server
	 *            The server information, must fill all the member data.
	 * @throws SQLException
	 *             If the current server already exists.
	 * @return 1 - If everything were okay, -6 if the server already exists.
	 */
	public int addNewServer(Server server) throws SQLException;

	/**
	 * Deletes a server from the database.
	 * 
	 * @param server
	 *            Target server, must contain the ServerName and the UserName and the Password.
	 * @return 1 - If everything were okay, -3 if the server not exists.
	 * @throws SQLException
	 */
	public int deleteServer(Server server) throws SQLException;

	/**
	 * Get a server from the database.
	 * 
	 * @param server
	 *            Must contain the ServerName and the UserName.
	 * @return The server from the database or null if it not exists.
	 * @throws SQLException
	 */
	public Server loadServer(Server server) throws SQLException;

	/**
	 * Set the server online or offline.
	 * 
	 * @param server
	 *            Must contain the ServerName, UserName and isOnline.
	 * @return 1 - If everything were okay, -3 if the server already not exists.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public int changeIsOnline(Server server) throws SQLException;

	/**
	 * Returns that the server is online or not.
	 * 
	 * @param server
	 *            Must contain ServerName and UserName.
	 * @return True - if the server is online / False - if the server is offline or there are no server with the specified
	 *         ServerName and UserName.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public boolean isOnline(Server server) throws SQLException;

	/**
	 * Sets a server IP address.
	 * 
	 * @param server
	 *            Must contain the ServerName, UserName and the new IP address.
	 * @return 1 - If everything were okay, -3 if the server already not exists.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public int changeServerIP(Server server) throws SQLException;

	/**
	 * Sets a server Port address.
	 * 
	 * @param server
	 *            Must contain the ServerName, UserName and the new Port address.
	 * @return 1 - If everything were okay, -3 if the server already not exists.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public int changeServerPort(Server server) throws SQLException;

	/**
	 * Returns the server IP address.
	 * 
	 * @param server
	 *            Must contain the ServerName and the UserName.
	 * @return Server IP if the server exists or null if not.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public String getIP(Server server) throws SQLException;

	/**
	 * Returns the server Port.
	 * 
	 * @param server
	 *            Must contain the ServerName and the UserName.
	 * @return Server Port if the server exists or -3 if not.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public int getPort(Server server) throws SQLException;

	/**
	 * Sets the server last online date.
	 * 
	 * @param server
	 *            Must contain the ServerName, UserName and the lastOnline Date.
	 * @return 1 - If everything were okay, -3 if the server already not exists.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public int changeServerLastOnline(Server server) throws SQLException;

	/**
	 * Returns when was the server online at last time.
	 * 
	 * @param server
	 *            Must contain the ServerName and the UserName.
	 * @return The server last online date if the server exists or null if not.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public Calendar getLastOnline(Server server) throws SQLException;

	/**
	 * Sets a user email address.
	 * 
	 * @param server
	 *            Must contain the ServerName, UserName and the new email address.
	 * @return 1 - If everything were okay, -3 if the server already not exists.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public int changeUserEmail(Server server) throws SQLException;

	/**
	 * Returns a user email address.
	 * 
	 * @param server
	 *            Must contain the UserName and the ServerName.
	 * @return Users email if the server exists or null if not.
	 * @throws SQLException
	 *             If the server does not exists.
	 */
	public String getEmail(Server server) throws SQLException;

	/**
	 * Change the users password.
	 * 
	 * @param server
	 *            Must contain the ServerName, UserName, new password!
	 * @return 1 - If everything were okay, -3 if the server already not exists.
	 * @throws SQLException
	 */
	public int changePassword(Server server) throws SQLException;

	/**
	 * Check if the password is correct or not,
	 * 
	 * @param server
	 *            Must contain ServerName, UserName, Password which you want to check.
	 * @return -3 if the server does not exists, -4 if the password is wrong, 1 if the password were correct.
	 * @throws SQLException
	 */
	public int passwordCheck(Server server) throws SQLException;

	/**
	 * Log in into the server. Check the UserName-ServerName-Password and if all were correct than sets: Online=true,
	 * LastOnline=Current Date, IP= Given IP, Port = Given Port.
	 * 
	 * @param server
	 *            Must contain UserName, Password, ServerName, IP, Port, LastOnline.
	 * @return -3 if the server does not exist / -4 if the password is incorrect / 1 successful login.
	 * @throws SQLException
	 */
	public int logIn(Server server) throws SQLException;

	/**
	 * Log out from server. Check the UserName-ServerName-Password and if all were correct than sets: Online = false, LastOnline =
	 * Current Date
	 * 
	 * @param server
	 *            Must contain UserName, Password, ServerName, LastOnline.
	 * @return -3 if the server does not exist / -4 if the password is incorrect / 1 successful login.
	 * @throws SQLException
	 */
	public int logOut(Server server) throws SQLException;

	/**
	 * Change the server IP in the database. (Change the last online)
	 * 
	 * @param server
	 *            Must contain ServerName, UserName, Password, NewIP, LastOnline.
	 * @return -3 if the server does not exist / -4 if the password is incorrect / 1 successful login.
	 * @throws SQLException
	 */
	public int changeIpWithPswCheck(Server server) throws SQLException;
}
