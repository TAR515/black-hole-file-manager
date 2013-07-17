package ro.edu.ubb.blackhole.gui;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Provides a loading dialog.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class LoadingScreen extends Thread {

	/**
	 * Semaphore which controls the loading screen.
	 */
	private Object semaphore = new Object();

	/**
	 * The loading dialog.
	 */
	private ProgressDialog loadingDialog = null;

	/**
	 * Constructor, initializing the components.
	 * 
	 * @param context
	 *            The context where to draw the loading dialog.
	 * @param guiRequestHandler
	 *            The request handler, it needs when we want to cancel the loading.
	 */
	public LoadingScreen(Context context, final GuiRequestHandler guiRequestHandler) {

		// Initializing the dialog.
		this.loadingDialog = new ProgressDialog(context);
		this.loadingDialog.setTitle(context.getString(R.string.loading_dialog_title));
		this.loadingDialog.setMessage(context.getString(R.string.loading_dialog_message));
		this.loadingDialog.setButton(-1, context.getString(R.string.loading_dialog_cancel),
				new DialogInterface.OnClickListener() {

					// The cancel button. Which cancels the loading.
					public void onClick(DialogInterface dialog, int which) {
						Log.i("Cancelled!");

						// Cancel the AsyncTask, the user interface request handler.
						guiRequestHandler.onCancelled();
					}
				});

		// Showing the dialog to the given context, and starting the loading.
		this.loadingDialog.show();
		this.start();
	}

	/**
	 * Loading until somebody calls the "dismissDialog" method.
	 */
	public void run() {
		synchronized (this.semaphore) { // Waiting for dismissDialog method to notify the semaphore.
			try {
				this.semaphore.wait();
			} catch (InterruptedException e) {
				Log.w(e.getMessage());
			}
		}

		// Dismiss the dialog, stops the loading.
		this.loadingDialog.dismiss();
	}

	/**
	 * Stops the loading, dismiss the dialog from the context.
	 */
	public void dismissDialog() {
		synchronized (this.semaphore) { // Notify the semaphore and stops the loading.
			Log.i("Stop the loading.");

			this.semaphore.notifyAll();
		}
	}
}
