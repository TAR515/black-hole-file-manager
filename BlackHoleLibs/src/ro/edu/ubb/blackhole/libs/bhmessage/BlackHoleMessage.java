package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * Black Hole message class. All Black Hole messages are extended from this class.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public abstract class BlackHoleMessage {
	/**
	 * This constant indicates the type of the message.
	 */
	int messageType = Integer.MIN_VALUE;

	/**
	 * The type of the request message.
	 */
	int requestMessageType = Integer.MIN_VALUE;

	public BlackHoleMessage() {
		super();
	}

	public BlackHoleMessage(int messageType) {
		super();
		this.messageType = messageType;
	}

	public BlackHoleMessage(int messageType, int requestMessageType) {
		super();
		this.messageType = messageType;
		this.requestMessageType = requestMessageType;
	}

	public int getMessageType() {
		return messageType;
	}

	@Deprecated
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getRequestMessageType() {
		return requestMessageType;
	}

	public void setRequestMessageType(int requestMessageType) {
		this.requestMessageType = requestMessageType;
	}

}
