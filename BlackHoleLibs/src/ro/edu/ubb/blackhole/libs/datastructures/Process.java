package ro.edu.ubb.blackhole.libs.datastructures;

/**
 * This object contain the basic informations about a process.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class Process implements Comparable<Process> {

	/**
	 * Name of the process.
	 */
	private String processName = "";

	/**
	 * PID of the process.
	 */
	private int pid = -1;

	/**
	 * Session name of the process.
	 */
	private String sessionName = "";

	/**
	 * Session count of the process.
	 */
	private int sessionCount = -1;

	/**
	 * Memory usage of the process.
	 */
	private String memoryUsage = "";

	public Process(String processName) {
		this.processName = processName;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public int getSessionCount() {
		return sessionCount;
	}

	public void setSessionCount(int sessionCount) {
		this.sessionCount = sessionCount;
	}

	public String getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((memoryUsage == null) ? 0 : memoryUsage.hashCode());
		result = prime * result + pid;
		result = prime * result + ((processName == null) ? 0 : processName.hashCode());
		result = prime * result + sessionCount;
		result = prime * result + ((sessionName == null) ? 0 : sessionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Process other = (Process) obj;
		if (memoryUsage == null) {
			if (other.memoryUsage != null)
				return false;
		} else if (!memoryUsage.equals(other.memoryUsage))
			return false;
		if (pid != other.pid)
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (sessionCount != other.sessionCount)
			return false;
		if (sessionName == null) {
			if (other.sessionName != null)
				return false;
		} else if (!sessionName.equals(other.sessionName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Process [processName=" + processName + ", pid=" + pid + ", sessionName=" + sessionName
				+ ", sessionCount=" + sessionCount + ", memoryUsage=" + memoryUsage + "]";
	}
	
	@Override
	public int compareTo(Process another) {
		return this.processName.compareToIgnoreCase(another.processName);
	}

}
