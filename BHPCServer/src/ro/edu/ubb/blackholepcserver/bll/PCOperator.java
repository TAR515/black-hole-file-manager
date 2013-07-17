package ro.edu.ubb.blackholepcserver.bll;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Properties;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.bhmessage.BHMessageType;
import ro.edu.ubb.blackhole.libs.bhmessage.IpChangeBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.LoginBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.LogoutBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.RegistrationBHMessage;
import ro.edu.ubb.blackhole.libs.bhmessage.SimpleBHMessage;
import ro.edu.ubb.blackhole.libs.coding.HashCoding;
import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackhole.libs.parser.JSonParser;
import ro.edu.ubb.blackholepcserver.gui.ServerCommunicatorGUI;
import ro.edu.ubb.blackholepcserver.servicelayer.DataProviderServlet;
import ro.edu.ubb.blackholepcserver.servicelayer.FileDownloadServlet;
import ro.edu.ubb.blackholepcserver.servicelayer.FileUploadServlet;
import ro.edu.ubb.blackholepcserver.servicelayer.ServerCommunicatior;

/**
 * This object can operate many system basic operations.
 * 
 * @author Turdean Arnold Robert
 * 
 * @version 1.0
 */
public class PCOperator implements PCOperationService {
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(PCOperator.class);

	/**
	 * The caller object which have to implement "ServerCommunicatorGUI" interface.
	 */
	private ServerCommunicatorGUI caller = null;

	/**
	 * GUI Properties.
	 */
	private Properties guiMessagesProperties = new Properties();
	
	/**
	 * Black Hole main server URL.
	 */
	private static String SERVER_URL = "http://192.168.1.100:8080/BlackHoleMainServer/pcservlet";

	/**
	 * Instance of an embedded Tomcat web server.
	 */
	private Tomcat tomcatInstance = null;

