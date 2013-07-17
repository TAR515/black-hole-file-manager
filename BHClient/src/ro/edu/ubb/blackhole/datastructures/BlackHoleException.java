package ro.edu.ubb.blackhole.datastructures;

/**
 * This object realize a typical Black Hole exception.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class BlackHoleException extends Exception {

	/**
	 * Default serial version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * File not found exception message.
	 */
	public static final String FILE_NOT_FOUND_EXCEPTION = "FILE_NOT_FOUND_EXCEPTION";

	/**
	 * Contains the exception message.
	 */
	private String blackHoleException = null;

	/**
	 * Contains the exception detailed message.
	 */
	private String detailMessage = null;

	/**
	 * Contains the exception throwable.
	 */
	private Throwable throwable = null;

	/**
	 * Generate a new BlackHoleException
	 * 
	 * @param blackHoleException
	 *            One of the exception type. (example: BlackHoleException.FILE_NOT_FOUND_EXCEPTION)
	 * @param detailMessage
	 *            The class and the method which throws the exception with a short description about the exception.
	 * @param throwable
	 *            The throwable.
	 */
	public BlackHoleException(String blackHoleException, String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);

		this.blackHoleException = blackHoleException;
		this.detailMessage = detailMessage;
		this.throwable = throwable;
	}

	/**
	 * Generate a new BlackHoleException
	 * 
	 * @param blackHoleException
	 *            One of the exception type. (example: BlackHoleException.FILE_NOT_FOUND_EXCEPTION)
	 * @param detailMessage
	 *            The class and the method which throws the exception with a short description about the exception.
	 */
	public BlackHoleException(String blackHoleException, String detailMessage) {
		super(detailMessage);

		this.blackHoleException = blackHoleException;
		this.detailMessage = detailMessage;
	}

	public String getException() {
		return this.blackHoleException;
	}

	public String getDetailMessage() {
		return this.detailMessage;
	}

	public Throwable getThrowable() {
		return this.throwable;
	}
}
