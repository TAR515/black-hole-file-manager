package ro.edu.ubb.blackholemainserver.bll;

import ro.edu.ubb.blackholemainserver.dal.localdatabase.model.Server;

/**
 * Interface of the command design pattern.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface Command {

	/**
	 * Execute the actual command.
	 * 
	 * @return The result of the execution.
	 */
	public String execute();

	/**
	 * Loads the server informations from the request message.
	 * 
	 * @return Server informations.
	 * @throws Exception
	 *             If some error were ocured.
	 */
	Server loadServerFromMessage() throws Exception;
}