	/**
	 * Constructor for PCOperator.
	 * @param caller ServerCommunicatorGUI
	 */
	public PCOperator(ServerCommunicatorGUI caller) {
		try {
			this.caller = caller;

			// Load the guiMessagesProperties property file.
			guiMessagesProperties.load(getClass().getClassLoader().getResourceAsStream(
					"guimessages.properties"));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * The standard response handler method. This method will forward the response to the correct response handler function. This
	 * method will be called when the Black Hole main server provide a response.
	 * 
	 * @param responseXML
	 */
	public void responseHandler(String responseXML) {
		int responseCode = JSonParser.getRequestMessageType(responseXML);
		System.out.println(responseXML + " - " + responseCode);

		switch (responseCode) {
		case BHMessageType.BH_REGISTRATION_MESSAGE:
			simpleResponseHandler(responseXML);
			break;
		case BHMessageType.BH_LOG_IN_MESSAGE:
			simpleResponseHandler(responseXML);
			break;
		case BHMessageType.BH_LOG_OUT_MESSAGE:
			simpleResponseHandler(responseXML);
			break;
		case BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE:
			simpleResponseHandler(responseXML);
			break;
		case BHMessageType.BH_IP_CHANGE_MESSAGE:
			// Do nothing!
			break;
		default:
			int errCode = PCSErrorMessageInterpreter.getErrorCode(PCSErrorMessageInterpreter.PCS_MAIN_SERVER_IS_OFFLINE);
			logger.warn("Invalit response type!");
			simpleResponseHandler(JSonParser.createSimpleResponse(errCode, Integer.MIN_VALUE));
			break;
		}

	}

	/**
	 * Handle a "SimpleResponse" response from the Black Hole server.
	 * 
	 * @param responseXML
	 *            XML structured response string.
	 */
	private void simpleResponseHandler(String responseXML) {
		// Getting the SimpleResponseStorage object from the XML formated
		// string.
		SimpleBHMessage response = (SimpleBHMessage) JSonParser.loadBHMessage(responseXML,
				SimpleBHMessage.class);
		int responseCode = response.getResponseCode();

		// Checking the response and forwarding the information to the GUI.
		if (responseCode == 1) { // Successful operation!
			this.caller.responseHandler("OK");
		} else {
			String guiMessage = guiMessagesProperties.getProperty(Integer.toString(responseCode));
			this.caller.responseHandler(guiMessage);
		}
	}

	/**
	
	 * @param serverName String
	 * @param password String
	 * @param email String
	 * @param port int
	 * @see #registration(String, String, String) */
	@Override
	public void registration(String serverName, String password, String email, int port) {
		RegistrationBHMessage registrationDataStorage = new RegistrationBHMessage();

		try {
			registrationDataStorage.setServerName(serverName);
			registrationDataStorage.setServerPassword(HashCoding.hashString(password));
			registrationDataStorage.setEmail(email);
			registrationDataStorage.setServerPort(port);

			String xmlMessage = JSonParser.createBHMessage(registrationDataStorage);

			ServerCommunicatior thread = new ServerCommunicatior(PCOperator.SERVER_URL, xmlMessage,
					PCOperator.this);
			thread.start();
		} catch (Exception e) {
			int errCode = PCSErrorMessageInterpreter.getErrorCode(PCSErrorMessageInterpreter.PCS_INVALID_INPUT_TYPE);
			logger.error(e.getMessage());
			String invalidInput = guiMessagesProperties.getProperty(Integer.toString(errCode));
			this.caller.responseHandler(invalidInput);
		}
	}

	/**
	
	 * @param serverName String
	 * @param password String
	 * @param port int
	 * @see #login(String, String) */
	@Override
	public void login(String serverName, String password, int port) {
		LoginBHMessage loginDataStorage = new LoginBHMessage();

		try {
			loginDataStorage.setServerName(serverName);
			loginDataStorage.setServerPassword(HashCoding.hashString(password));
			loginDataStorage.setServerPort(port);

			String xmlMessage = JSonParser.createBHMessage(loginDataStorage);

			ServerCommunicatior thread = new ServerCommunicatior(PCOperator.SERVER_URL, xmlMessage,
					PCOperator.this);
			thread.start();
		} catch (Exception e) {
			logger.error("Login faild: " + e.getMessage());
		}
	}

	/**
	
	 * @param serverName String
	 * @param password String
	 * @param port int
	 * @see #logout(String, String) */
	@Override
	public void logout(String serverName, String password, int port) {
		LogoutBHMessage logoutDataStorage = new LogoutBHMessage();

		try {
			logoutDataStorage.setServerName(serverName);
			logoutDataStorage.setServerPassword(HashCoding.hashString(password));
			logoutDataStorage.setServerPort(port);

			String xmlMessage = JSonParser.createBHMessage(logoutDataStorage);

			ServerCommunicatior thread = new ServerCommunicatior(PCOperator.SERVER_URL, xmlMessage,
					PCOperator.this);
			thread.start();
		} catch (Exception e) {
			logger.error("Logout failed: " + e.getMessage());
		}
	}
	
	/**
	 * Method ipChangeCommand.
	 * @param serverName String
	 * @param password String
	 * @param port int
	 * @see ro.edu.ubb.blackholepcserver.bll.PCOperationService#ipChangeCommand(String, String, int)
	 */
	@Override
	public void ipChangeCommand(String serverName, String password, int port) {
		IpChangeBHMessage ipChangeBHMessage = new IpChangeBHMessage();
		
		
		try {
			ipChangeBHMessage.setServerName(serverName);
			ipChangeBHMessage.setServerPassword(HashCoding.hashString(password));
			ipChangeBHMessage.setServerPort(port);

			String xmlMessage = JSonParser.createBHMessage(ipChangeBHMessage);

			ServerCommunicatior thread = new ServerCommunicatior(PCOperator.SERVER_URL, xmlMessage,
					PCOperator.this);
			thread.start();
		} catch (Exception e) {
			logger.error("IP Change failed: " + e.getMessage());
		}
	}

	/**
	 * @see #getCodedPassword(String)
	 */
	@Override
	public byte[] getCodedPassword(String password) {
		try {
			return HashCoding.hashString(password);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @see #startTomcat(int)
	 */
	@Override
	public boolean startTomcat(int port) {
		if (!isPortAvailable(port)) {
			return false;
		}

		this.tomcatInstance = new Tomcat();
		this.tomcatInstance.setPort(port);

		File base = new File("context");
		if (!base.exists()) {
			boolean success = base.mkdir();
			if (!success) {
				logger.error("Cannot make context directory!");
				this.tomcatInstance = null;
				return false;
			}
		}
		
		
		Context context = this.tomcatInstance.addContext("", base.getAbsolutePath());
		Tomcat.addServlet(context, "DataProviderServlet", new DataProviderServlet());
		Tomcat.addServlet(context, "FileUploadServlet", new FileUploadServlet());
		Tomcat.addServlet(context, "FileDownloadServlet", new FileDownloadServlet());
		context.addServletMapping("/DataProviderServlet", "DataProviderServlet");
		context.addServletMapping("/FileUploadServlet", "FileUploadServlet");
		context.addServletMapping("/FileDownloadServlet", "FileDownloadServlet");
		
		try {
			this.tomcatInstance.start();
		} catch (Throwable e) {
			logger.error("LifecycleException: " + e.getMessage());
			this.tomcatInstance = null;
			return false;
		}

		return true;
	}

	/**
	 * @see #stopTomcat()
	 */
	@Override
	public boolean stopTomcat() {
		try {
			if (this.tomcatInstance != null) {
				this.tomcatInstance.stop();
				this.tomcatInstance.destroy();
				this.tomcatInstance = null;
			}
		} catch (Exception e) {
			logger.error("LifecycleException: " + e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Test if the given port is available or not. (source: http://stackoverflow.com/questions
	 * /434718/sockets-discover-port-availability-using-java)
	 * 
	 * @param port
	 *            The port.
	
	 * @return True - if the given port is available / False - if not. */
	private static boolean isPortAvailable(int port) {
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}

		return false;
	}

}
