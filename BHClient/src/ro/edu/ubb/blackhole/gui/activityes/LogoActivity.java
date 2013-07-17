package ro.edu.ubb.blackhole.gui.activityes;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Visualize the project logo when the application starts. The user can skip it by clicking on it.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class LogoActivity extends Activity {

	/**
	 * This thread is counting the time as long as the loading screen alives.
	 */
	private Timer timerThread = null;

	/**
	 * @see #onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
	}

	/**
	 * @see #onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		this.timerThread = new Timer();
		this.timerThread.start();
	}

	/**
	 * @see #onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		// If we press back after the activity started correctly the timer thread have't start the ServerSelectActivity anymore.
		if (this.timerThread != null) {
			this.timerThread.serverSelectActivityStarted();
			this.timerThread = null;
		}
	}

	/**
	 * Starts the {@link ServerSelectActivity} insantly when the user click on the {@link LogoActivity}.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.timerThread != null) { // If the activity started correctly.
			// The timer thread have't start the ServerSelectActivity anymore.
			this.timerThread.serverSelectActivityStarted();
			this.timerThread = null;

			// Starts the Server Select Activity.
			Intent myIntent = new Intent(LogoActivity.this, ServerSelectActivity.class);
			startActivityForResult(myIntent, 0);
		}
		return super.onTouchEvent(event);
	}

	/**
	 * After some secounds this thread will starts ServerSelectActivity.
	 * 
	 * @author Turdean Arnold Robert
	 * 
	 */
	class Timer extends Thread {
		/**
		 * If this is true than the tread will starts ServerSelectActivity, else it does nothing.
		 */
		private boolean startServerSelectActiviry = true;

		private int sleepingTimeInSecounds = 4;

		public void run() {
			try {
				sleep(this.sleepingTimeInSecounds * 1000);

				if (this.startServerSelectActiviry) {
					// Starts the Server Select Activity.
					Intent myIntent = new Intent(LogoActivity.this, ServerSelectActivity.class);
					startActivityForResult(myIntent, 0);
				}
			} catch (InterruptedException e) {
				Log.w("Interrupted sleep: " + e.getMessage());
			}
		}

		public void serverSelectActivityStarted() {
			this.startServerSelectActiviry = false;
		}
	}
}
