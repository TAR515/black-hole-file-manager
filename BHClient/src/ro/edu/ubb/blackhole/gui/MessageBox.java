package ro.edu.ubb.blackhole.gui;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import android.app.AlertDialog;
import android.content.Context;

/**
 * Provide some standard message box.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class MessageBox {

	/**
	 * Context of the application.
	 */
	private Context context = null;

	public MessageBox(Context context) {
		this.context = context;
	}

	/**
	 * Creates an information message box.
	 * 
	 * @param message
	 *            The information which you want to visualize.
	 */
	public void createInformationMessageBox(String message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
		alertDialog.setTitle(getString(R.string.information_dialog_title));
		alertDialog.setMessage(message);

		alertDialog.show();
	}

	/**
	 * Creates a warning message box.
	 * 
	 * @param message
	 *            The information which you want to visualize.
	 */
	public AlertDialog.Builder createWarningMessageBox(String message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
		alertDialog.setTitle(getString(R.string.warning_dialog_title));
		alertDialog.setMessage(message);

		alertDialog.show();
		return alertDialog;
	}

	/**
	 * Creates a message box which visualize the given error message!
	 * 
	 * @param errorMessage
	 *            error message.
	 */
	public void createErrorMessageBox(String errorMessage) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
		alertDialog.setTitle(getString(R.string.error_dialog_title));
		alertDialog.setMessage(errorMessage);

		alertDialog.show();
	}

	/**
	 * Creates a message box which visualize all the properties of the given file.
	 * 
	 * @param file
	 */
	public void createPropertiesMessageBox(FileItem file) {
		String fileNameRes = getString(R.string.inf_file_name) + " ";
		String filePathRes = getString(R.string.inf_file_path) + " ";
		String fileSizeRes = getString(R.string.inf_file_size) + " ";
		String filePermRes = getString(R.string.inf_file_permissions) + " ";
		String fileDateRes = getString(R.string.inf_file_date) + " ";
		String nl = "\n";

		String message = fileNameRes + file.getFileName() + nl + nl + filePathRes + file.getFilePath() + nl
				+ fileSizeRes + file.getFileSize() + nl + filePermRes + file.getFilePermissions() + nl
				+ fileDateRes + file.getLastModifiedDateUserFriendly();

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
		alertDialog.setTitle(getString(R.string.context_meniu_properties));
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton("Ok", null);

		alertDialog.show();
	}

	/**
	 * Returns a value from Android the values.
	 * 
	 * @param resourceId
	 *            id of the resource.
	 * @return value.
	 */
	private String getString(int resourceId) {
		return this.context.getString(resourceId);
	}

}
