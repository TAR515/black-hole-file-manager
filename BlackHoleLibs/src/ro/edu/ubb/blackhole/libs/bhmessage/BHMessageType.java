package ro.edu.ubb.blackhole.libs.bhmessage;

/**
 * Contains all Black Hole message types.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class BHMessageType {
	// 0 - 100
	public static final int BH_SIMPLE_RESPONSE_MESSAGE = 1;

	// PCServer - MainServer 100 - 399
	public static final int BH_REGISTRATION_MESSAGE = 100;
	public static final int BH_LOG_IN_MESSAGE = 101;
	public static final int BH_LOG_OUT_MESSAGE = 102;
	public static final int BH_IP_CHANGE_MESSAGE = 103;

	// Phone - Main Server 400 - 699
	public static final int BH_GET_IP_AND_PORT_MESSAGE = 400;
	public static final int BH_PC_SERVER_IP_AND_PORT_MESSAGE = 401;

	// Phone - PCServer 700 - 999
	public static final int BH_GET_LIST_OF_FILES_MESSAGE = 700;
	public static final int BH_LIST_OF_FILES_MESSAGE = 701;
	public static final int BH_GET_DIRECTORY_SIZE_MESSAGE = 702;
	public static final int BH_DIRECTORY_SIZE_MESSAGE = 703;
	public static final int BH_DELETE_FILE_MESSAGE = 704;
	public static final int BH_PC_SERVER_CHECK_MESSAGE = 705;
	public static final int BH_GET_RUNNING_PROGRAMS_MESSAGE = 706;
	public static final int BH_RUNNING_PROGRAMS_MESSAGE = 707;
	public static final int BH_STOP_PROGRAM_MESSAGE = 708;
	public static final int BH_START_PROGRAM_MESSAGE = 709;
}
