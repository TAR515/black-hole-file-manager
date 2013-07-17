package ro.edu.ubb.blackholepcserver.bll.processcontroller.windowsprocesscontroller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import ro.edu.ubb.blackhole.libs.datastructures.Process;
import ro.edu.ubb.blackholepcserver.bll.processcontroller.ProcessController;

public class WindowsProcessOperationAdapter implements ProcessController {

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getLogger(WindowsProcessOperationAdapter.class);

	private static final String TASKLIST = "tasklist";
	private static final String KILL = "taskkill /F /IM ";

	/**
	 * @see #getRunningProcesses()
	 */
	@Override
	public List<Process> getRunningProcesses() throws IOException {
		List<Process> runningProcesses = new ArrayList<Process>();

		try {
			java.lang.Process processCommand = Runtime.getRuntime().exec(TASKLIST);
			BufferedReader input = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));

			String line = null;
			while ((line = input.readLine()) != null) {
				Pattern pattern = Pattern.compile("(.*?)[ ]{2,}(\\d+) (\\w+) *(\\d+) *(.*)");
				Matcher matcher = pattern.matcher(line);

				while (matcher.find()) {
					try {
						Process process = new Process(matcher.group(1));
						process.setPid(Integer.parseInt(matcher.group(2)));
						process.setSessionName(matcher.group(3));
						process.setSessionCount(Integer.parseInt(matcher.group(4)));
						process.setMemoryUsage(matcher.group(5));

						runningProcesses.add(process);
					} catch (Exception e) {
						// We would not stop scanning all processes because a problem with one line.
						logger.warn("Problem occured in a row while we would take the process informations from it.");
					}

				}
			}

			input.close();
			Collections.sort(runningProcesses);
			return runningProcesses;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new IOException(e.getMessage(), e);
		}
	}

	/**
	 * @see #isProcessRunging(String)
	 */
	@Override
	public boolean isProcessRunging(String serviceName) throws IOException {
		if (serviceName == null) {
			logger.warn("serviceName = NULL.");
			throw new IOException("serviceName = null");
		}

		java.lang.Process runningProcesses = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(runningProcesses.getInputStream()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.contains(serviceName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see #startProgram(String)
	 */
	@Override
	public boolean startProgram(String programPath) throws IOException {
		if (programPath == null) {
			logger.warn("programPath = NULL.");
			throw new IOException("programPath = null");
		}

		File runnableFile = new File(programPath);
		if (runnableFile.exists()) {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(runnableFile);
				return true;
			} else {
				return false;
			}
		} else {
			logger.warn("File not found: " + programPath);
			throw new IOException("File not found!");
		}
	}

	/**
	 * @see #startPrograms(List)
	 */
	@Override
	public boolean startPrograms(List<String> programPaths) {
		boolean allProcessAreStarted = true;

		for (String currentProgramPath : programPaths) {
			try {
				if (!startProgram(currentProgramPath)) {
					allProcessAreStarted = false;
				}
			} catch (Exception e) {
				allProcessAreStarted = false;
			}
		}

		return allProcessAreStarted;
	}

	/**
	 * @see #killProcess(String)
	 */
	@Override
	public boolean killProcess(String serviceName) throws IOException {
		if (serviceName == null) {
			logger.warn("serviceName = NULL.");
			throw new IOException("serviceName = null");
		}

		if (isProcessRunging(serviceName)) {
			try {
				java.lang.Process processKilled = Runtime.getRuntime().exec(KILL + serviceName);
				BufferedReader stdError = new BufferedReader(new 
			             InputStreamReader(processKilled.getErrorStream()));
				
				if (stdError.readLine() != null) {
					return false;
				}
				
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @see #killProcesses(List)
	 */
	public boolean killProcesses(List<String> serviceNames) {
		boolean allProcessWereKilled = true;

		for (String currentServiceName : serviceNames) {
			try {
				if (!killProcess(currentServiceName)) {
					allProcessWereKilled = false;
				}
			} catch (Exception e) {
				allProcessWereKilled = false;
			}
		}

		return allProcessWereKilled;
	}

}
