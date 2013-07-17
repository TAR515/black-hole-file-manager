package ro.edu.ubb.blackholepcserver.bll.processcontroller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.datastructures.Process;
import ro.edu.ubb.blackholepcserver.bll.processcontroller.windows.WindowsProcessOperationAdapter;

/**
 * This class recognize the operation system which runs into the current machine. After the operation system recognition the
 * request is transmitted to the correct subcomponent which implements the basic operations to control the processes on the
 * current machine.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ProcessOperationAdapter implements ProcessController {

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getLogger(ProcessOperationAdapter.class);

	/**
	 * This variable indicates the type of the operation system which runs on the current machine.
	 */
	private static String OS = System.getProperty("os.name").toLowerCase();

	/**
	 * Process Controller.
	 */
	private ProcessController processController = null;

	public ProcessOperationAdapter() {
		if (isWindows()) {
			this.processController = new WindowsProcessOperationAdapter();
		} else if (isMac()) {
			logger.warn("The operation system is not supported yet!");
		} else if (isUnix()) {
			logger.warn("The operation system is not supported yet!");
		} else if (isSolaris()) {
			logger.warn("The operation system is not supported yet!");
		} else {
			logger.warn("The operation system is not supported!");
			this.processController = null;
		}
	}

	/**
	 * @see #getRunningProcesses()
	 */
	@Override
	public List<Process> getRunningProcesses() throws IOException {
		if (this.processController != null) {
			return this.processController.getRunningProcesses();
		}

		return null;
	}

	/**
	 * @see #isProcessRunging(String)
	 */
	@Override
	public boolean isProcessRunging(String serviceName) throws IOException {
		if (this.processController != null) {
			return this.processController.isProcessRunging(serviceName);
		}

		return false;
	}

	/**
	 * @see #startProgram(String)
	 */
	@Override
	public boolean startProgram(String programPath) throws IOException {
		if (this.processController != null) {
			return this.processController.startProgram(programPath);
		}

		return false;
	}

	/**
	 * @see #startPrograms(List)
	 */
	public boolean startPrograms(List<String> programPaths) {
		if (this.processController != null) {
			return this.processController.startPrograms(programPaths);
		}

		return false;
	}

	/**
	 * @see #killProcess()
	 */
	@Override
	public boolean killProcess(String serviceName) throws IOException {
		if (this.processController != null) {
			return this.processController.killProcess(serviceName);
		}

		return false;
	}

	/**
	 * @see #killProcesses(List)
	 */
	public boolean killProcesses(List<String> serviceNames) {
		if (this.processController != null) {
			return this.processController.killProcesses(serviceNames);
		}

		return false;
	}

	/**
	 * Check if the current operation system is Windows or not.
	 * @return True - if the operation system is Windows / False - if not.
	 */
	private boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
	
	/**
	 * Check if the current operation system is Mac or not.
	 * @return True - if the operation system is Mac / False - if not.
	 */
	private boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	/**
	 * Check if the current operation system is Unix or not.
	 * @return True - if the operation system is Unix / False - if not.
	 */
	private boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	/**
	 * Check if the current operation system is Solaris or not.
	 * @return True - if the operation system is Solaris / False - if not.
	 */
	private boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

}
