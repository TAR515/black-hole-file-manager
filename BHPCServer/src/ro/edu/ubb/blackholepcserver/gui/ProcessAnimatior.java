package ro.edu.ubb.blackholepcserver.gui;

/**
 * This thread handled object can realize a simple process animation for a GUI. The GUI must implement "ServerCommunicatorGUI"
 * interface.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ProcessAnimatior extends Thread {

	/**
	 * A GUI object which use this class. The caller object must implement "ServerCommunicatorGUI" interface.
	 */
	private ServerCommunicatorGUI caller = null;

	/**
	 * This is the standard processing text.
	 */
	private String mainText = null;

	/**
	 * True - if the thread is running / False - if not.
	 */
	private boolean running = false;

	/**
	 * Default sleeping time between two animated state.
	 */
	private static final int sleepTime = 300;

	public static final String LOADING_MESSAGE = " Loading ";

	/**
	 * Constructor
	 * 
	 * @param caller
	 *            An object which implement "ServerCommunicatorGUI" interface.
	 */
	public ProcessAnimatior(ServerCommunicatorGUI caller, String mainText) {
		this.caller = caller;
		this.mainText = mainText;
	}

	public void run() {
		// Initializations.
		int iterations = 0;
		this.running = true;
		caller.setMessageText(this.mainText, false);

		// While the thread running do the animation on the GUI.
		while (this.running) {
			try {
				if (iterations < 3) {
					caller.setMessageText(caller.getMessageText() + ".", false);
					iterations++;
				} else { // Restart the animation.
					iterations = 0;
					caller.setMessageText(this.mainText, false);
				}

				sleep(ProcessAnimatior.sleepTime);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Stop the animation.
	 */
	public void stopProcessing() {
		this.running = false;
		caller.setMessageText("", false);
	}

	/**
	 * Stop the animation and write a final message to the GUI.
	 * 
	 * @param newMessage
	 *            final message.
	 */
	public void stopProcessing(String newMessage) {
		this.running = false;
		caller.setMessageText(newMessage, false);
	}
}
