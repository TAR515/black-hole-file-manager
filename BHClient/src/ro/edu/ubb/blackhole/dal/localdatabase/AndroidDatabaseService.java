package ro.edu.ubb.blackhole.dal.localdatabase;

import java.util.List;

import ro.edu.ubb.blackhole.datastructures.Server;
import android.database.SQLException;

/**
 * This is the interface of the Android Database Acess Layer component. This component provides the basic operations to get access
 * to the SQLite database.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface AndroidDatabaseService {

	/**
	 * Inserts a new server into the database.
	 * 
	 * @param server
	 *            The new server.
	 * @return "the row ID of the newly inserted row".
	 */
	public long insertNewServer(Server server) throws SQLException;

	/**
	 * Returns the server names from the database.
	 * 
	 * @return All server names.
	 */
	public List<String> getAllServerNames();

	/**
	 * Deletes a server from the database.
	 * 
	 * @param serverName
	 *            Target server name.
	 * @return True - if the server were deleted / False - if there is no server with the given server name.
	 */
	public boolean deleteServer(String serverName);

	/**
	 * Get all informations about a server from the database.
	 * 
	 * @param serverName
	 *            Target server name.
	 * @return All informations about the given server or null if the given server do not exists in the database. All these
	 *         informations are stored into the server attribute.
	 */
	public Server getServerInformations(String serverName);

	/**
	 * Update the given server informations. The target server name must be inicialized with the "server" attribute and the new
	 * server informations with the "updatedServer" attribute.
	 * 
	 * @param oldServerName
	 *            Old server name.
	 * @param newServer
	 *            The new server.
	 * @return True - if there were updated one or more items / False - if there weren't item update.
	 */
	public boolean updateServer(String oldServerName, Server newServer);
}
