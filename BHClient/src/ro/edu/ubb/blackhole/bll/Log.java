package ro.edu.ubb.blackhole.bll;

/**
 * This object provide a custom log system based to Android log system. The difference between the two logging system is the TAG
 * component. In this case the TAG component allways contains the class name, method name and line number where the log were
 * created.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class Log {

	/**
	 * Full class name in which the log were created.
	 */
	private static String FULL_CLASS_NAME = null;

	/**
	 * Short class name in which the log were created.
	 */
	private static String CLASS_NAME = null;

	/**
	 * Method name in which the log were created.
	 */
	private static String METHOD_NAME = null;

	/**
	 * Line number in which the log were created.
	 */
	private static int LINE_NUMBER = -1;

	/**
	 * Creates a verbose log.
	 * 
	 * @param message
	 *            Message of the log.
	 */
	public static void v(String message) {
		getLogInfo();

		android.util.Log.v(CLASS_NAME + "." + METHOD_NAME + "():" + LINE_NUMBER, message);
	}

	/**
	 * Creates a debug log.
	 * 
	 * @param message
	 *            Message of the log.
	 */
	public static void d(String message) {
		getLogInfo();

		android.util.Log.d(CLASS_NAME + "." + METHOD_NAME + "():" + LINE_NUMBER, message);
	}

	/**
	 * Creates a inform log.
	 * 
	 * @param message
	 *            Message of the log.
	 */
	public static void i(String message) {
		getLogInfo();

		android.util.Log.i(CLASS_NAME + "." + METHOD_NAME + "():" + LINE_NUMBER, message);
	}

	/**
	 * Creates a warning log.
	 * 
	 * @param message
	 *            Message of the log.
	 */
	public static void w(String message) {
		getLogInfo();

		android.util.Log.w(CLASS_NAME + "." + METHOD_NAME + "():" + LINE_NUMBER, message);
	}

	/**
	 * Creates a error log.
	 * 
	 * @param message
	 *            Message of the log.
	 */
	public static void e(String message) {
		getLogInfo();

		android.util.Log.e(CLASS_NAME + "." + METHOD_NAME + "():" + LINE_NUMBER, message);
	}

	/**
	 * Creates a wtf log.
	 * 
	 * @param message
	 *            Message of the log.
	 */
	public static void wtf(String message) {
		getLogInfo();

		android.util.Log.wtf(CLASS_NAME + "." + METHOD_NAME + "():" + LINE_NUMBER, message);
	}

	/**
	 * Get all required informations to create the log.
	 */
	private static void getLogInfo() {
		FULL_CLASS_NAME = Thread.currentThread().getStackTrace()[4].getClassName();
		CLASS_NAME = FULL_CLASS_NAME.substring(FULL_CLASS_NAME.lastIndexOf(".") + 1);
		METHOD_NAME = Thread.currentThread().getStackTrace()[4].getMethodName();
		LINE_NUMBER = Thread.currentThread().getStackTrace()[4].getLineNumber();
	}

}
