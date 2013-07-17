package ro.edu.ubb.blackholepcserver.bll.processcontroller;

import java.io.IOException;
import java.util.List;
import ro.edu.ubb.blackhole.libs.datastructures.Process;

/**
 * This is the interface of the {@ling ProcessController} component. The component provides the basic operations to control the
 * processes on the current machine, such as starting or stopping a process.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface ProcessController {

	/**
	 * Returns all processes running on computer.
	 * 
	 * @return Running processes on computer or NULL if the current operating system is not supported.
	 * @throws IOException
	 *             If some unforeseen error were occurred.
	 */
	public List<Process> getRunningProcesses() throws IOException;

	/**
	 * Check if the given process is running on computer or not.
	 * 
	 * @param serviceName
	 *            Name of the process/service.
	 * @return True - if the given process is running on the computer / False - if the given process isn't running or if the
	 *         current operating system is not supported.
	 * @throws IOException
	 *             If the given serviceName equals with null, or if some unforeseen error were occurred.
	 */
	public boolean isProcessRunging(String serviceName) throws IOException;

	/**
	 * Execute the given program on the computer with the default settings.
	 * 
	 * @param programPath
	 *            Path of the program.
	 * @return True - If the program were executed correctly / False - If the given path is invalid or the current operating
	 *         system is not supported.
	 * @throws IOException
	 *             If the given path equals with null, or if the file not found or if some unforeseen error were occurred.
	 */
	public boolean startProgram(String programPath) throws IOException;

	/**
	 * Starts the given programs on the computer.
	 * 
	 * @param programPaths
	 *            Paths of the programs.
	 * @return True - If all programs were started correctly / False - If one or more programs were not started or the operating
	 *         system is not supported.
	 * @throws IOException
	 *             If some unforeseen error were occurred.
	 */
	public boolean startPrograms(List<String> programPaths);

	/**
	 * Kills a process on the computer.
	 * 
	 * @param serviceName
	 *            Name of the process/service.
	 * @return True - if the given process is killed now or it were not running / False - If the current operating system is not
	 *         supported or the process were not stopped correctly.
	 * @throws IOException
	 *             If the given serviceName equals with null, or if some unforeseen error were occurred.
	 */
	public boolean killProcess(String serviceName) throws IOException;

	/**
	 * Kills the given processes on the computer.
	 * 
	 * @param serviceNames
	 *            Name of the processes.
	 * @return True - If all processes were killed successfully (or some of them were not running) / False - If one or more
	 *         processes were not killed successfully.
	 */
	public boolean killProcesses(List<String> serviceNames);

}
