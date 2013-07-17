package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * This type of object contains a simple response from the Black Hole main server or the Black Hole PC server.
 * 
 * @author Administrator
 * @version 1.0
 */
public class SimpleBHMessage extends BlackHoleMessage {
	/**
	 * This code represents the server response.
	 */
	private int responseCode = Integer.MIN_VALUE;

	public SimpleBHMessage() {
		super(BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE, Integer.MIN_VALUE);
	}

	public SimpleBHMessage(int responseCode, int requestMessageType) {
		super(BHMessageType.BH_SIMPLE_RESPONSE_MESSAGE, requestMessageType);
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

}
