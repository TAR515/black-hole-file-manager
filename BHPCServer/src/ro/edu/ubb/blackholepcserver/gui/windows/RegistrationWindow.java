package ro.edu.ubb.blackholepcserver.gui.windows;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.errmsg.PCSErrorMessageInterpreter;
import ro.edu.ubb.blackholepcserver.bll.PCOperationService;
import ro.edu.ubb.blackholepcserver.bll.PCOperator;
import ro.edu.ubb.blackholepcserver.gui.BHInputListener;
import ro.edu.ubb.blackholepcserver.gui.ProcessAnimatior;
import ro.edu.ubb.blackholepcserver.gui.ServerCommunicatorGUI;

/**
 * This window provide a GUI for a registration.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class RegistrationWindow implements ServerCommunicatorGUI {

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getLogger(RegistrationWindow.class);

	/**
	 * Main frame.
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
	 * Email text field.
	 */
	private JTextField emailTF;

	/**
	 * Message Label, here will display the error and the information messages.
	 */
	private JLabel messageLabel;

	/**
	 * Port text field.
	 */
	private JTextField portTF;

	/**
	 * Registration button.
	 */
	private JButton btnRegistration;

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
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrationWindow window = new RegistrationWindow();
					window.frmBlackHole.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RegistrationWindow() {
		try {
			guiMessagesProperties.load(getClass().getClassLoader().getResourceAsStream(
					"guimessages.properties"));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlackHole = new JFrame();
		frmBlackHole.getContentPane().setBackground(Color.GRAY);
		frmBlackHole.setResizable(false);
		frmBlackHole.setTitle("Black Hole - PC Server -");
		frmBlackHole.setBounds(600, 300, 395, 305);
		frmBlackHole.getContentPane().setLayout(null);
		// Do not exit application when exit button pressed, just dispose this
		// window.
		frmBlackHole.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JLabel lblNewLabel = new JLabel("Register your server");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(10, 11, 369, 25);
		frmBlackHole.getContentPane().add(lblNewLabel);

		JLabel lblServerName = new JLabel("Server Name:");
		lblServerName.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerName.setForeground(Color.WHITE);
		lblServerName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblServerName.setBounds(10, 47, 117, 25);
		frmBlackHole.getContentPane().add(lblServerName);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(10, 83, 117, 25);
		frmBlackHole.getContentPane().add(lblPassword);

		JLabel lblEmailAddress = new JLabel("Email Address:");
		lblEmailAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmailAddress.setForeground(Color.WHITE);
		lblEmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmailAddress.setBounds(10, 119, 117, 25);
		frmBlackHole.getContentPane().add(lblEmailAddress);

		serverNameTF = new JTextField();
		serverNameTF.setDocument(new BHInputListener(BHInputListener.SERVER_NAME_TEXT_FIELD));
		serverNameTF.setHorizontalAlignment(SwingConstants.CENTER);
		serverNameTF.setBounds(161, 51, 163, 20);
		frmBlackHole.getContentPane().add(serverNameTF);
		serverNameTF.setColumns(10);

		passwordTF = new JTextField();
		passwordTF.setDocument(new BHInputListener(BHInputListener.PASSWORD_TEXT_FIELD));
		passwordTF.setHorizontalAlignment(SwingConstants.CENTER);
		passwordTF.setColumns(10);
		passwordTF.setBounds(161, 87, 163, 20);
		frmBlackHole.getContentPane().add(passwordTF);

		emailTF = new JTextField();
		emailTF.setDocument(new BHInputListener(BHInputListener.EMAIL_TEXT_FIELD));
		emailTF.setHorizontalAlignment(SwingConstants.CENTER);
		emailTF.setColumns(30);
		emailTF.setBounds(161, 123, 163, 20);
		frmBlackHole.getContentPane().add(emailTF);

		messageLabel = new JLabel("");
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		messageLabel.setBounds(10, 206, 369, 25);
		frmBlackHole.getContentPane().add(messageLabel);

		btnRegistration = new JButton("Registration");
		btnRegistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String serverName = RegistrationWindow.this.serverNameTF.getText();
				String password = RegistrationWindow.this.passwordTF.getText();
				String email = RegistrationWindow.this.emailTF.getText();
				int port = Integer.parseInt(RegistrationWindow.this.portTF.getText());

				// If some informations are missing.
				if ("".equals(serverName) || "".equals(password) || "".equals(email) || "".equals(port)) {
					// Visualizes "All informations are required" error message.
					int errCode = PCSErrorMessageInterpreter.getErrorCode(PCSErrorMessageInterpreter.PCS_ALL_INFORMATIONS_ARE_REQUIRED);
					String errMsg = guiMessagesProperties.getProperty(Integer.toString(errCode));
					RegistrationWindow.this.messageLabel.setText(errMsg);
				} else { // Send a registration message to server.
					RegistrationWindow.this.messageLabel.setText("");
					processAnimation = new ProcessAnimatior(RegistrationWindow.this,
							ProcessAnimatior.LOADING_MESSAGE);
					processAnimation.start();
					btnRegistration.setEnabled(false);

					RegistrationWindow.this.controller.registration(serverName, password, email, port);
				}
			}
		});
		btnRegistration.setBounds(58, 242, 108, 23);
		frmBlackHole.getContentPane().add(btnRegistration);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Close this window
				RegistrationWindow.this.frmBlackHole.dispose();

				// Start a new Login Window
				new Thread() {
					@Override
					public void run() {
						new LoginWindow();
					}
				}.start();
			}
		});
		btnCancel.setBounds(199, 242, 108, 23);
		frmBlackHole.getContentPane().add(btnCancel);

		JLabel lblServerPort = new JLabel("Server Port:");
		lblServerPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerPort.setForeground(Color.WHITE);
		lblServerPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblServerPort.setBounds(10, 155, 117, 25);
		frmBlackHole.getContentPane().add(lblServerPort);

		portTF = new JTextField();
		portTF.setDocument(new BHInputListener(BHInputListener.PORT_TEXT_FIELD));
		portTF.setHorizontalAlignment(SwingConstants.CENTER);
		portTF.setText("9090");
		portTF.setColumns(10);
		portTF.setBounds(161, 159, 163, 20);
		frmBlackHole.getContentPane().add(portTF);

		JLabel lblJustFor = new JLabel("Just for experienced users.");
		lblJustFor.setHorizontalAlignment(SwingConstants.CENTER);
		lblJustFor.setForeground(Color.WHITE);
		lblJustFor.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblJustFor.setBounds(161, 179, 163, 20);
		frmBlackHole.getContentPane().add(lblJustFor);

		frmBlackHole.setVisible(true);
	}

	/**
	 * @see #responseHandler(String)
	 */
	@Override
	public void responseHandler(String response) {
		if ("OK".equals(response)) { // Successful registration!

			// Start a new Login Window.
			new Thread() {
				@Override
				public void run() {
					String serverName = RegistrationWindow.this.serverNameTF.getText();

					int port = Integer.parseInt(RegistrationWindow.this.portTF.getText());

					new LoginWindow(serverName, port);
				}
			}.start();

			frmBlackHole.dispose();
		} else { // Unsuccessful registration
			btnRegistration.setEnabled(true);
		}

		processAnimation.stopProcessing(response);
	}

	/**
	 * @see #setMessageText(String, boolean)
	 */
	@Override
	public void setMessageText(String text, boolean stopProcessing) {
		if (stopProcessing) {
			processAnimation.stopProcessing(text);
			btnRegistration.setEnabled(true);
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
}
