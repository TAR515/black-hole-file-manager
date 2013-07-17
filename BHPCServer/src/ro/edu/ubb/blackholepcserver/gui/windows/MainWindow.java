package ro.edu.ubb.blackholepcserver.gui.windows;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackholepcserver.bll.IPChanger;
import ro.edu.ubb.blackholepcserver.bll.PCOperationService;
import ro.edu.ubb.blackholepcserver.bll.PCOperator;
import ro.edu.ubb.blackholepcserver.gui.ProcessAnimatior;
import ro.edu.ubb.blackholepcserver.gui.ServerCommunicatorGUI;

/**
 * This object provide a GUI. This GUI represents the control panel of the application.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class MainWindow implements ServerCommunicatorGUI {
	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getLogger(RegistrationWindow.class);

	/**
	 * Main frame
	 */
	private JFrame frmBlackHole;

	/**
	 * Message label.
	 */
	private JLabel messageLabel;

	/**
	 * Login button.
	 */
	private JButton btnLogout;

	/**
	 * Server Name.
	 */
	public static String serverName = null;

	/**
	 * Password.
	 */
	public static String password = null;

	/**
	 * Port.
	 */
	public static int port = -1;

	/**
	 * Controller which will process the operations.
	 */
	private PCOperationService controller = new PCOperator(this);

	/**
	 * This thread handled object will realize the processing animations.
	 */
	private ProcessAnimatior processAnimation = null;

	/**
	 * Default GUI message property controller.
	 */
	private static Properties guiMessagesProperties = new Properties();
	
	/**
	 * This thread periodically refresh the current IP.
	 */
	private IPChanger ipChanger = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainWindow("", "", 9090);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow(String serverName, String password, int port) {
		MainWindow.serverName = serverName;
		MainWindow.password = password;
		MainWindow.port = port;

		try {
			guiMessagesProperties.load(getClass().getClassLoader().getResourceAsStream(
					"guimessages.properties"));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		initialize();

		// Start the Tomcat web server.
		startServer();
		
		// Start the IP refreshing mechanism.
		this.ipChanger = new IPChanger(controller, serverName, password, port);
		this.ipChanger.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlackHole = new JFrame();
		frmBlackHole.getContentPane().setBackground(Color.GRAY);
		frmBlackHole.setResizable(false);
		frmBlackHole.setTitle("Black Hole - PC Server -");
		frmBlackHole.setBounds(600, 300, 350, 209);
		frmBlackHole.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBlackHole.getContentPane().setLayout(null);
		// Do not exit application when exit button pressed, just dispose this
		// window.
		frmBlackHole.setDefaultCloseOperation(1);

		frmBlackHole.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// Stop the IP refreshing mechanism.
				ipChanger.stopIPChanger();
				
				stopServer();

				// Dispose window and exit application!
				MainWindow.this.frmBlackHole.dispose();
				System.exit(1);
			}
		});

		JLabel lblBlackHoleControl = new JLabel("Black Hole Control Panel");
		lblBlackHoleControl.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlackHoleControl.setForeground(Color.WHITE);
		lblBlackHoleControl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBlackHoleControl.setBounds(10, 11, 324, 24);
		frmBlackHole.getContentPane().add(lblBlackHoleControl);

		btnLogout = new JButton("Stop Server");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Creates a loading animation untill log out.
				processAnimation = new ProcessAnimatior(MainWindow.this, ProcessAnimatior.LOADING_MESSAGE);
				processAnimation.start();
				btnLogout.setEnabled(false);

				// Send a logout request to the Black Hole main server.
				MainWindow.this.controller.logout(serverName, password, port);
			}
		});
		btnLogout.setBounds(206, 142, 124, 23);
		frmBlackHole.getContentPane().add(btnLogout);

		messageLabel = new JLabel("");
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		messageLabel.setBounds(10, 80, 324, 24);
		frmBlackHole.getContentPane().add(messageLabel);

		JLabel lblServerStatus = new JLabel("Server Status:");
		lblServerStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerStatus.setForeground(Color.WHITE);
		lblServerStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblServerStatus.setBounds(10, 56, 138, 24);
		frmBlackHole.getContentPane().add(lblServerStatus);

		frmBlackHole.setVisible(true);
	}

	/**
	 * @see #responseHandler(String)
	 */
	@Override
	public void responseHandler(String response) {
		// Stops the loading animation.
		processAnimation.stopProcessing(response);

		if ("OK".equals(response)) { // Successful logout!
			if (stopServer()) {
				// Stop the IP refreshing mechanism.
				this.ipChanger.stopIPChanger();
				
				// Start the login window.
				new Thread() {
					@Override
					public void run() {
						new LoginWindow(serverName, port);
					}
				}.start();

				frmBlackHole.dispose();
			} else { // If the server wont shutdown then kill all application!
				System.exit(1);
			}
		} else { // Unsuccessful logout, stops the server and exit from
					// application.
			if (stopServer()) {
				// Start the login window.
				new Thread() {
					@Override
					public void run() {
						new LoginWindow();
					}
				}.start();

				frmBlackHole.dispose();
			} else { // If the server wont shutdown then kill all application!
				System.exit(1);
			}
		}
	}

	/**
	 * @see #setMessageText(String, boolean)
	 */
	@Override
	public void setMessageText(String text, boolean stopProcessing) {
		if (stopProcessing) {
			processAnimation.stopProcessing(text);
			btnLogout.setEnabled(true);
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
	 * Starts the Tomcat main server.
	 * 
	 * @return True - if the server were started / False - if not
	 */
	private boolean startServer() {
		boolean isStarted = MainWindow.this.controller.startTomcat(MainWindow.port);
		if (isStarted) { // Tomcat server were started, setting the message!
			int errCode = PCSErrorMessageInterpreter.getErrorCode(PCSErrorMessageInterpreter.PCS_SERVER_IS_RUNNING);
			MainWindow.this.messageLabel.setText(guiMessagesProperties.getProperty(Integer.toString(errCode)));
		} else { // The Tomcat server weren't started, make an error message and logout.
			int errCode = PCSErrorMessageInterpreter.getErrorCode(PCSErrorMessageInterpreter.PCS_SERVER_WONT_STARTED);
			String message = guiMessagesProperties.getProperty(Integer.toString(errCode));
			message += " Loging out ";

			processAnimation = new ProcessAnimatior(MainWindow.this, message);
			processAnimation.start();
			btnLogout.setEnabled(false);

			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// Nothing to do.
			}

			MainWindow.this.controller.logout(serverName, password, port);
		}

		return isStarted;
	}

	/**
	 * Stops the Tomcat web server.
	 * 
	 * @return True - If the server were stoped / False - if not.
	 */
	private boolean stopServer() {
		boolean isStoped = MainWindow.this.controller.stopTomcat();
		if (isStoped) { // The server has stopped.
			MainWindow.this.messageLabel.setText(guiMessagesProperties.getProperty("3"));
		} else { // The server were not stopped, make an error message.
			MainWindow.this.messageLabel.setText(guiMessagesProperties.getProperty("-103"));
		}

		return isStoped;
	}
}
