package ro.edu.ubb.blackhole.gui;

import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import android.app.Activity;
import android.widget.ListView;

/**
 * The interface give possibility to the {@link Activity} or {@link Object} to bet commands. e.g. Refresh a {@link ListView}.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface ResponseHandler {

	/**
	 * If the {@link Activity} or {@link Object} gets a command this method have to realizes it. The method have to log a warning
	 * message if the command realization is not implemented or the command isn't valid.
	 * 
	 * @param command
	 *            The command which have to contains a commandID which indicates which operation should do the user interface.
	 */
	public void responseHandler(CommandToGui command);
}
