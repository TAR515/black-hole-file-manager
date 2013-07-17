package ro.edu.ubb.blackholepcserver.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class can ensure an input type to Black Hole text fields.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class BHInputListener extends PlainDocument {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Number of maximum characters what the user can enter into the text field.
	 */
	private int maxChar;

	/**
	 * Text Field type. example Server Name, Port ...
	 */
	private String currentTFType = null;

	/**
	 * Server name text field.
	 */
	public static final String SERVER_NAME_TEXT_FIELD = "SERVER_NAME_TEXT_FIELD";

	/**
	 * Password text field.
	 */
	public static final String PASSWORD_TEXT_FIELD = "PASSWORD_TEXT_FIELD";

	/**
	 * Email text field.
	 */
	public static final String EMAIL_TEXT_FIELD = "EMAIL_TEXT_FIELD";

	/**
	 * Port text field.
	 */
	public static final String PORT_TEXT_FIELD = "PORT_TEXT_FIELD";

	/**
	 * Maximum characters in a Server Name text field.
	 */
	private static final int SERVER_NAME_TEXT_FIELD_MAX_CHAR = 15;

	/**
	 * Maximum characters in a Password text field.
	 */
	private static final int PASSWORD_TEXT_FIELD_MAX_CHAR = 15;

	/**
	 * Maximum characters in an Email field.
	 */
	private static final int EMAIL_TEXT_FIELD_MAX_CHAR = 15;

	/**
	 * Maximum characters in a Port text field.
	 */
	private static final int PORT_TEXT_FIELD_MAX_CHAR = 4;

	/**
	 * 
	 * @param textFieldType
	 *            specify the type of the text field. - SERVER_NAME_TEXT_FIELD - PASSWORD_TEXT_FIELD - EMAIL_TEXT_FIELD -
	 *            PORT_TEXT_FIELD
	 */
	public BHInputListener(String textFieldType) {
		this.currentTFType = textFieldType;

		switch (textFieldType) {
		case SERVER_NAME_TEXT_FIELD:
			this.maxChar = SERVER_NAME_TEXT_FIELD_MAX_CHAR;
			break;
		case PASSWORD_TEXT_FIELD:
			this.maxChar = PASSWORD_TEXT_FIELD_MAX_CHAR;
			break;
		case EMAIL_TEXT_FIELD:
			this.maxChar = EMAIL_TEXT_FIELD_MAX_CHAR;
			break;
		case PORT_TEXT_FIELD:
			this.maxChar = PORT_TEXT_FIELD_MAX_CHAR;
			break;
		}
	}

	/**
	 * @see #insertString(int, String, AttributeSet)
	 */
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (str == null) {
			return;
		}

		boolean correctInput = true;
		// Check the length.
		if ((getLength() + str.length()) > this.maxChar) {
			correctInput = false;
		}

		// In case of Port text field check the numeric format.
		if (this.currentTFType.equals(BHInputListener.PORT_TEXT_FIELD)) {
			Pattern pattern = Pattern.compile("([0-9]*)");
			Matcher matcher = pattern.matcher(str);

			if (!matcher.matches()) {
				correctInput = false;
			}
		}

		if (correctInput) {
			super.insertString(offs, str, a);
		}
	}

}
