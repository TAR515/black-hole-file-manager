package ro.edu.ubb.blackhole.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ro.edu.ubb.blackhole.datastructures.FileItem;
import ro.edu.ubb.blackhole.gui.activityes.FileManager;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Provide the base operations to {@link PCListViewAdapter} and {@link PhoneListViewAdapter}.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public abstract class BHListViewAdapter extends ArrayAdapter<FileItem> {

	/**
	 * Context of the application.
	 */
	Context context = null;

	/**
	 * Items containint the {@link ListView}
	 */
	List<FileItem> fileItems = null;

	/**
	 * File Manager Activity
	 */
	FileManager fileManager = null;

	/**
	 * The path of the {@link ListView}
	 */
	String currentPath = "";

	/**
	 * ID of the current row.
	 */
	int rowResourceId = -1;

	/**
	 * The selected file which clicked the user.
	 */
	FileItem selectedFile = null;

	/**
	 * This is used when somebody downloads a file just for opening it.
	 */
	private FileItem temporalyFile = null;

	public BHListViewAdapter(Context context, int layoutResourceId, FileManager fileManager) {
		super(context, layoutResourceId, new ArrayList<FileItem>());

		// Initializing
		this.context = context;
		this.rowResourceId = layoutResourceId;
		this.fileManager = fileManager;
	}

	/**
	 * Fill up the {@link ListView} with the given items.
	 * 
	 * @param files
	 *            List of FileItems.
	 */
	public void fillUpPhoneListView(List<FileItem> files) {
		// Sorting the given items. All folders will enjoy benefits.
		Collections.sort(files);

		// If the given items equals with null then create an empty content.
		if (files != null) {
			this.setFileItems(files);
		} else {
			this.setFileItems(new ArrayList<FileItem>());
		}
	}

	/**
	 * Initialize the {@link ListView}
	 */
	public abstract void init();

	/**
	 * Execute a file on the current device.
	 * 
	 * @param fileItem
	 *            executable file.
	 */
	public abstract void executeFile(FileItem fileItem);

	/**
	 * Refresh the content of the {@link ListView}
	 */
	public abstract void refresh();

	/**
	 * Restore the content of the {@link ListView} to default content.
	 */
	public abstract void getHome();

	/**
	 * Get property of the selected file.
	 */
	public abstract void getPropertiesOfSelectedFile();

	/**
	 * Delete selected files from the Phone or the PC.
	 */
	public abstract void deleteSelectedFiles();

	/**
	 * Returns all selected files.
	 * 
	 * @return all selected files.
	 */
	public List<FileItem> getSelectedFileItems() {
		List<FileItem> selectedFileItems = new ArrayList<FileItem>();

		if (this.fileItems != null) {
			for (FileItem currentFileItem : this.fileItems) {
				if (currentFileItem.isSelected()) {
					selectedFileItems.add(currentFileItem);
				}
			}
		}

		return selectedFileItems;
	}

	public List<FileItem> getFileItems() {
		return fileItems;
	}

	public void setFileItems(List<FileItem> fileItems) {
		Collections.sort(fileItems);

		this.fileItems = fileItems;

		this.clear();
		for (FileItem currentFileItem : fileItems) {
			this.add(currentFileItem);
		}
		this.notifyDataSetChanged();
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public FileItem getSelectedFile() {
		return selectedFile;
	}

	public FileItem getTemporalyFile() {
		return temporalyFile;
	}

	public void setTemporalyFile(FileItem temporalyFile) {
		this.temporalyFile = temporalyFile;
	}
}
