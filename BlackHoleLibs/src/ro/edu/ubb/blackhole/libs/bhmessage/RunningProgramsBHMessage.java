package ro.edu.ubb.blackhole.libs.bhmessage;

import java.util.ArrayList;
import java.util.List;
import ro.edu.ubb.blackhole.libs.datastructures.Process;

/**
 * This Black Hole message contains all {@link Process}-es running on the client's computer.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class RunningProgramsBHMessage extends BlackHoleMessage {

	/**
	 * Running processes.
	 */
	List<Process> runningProcesses = null;
	
	public RunningProgramsBHMessage() {
		super(BHMessageType.BH_RUNNING_PROGRAMS_MESSAGE);
	}
	
	public RunningProgramsBHMessage(int requestMessageType) {
		super(BHMessageType.BH_RUNNING_PROGRAMS_MESSAGE, requestMessageType);
	}

	public List<Process> getRunningProcesses() {
		return runningProcesses;
	}

	public void setRunningProcesses(List<Process> runningProcesses) {
		this.runningProcesses = runningProcesses;
	}
	
	public void addRunningProcess(Process runningProcess) {
		if (this.runningProcesses == null) {
			this.runningProcesses = new ArrayList<Process>();
		}
		
		this.runningProcesses.add(runningProcess);
	}

}
