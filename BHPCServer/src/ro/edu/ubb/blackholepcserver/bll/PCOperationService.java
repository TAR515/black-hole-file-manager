package ro.edu.ubb.blackholepcserver.bll;

/**
 * This interface provides many BLL operations.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface PCOperationService {

	/**
	 * Return the coded password.
	 * 
	 * @param password
	 *            Passowrd.
	 * @return Coded password in byte array. */
	public byte[] getCodedPassword(String password);

	/**
	 * Send a registration request to the main server.
	 * 
	 * @param serverName
	 *            New server name.
	 * @param password
	 *            Password.
	 * @param email
	 *            Email address.
	 * @param port
	 *            PC server communication port.
	 */
	public void registration(String serverName, String password, String email, int port);

	/**
	 * Send a login request to the main server.
	 * 
	 * @param serverName
	 *            Server Name.
	 * @param password
	 *            Password.
	 * @param port
	 *            PC server communication port.
	 */
	public void login(String serverName, String password, int port);

	/**
	 * Send a logout request to the main server.
	 * 
	 * @param serverName
	 *            Server Name.
	 * @param password
	 *            Password.
	 * @param port
	 *            PC server communication port.
	 */
	public void logout(String serverName, String password, int port);

	/**
	 * Send an IP change request to the main server.
	 * 
	 * @param serverName
	 *            Server Name.
	 * @param password
	 *            Password.
	 * @param port
	 *            PC server communication port.
	 */
	public void ipChangeCommand(String serverName, String password, int port);

	/**
	 * Starts the embedded Tomcat web server.
	 * 
	 * @param port
	 *            The port which the server will use.
	
	 * @return true - if the server were started / False - if not. */
	public boolean startTomcat(int port);

	/**
	 * Stops the embedded Tomcat web server.
	 * 
	
	 * @return True - if the server were stopped / False - if not. */
	public boolean stopTomcat();

}
