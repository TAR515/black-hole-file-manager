package ro.edu.ubb.blackhole.dal.pcfilemanager;

/**
 * This object contains just the response of the PC Server or the Main Server.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ResponseStringContainer {

	/**
	 * Response of the PC Server or the Main Server.
	 */
	private String response = null;

	public ResponseStringContainer(String response) {
		super();

		this.response = response;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
