package ro.edu.ubb.blackholepcserver.gui;

/**
 * Classes which implements this interface are prepared to server-client communication.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface ServerCommunicatorGUI {

	/**
	 * Handle a response from the BLL. This is an asyncron mechanism.
	 * 
	 * @param response
	 *            A message to the GUI.
	 */
	public void responseHandler(String response);

	/**
	 * Set a message to the GUI.
	 * 
	 * @param text
	 *            The message.
	 * @param stopProcessing
	 *            true - if a process have to stop / false - if no process have to stop.
	 */
	public void setMessageText(String text, boolean stopProcessing);

	/**
	 * Returns the current text from the GUI.
	 * 
	 * @return Current message
	 */
	public String getMessageText();

}
