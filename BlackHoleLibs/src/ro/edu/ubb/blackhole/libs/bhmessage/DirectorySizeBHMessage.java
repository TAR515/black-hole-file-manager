package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * Contains the size of a directory.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class DirectorySizeBHMessage extends BlackHoleMessage {

	/**
	 * Size of the directory.
	 */
	private long directorySize = -1;
	
	public DirectorySizeBHMessage() {
		super(BHMessageType.BH_DIRECTORY_SIZE_MESSAGE);
	}
	
	public DirectorySizeBHMessage(int requestMessageType) {
		super(BHMessageType.BH_DIRECTORY_SIZE_MESSAGE, requestMessageType);
	}

	public long getDirectorySize() {
		return directorySize;
	}

	public void setDirectorySize(long directorySize) {
		this.directorySize = directorySize;
	}
	
}
