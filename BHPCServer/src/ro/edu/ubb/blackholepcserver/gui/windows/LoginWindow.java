package ro.edu.ubb.blackholepcserver.gui.windows;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackholepcserver.bll.PCOperationService;
import ro.edu.ubb.blackholepcserver.bll.PCOperator;
import ro.edu.ubb.blackholepcserver.gui.BHInputListener;
import ro.edu.ubb.blackholepcserver.gui.ProcessAnimatior;
import ro.edu.ubb.blackholepcserver.gui.ServerCommunicatorGUI;

/**
 * This window provide a GUI for logging in into server.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class LoginWindow implements ServerCommunicatorGUI {

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getLogger(LoginWindow.class);

	/**
	 * Main Frame.
	 */
	private JFrame frmBlackHole;

	/**
	 * Server name text field.
	 */
	private JTextField serverNameTF;

	/**
	 * Password text field.
	 */
	private JTextField passwordTF;

	/**
	 * Port text field.
	 */
	private JTextField portTF;

	/**
	 * Message Label, here will display the error and the information messages.
	 */
	private JLabel messageLabel;

	/**
	 * Login button.
	 */
	private JButton btnLogin = null;

	/**
	 * Registration button.
	 */
	private JButton btnRegistration = null;

	/**
	 * Controller which will process the operations.
	 */
	private PCOperationService controller = new PCOperator(this);

	/**
	 * This thread handled object will realize the processing animations.
	 */
	private ProcessAnimatior processAnimation = null;

	/**
	 * Launch the application in a single instance.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Do not let the application to run in more instances. (source:
				// http://swingdepot.blogspot.ro/2009/09/single-instance-application-java.html)
				try {
					@SuppressWarnings("resource")
					ServerSocket serverSocket = new ServerSocket();
					serverSocket.bind(new InetSocketAddress(8881));

					new LoginWindow();
				} catch (SocketException e) {
					logger.warn("Application allready running. Message: " + e.getMessage());
					System.exit(1);
				} catch (Exception e) {
					logger.info("Application starting. Message: " + e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginWindow() {
		initialize();

		loadServerNameAndPort();

	}

	/**
	 * Create the application.
	 */
	public LoginWindow(String serverName, int port) {
		initialize();

		saveServerNameAndPort(serverName, port);
		this.serverNameTF.setText(serverName);
		this.portTF.setText(Integer.toString(port));
	}

	/**
	 * Create the application.
	 */
	public LoginWindow(String serverName, int port, String message) {
		initialize();

		saveServerNameAndPort(serverName, port);
		this.serverNameTF.setText(serverName);
		this.portTF.setText(Integer.toString(port));
		this.messageLabel.setText(message);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlackHole = new JFrame();
		frmBlackHole.setResizable(false);
		frmBlackHole.getContentPane().setBackground(Color.GRAY);
		frmBlackHole.getContentPane().setLayout(null);
		frmBlackHole.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblEnterToBlack = new JLabel("Enter to Black Hole");
		lblEnterToBlack.setForeground(Color.WHITE);
		lblEnterToBlack.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterToBlack.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEnterToBlack.setBounds(10, 11, 316, 25);
		frmBlackHole.getContentPane().add(lblEnterToBlack);

		messageLabel = new JLabel("");
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		messageLabel.setBounds(10, 154, 316, 25);
		frmBlackHole.getContentPane().add(messageLabel);

		JLabel lblServerName = new JLabel("Server Name:");
		lblServerName.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerName.setForeground(Color.WHITE);
		lblServerName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblServerName.setBounds(10, 47, 134, 25);
		frmBlackHole.getContentPane().add(lblServerName);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(10, 83, 134, 25);
		frmBlackHole.getContentPane().add(lblPassword);

		serverNameTF = new JTextField();
		serverNameTF.setHorizontalAlignment(SwingConstants.CENTER);
		serverNameTF.setDocument(new BHInputListener(BHInputListener.SERVER_NAME_TEXT_FIELD));
		serverNameTF.setBounds(168, 51, 134, 20);
		frmBlackHole.getContentPane().add(serverNameTF);
		serverNameTF.setColumns(10);

		passwordTF = new JTextField();
		passwordTF.setHorizontalAlignment(SwingConstants.CENTER);
		passwordTF.setDocument(new BHInputListener(BHInputListener.PASSWORD_TEXT_FIELD));
		passwordTF.setColumns(10);
		passwordTF.setBounds(168, 87, 134, 20);
		frmBlackHole.getContentPane().add(passwordTF);

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Start the loading animation
				processAnimation = new ProcessAnimatior(LoginWindow.this, ProcessAnimatior.LOADING_MESSAGE);
				processAnimation.start();
				btnLogin.setEnabled(false);

				// Send the login request to the server!
				String serverName = LoginWindow.this.serverNameTF.getText();
				String password = LoginWindow.this.passwordTF.getText();
				int port = Integer.parseInt(LoginWindow.this.portTF.getText());

				LoginWindow.this.controller.login(serverName, password, port);
			}
		});
		btnLogin.setBounds(63, 197, 110, 23);
		frmBlackHole.getContentPane().add(btnLogin);

		btnRegistration = new JButton("Registration");
		btnRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Start the registration window.
				new Thread() {
					@Override
					public void run() {
						new RegistrationWindow();
					}

				}.start();

				LoginWindow.this.frmBlackHole.dispose();
			}
		});
		btnRegistration.setBounds(195, 197, 108, 23);
		frmBlackHole.getContentPane().add(btnRegistration);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setForeground(Color.WHITE);
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPort.setBounds(10, 118, 134, 25);
		frmBlackHole.getContentPane().add(lblPort);

		portTF = new JTextField();
		portTF.setHorizontalAlignment(SwingConstants.CENTER);
		portTF.setDocument(new BHInputListener(BHInputListener.PORT_TEXT_FIELD));
		portTF.setText("9090");
		portTF.setColumns(10);
		portTF.setBounds(168, 123, 134, 20);
		frmBlackHole.getContentPane().add(portTF);
		frmBlackHole.setTitle("Black Hole - PC Server -");
		frmBlackHole.setBounds(600, 300, 344, 264);
		frmBlackHole.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBlackHole.setVisible(true);
	}

	/**
	 * @see #responseHandler(String)
	 */
	@Override
	public void responseHandler(String response) {
		if ("OK".equals(response)) { // Successful login!
			final String serverName = LoginWindow.this.serverNameTF.getText();
			final String password = LoginWindow.this.passwordTF.getText();
			final int port = Integer.parseInt(LoginWindow.this.portTF.getText());

			// Start the main window.
			new Thread() {
				@Override
				public void run() {
					new MainWindow(serverName, password, port);
				}
			}.start();

			// Save the Server name and port to a property file for the next
			// launch.
			saveServerNameAndPort(serverName, port);

			frmBlackHole.dispose();
		} else { // Unsuccessful login
			processAnimation.stopProcessing(response);
			btnLogin.setEnabled(true);
		}
	}

	/**
	 * @see #setMessageText(String, boolean)
	 */
	@Override
	public void setMessageText(String text, boolean stopProcessing) {
		if (stopProcessing) {
			processAnimation.stopProcessing(text);
			btnLogin.setEnabled(true);
		} else {
			this.messageLabel.setText(text);
		}
	}

	/**
	 * @see #getMessageText()
	 */
	@Override
	public String getMessageText() {
		return this.messageLabel.getText();
	}

	/**
	 * Extend the Server Name and Port text fields. conf.properties file will be used for it, if it doesn't exist now it will
	 * create it.
	 */
	private void loadServerNameAndPort() {
		try {
			Properties confProperty = new Properties();

			// Check the conf.propeties file and creates it if it not exists
			// yet.
			File confFile = new File("conf.properties");
			if (!confFile.exists()) {
				confFile.createNewFile();
				saveServerNameAndPort("Server Name", 9090);
			}

			// Load the date from the property file.
			confProperty.load(getClass().getClassLoader().getResourceAsStream("conf.properties"));
			String lastServerName = confProperty.getProperty("lastservername");
			String lastServerPort = confProperty.getProperty("lastserverport");

			// Extends the text fields.
			this.serverNameTF.setText(lastServerName);
			this.portTF.setText(lastServerPort);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Saves the server name and the port to the conf.properties property file.
	 * 
	 * @param lastServerName
	 *            Server Name
	 * @param lastServerPort
	 *            Password
	 */
	private void saveServerNameAndPort(String lastServerName, int lastServerPort) {
		try {
			Properties confProperty = new Properties();

			// Check the conf.properties file and creates it if it not exists
			// yet.
			File confFile = new File("conf.properties");
			if (!confFile.exists()) {
				confFile.createNewFile();
			}

			// Saves the informations to the property file.
			confProperty.setProperty("lastservername", lastServerName);
			confProperty.setProperty("lastserverport", Integer.toString(lastServerPort));
			confProperty.store(new FileOutputStream("conf.properties"), null);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

}
