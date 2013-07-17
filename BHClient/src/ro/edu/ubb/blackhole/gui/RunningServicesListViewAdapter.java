package ro.edu.ubb.blackhole.gui;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.bll.Log;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.datastructures.guirequest.GetRunningProgramsGR;
import ro.edu.ubb.blackhole.datastructures.guirequest.StopProgramsOnPCGR;
import ro.edu.ubb.blackhole.gui.activityes.RunningServicesActivity;
import ro.edu.ubb.blackhole.libs.datastructures.Process;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Provide an adapter to a {@link ListView}. This adapter provides all functionality to stopping a running process on the PC or
 * navigating between them.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class RunningServicesListViewAdapter extends ArrayAdapter<Process> implements ResponseHandler {

	/**
	 * Context of the application.
	 */
	Context context = null;

	/**
	 * Items containing the {@link ListView}
	 */
	List<Process> runningProcesses = null;

	/**
	 * Name of the Server.
	 */
	private String serverName = null;

	/**
	 * Caller activity.
	 */
	private RunningServicesActivity runningServicesActivity = null;

	/**
	 * ID of the current row.
	 */
	int rowResourceId = -1;

	/**
	 * The selected process which has clicked the user.
	 */
	FileItem selectedProcess = null;

	public RunningServicesListViewAdapter(Context context, int layoutResourceId,
			RunningServicesActivity runningServicesActivity, String serverName) {
		super(context, layoutResourceId, new ArrayList<Process>());

		// Initializing
		this.context = context;
		this.rowResourceId = layoutResourceId;
		this.runningServicesActivity = runningServicesActivity;
		this.serverName = serverName;

		init();
	}

	/**
	 * @see #getView(int, View, ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(this.rowResourceId, parent, false);
		}

		Process currentProcess = this.runningProcesses.get(position);
		String processName = currentProcess.getProcessName();
		int pid = currentProcess.getPid();
		String sessionName = currentProcess.getSessionName();
		int sessionCount = currentProcess.getSessionCount();
		String memoryUsage = currentProcess.getMemoryUsage();

		if (currentProcess != null) {
			TextView processNameView = (TextView) row.findViewById(R.id.textview_process_name);
			TextView pidView = (TextView) row.findViewById(R.id.textview_pid);
			TextView sessionNameView = (TextView) row.findViewById(R.id.textview_session_name);
			TextView sessionCountView = (TextView) row.findViewById(R.id.textview_session_count);
			TextView memoryUsageView = (TextView) row.findViewById(R.id.textview_memory_usage);

			if (processNameView != null) {
				processNameView.setText(processName);
			}

			if (pidView != null) {
				pidView.setText(Integer.toString(pid));
			}

			if (sessionNameView != null) {
				sessionNameView.setText(sessionName);
			}

			if (sessionCountView != null) {
				sessionCountView.setText(Integer.toString(sessionCount));
			}

			if (memoryUsageView != null) {
				memoryUsageView.setText(memoryUsage);
			}

			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

					} catch (Exception e) {
						// Nothing to do :) This try catch is to avoid a runtime exception when the user click too fast on the
						// items and the Android can't handle it.
					}
				}
			});

			// When somebody long clicks on a row.
			row.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					StopProgramsOnPCGR request = new StopProgramsOnPCGR(
							RunningServicesListViewAdapter.this.runningServicesActivity
									.getApplicationContext());
					request.setServerName(RunningServicesListViewAdapter.this.serverName);
					List<String> stopableProcesses = new ArrayList<String>();
					stopableProcesses.add(runningProcesses.get(position).getProcessName());
					request.setProcessNames(stopableProcesses);

					new GuiRequestHandler(RunningServicesListViewAdapter.this.runningServicesActivity,
							RunningServicesListViewAdapter.this, true).execute(request);

					return false;
				}
			});
		}

		return row;
	}

	/**
	 * Fill up the {@link ListView} with the given items.
	 * 
	 * @param files
	 *            List of FileItems.
	 */
	public void fillUpListView(List<Process> runningProcesses) {
		// If the given items equals with null then create an empty content.
		if (runningProcesses != null) {
			this.setRunningProcesses(runningProcesses);
		} else {
			this.setRunningProcesses(new ArrayList<Process>());
		}
	}

	/**
	 * Refresh the list view items.
	 */
	public void refresh() {
		init();
	}

	/**
	 * Initialate the list view.
	 */
	public void init() {
		GetRunningProgramsGR request = new GetRunningProgramsGR(
				RunningServicesListViewAdapter.this.runningServicesActivity.getApplicationContext());
		request.setServerName(RunningServicesListViewAdapter.this.serverName);
		new GuiRequestHandler(RunningServicesListViewAdapter.this.runningServicesActivity,
				RunningServicesListViewAdapter.this, true).execute(request);
	}

	public List<Process> getRunningProcesses() {
		return runningProcesses;
	}

	public void setRunningProcesses(List<Process> runningProcesses) {
		this.runningProcesses = runningProcesses;

		this.clear();
		for (Process currentProcess : this.runningProcesses) {
			this.add(currentProcess);
		}

		this.notifyDataSetChanged();
	}

	public FileItem getSelectedProcess() {
		return selectedProcess;
	}

	public void setSelectedProcess(FileItem selectedProcess) {
		this.selectedProcess = selectedProcess;
	}

	/**
	 * @see #responseHandler(CommandToGui)
	 */
	@Override
	public void responseHandler(CommandToGui command) {
		try {
			switch (command.getCommandID()) {
			case R.integer.COMMAND_GET_RUNNING_PROGRAMS:
				fillUpListView(command.getProcesses());
				break;
			case R.integer.COMMAND_STOP_PROGRAMS:
				boolean allProgramsStarted = (command.getSimpleResponse() == 1) ? true : false;
				MessageBox allProgramsStartedMessageBox = new MessageBox(this.runningServicesActivity);

				if (allProgramsStarted) {
					allProgramsStartedMessageBox.createInformationMessageBox(this.runningServicesActivity
							.getString(R.string.all_programs_were_stopped));
				} else {
					allProgramsStartedMessageBox.createInformationMessageBox(this.runningServicesActivity
							.getString(R.string.not_all_programs_were_stopped));
				}
				refresh();

				break;
			default:
				Log.w("Not implemented proccess for this response.");
			}
		} catch (NullPointerException e) {
			Log.e("Gui can't process the response!");
		}
	}

}
