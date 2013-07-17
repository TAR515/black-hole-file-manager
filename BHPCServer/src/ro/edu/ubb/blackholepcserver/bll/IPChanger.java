package ro.edu.ubb.blackholepcserver.bll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * This class sends the new IP address to the main server if the client IP were changed.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class IPChanger extends Thread {

	/**
	 * Controller.
	 */
	private PCOperationService controller = null;

	/**
	 * This boolean is regulating the thread.
	 */
	private boolean isRunning = false;

	/**
	 * Name of the running server.
	 */
	private String serverName = null;

	/**
	 * Password of the running server.
	 */
	private String password = null;

	/**
	 * Port of the running server.
	 */
	private int port = -1;

	/**
	 * In every given millisecundum the system refresh the current external IP.
	 */
	private final static int IP_REFRESHING_PERIOD = 1000;

	/**
	 * In case of when the system cannot reach the external service to refresh the IP, this variable specify when the system send
	 * an IP refresh message to the main server.
	 */
	private final static int REFRESHING_PERIOD = 20;

	/**
	 * IP address of the external service.
	 */
	private final static String EXTERNAL_IP_CHECK_SERVICE = "http://checkip.amazonaws.com";

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(PCOperator.class);

	/**
	 * Constructor for IPChanger.
	 * 
	 * @param controller
	 *            PCOperationService
	 * @param serverName
	 *            String
	 * @param password
	 *            String
	 * @param port
	 *            int
	 */
	public IPChanger(PCOperationService controller, String serverName, String password, int port) {
		this.controller = controller;
		this.serverName = serverName;
		this.password = password;
		this.port = port;
	}

	/**
	 * Method run.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		this.isRunning = true;
		String currentIP = getExternalIP();
		int counter = 1;

		while (this.isRunning) {
			try {
				sleep(IP_REFRESHING_PERIOD);

				String externalIP = getExternalIP();
				if (externalIP != null) {
					if (!currentIP.equals(externalIP)) {
						this.controller.ipChangeCommand(serverName, password, port);
						currentIP = externalIP;
					}
				} else {
					if (counter++ % REFRESHING_PERIOD == 0) {
						this.controller.ipChangeCommand(serverName, password, port);
						counter = 1;
					}
				}
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * Stops the IP check.
	 */
	public void stopIPChanger() {
		this.isRunning = false;
	}

	/**
	 * Returns the external IP address of the client.
	 * 
	 * @return External IP / Null if some error were occurred.
	 */
	private String getExternalIP() {
		BufferedReader in = null;

		try {
			URL url = new URL(EXTERNAL_IP_CHECK_SERVICE);

			in = new BufferedReader(new InputStreamReader(url.openStream()));
			return in.readLine();
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.warn(e.getMessage());
				}
			}
		}
	}

}
