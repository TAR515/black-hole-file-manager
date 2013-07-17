package ro.edu.ubb.blackholepcserver.bll;

/**
 * An interface to all phone commands. These commands will serve the requests from the phone.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface PhoneCommand {

	/**
	 * Execute the given Black Hole command.
	 * @return XML formated response string.
	 */
	public String execute();
	
}
